package com.hzih.sslvpn.web.action.sslvpn.ccd;

import cn.collin.commons.domain.PageResult;
//import com.hzih.sslvpn.dao.PrivateNetDao;
import com.hzih.sslvpn.dao.UserDao;
//import com.hzih.sslvpn.domain.PrivateNet;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.UserGroupService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 14-4-16
 * Time: 上午9:13
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupAction extends ActionSupport {

    private Logger logger = Logger.getLogger(UserGroupAction.class);
    private int start;
    private int limit;

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

   /* public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }*/

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private UserGroupService userGroupService;
    private UserDao userDao;
//    private PrivateNetDao privateNetDao;

    public UserGroupService getUserGroupService() {
        return userGroupService;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 查找
     *
     * @return
     * @throws Exception
     */
    public String findByPages() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String roleId = request.getParameter("roleId");
        String json = userGroupService.getUsersByRoleId(Integer.parseInt(roleId), start, limit);
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /**
     * @return
     * @throws Exception
     */
    public String findByOtherRoleId() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        PageResult ps = userGroupService.findCaUserByOtherRoleId(start, limit);
        if (ps != null) {
            List<User> list = ps.getResults();
            int count = ps.getAllResultsAmount();
            if (list != null) {
                StringBuilder sb = new StringBuilder("{success:true,total:" + count + ",rows:[");
                Iterator<User> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    User log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        sb.append("{");
                        sb.append("id:").append("'").append(log.getId()).append("'").append(",");

                        if (null == log.getCn())
                            sb.append("cn:").append("'").append("").append("'").append(",");
                        else
                            sb.append("cn:").append("'").append(log.getCn()).append("'").append(",");

                        if (null == log.getSubject())
                            sb.append("subject:").append("'").append("").append("'").append(",");
                        else
                            sb.append("subject:").append("'").append(log.getSubject()).append("'").append(",");

                        sb.append("dynamic_ip:").append("'").append(log.getDynamic_ip()).append("'").append(",");

                        if (null == log.getStatic_ip())
                            sb.append("static_ip:").append("'").append("").append("'").append(",");
                        else
                            sb.append("static_ip:").append("'").append(log.getStatic_ip()).append("'").append(",");

                        sb.append("allow_all_client:").append("'").append(log.getAllow_all_client()).append("'").append(",");

                        if (null == log.getSerial_number())
                            sb.append("serial_number:").append("'").append("").append("'").append(",");
                        else
                            sb.append("serial_number:").append("'").append(log.getSerial_number()).append("'").append(",");

                        sb.append("enabled:").append("'").append(log.getEnabled()).append("'").append(",");

                        if (null == log.getReal_address())
                            sb.append("real_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("real_address:").append("'").append(log.getReal_address()).append("'").append(",");

                        sb.append("byte_received:").append("'").append(log.getByte_received()).append("'").append(",");
                        sb.append("byte_send:").append("'").append(log.getByte_send()).append("'").append(",");

                        sb.append("connected_since:").append("'").append(log.getConnected_since()).append("'").append(",");

                        if (null == log.getVirtual_address())
                            sb.append("virtual_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("virtual_address:").append("'").append(log.getVirtual_address()).append("'").append(",");


                        if (null == log.getNet_id())
                            sb.append("net_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("net_id:").append("'").append(log.getNet_id()).append("'").append(",");

                        if (null == log.getTerminal_id())
                            sb.append("terminal_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("terminal_id:").append("'").append(log.getTerminal_id()).append("'").append(",");

                        if (null == log.getDescription())
                            sb.append("description:").append("'").append("").append("'").append(",");
                        else
                            sb.append("description:").append("'").append(log.getDescription()).append("'").append(",");

                        sb.append("view_flag:").append("'").append(log.getView_flag()).append("'").append(",");
                        sb.append("gps_flag:").append("'").append(log.getGps_flag()).append("'").append(",");
//                        sb.append("level:").append("'").append(log.getLevel()).append("'").append(",");
                        sb.append("last_ref:").append("'").append(log.getLast_ref()).append("'");
                        sb.append("}");
                        sb.append(",");
                    } else {
                        sb.append("{");
                        sb.append("id:").append("'").append(log.getId()).append("'").append(",");

                        if (null == log.getCn())
                            sb.append("cn:").append("'").append("").append("'").append(",");
                        else
                            sb.append("cn:").append("'").append(log.getCn()).append("'").append(",");

                        if (null == log.getSubject())
                            sb.append("subject:").append("'").append("").append("'").append(",");
                        else
                            sb.append("subject:").append("'").append(log.getSubject()).append("'").append(",");

                        sb.append("dynamic_ip:").append("'").append(log.getDynamic_ip()).append("'").append(",");

                        if (null == log.getStatic_ip())
                            sb.append("static_ip:").append("'").append("").append("'").append(",");
                        else
                            sb.append("static_ip:").append("'").append(log.getStatic_ip()).append("'").append(",");

                        sb.append("allow_all_client:").append("'").append(log.getAllow_all_client()).append("'").append(",");

                        if (null == log.getSerial_number())
                            sb.append("serial_number:").append("'").append("").append("'").append(",");
                        else
                            sb.append("serial_number:").append("'").append(log.getSerial_number()).append("'").append(",");

                        sb.append("enabled:").append("'").append(log.getEnabled()).append("'").append(",");

                        if (null == log.getReal_address())
                            sb.append("real_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("real_address:").append("'").append(log.getReal_address()).append("'").append(",");

                        sb.append("byte_received:").append("'").append(log.getByte_received()).append("'").append(",");
                        sb.append("byte_send:").append("'").append(log.getByte_send()).append("'").append(",");

                        sb.append("connected_since:").append("'").append(log.getConnected_since()).append("'").append(",");

                        if (null == log.getVirtual_address())
                            sb.append("virtual_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("virtual_address:").append("'").append(log.getVirtual_address()).append("'").append(",");


                        if (null == log.getNet_id())
                            sb.append("net_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("net_id:").append("'").append(log.getNet_id()).append("'").append(",");

                        if (null == log.getTerminal_id())
                            sb.append("terminal_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("terminal_id:").append("'").append(log.getTerminal_id()).append("'").append(",");

                        if (null == log.getDescription())
                            sb.append("description:").append("'").append("").append("'").append(",");
                        else
                            sb.append("description:").append("'").append(log.getDescription()).append("'").append(",");

                        sb.append("view_flag:").append("'").append(log.getView_flag()).append("'").append(",");
                        sb.append("gps_flag:").append("'").append(log.getGps_flag()).append("'").append(",");
//                        sb.append("level:").append("'").append(log.getLevel()).append("'").append(",");
                        sb.append("last_ref:").append("'").append(log.getLast_ref()).append("'");
                        sb.append("}");
                    }
                }
                sb.append("]}");
                actionBase.actionEnd(response, sb.toString(), result);
            }
        }
        return null;
    }

    /**
     * @return
     * @throws Exception
     */
    public String addUserToRoleId() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String roleId = request.getParameter("roleId");
        String[] uIds = request.getParameterValues("uIds");
        if (uIds != null) {
            for (String uId : uIds) {
                try {
                    userGroupService.addUserToRoleId(Integer.parseInt(uId), Integer.parseInt(roleId));
                    User u = userDao.findById(Integer.parseInt(uId));
                    if (u != null) {
                        VPNConfigUtil.configUser(u, StringContext.ccd);
                        msg = "添加成功";
                        json = "{success:true,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "1", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                } catch (Exception e) {
                    msg = "添加失败";
                    json = "{success:false,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.error(e.getMessage(),e);
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String removeRoleIdUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String roleId = request.getParameter("roleId");
        String userId = request.getParameter("userId");
        String msg = null;
        String json = "{success:false,msg:'移除失败'}";
        try {
            userGroupService.delByRoleIdAndUserId(Integer.parseInt(roleId), Integer.parseInt(userId));
            User u = userDao.findById(Integer.parseInt(userId));
            if (u != null) {
                VPNConfigUtil.configUser(u, StringContext.ccd);
                msg = "移除成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }

        } catch (Exception e) {
            msg = "移除失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

}
