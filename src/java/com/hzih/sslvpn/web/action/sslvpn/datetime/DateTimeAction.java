package com.hzih.sslvpn.web.action.sslvpn.datetime;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 15-8-6.
 */
public class DateTimeAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(DateTimeAction.class);
    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String setDateTime()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String time = request.getParameter("time");
        String result = actionBase.actionBegin(request);
        String msg = "设置时间失败";
        String json = "{success:false,msg:'" + msg + "'}";
        Proc proc = new Proc();
        proc.exec("date -s "+time);
        if (proc.getResultCode() != -1) {
            if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                msg = "设置时间成功";
                json = "{success:true,msg:'" + msg + "'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "设置时间", msg);
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
    public String setNtpServer()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String host = request.getParameter("host");
        String result = actionBase.actionBegin(request);
        String msg = "同步时间失败";
        String json = "{success:false,msg:'" + msg + "'}";
        Proc proc = new Proc();
        proc.exec("ntpdate  "+host);
        if (proc.getResultCode() != -1) {
            if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                msg = "同步时间成功";
                json = "{success:true,msg:'" + msg + "'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "同步时间", msg);
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
