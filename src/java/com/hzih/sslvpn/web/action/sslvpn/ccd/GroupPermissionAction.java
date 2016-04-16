package com.hzih.sslvpn.web.action.sslvpn.ccd;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupDao;
import com.hzih.sslvpn.domain.Groups;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.service.LogService;
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
import java.util.Set;

public class GroupPermissionAction extends ActionSupport {
    private Logger logger = Logger.getLogger(GroupPermissionAction.class);

    private LogService logService;

    private int start;

    private int limit;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private GroupDao groupDao;

    private Groups group;

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }

    public String add()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        Groups old = groupDao.findByName(group.getGroup_name());
        if(null!=old){
            msg = "用户组已存在";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }else {
            groupDao.add(group);
            msg = "添加用户组成功";
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

    public String modify()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        Groups old = groupDao.findById(Integer.parseInt(id));
        if(null!=old){
             old.setGroup_name(group.getGroup_name());
             old.setGroup_desc(group.getGroup_desc());
             groupDao.modify(old);
            msg = "更新成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        }else {
            msg = "更新失败,未找到对应终端分组";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        Groups old = null;
        try {
            old = groupDao.findById(Integer.parseInt(id));
        } catch (Exception e) {

            msg = "删除失败,查找记录出现异常";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        try {
            if(old!=null) {
                groupDao.delete(old);
                msg = "删除成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }else {
                msg = "删除失败,未找到对应记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } catch (Exception e) {
            msg = "删除失败";
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

    public String find()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int pageIndex = start/limit+1;
        String group_name = request.getParameter("group_name");
        PageResult pageResult =  groupDao.listByPage(group_name,pageIndex,limit);
        if(pageResult!=null){
            List<Groups> list = pageResult.getResults();
            int count =  pageResult.getAllResultsAmount();
            if(list!=null){
                String  json= "{success:true,total:" + count + ",rows:[";
                Iterator<Groups> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    Groups log = raUserIterator.next();
                    if(raUserIterator.hasNext()){
                        json += "{" +
                                "id:'"+log.getId()+
                                "',deny_access:'" + log.getDeny_access() +
                                "',group_name:'" + log.getGroup_name() +
                                "',group_desc:'" + log.getGroup_desc() +"'" +
                                "},";
                    }else {
                        json += "{" +
                                "id:'"+log.getId()+
                                "',deny_access:'" + log.getDeny_access() +
                                "',group_name:'" + log.getGroup_name() +
                                "',group_desc:'" + log.getGroup_desc() +"'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }

    public String findUserByGroup()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String id = request.getParameter("id");
        Groups old = groupDao.findById(Integer.parseInt(id));
        Set<User> users = old.getUserSet();
        if(users!=null){
//            String  json= "{success:true,total:" + users.size() + ",rows:[";
            StringBuilder sb = new StringBuilder("{success:true,total:" + users.size() + ",rows:[");
            Iterator<User> userIterator = users.iterator();
            while (userIterator.hasNext()){
                User log = userIterator.next();
                if(userIterator.hasNext()){
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

                    sb.append("view_flag:").append("'").append(log.getView_flag()).append("'").append(",");
                    sb.append("gps_flag:").append("'").append(log.getGps_flag()).append("'").append(",");
//                    sb.append("level:").append("'").append(log.getLevel()).append("'").append(",");
                    sb.append("last_ref:").append("'").append(log.getLast_ref()).append("'");
                    sb.append("}");
                    sb.append(",");

                   /* json += "{" +
                            "id:'"+log.getId()+
                            "',cn:'" + log.getCn() +
//                            "',id_card:'" + log.getId_card() +
//                            "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()).toString() +
//                            "',deny_access:'" + log.getDeny_access() +
                            "',dynamic_ip:'" + log.getDynamic_ip() +
                            "',static_ip:'" + log.getStatic_ip() +
//                            "',allow_all_subnet:'" + log.getAllow_all_subnet() +
                            "',allow_all_client:'" + log.getAllow_all_client() +
//                            "',create_time:'" + log.getCreate_time() +
//                            "',count_bytes_cycle:'" + log.getQuota_cycle() +
//                            "',max_bytes:'" + log.getQuota_bytes() +
//                            "',active:'" + log.getActive() +
//                            "',email:'" + log.getEmail() +
//                            "',phone:'" + log.getPhone() +
//                            "',address:'" + log.getAddress() +
                            "',serial_number:'" + log.getSerial_number() +
//                            "',type:'" + log.getType() +
//                            "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
//                            "',revoked:'" + log.getRevoked() +
                            "',enabled:'" + log.getEnabled() +
                            "',real_address:'" + log.getReal_address() +
                            "',byte_received:'" + log.getByte_received() +
                            "',byte_send:'" + log.getByte_send() +
                            "',connected_since:'" + log.getConnected_since() +
                            "',virtual_address:'" + log.getVirtual_address() +
                            "',last_ref:'" + log.getLast_ref() +"'" +
                            "},";*/
                }else {


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

                    sb.append("view_flag:").append("'").append(log.getView_flag()).append("'").append(",");
                    sb.append("gps_flag:").append("'").append(log.getGps_flag()).append("'").append(",");
//                    sb.append("level:").append("'").append(log.getLevel()).append("'").append(",");
                    sb.append("last_ref:").append("'").append(log.getLast_ref()).append("'");
                    sb.append("}");
                    /*json += "{" +
                            "id:'"+log.getId()+
                            "',cn:'" + log.getCn() +
//                            "',id_card:'" + log.getId_card() +
//                            "',group_id:'" + JsonUtil.checkNull(log.getGroup_id()).toString() +
//                            "',deny_access:'" + log.getDeny_access() +
                            "',dynamic_ip:'" + log.getDynamic_ip() +
                            "',static_ip:'" + log.getStatic_ip() +
//                            "',allow_all_subnet:'" + log.getAllow_all_subnet() +
                            "',allow_all_client:'" + log.getAllow_all_client() +
//                            "',create_time:'" + log.getCreate_time() +
//                            "',count_bytes_cycle:'" + log.getQuota_cycle() +
//                            "',max_bytes:'" + log.getQuota_bytes() +
//                            "',active:'" + log.getActive() +
//                            "',email:'" + log.getEmail() +
//                            "',phone:'" + log.getPhone() +
//                            "',address:'" + log.getAddress() +
                            "',serial_number:'" + log.getSerial_number() +
//                            "',type:'" + log.getType() +
//                            "',key_size:'" + log.getKey_size() +
//                                "',cert:'" + log.getCert() +
//                                "',key:'" + log.getKey() +
//                            "',revoked:'" + log.getRevoked() +
                            "',enabled:'" + log.getEnabled() +
                            "',real_address:'" + log.getReal_address() +
                            "',byte_received:'" + log.getByte_received() +
                            "',byte_send:'" + log.getByte_send() +
                            "',connected_since:'" + log.getConnected_since() +
                            "',virtual_address:'" + log.getVirtual_address() +
                            "',last_ref:'" + log.getLast_ref() +"'" +
                            "}";*/
                }
            }
//            json += "]}";
            sb.append("]}");
            actionBase.actionEnd(response, sb.toString(), result);
        }
        return null;
    }

    public String disable() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        boolean old = groupDao.disable(Integer.parseInt(id));
        if (old) {
            Groups groups = null;
            try {
                groups = groupDao.findById(Integer.parseInt(id));
            } catch (Exception e) {
                msg = "禁用用户组失败,查找用户组出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.error(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
            if (null != groups) {
                groups.setDeny_access(1);
                VPNConfigUtil.configGroup(StringContext.ccd,groups);
                msg = "禁用用户组成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }else {
                msg = "禁用用户组失败,未找到用户组";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String enable()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        boolean old = groupDao.enable(Integer.parseInt(id));
        if (old) {
            Groups groups = null;
            try {
                groups = groupDao.findById(Integer.parseInt(id));
            } catch (Exception e) {

                msg = "启用用户组失败,查找用户组出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.error(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
            if (null != groups) {
                groups.setDeny_access(0);
                VPNConfigUtil.configGroup(StringContext.ccd, groups);
                msg = "启用用户组成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }else {
                msg = "启用用户组失败,未找到对应记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "终端分组", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "终端分组", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

}
