package com.hzih.sslvpn.web.action.system;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.OSReBoot;
import com.inetec.common.util.OSShutDown;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
public class PlatformAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(PlatformAction.class);
    private LogService logService;

    /**
     * 系统重启
     * @return
     * @throws Exception
     */
    public String sysRestart() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "平台管理", "用户重启系统成功 ");
            }
            Proc proc;
            OSInfo osinfo = OSInfo.getOSInfo();
            if (osinfo.isWin()) {
                proc = new Proc();
                proc.exec("nircmd service restart "+ StringContext.service_name);
            }
            if (osinfo.isLinux()) {
                proc = new Proc();
                proc.exec("service "+StringContext.service_name+" restart");
            }
            Thread.sleep(1000*6);
            msg = "重启系统成功";
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("平台管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "平台管理", "用户重启系统失败 ");
            }
            msg = "重启系统失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    /**
     * 设备重启
     * @return
     * @throws Exception
     */
    public String equipRestart() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "平台管理", "用户重启设备成功 ");
            }
            OSReBoot.exec();
            Thread.sleep(1000*6);
            msg = "重启设备成功";
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("平台管理", e);
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "平台管理", "用户重启设备失败 ");
            }
            msg = "重启设备失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    /**
     * 设备关闭
     * @return
     * @throws Exception
     */
    public String equipShutdown() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        try {
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "平台管理","用户关闭设备成功 ");
            OSShutDown.exec();
            Thread.sleep(1000*6);
            msg = "关闭设备成功";
        } catch (Exception e) {
            logger.error("平台管理", e);
            logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "平台管理","用户关闭设备失败 ");
            msg = "关闭设备失败";
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
