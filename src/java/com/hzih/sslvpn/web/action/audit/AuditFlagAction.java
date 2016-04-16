package com.hzih.sslvpn.web.action.audit;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * Created by Administrator on 15-6-16.
 */
public class AuditFlagAction extends ActionSupport {
    private static Logger logger = Logger.getLogger(AuditFlagAction.class);
    public static final String config = "config";
    public static final String audit_flag = "audit_flag";
    public static final String charset = "utf-8";
    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String save() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String audit_flag = request.getParameter("audit_flag");
        if(audit_flag!=null){
            audit_flag = "1";
        }else {
            audit_flag = "0";
        }
        boolean flag = save(audit_flag);
        if(flag){
            msg = "审计开启或关闭成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "审计管理", msg);
            }
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "审计管理", "info", "001", audit_flag, new Date());
                SyslogSender.sysLog(log);
        }else {
            msg = "审计开启或关闭失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "审计管理", msg);
            }
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "审计管理", "info", "001", audit_flag, new Date());
                SyslogSender.sysLog(log);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String find()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int totalCount =0;
        StringBuilder sb = new StringBuilder();
        jsonResult(sb);
        totalCount = totalCount+1;
        StringBuilder json=new StringBuilder("{totalCount:"+totalCount+",root:[");
        json.append(sb.toString());
        json.append("]}");
        actionBase.actionEnd(response,json.toString(),result);
        if(AuditFlagAction.getAuditFlag()) {
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "审计管理", "用户读取审计开关信息成功");
        }
        return null;
    }


    private void jsonResult(StringBuilder sb) {
        sb.append("{");
        sb.append("audit_flag:'"+ isNULL(getValue(audit_flag))).append("'");
        sb.append("}");
    }

    public String isNULL(Object o){
        if(o==null){
            return "";
        } else {
            return o.toString();
        }
    }


    public static String getValue(String name) {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        String result = null;
        try {
            doc = saxReader.read(new File(StringContext.audit_flag_xml));
        } catch (DocumentException e) {
            logger.error(e.getMessage(),e);
        }
        if(doc!=null){
            Element ldap = doc.getRootElement();
            Element el = ldap.element(name);
            result = el.getText();
        }
        return result;
    }

    public static boolean getAuditFlag() {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        String result = null;
        try {
            doc = saxReader.read(new File(StringContext.audit_flag_xml));
        } catch (DocumentException e) {
            logger.error(e.getMessage(),e);
            return true;
        }
        if(doc!=null){
            Element ldap = doc.getRootElement();
            Element el = ldap.element(audit_flag);
            result = el.getText();
            if(result!=null&&result.equals("1")){
                return true;
            }
        }
        return false;
    }

    public static boolean save(String audit_flag) {
        boolean flag = false;
        Document doc = DocumentHelper.createDocument();
        Element config = doc.addElement(AuditFlagAction.config);
        Element audit_flag_el = config.addElement(AuditFlagAction.audit_flag);
        audit_flag_el.addText(audit_flag);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        format.setIndent(true);
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.audit_flag_xml)), format);
            try {
                xmlWriter.write(doc);
                flag = true;
            } catch (IOException e) {
                logger.info(e.getMessage(),e);
            } finally {
                try {
                    xmlWriter.flush();
                    xmlWriter.close();
                } catch (IOException e) {
                   logger.error(e.getMessage(),e);
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage(),e);
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage(),e);
        }
        return flag;
    }
}
