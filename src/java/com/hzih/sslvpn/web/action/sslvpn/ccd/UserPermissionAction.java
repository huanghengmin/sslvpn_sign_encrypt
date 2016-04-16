package com.hzih.sslvpn.web.action.sslvpn.ccd;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.*;
import com.hzih.sslvpn.domain.*;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午6:58
 * To change this template use File | Settings | File Templates.
 */
public class UserPermissionAction extends ActionSupport {
    private Logger logger = Logger.getLogger(UserPermissionAction.class);
    private UserDao userDao;
    private LogService logService;
    private StaticIpDao staticIpDao;
//    private RouteUserDao routeUserDao;
    private ServerDao serverDao;
/*    private UserPrivateNetsDao userPrivateNetsDao;
    private PrivateNetDao privateNetDao;*/

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    /*public RouteUserDao getRouteUserDao() {
        return routeUserDao;
    }

    public void setRouteUserDao(RouteUserDao routeUserDao) {
        this.routeUserDao = routeUserDao;
    }*/

    public StaticIpDao getStaticIpDao() {
        return staticIpDao;
    }

    public void setStaticIpDao(StaticIpDao staticIpDao) {
        this.staticIpDao = staticIpDao;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

   /* public PrivateNetDao getPrivateNetDao() {
        return privateNetDao;
    }

    public void setPrivateNetDao(PrivateNetDao privateNetDao) {
        this.privateNetDao = privateNetDao;
    }

    public UserPrivateNetsDao getUserPrivateNetsDao() {
        return userPrivateNetsDao;
    }

    public void setUserPrivateNetsDao(UserPrivateNetsDao userPrivateNetsDao) {
        this.userPrivateNetsDao = userPrivateNetsDao;
    }*/

    private User user;
    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String add() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        User old = userDao.checkCn(user.getCn());
        if (null != old) {
            msg = "用户名已存在";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        } else {
            userDao.add(user);
            String u_name = user.getCn();
            User old_user = userDao.findByCommonName(u_name);
//            Set<SourceNet> sourceNetSet = old_user.getSourceNets();
            VPNConfigUtil.configUser(old_user, StringContext.ccd);
            msg = "添加用户成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String modify() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        if (null != old) {
//            old.setCn(user.getCn());
//            old.setId_card(user.getId_card());
//            old.setEmail(user.getEmail());
//            old.setPhone(user.getPhone());
//            old.setAddress(user.getAddress());
//            if(user.getSerial_number()!=null){
//                old.setSerial_number(user.getSerial_number());
//            }
            if (user.getNet_id() != null)
                old.setNet_id(user.getNet_id());
            if (user.getTerminal_id() != null)
                old.setTerminal_id(user.getTerminal_id());
//                old.setLevel(user.getLevel());
            try {
                userDao.modify(old);
                VPNConfigUtil.configUser(old, StringContext.ccd);
                msg = "更新用户成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } catch (Exception e) {
                msg = "添加用户失败";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.error(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
//            List<PrivateNet> nets = privateNetDao.findAll();
//            Set<SourceNet> nets = old.getSourceNets();
        } else {
            msg = "添加用户失败,未找到指定用户";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        try {
            userDao.delete(old);
            File file = new File(StringContext.ccd + "/" + old.getCn());
            if (file.exists()) {
                file.delete();
            }
            msg = "删除成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            msg = "删除失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String cleanThreeYards() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        User old = null;
        try {
            old = userDao.findById(Integer.parseInt(id));
        } catch (Exception e) {
            msg = "清除终端绑定失败,查找用户出错";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        old.setNet_id("");
        old.setTerminal_id("");
        try {
            userDao.modify(old);
            msg = "清除终端绑定成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            msg = "清除终端绑定失败,出现异常";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String cn = request.getParameter("cn");
        String status = request.getParameter("status");
        int enable = -1;
        if (null != status && !status.equals("")) {
            enable = Integer.parseInt(status);
        }
        PageResult pageResult = userDao.findByPages(cn, enable, start, limit);
        if (pageResult != null) {
            List<User> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                StringBuilder sb = new StringBuilder("{success:true,total:" + count + ",rows:[");
                Iterator<User> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    User log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        sb.append("{");
                        sb.append("id:").append("'").append(log.getId()).append("'").append(",");

                        if(null==log.getCn())
                            sb.append("cn:").append("'").append("").append("'").append(",");
                        else
                            sb.append("cn:").append("'").append(log.getCn()).append("'").append(",");

                        if(null==log.getSubject())
                            sb.append("subject:").append("'").append("").append("'").append(",");
                        else
                            sb.append("subject:").append("'").append(log.getSubject()).append("'").append(",");

                        sb.append("dynamic_ip:").append("'").append(log.getDynamic_ip()).append("'").append(",");

                        if(null==log.getStatic_ip())
                            sb.append("static_ip:").append("'").append("").append("'").append(",");
                        else
                            sb.append("static_ip:").append("'").append(log.getStatic_ip()).append("'").append(",");

                        sb.append("allow_all_client:").append("'").append(log.getAllow_all_client()).append("'").append(",");

                        if(null==log.getSerial_number())
                            sb.append("serial_number:").append("'").append("").append("'").append(",");
                        else
                            sb.append("serial_number:").append("'").append(log.getSerial_number()).append("'").append(",");

                        sb.append("enabled:").append("'").append(log.getEnabled()).append("'").append(",");

                        if(null==log.getReal_address())
                            sb.append("real_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("real_address:").append("'").append(log.getReal_address()).append("'").append(",");

                        sb.append("byte_received:").append("'").append(log.getByte_received()).append("'").append(",");
                        sb.append("byte_send:").append("'").append(log.getByte_send()).append("'").append(",");

                        sb.append("connected_since:").append("'").append(log.getConnected_since()).append("'").append(",");

                        if(null==log.getVirtual_address())
                            sb.append("virtual_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("virtual_address:").append("'").append(log.getVirtual_address()).append("'").append(",");


                        if(null==log.getNet_id())
                            sb.append("net_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("net_id:").append("'").append(log.getNet_id()).append("'").append(",");

                        if(null==log.getTerminal_id())
                            sb.append("terminal_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("terminal_id:").append("'").append(log.getTerminal_id()).append("'").append(",");

                        if(null==log.getDescription())
                            sb.append("description:").append("'").append("").append("'").append(",");
                        else
                            sb.append("description:").append("'").append(log.getDescription()).append("'").append(",");

                     /*   if(null==log.getDownload_speed())
                            sb.append("download_speed:").append("'").append("").append("'").append(",");
                        else
                            sb.append("download_speed:").append("'").append(log.getDownload_speed()).append("'").append(",");

                        if(null==log.getUpload_speed())
                            sb.append("upload_speed:").append("'").append("").append("'").append(",");
                        else
                            sb.append("upload_speed:").append("'").append(log.getUpload_speed()).append("'").append(",");*/

                        sb.append("view_flag:").append("'").append(log.getView_flag()).append("'").append(",");
                        sb.append("gps_flag:").append("'").append(log.getGps_flag()).append("'").append(",");
//                        sb.append("level:").append("'").append(log.getLevel()).append("'").append(",");

                        sb.append("last_ref:").append("'").append(log.getLast_ref()).append("'");
                        sb.append("}");
                        sb.append(",");
                    } else {
                        sb.append("{");
                        sb.append("id:").append("'").append(log.getId()).append("'").append(",");

                        if(null==log.getCn())
                            sb.append("cn:").append("'").append("").append("'").append(",");
                        else
                            sb.append("cn:").append("'").append(log.getCn()).append("'").append(",");

                        if(null==log.getSubject())
                            sb.append("subject:").append("'").append("").append("'").append(",");
                        else
                            sb.append("subject:").append("'").append(log.getSubject()).append("'").append(",");

                        sb.append("dynamic_ip:").append("'").append(log.getDynamic_ip()).append("'").append(",");

                        if(null==log.getStatic_ip())
                            sb.append("static_ip:").append("'").append("").append("'").append(",");
                        else
                            sb.append("static_ip:").append("'").append(log.getStatic_ip()).append("'").append(",");

                        sb.append("allow_all_client:").append("'").append(log.getAllow_all_client()).append("'").append(",");

                        if(null==log.getSerial_number())
                            sb.append("serial_number:").append("'").append("").append("'").append(",");
                        else
                            sb.append("serial_number:").append("'").append(log.getSerial_number()).append("'").append(",");

                        sb.append("enabled:").append("'").append(log.getEnabled()).append("'").append(",");

                        if(null==log.getReal_address())
                            sb.append("real_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("real_address:").append("'").append(log.getReal_address()).append("'").append(",");

                        sb.append("byte_received:").append("'").append(log.getByte_received()).append("'").append(",");
                        sb.append("byte_send:").append("'").append(log.getByte_send()).append("'").append(",");

                        sb.append("connected_since:").append("'").append(log.getConnected_since()).append("'").append(",");

                        if(null==log.getVirtual_address())
                            sb.append("virtual_address:").append("'").append("").append("'").append(",");
                        else
                            sb.append("virtual_address:").append("'").append(log.getVirtual_address()).append("'").append(",");


                        if(null==log.getNet_id())
                            sb.append("net_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("net_id:").append("'").append(log.getNet_id()).append("'").append(",");

                        if(null==log.getTerminal_id())
                            sb.append("terminal_id:").append("'").append("").append("'").append(",");
                        else
                            sb.append("terminal_id:").append("'").append(log.getTerminal_id()).append("'").append(",");

                        if(null==log.getDescription())
                            sb.append("description:").append("'").append("").append("'").append(",");
                        else
                            sb.append("description:").append("'").append(log.getDescription()).append("'").append(",");

                 /*       if(null==log.getDownload_speed())
                            sb.append("download_speed:").append("'").append("").append("'").append(",");
                        else
                            sb.append("download_speed:").append("'").append(log.getDownload_speed()).append("'").append(",");

                        if(null==log.getUpload_speed())
                            sb.append("upload_speed:").append("'").append("").append("'").append(",");
                        else
                            sb.append("upload_speed:").append("'").append(log.getUpload_speed()).append("'").append(",");*/

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

    /*public String findRouteUsers() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        User u = userDao.findById(Integer.parseInt(id));
        if (u != null) {
            Set<RouteUser> routeUsers = u.getRouteUsers();
            if (routeUsers != null && routeUsers.size() > 0) {
                String json = "{success:true,total:" + routeUsers.size() + ",rows:[";
                Iterator<RouteUser> raUserIterator = routeUsers.iterator();
                while (raUserIterator.hasNext()) {
                    RouteUser log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',user_name:'" + log.getUser_name() +
                                "',user_idcard:'" + log.getUser_idcard() +
                                "',user_province:'" + log.getUser_province() +
                                "',user_city:'" + log.getUser_city() +
                                "',user_organization:'" + log.getUser_organization() +
                                "',user_institution:'" + log.getUser_institution() +
                                "',user_phone:'" + log.getUser_phone() +
                                "',user_address:'" + log.getUser_address() +
                                "',user_email:'" + log.getUser_email() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',user_name:'" + log.getUser_name() +
                                "',user_idcard:'" + log.getUser_idcard() +
                                "',user_province:'" + log.getUser_province() +
                                "',user_city:'" + log.getUser_city() +
                                "',user_organization:'" + log.getUser_organization() +
                                "',user_institution:'" + log.getUser_institution() +
                                "',user_phone:'" + log.getUser_phone() +
                                "',user_address:'" + log.getUser_address() +
                                "',user_email:'" + log.getUser_email() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);

            }
        }
        return null;
    }*/

    /*public String findTerminalByRouteUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        RouteUser u = routeUserDao.findById(Integer.parseInt(id));
        if (u != null) {
            Terminal terminal = u.getTerminal();
            String json = "{success:true,total:" + 1 + ",rows:[";
            json += "{" +
                    "id:'" + terminal.getId() +
                    "',terminal_name:'" + terminal.getTerminal_name() +
                    "',terminal_type:'" + terminal.getTerminal_type() +
                    "',user_name:'" + terminal.getUser_name() +
                    "',terminal_status:'" + terminal.getTerminal_status() +
                    "',terminal_desc:'" + terminal.getTerminal_desc() +
                    "',ip:'" + terminal.getIp() +
                    "',mac:'" + terminal.getMac() +
                    "',on_line:'" + terminal.getOn_line() + "'" +
                    "}";
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }*/

    public String disable() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        boolean old = userDao.disableUser(Integer.parseInt(id));
        if (old) {
            User oldUser = userDao.findById(Integer.parseInt(id));
            if (null != oldUser) {
//            User user = userDao.findByCommonName(oldUser.getSerial_number());
//                oldUser.setEnabled(0);
//            userDao.update(user);
                Proc kill_proc = new Proc();
                String kill_command = "sh " + StringContext.systemPath + "/script/kill_user.sh " + oldUser.getCn();
                kill_proc.exec(kill_command);
//                List<PrivateNet> nets = privateNetDao.findAll();
//                Set<SourceNet> nets = oldUser.getSourceNets();
                VPNConfigUtil.configUser(oldUser, StringContext.ccd);
                Thread.sleep(2*1000);
                msg = "禁止用户访问成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }
//        List<PrivateNet> nets =  privateNetDao.findAll();
//        VPNConfigUtil.configUser(oldUser, StringContext.ccd,nets);
        } else {
            msg = "禁止用户访问失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String enable() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        boolean old = userDao.enableUser(Integer.parseInt(id));
        if (old) {
//            json = "{success:true,msg:'启用用户成功'}";
            User oldUser = userDao.findById(Integer.parseInt(id));
//        List<PrivateNet> nets = privateNetDao.findAll();
//        Set<SourceNet> nets = oldUser.getSourceNets();
            VPNConfigUtil.configUser(oldUser, StringContext.ccd);
            msg = "启用用户成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        } else {
            msg = "启用用户失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

   /* public String addNetsToUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'添加子网失败'}";
        String userId = request.getParameter("userId");
        String[] pIds = request.getParameterValues("pIds");
        if (pIds != null) {
            try {
                for (String uId : pIds) {
//                    userPrivateNetsDao.addPrivateNetToUser(Integer.parseInt(userId), Integer.parseInt(uId));
                }
                User old = userDao.findById(Integer.parseInt(userId));
//                List<PrivateNet> nets = privateNetDao.findAll();
//                Set<SourceNet> nets = old.getSourceNets();
                VPNConfigUtil.configUser(old, StringContext.ccd);
                json = "{success:true,msg:'添加子网成功'}";
            } catch (Exception e) {
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
*/
   /* public String removeNetForUser() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'移除子网失败'}";
        String userId = request.getParameter("userId");
        String pId = request.getParameter("pId");
//        userPrivateNetsDao.removePrivateNetToUser(Integer.parseInt(userId), Integer.parseInt(pId));
        User old = userDao.findById(Integer.parseInt(userId));
//        List<PrivateNet> nets = privateNetDao.findAll();
//        Set<SourceNet> nets = old.getSourceNets();
        VPNConfigUtil.configUser(old, StringContext.ccd*//*, nets*//*);
        json = "{success:true,msg:'移除子网成功'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }*/

   /* public String findOtherUserIdNets() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        PageResult pageResult = null;// userPrivateNetsDao.findOtherUserIdPrivateNets(Integer.parseInt(id), start, limit);
        if (pageResult != null) {
            List<PrivateNet> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<PrivateNet> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    PrivateNet log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }*/

    public String findUserNets() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        if (null != old) {
            Set<SourceNet> nets = old.getSourceNets();
            if (nets != null) {
                String json = "{success:true,total:" + nets.size() + ",rows:[";
                Iterator<SourceNet> raUserIterator = nets.iterator();
                while (raUserIterator.hasNext()) {
                    SourceNet log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String updateUserPermission() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        String client_to_client = request.getParameter("allow_client_to_client");
//        String allow_all_nets = request.getParameter("allow_all_nets");
        String dynamic_ip = request.getParameter("dynamic_ip");
        String static_ip = request.getParameter("static_ip");
//        String download_speed = request.getParameter("download_speed");
//        String upload_speed = request.getParameter("upload_speed");
        User old = userDao.findById(Integer.parseInt(id));
        int dynamic = Integer.parseInt(dynamic_ip);
        if (dynamic == 0) {
//            Server server = serverDao.find();
//            String start_static_ip = static_ip.substring(0, static_ip.lastIndexOf("."));
            String end = static_ip.substring(static_ip.lastIndexOf(".") + 1, static_ip.length());
//            if (server.getStatic_net().startsWith(start_static_ip)) {
            StaticIp staticIp = staticIpDao.findById(Integer.parseInt(end));
            if (null != staticIp) {
                if (null != old)
                    if (null != client_to_client && client_to_client.equals("on"))
                        old.setAllow_all_client(1);
                    else
                        old.setAllow_all_client(0);
//                if (null != allow_all_nets && allow_all_nets.equals("on"))
//                    old.setAllow_all_subnet(1);
//                else
//                    old.setAllow_all_subnet(0);
                old.setDynamic_ip(dynamic);
                old.setStatic_ip(static_ip);
            /*    if(download_speed!=null)
                 old.setDownload_speed(download_speed);
                if(upload_speed!=null)
                    old.setUpload_speed(upload_speed);
                userDao.update(old);*/

//                List<PrivateNet> nets = privateNetDao.findAll();
//                Set<SourceNet> nets = old.getSourceNets();
                VPNConfigUtil.configUser(old, StringContext.ccd/*, nets*/);
//                json = "{success:true,msg:'更新用户权限成功'}";
                msg = "更新用户权限成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
//                json = "{success:false,msg:'更新用户权限失败!,请确认IP未位是4的倍数+1'}";
                msg = "更新用户权限失败!,请确认IP未位是4的倍数+1";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
//            } else {
//                json = "{success:false,msg:'更新用户权限失败!,请确定IP在静态IP组:" + server.getStatic_net() + ",且IP未位是4的倍数+1'}";
//            }
        } else {
            if (null != old)
                if (null != client_to_client && client_to_client.equals("on"))
                    old.setAllow_all_client(1);
                else
                    old.setAllow_all_client(0);
//            if (null != allow_all_nets && allow_all_nets.equals("on"))
//                old.setAllow_all_subnet(1);
//            else
//                old.setAllow_all_subnet(0);
            old.setDynamic_ip(dynamic);
            old.setStatic_ip("");
       /*     if(download_speed!=null)
                old.setDownload_speed(download_speed);
            if(upload_speed!=null)
                old.setUpload_speed(upload_speed);*/

            userDao.update(old);

//            List<PrivateNet> nets = privateNetDao.findAll();
//            Set<SourceNet> nets = old.getSourceNets();
            VPNConfigUtil.configUser(old, StringContext.ccd/*, nets*/);
//            json = "{success:true,msg:'更新用户权限成功'}";
            msg = "更新用户权限成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户管理", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findUserPermission() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String id = request.getParameter("id");
        User old = userDao.findById(Integer.parseInt(id));
        String json = "{success:true,totalCount:" + 1 + ",root:[";
        if (null != old) {
            json += "{";
            if (old.getAllow_all_client() == 1)
                json += "allow_client_to_client:'on',";
            else
                json += "allow_client_to_client:'off',";
          /*  if (old.getAllow_all_subnet() == 1) {
                json += "allow_all_nets:'on',";
            } else {
                json += "allow_all_nets:'off',";
            }*/
            json += "dynamic_ip:'" + old.getDynamic_ip() + "',";
         /*   json += "download_speed:'" + (old.getDownload_speed()==null?"":old.getDownload_speed()) + "',";
            json += "upload_speed:'" + (old.getUpload_speed()==null?"":old.getUpload_speed()) + "',";*/
            json += "static_ip:'" + old.getStatic_ip() + "'" + "}";
        }
        json += "]}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

}
