package com.hzih.sslvpn.web.action.sslvpn.service;

import com.hzih.sslvpn.service.LogService;
//import com.hzih.sslvpn.config.ShellUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-6
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class SnmpStatusAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(SnmpStatusAction.class);

    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public LogService getLogService() {
        return logService;
    }

    public String openServer() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "开启失败";
        try {
            Proc proc = new Proc();
            proc.exec("service snmpd start");
            Thread.sleep(1000 * 2);
            proc.exec("service snmpd status");
            String msg_on = proc.getOutput();
//            if (msg_on.contains("is running")) {
            if (msg_on.contains("running")) {
//            if (msg_on.contains("active")) {
                msg = "开启成功";
//                ShellUtils.start_snmpd();
            } else {
                msg = "开启失败";
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "开启失败";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String closeServer() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "关闭失败";
        try {
            Proc proc = new Proc();
            proc.exec("service snmpd stop");
            Thread.sleep(1000 * 2);
            proc.exec("service snmpd status");
            String msg_on = proc.getOutput();
//            if (!msg_on.contains("is running")) {
            if (!msg_on.contains("running")) {

//            if (msg_on.contains("active")) {
                msg = "关闭成功";
//                ShellUtils.stop_snmpd();
            } else {
                msg = "关闭失败";
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "关闭失败";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String checkServerStatus() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service snmpd status");
            String msg_on = proc.getOutput();
//            if (msg_on.contains("is running")) {
                if (msg_on.contains("running")) {
//            if (msg_on.contains("active")) {
                msg = "1";
            } else {
                msg = "0";
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "0";
        }
        StringBuilder sb = new StringBuilder("{'success':true,'totalCount':1,'root':[");
        sb.append("{");
        sb.append("service:'SNMP',");
        sb.append("status:'" + msg + "'");
        sb.append("}");
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String reloadServer() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "重启失败";
        try {
            Proc proc = new Proc();
            proc.exec("service snmpd restart");
            Thread.sleep(1000 * 2);
            try {
                proc.exec("service snmpd status");
                String msg_on = proc.getOutput();
//                if (msg_on.contains("is running")) {
                if (msg_on.contains("running")) {
//                if (msg_on.contains("active")) {
                    msg = "重启成功";
                } else {
                    msg = "重启失败";
                }
            } catch (Exception e) {
                logger.info(e.getMessage(),e);
                msg = "重启失败";
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "重启失败";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
