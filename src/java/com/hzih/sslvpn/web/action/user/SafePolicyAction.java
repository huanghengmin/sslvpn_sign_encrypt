package com.hzih.sslvpn.web.action.user;

import com.hzih.sslvpn.domain.SafePolicy;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.SafePolicyService;
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
 * Date: 12-6-11
 * Time: 上午9:47
 * To change this template use File | Settings | File Templates.
 */
public class SafePolicyAction extends ActionSupport{
    private static final Logger logger = Logger.getLogger(SafePolicyAction.class);
    private SafePolicy safePolicy;
    private LogService logService;
    private SafePolicyService safePolicyService;
//    private String remoteDisabled;
    private String macDisabled;

    public String select() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        try {
            json = safePolicyService.select();
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "安全策略","用户获取安全策略信息成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("安全策略", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "安全策略", "用户获取安全策略信息失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String selectPasswordRules() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
		String json = null;
        try {
            json = safePolicyService.selectPasswordRules();
//            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "用户管理","用户获取安全策略密码规则信息成功");
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("用户管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "用户管理", "用户获取安全策略密码规则信息失败");
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
		String msg = null;
        String json = null;
        try {
//            if(remoteDisabled!=null){
//                if(remoteDisabled.equals("on")){
//                    safePolicy.setRemoteDisabled(true);
//                } else {
//                    safePolicy.setRemoteDisabled(false);
//                }
//            } else {
//                safePolicy.setRemoteDisabled(false);
//            }
            if(macDisabled!=null ){
                if( macDisabled.equals("on")){
                    safePolicy.setMacDisabled(true);
                } else {
                    safePolicy.setMacDisabled(false);
                }
            } else {
                safePolicy.setMacDisabled(false);
            }
            msg = safePolicyService.update(safePolicy);
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "安全策略", "用户修改安全策略信息成功");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        } catch (Exception e) {
            msg = "<font color=\"red\">用户修改安全策略信息失败</font>";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("安全策略", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "安全策略", "用户修改安全策略信息失败");
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
//        String json = "{success:true,msg:'"+msg+"'}";
        macDisabled = null;
//        remoteDisabled = null;
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public SafePolicy getSafePolicy() {
        return safePolicy;
    }

    public void setSafePolicy(SafePolicy safePolicy) {
        this.safePolicy = safePolicy;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public SafePolicyService getSafePolicyService() {
        return safePolicyService;
    }

    public void setSafePolicyService(SafePolicyService safePolicyService) {
        this.safePolicyService = safePolicyService;
    }

//    public String getRemoteDisabled() {
//        return remoteDisabled;
//    }
//
//    public void setRemoteDisabled(String remoteDisabled) {
//        this.remoteDisabled = remoteDisabled;
//    }

    public String getMacDisabled() {
        return macDisabled;
    }

    public void setMacDisabled(String macDisabled) {
        this.macDisabled = macDisabled;
    }
}
