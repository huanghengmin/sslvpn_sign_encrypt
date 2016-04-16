package com.hzih.sslvpn.web.action.sslvpn.backup;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.domain.BackUp;
import com.hzih.sslvpn.service.AccountService;
import com.hzih.sslvpn.service.BackUpService;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 15-3-31.
 */
public class BackUpAction extends ActionSupport {
    private Logger logger = Logger.getLogger(BackUpAction.class);
    private BackUpService backUpService;
    private AccountService accountService;
    private LogService logService;

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public BackUpService getBackUpService() {
        return backUpService;
    }

    public void setBackUpService(BackUpService backUpService) {
        this.backUpService = backUpService;
    }

    public String backup() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String backup_all = request.getParameter("backup_all");
        if(backup_all==null){
            backup_all = "0";
        }
        String backup_server = request.getParameter("backup_server");
        if(backup_server==null){
            backup_server = "0";
        }
        String backup_pki = request.getParameter("backup_pki");
        if(backup_pki==null){
            backup_pki = "0";
        }
        String backup_net = request.getParameter("backup_net");
        if(backup_net==null){
            backup_net = "0";
        }
        String backup_desc = request.getParameter("backup_desc");

        Account account = SessionUtils.getAccount(request);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String time = format.format(new Date());

        if(backup_all.equals("0")&&backup_server.equals("0")&&backup_pki.equals("0")&&backup_net.equals("0")){
            msg = "备份失败,未选择备份内容,备份时间" + time;
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统备份", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统备份", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }else {
            String file_name = "backup" + "_" + time + ".tar.gz";
            boolean flag = BakRestoreUtils.bak(StringContext.systemPath, file_name,backup_all,backup_server,backup_pki,backup_net);
            File file = new File(StringContext.systemPath + "/" + file_name);
            if (file.exists() && flag) {
                BackUp backUp = new BackUp();
                backUp.setBackup_account_id(account.getId());
                backUp.setBackup_all(Integer.parseInt(backup_all));
                backUp.setBackup_server(Integer.parseInt(backup_server));
                backUp.setBackup_pki(Integer.parseInt(backup_pki));
                backUp.setBackup_net(Integer.parseInt(backup_net));
                backUp.setBackup_desc(backup_desc);
                backUp.setBackup_file(file_name);
                backUp.setBackup_time(time);
                try {
                    boolean f = backUpService.add(backUp);
                    if (f) {
                        msg = "备份成功,备份时间" + time;
                        json = "{success:true,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统备份", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统备份", "info", "004", "1", new Date());
                            SyslogSender.sysLog(log);
                        }
                    } else {
                        msg = "备份失败,备份时间" + time;
                        json = "{success:false,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统备份", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统备份", "info", "004", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                } catch (Exception e) {

                    msg = "备份失败,备份时间" + time;
                    json = "{success:false,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.error(e.getMessage(),e);
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统备份", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统备份", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                }
            } else {
                msg = "备份失败,备份时间" + time;
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统备份", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统备份", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String recover() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        try {
            BackUp backUp = backUpService.findById(Integer.parseInt(id));
            if (backUp != null) {
                String back_file = backUp.getBackup_file();
                File file = new File(StringContext.systemPath + "/" + back_file);
                if (file.exists()) {
                    boolean flag = BakRestoreUtils.bakRestore(StringContext.systemPath, back_file,backUp.getBackup_all(),backUp.getBackup_server(),backUp.getBackup_pki(),backUp.getBackup_net());
                    if (flag) {
                        msg = "恢复成功";
                        json = "{success:true,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统恢复", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统恢复", "info", "004", "1", new Date());
                            SyslogSender.sysLog(log);
                        }
                    } else {
                        msg = "恢复失败,操作过各出现错误";
                        json = "{success:false,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统恢复", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统恢复", "info", "004", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                } else {
                    msg = "恢复失败,未找到对应备份文件";
                    json = "{success:false,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统恢复", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统恢复", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                }
            } else {
                msg = "恢复失败,未找到对应数据";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统恢复", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统恢复", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } catch (Exception e) {
            msg = "恢复失败,未找到对应数据";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统恢复", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "系统恢复", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findBackUp() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String json = null;
        PageResult pageResult = backUpService.findByPages(Integer.parseInt(start), Integer.parseInt(limit));
        if (pageResult != null) {
            List<BackUp> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                json = "{success:true,total:" + count + ",rows:[";
                Iterator<BackUp> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    BackUp log = raUserIterator.next();
                    Account account = accountService.getAccountById(log.getBackup_account_id());
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',backup_all:'" + log.getBackup_all() +
                                "',backup_server:'" + log.getBackup_server() +
                                "',backup_pki:'" + log.getBackup_pki() +
                                "',backup_net:'" + log.getBackup_net() +
                                "',backup_desc:'" + log.getBackup_desc() +
                                "',backup_time:'" + log.getBackup_time() +
                                "',backup_account_id:'" + account.getName() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',backup_all:'" + log.getBackup_all() +
                                "',backup_server:'" + log.getBackup_server() +
                                "',backup_pki:'" + log.getBackup_pki() +
                                "',backup_net:'" + log.getBackup_net() +
                                "',backup_desc:'" + log.getBackup_desc() +
                                "',backup_time:'" + log.getBackup_time() +
                                "',backup_account_id:'" + account.getName() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String delete() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String id = request.getParameter("id");
        try {
            BackUp backUp = backUpService.findById(Integer.parseInt(id));
            if (backUp != null) {
                String back_file = backUp.getBackup_file();
                File file = new File(StringContext.systemPath + "/" + back_file);
                if (file.exists()) {
                    boolean flag = file.delete();
                    if (flag) {
                        backUpService.delete(backUp);
                        msg = "删除成功";
                        json = "{success:true,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "删除备份", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "删除备份", "info", "004", "1", new Date());
                            SyslogSender.sysLog(log);
                        }
                    } else {
                        msg = "删除失败,删除备份文件出错";
                        json = "{success:false,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "删除备份", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "删除备份", "info", "004", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                } else {
                    boolean f = backUpService.delete(backUp);
                    if (f) {
                        msg = "删除成功";
                        json = "{success:true,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "删除备份", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "删除备份", "info", "004", "1", new Date());
                            SyslogSender.sysLog(log);
                        }
                    } else {
                        msg = "删除失败";
                        json = "{success:false,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "删除备份", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "删除备份", "info", "004", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                }
            } else {
                msg = "删除失败,未找到备份记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "删除备份", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "删除备份", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } catch (Exception e) {
            msg = "删除失败,查找备份记录出现异常";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "删除备份", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "删除备份", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
