package com.hzih.sslvpn.web.action.sslvpn.recovery;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.sslvpn.backup.mysql.MysqlBakUtils;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 15-5-27.
 */
public class RecoveryAction extends ActionSupport {
    private Logger logger = Logger.getLogger(RecoveryAction.class);
    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String recovery()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        try {
            MysqlBakUtils.recover_system();
            msg = "恢复出厂成功,服务将重启启动......";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统恢复", msg);
            }
            Proc proc;
            OSInfo osinfo = OSInfo.getOSInfo();
            if (osinfo.isWin()) {
                proc = new Proc();
                proc.exec("nircmd service restart "+StringContext.service_name);
            }
            if (osinfo.isLinux()) {
                proc = new Proc();
                proc.exec("service "+StringContext.service_name+" restart");
            }

        } catch (IOException e) {
            msg = "恢复出厂失败,恢复数据库时出现异常";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info(e.getMessage(),e);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "系统恢复", msg);
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }
}
