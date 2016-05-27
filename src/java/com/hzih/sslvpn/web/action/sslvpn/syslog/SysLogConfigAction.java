package com.hzih.sslvpn.web.action.sslvpn.syslog;

import com.hzih.sslvpn.entity.StatusMsg;
import com.hzih.sslvpn.entity.SysLogServer;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.syslog.sender.SyslogConfigXML;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hhm
 * Date: 12-10-26
 * Time: 下午4:01
 * To change this template use File | Settings | File Templates.
 */
public class SysLogConfigAction extends ActionSupport {
    private Logger logger = Logger.getLogger(SysLogConfigAction.class);
    private SysLogServer sysLogServer;
    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public SysLogServer getSysLogServer() {
        return sysLogServer;
    }

    public void setSysLogServer(SysLogServer sysLogServer) {
        this.sysLogServer = sysLogServer;
    }

    /**
     * @return
     * @throws Exception
     */
    public String add() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        SyslogConfigXML sysLogConfigXML = new SyslogConfigXML();
        StatusMsg statusMsg = sysLogConfigXML.add(StringContext.syslog_xml, this.sysLogServer);
        if (statusMsg.isFlag()) {
            msg = statusMsg.getMsg() + ",服务器主机:" + sysLogServer.getHost() + ",服务器端口:" + sysLogServer.getPort();
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
//            SyslogSender.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志服务器", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志服务器", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
            SiteContextLoaderServlet.restartSyslogSender();
        } else {
            msg = statusMsg.getMsg() + ",服务器主机:" + sysLogServer.getHost() + ",服务器端口:" + sysLogServer.getPort();
            json = "{success:false,msg:'" + msg + "'}";

            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
//            SyslogSender.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志服务器", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志服务器", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    /**
     * @return
     * @throws Exception
     */
    public String delete() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String json = null;
        String msg = null;
        String result = actionBase.actionBegin(request);
        String port = request.getParameter("port");
        String host = request.getParameter("host");
        SyslogConfigXML sysLogConfigXML = new SyslogConfigXML();
        StatusMsg statusMsg = sysLogConfigXML.delete(StringContext.syslog_xml, new SysLogServer(host, Integer.parseInt(port)));
        if (statusMsg.isFlag()) {
            msg = statusMsg.getMsg() + ",服务器主机:" + host + ",服务器端口:" + port;
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
//            SyslogSender.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志服务器", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志服务器", msg);
            }
            SiteContextLoaderServlet.restartSyslogSender();
        } else {
            msg = statusMsg.getMsg() + ",服务器主机:" + host + ",服务器端口:" + port;
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
//            SyslogSender.sysLog("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志服务器", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志服务器", msg);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /**
     * @return
     * @throws Exception
     */
    public String modify() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String port = request.getParameter("port");
        String host = request.getParameter("host");
        String json = null;
        String msg = new String();
        SyslogConfigXML sysLogConfigXML = new SyslogConfigXML();
        StatusMsg statusMsg = sysLogConfigXML.update(StringContext.syslog_xml, new SysLogServer(host, Integer.parseInt(port)), this.sysLogServer);
        if (statusMsg.isFlag()) {
            msg = statusMsg.getMsg() + ",服务器主机:" + host + ",服务器端口:" + port;
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志服务器", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志服务器", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
            SiteContextLoaderServlet.restartSyslogSender();
        } else {
            msg = statusMsg.getMsg() + ",服务器主机:" + host + ",服务器端口:" + port;
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "日志服务器", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "日志服务器", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /**
     * @param ipPort
     * @return
     */
    public String object(SysLogServer ipPort) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("host:'" + ipPort.getHost() + "',");
        sb.append("port:'" + ipPort.getPort() + "'");
        sb.append("}");
        return sb.toString();
    }

    /**
     * @param ipPorts
     * @return
     * @throws Exception
     */
    public String list_Json(List<SysLogServer> ipPorts) throws Exception {
        SysLogServer itIpPort = null;
        StringBuilder sb = new StringBuilder();
        Iterator<SysLogServer> ipPortIterator = ipPorts.iterator();
        while (ipPortIterator.hasNext()) {
            itIpPort = ipPortIterator.next();
            if (ipPortIterator.hasNext())
                sb.append(object(itIpPort)).append(",");
            else
                sb.append(object(itIpPort));
        }
        StringBuilder json = new StringBuilder();
        json.append("{totalCount:" + ipPorts.size() + ",root:[" + sb.toString() + "]}");
        return json.toString();
    }


    /**
     * @param first
     * @param limitInt
     * @param list
     * @return
     */
    public String getResultData(Integer first, Integer limitInt, List<SysLogServer> list) {
        StringBuffer showData = new StringBuffer();
        int end = first + limitInt;
        int index = end > list.size() ? list.size() : end;
        for (int i = first; i < index; i++) {
            showData.append(object(list.get(i)));
            if (i != index - 1) {
                showData.append(",");
            }
        }
        return showData.toString();
    }


    /**
     * @return
     * @throws Exception
     */
    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String sStart = request.getParameter("start");
        String sLimit = request.getParameter("limit");
        SyslogConfigXML sysLogConfigXML = new SyslogConfigXML();
        List<SysLogServer> ipPorts = sysLogConfigXML.findAll(StringContext.syslog_xml);
        int start = Integer.parseInt(sStart);
        int limit = Integer.parseInt(sLimit);
        String json = getResultData(start, limit, ipPorts);
        json = "{totalCount:" + ipPorts.size() + ",root:[" + json.toString() + "]}";
        actionBase.actionEnd(response, json.toString(), result);
        return null;
    }
}
