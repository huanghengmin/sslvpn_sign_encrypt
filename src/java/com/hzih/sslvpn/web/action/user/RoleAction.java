package com.hzih.sslvpn.web.action.user;


import com.hzih.sslvpn.domain.Role;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.RoleService;
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

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-8
 * Time: 下午12:24
 * To change this template use File | Settings | File Templates.
 */
public class RoleAction extends ActionSupport {
    private Logger logger = Logger.getLogger(RoleAction.class);
    private RoleService roleService;
    private LogService logService;
    private Role role;
    private int start;
    private int limit;

    public String insert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        try {
            String[] pIds = request.getParameterValues("pIds");
            role.setCreatedTime(new Date());
            json = roleService.insert(role,pIds);
            msg = "用户新增角色信息成功";
            json = "{success:true,msg:'"+msg+"'}";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","用户新增角色信息成功");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "角色管理", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                msg = "新增角色失败";
                json = "{success:true,msg:'" + msg + "'}";
                logger.error("角色管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户新增角色信息失败");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "角色管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String delete() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            json = roleService.delete(id);
            msg = "用户删除角色信息成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户删除角色信息成功");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "角色管理", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            msg = "删除角色失败";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("角色管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户删除角色信息失败");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "角色管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        try {
            String[] pIds = request.getParameterValues("pIds");
            json = roleService.update(role,pIds);
            msg = "用户更新角色信息成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户更新角色信息成功");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "角色管理", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            msg = "更新角色失败";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("角色管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户更新角色信息失败");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "角色管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String select() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            json = roleService.select(start ,limit);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","用户获取角色信息成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("角色管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户获取角色信息失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String readNameKeyValue() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            json = roleService.getNameKeyValue();
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户管理","用户获取所有角色名成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户获取所有角色名失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String permissionInsert() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            json = roleService.getPermissionInsert(start,limit);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","用户获取角色权限信息用于新增成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("角色管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户获取角色权限信息用于新增失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String permissionUpdate() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            json = roleService.getPermissionUpdate(start,limit,id);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","用户获取角色权限信息用于修改成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("角色管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户获取角色权限信息用于修改失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String check() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        try {
            String name = request.getParameter("name");
            json = roleService.checkRoleName(name);
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "角色管理","用户校验角色名是否存在成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("角色管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "角色管理", "用户校验角色名是否存在失败");
            }
            json = "{success:true,msg:'校验角色名是否存在失败'}";

        }
        actionBase.actionEnd(response, json, result);
        return null;
    }



    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
}
