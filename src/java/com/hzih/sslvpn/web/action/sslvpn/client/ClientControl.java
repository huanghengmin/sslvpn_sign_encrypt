package com.hzih.sslvpn.web.action.sslvpn.client;

import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.User;
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
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-29
 * Time: 下午12:48
 * To change this template use File | Settings | File Templates.
 */
public class ClientControl extends ActionSupport {
    private Logger logger = Logger.getLogger(ClientControl.class);

    private UserDao userDao;

    private LogService logService;

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

    public String view()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String json = null;
        String msg = null;
        String result =	actionBase.actionBegin(request);
        String cn = request.getParameter("cn");
        User user = userDao.findByCommonName(cn);
        if(null!=user) {
            user.setView_flag(1);
            boolean flag = userDao.modify(user);
            if (flag) {
                msg = "启用客户端截屏成功,"+cn;
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }else {
                msg = "启用客户端截屏失败";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

     public String kill()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String cn = request.getParameter("cn");
        Proc kill_proc = new Proc();
        String kill_command = "sh "+ StringContext.systemPath+"/script/kill_user.sh "+cn;
        kill_proc.exec(kill_command);
         msg = "暂时切断客户端访问成功."+cn;
         json = "{success:true,msg:'" + msg + "'}";
         if(AuditFlagAction.getAuditFlag()) {
             logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
             logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
             String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "1", new Date());
             SyslogSender.sysLog(log);
         }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String disable()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String cn = request.getParameter("cn");
        if(null!=cn){
            User user = userDao.findByCommonName(cn);
            if(null!=user){
                user.setEnabled(0);
                boolean flag = userDao.disableUser(user.getId());
                if(flag) {
                    Proc kill_proc = new Proc();
                    String kill_command = "sh " + StringContext.systemPath + "/script/kill_user.sh " + cn;
                    kill_proc.exec(kill_command);
                    VPNConfigUtil.configUser(user, StringContext.ccd);
                    Thread.sleep(2 * 1000);
                    msg = "禁止客户端" + user.getCn() + "访问成功," + cn;
                    json = "{success:true,msg:'" + msg + "'}";
                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "1", new Date());
                        SyslogSender.sysLog(log);
                    }
                }else {
                    msg = "禁止客户端" + user.getCn() + "访问失败," + cn;
                    json = "{success:false,msg:'" + msg + "'}";
                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                }
            }else {
                msg ="禁止客户端"+cn+"访问失败,未找到对应用户";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "0", new Date());
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
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String cn = request.getParameter("cn");
        if(null!=cn){
            User user = userDao.findByCommonName(cn);
            if(null!=user){
                user.setEnabled(1);
                boolean flag = userDao.enableUser(user.getId());
                if(flag) {
                    VPNConfigUtil.configUser(user, StringContext.ccd);
                    msg = "启用客户端" + user.getCn() + "访问成功," + cn;
                    json = "{success:true,msg:'" + msg + "'}";
                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "1", new Date());
                        SyslogSender.sysLog(log);
                    }
                }else {
                    msg = "启用客户端" + user.getCn() + "访问失败," + cn;
                    json = "{success:false,msg:'" + msg + "'}";
                    if (AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                }
            }else {
                msg ="启用客户端"+cn+"访问失败,未找到对应用户";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户控制", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户控制", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
