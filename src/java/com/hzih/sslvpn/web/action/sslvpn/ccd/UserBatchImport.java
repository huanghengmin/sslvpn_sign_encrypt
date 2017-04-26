package com.hzih.sslvpn.web.action.sslvpn.ccd;

import com.hzih.sslvpn.dao.GroupDao;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.Groups;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.UserGroupService;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;

import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by hhm on 2017/3/7.
 */
public class UserBatchImport extends ActionSupport {
    private List<User> users = null;
    private UserDao userDao;
    private File uploadFile;
    private String uploadFileFileName;
    private String uploadFileContentType;
    private Logger logger = Logger.getLogger(UserBatchImport.class);
    private LogService logService;
    private GroupDao groupDao;
    private UserGroupService userGroupService;

    public UserGroupService getUserGroupService() {
        return userGroupService;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 下载批量导入用户模板文件
     *
     * @return
     * @throws Exception
     */
    public String downloadModel() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent, ";");
        st.nextToken();
        /*得到用户的浏览器名  MS IE  Firefox*/
        String userBrowser = st.nextToken();
        File file = new File(StringContext.systemPath + "/model/ImportUsers.xls");
        if (file.exists()) {
            FileUtil.downType(response, file.getName(), userBrowser);
            response = FileUtil.copy(file, response);
            json = "{success:true}";
        } else {
            logger.info("下载批量导入终端模板文件失败，文件不存在!");
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String batchFlag() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String msg = null;
        if (!uploadFileFileName.endsWith(".xls") && !uploadFileFileName.endsWith(".et")) {
            msg = "导入的文件不是[.xls]或者[.et]文件";
            json = "{success:false,msg:'" + msg + "'}";
        }
        if (msg == null) {
            HSSFWorkbook workbook = null;
            try {
                workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(uploadFile)));
            } catch (IOException e) {
                msg = "没有找到导入文件";
                json = "{success:false,msg:'" + msg + "'}";
                logger.info("没有找到导入文件::" + e.getMessage());
            }
            if (workbook != null) {
                HSSFSheet sheet = workbook.getSheetAt(0);
                int lastRowNum = sheet.getLastRowNum();
                try {
                    StringBuilder readMsg = new StringBuilder();
                    List<User> users = findCount(readMsg, sheet, lastRowNum);
                    if (users == null) {
                        json = "{success:false,msg:'" + readMsg.toString() + "'}";
                    } else {
                        String modify_msg = null;
                        SearchControls constraints = new SearchControls();
                        constraints.setSearchScope(SearchControls.OBJECT_SCOPE);
                        this.users = users;
                        for (User user : users) {
                            try {
                                User sql_user = userDao.findByCommonName(user.getCn());
                                if (sql_user!=null) {
                                    modify_msg = "Excel文件中某些用户已在在LDAP数据库,是否更新?";
                                    if (modify_msg != null)
                                        readMsg.append(modify_msg).append("\\n");
                                    break;
                                }
                            } catch (Exception e) {
                                //
                            }
                        }
                        if (modify_msg != null) {
                            json = "{success:true,msg:'" + readMsg + "'}";
                        } else {
                            msg = "Excel文件中没有任何用户存在LDAP数据库,是否添加?";
                            readMsg.append(msg).append("\\n");
                            json = "{success:true,msg:'" + readMsg + "'}";
                        }
                    }
                } catch (Exception e) {
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "ImportUser", "出错!" + msg);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String batchImportUser() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String flag = request.getParameter("flag");
        String json = "{success:false}";
        String msg = null;
        try {
            List<User> userLists = this.users;
            if (userLists == null) {
                json = "{success:false,msg:'未读取到任何用户,不能进行操作!'}";
            } else {
                for (User user : userLists) {
                    try {
                        User sql_user = userDao.findByCommonName(user.getCn());
                        if (sql_user!=null) {
                            if (flag.equals("true"))
                                modify_user(user);
                        } else {
                           add_user(user);
                        }
                    } catch (Exception e) {
                        add_user(user);
                    }
                }
                this.users = null;
                msg = "批量导入用户完成";
                json = "{success:true,msg:'" + msg + "'}";
                logger.info("批量导入用户完成");
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "ImportUser", "导入用户!");
            }
        } catch (Exception e) {
            msg = "批量导入用户失败::" + msg;
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("批量导入用户失败::" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "ImportUser", "导入用户失败!" + msg);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    private String getCellValue(HSSFCell aCell) {
        if (aCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {// 数字
            return String.valueOf(aCell.getNumericCellValue());
        } else if (aCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {// Boolean
            return String.valueOf(aCell.getBooleanCellValue());
        } else if (aCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {// 字符串
            return aCell.getStringCellValue();
        } else if (aCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {// 公式
            return String.valueOf(aCell.getCellFormula());
        } else if (aCell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {// 空值
            return null;
        } else if (aCell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {// 故障
            return null;
        } else {
            //未知类型
            return null;
        }
    }

    private List<User> findCount(StringBuilder readMsg, HSSFSheet sheet, int lastRowNum) throws NamingException {
        List<User> users = new ArrayList<>();
        boolean isEmptyLine = false;
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row != null) {
                int cellNum = 0;

                String msg = null;
                String cn = null;
                String serial = null;
                String group = null;

                HSSFCell cell = row.getCell(cellNum++);

                boolean isNeedToAddMany = true;
                //cn
                if (cell != null) {
                    cn = getCellValue(cell);
                    if (cn == null || "".equals(cn)) {
                        isNeedToAddMany = false;
                    }
                }

                //serial
                cell = row.getCell(cellNum++);
                if (cell != null) {
                    serial = getCellValue(cell);
                    if (serial == null || "".equals(serial)) {
                        isNeedToAddMany = false;
                    }
                }
                //group
                cell = row.getCell(cellNum++);
                if (cell != null) {
                    group = getCellValue(cell);
                    if (group == null || "".equals(group)) {
                        isNeedToAddMany = false;
                    }
                }
                if ((group == null || "".equals(group))
                        && (serial == null || "".equals(serial))
                        && (cn == null || "".equals(cn))) {
                    isEmptyLine = true;
                }

                if (!isEmptyLine) {
                    if (!isNeedToAddMany) {
                        msg = "第" + (i + 1) + "行,用户信息不完整,忽略操作!";
                        readMsg.append(msg).append("\\n");
                    }
                }

                if (isNeedToAddMany && !isEmptyLine) {
                    User user = new User();
                    user.setCn(cn);
                    user.setSerial_number(serial);
                    Groups sql_group = null;
                    try {
                         sql_group = groupDao.findByName(group);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(sql_group!=null){
                        Set<Groups> groupsSet = new HashSet<>();
                        groupsSet.add(sql_group);
                        user.setGroupsSet(groupsSet);
                    }else {
                        Groups groups = new Groups();
                        groups.setGroup_name(group);
                        groups.setGroup_desc(group);
                        try {
                            groupDao.add(groups);
                            Groups sql_groups =groupDao.findByName(group);
                            Set<Groups> groupsSet = new HashSet<>();
                            groupsSet.add(sql_groups);
                            user.setGroupsSet(groupsSet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    users.add(user);
                    if (users.size() > 18) {
                        msg = "Excel文件有效数据内容大于1000行,单次只能导入1000行,导入失败!<br/>";
                        readMsg.append(msg).append("\\n");
                        logger.info(msg);
                        return null;
                    }
                }
            }
        }
        return users;
    }

    private boolean modify_user(User user) {
        User sql_user = null;
        try {
           sql_user = userDao.findByCommonName(user.getCn());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sql_user!=null) {
            sql_user.setSerial_number(user.getSerial_number());
            sql_user.setGroupsSet(user.getGroupsSet());
            try {
                userDao.modify(sql_user);
                for (Groups groups:user.getGroupsSet()) {
                    try {
                        userGroupService.addUsersToRoleId(String.valueOf(sql_user.getId()),groups.getId() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                VPNConfigUtil.configUser(sql_user, StringContext.ccd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                userDao.add(user);
                sql_user = userDao.findByCommonName(user.getCn());
                for (Groups groups:user.getGroupsSet()) {
                    try {
                        userGroupService.addUsersToRoleId(String.valueOf(sql_user.getId()),groups.getId() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                VPNConfigUtil.configUser(sql_user, StringContext.ccd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean add_user(User user) {
        User sql_user = null;
        try {
            sql_user = userDao.findByCommonName(user.getCn());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sql_user!=null) {
            sql_user.setSerial_number(user.getSerial_number());
            sql_user.setGroupsSet(user.getGroupsSet());

            try {
                userDao.modify(sql_user);
                for (Groups groups:user.getGroupsSet()) {
                    try {
                        userGroupService.addUsersToRoleId(String.valueOf(sql_user.getId()),groups.getId() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                VPNConfigUtil.configUser(sql_user, StringContext.ccd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                userDao.add(user);
                sql_user = userDao.findByCommonName(user.getCn());
                for (Groups groups:user.getGroupsSet()) {
                    try {
                        userGroupService.addUsersToRoleId(String.valueOf(sql_user.getId()),groups.getId() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                VPNConfigUtil.configUser(sql_user, StringContext.ccd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
