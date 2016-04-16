package com.hzih.sslvpn.web.action.config;

import com.hzih.sslvpn.constant.AppConstant;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.JavaShortMessageUtil;
import com.hzih.sslvpn.utils.MailUtils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.SiteContext;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 报警配置
 */
public class AlertManagerAction extends ActionSupport{
    private static final Logger logger = LoggerFactory.getLogger(AlertManagerAction.class);

    private LogService logService;

    public String loadConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = null;
        try {
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));

            Map<Object, Object> model = new HashMap<Object, Object>();
            Node tempNode = document.selectSingleNode("//config/mailconfig/server");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/port");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/email");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/account");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/password");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/title");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/mailfrequency");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smsfrequency");
            model.put(tempNode.getName(), tempNode.getText());
            String modelStr = model.toString();
            json = "{success:true,";
            String[] fields = modelStr.split("\\{")[1].split("\\}")[0].split(",");
            int index = 0;
            for (int i = 0; i < fields.length; i++) {
                if(index == fields.length - 1){
                    json += "'"+ fields[i].split("=")[0].trim()+"':'"+fields[i].split("=")[1].trim()+"'";
                }else{
                    json += "'"+ fields[i].split("=")[0].trim()+"':'"+fields[i].split("=")[1].trim()+"',";
                }
                index ++;
            }
		    json +="}";
            if(AuditFlagAction.getAuditFlag()) {
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","用户获取报警配置信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户获取报警配置信息失败");
            }
        }
		base.actionEnd(response, json ,result);
        return null;
    }

    public String saveConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        SAXReader reader = new SAXReader();
		String fileName = SiteContext.getInstance().contextRealPath	+ AppConstant.XML_ALERT_CONFIG_PATH;
		Document document = reader.read(new File(fileName));
		request.getCharacterEncoding();
		response.setCharacterEncoding("UTF-8");
		Element root = document.getRootElement();
		XMLWriter writer = null;
		try {
			Node tempNode = document.selectSingleNode("//config/mailconfig/server");
			tempNode.setText(request.getParameter("server"));
			tempNode = document.selectSingleNode("//config/mailconfig/port");
			tempNode.setText(request.getParameter("port"));
			tempNode = document.selectSingleNode("//config/mailconfig/email");
			tempNode.setText(request.getParameter("email"));
			tempNode = document.selectSingleNode("//config/mailconfig/account");
			tempNode.setText(request.getParameter("account"));
			tempNode = document.selectSingleNode("//config/mailconfig/password");
			tempNode.setText(request.getParameter("password"));
			tempNode = document.selectSingleNode("//config/mailconfig/title");
			tempNode.setText(request.getParameter("title"));
			tempNode = document.selectSingleNode("//config/mailconfig/mailfrequency");
			tempNode.setText(request.getParameter("mailfrequency"));
			tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
			tempNode.setText(request.getParameter("smsnumber"));
			tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
			tempNode.setText(request.getParameter("smstitle"));
			tempNode = document.selectSingleNode("//config/smsconfig/smsfrequency");
			tempNode.setText(request.getParameter("smsfrequency"));

			OutputFormat format = OutputFormat.createPrettyPrint();

            format.setEncoding("UTF-8");

			writer = new XMLWriter(new FileOutputStream(fileName),format);

			writer.write(document);
			writer.close();
            msg = "用户修改报警配置信息成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户修改报警配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String validateEmail() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        String subject = " 测试邮件 ";
		String text = " 这是一封测试邮件 ";
		String contact = request.getParameter("contact");

		SAXReader reader = new SAXReader();
		String fileName = SiteContext.getInstance().contextRealPath
				+ AppConstant.XML_ALERT_CONFIG_PATH;
		Document document = reader.read(new File(fileName));
		try {
			Map<String, String> model = new HashMap<String, String>();
            Node tempNode = document.selectSingleNode("//config/mailconfig/server");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/port");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/email");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/account");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/password");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/title");
            model.put(tempNode.getName(), tempNode.getText());

            tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
            model.put(tempNode.getName(), tempNode.getText());

            String emailServer = model.get("server");
            String connectName = model.get("account");
            String password = model.get("password");
            String port = model.get("port");
            boolean isNeedAuth = true;
            String fromEmail = model.get("email");
            String toEmail = contact;
            String title = subject;
            boolean isSuccess = MailUtils.sendSimpleEmail(emailServer, Integer.parseInt(port), connectName, password, isNeedAuth, fromEmail, toEmail, null, null, "utf-8", title, text);
            if(isSuccess){
                msg = "测试邮件已发出,请注意查收!";
            } else {
                msg = "测试邮件发送失败";
            }

            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户测试邮件发送报警信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户测试邮件发送报警信息失败");
            }
            msg = "测试邮件发送失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String validateShortMessage() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        String subject = " 测试短信 ";
		String text = " 测试短信 ";
		String contact = request.getParameter("contact");

		SAXReader reader = new SAXReader();
		String fileName = SiteContext.getInstance().contextRealPath
				+ AppConstant.XML_ALERT_CONFIG_PATH;
		Document document = reader.read(new File(fileName));
		try {
			Map<String, String> model = new HashMap<String, String>();
            Node tempNode = document.selectSingleNode("//config/mailconfig/server");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/port");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/email");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/account");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/password");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mailconfig/title");
            model.put(tempNode.getName(), tempNode.getText());

            tempNode = document.selectSingleNode("//config/smsconfig/smsnumber");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/smsconfig/smstitle");
            model.put(tempNode.getName(), tempNode.getText());

            JavaShortMessageUtil.sendMessage(model, subject, text, contact);

            msg = "测试短信已发出,请注意查收!";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户测试短信发送报警信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置","用户测试短信发送报警信息失败");
            }
            msg = "测试短信发送失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String queryEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String json = null;
        try {
            int start = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("limit"));
            json = getQueryEmails(start,limit);
            if(AuditFlagAction.getAuditFlag()) {
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","用户查找收件人列表信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户查找收件人列表信息失败");
            }
            json = "{success:true:total:0,rows:[]}";
        }
		base.actionEnd(response, json ,result);
        return null;
    }

    private String getQueryEmails(int start, int limit) throws DocumentException {
        SAXReader reader = new SAXReader();
		String fileName = SiteContext.getInstance().contextRealPath
				+ AppConstant.XML_ALERT_CONFIG_PATH;
		Document document = reader.read(new File(fileName));
        List<Node> emails = document.selectNodes("//config/mailconfig/emails/email");
        int index = 0;
        int count = 0;
        String json = "{success:true,total:"+emails.size()+",rows:[";
        for(Node email : emails){
            if(index==start && count < limit) {
                json += "{email:'"+email.getText()+"',flag:2},";
                start ++;
                count ++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    public String checkEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath
                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            List<Node> emails = document.selectNodes("//config/mailconfig/emails/email");
            String email = request.getParameter("email");
            for(Node e : emails){
                if(e.getText().equals(email)) {
                    msg = "Email已经存在";
                    break;
                }
            }
            if(msg == null) {
                msg = "0000";
            }
            if(AuditFlagAction.getAuditFlag()) {
            logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置","用户校验email成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户校验email失败");
            }
            msg = "校验email失败,请稍后再试!";
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String deleteEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath
                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            Element emails = (Element) document.selectSingleNode("//config/mailconfig/emails");
            String[] emailArray = request.getParameterValues("emailArray");
            for(int i=0;i<emailArray.length;i++) {
                List<Element> _emails = document.selectNodes("//config/mailconfig/emails/email");
                for(Element e : _emails) {
                    if(emailArray[i].equals(e.getText())) {
                        emails.remove(e);
                        break;
                    }
                }
            }
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter output = new XMLWriter(new FileOutputStream(file),format);
            if(document != null){
                output.write(document);
            }
            output.close();
            msg = "用删除Email地址成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "删除Email地址失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg + e.getMessage());
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String insertEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath
                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            Element emails = (Element) document.selectSingleNode("//config/mailconfig/emails");
            String[] emailArray = request.getParameterValues("emailArray");
            for(int i=0;i<emailArray.length;i++) {
                List<Element> _emails = document.selectNodes("//config/mailconfig/emails/email");
                boolean isExist = false;
                for(Element e : _emails) {
                    if(emailArray[i].equals(e.getText())) {
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    Element child = emails.addElement("email");
                    child.setText(emailArray[i]);
                }
            }
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter output = new XMLWriter(new FileOutputStream(file),format);
            if(document != null){
                output.write(document);
            }
            output.close();
            msg = "用户新增Email地址成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户新增Email地址失败!";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg + e.getMessage());
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public String updateEmail() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		String msg = null;
        try {
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath
                    + AppConstant.XML_ALERT_CONFIG_PATH;
            Document document = reader.read(new File(fileName));
            String oldEmail = request.getParameter("oldEmail");
            String email = request.getParameter("email");
            List<Element> emails = document.selectNodes("//config/mailconfig/emails/email");
            for(Element e : emails) {
                if(oldEmail.equals(e.getText())) {
                    e.setText(email);
                    break;
                }
            }
            File file = new File(fileName);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter output = new XMLWriter(new FileOutputStream(file),format);
            if(document != null){
                output.write(document);
            }
            output.close();
            msg = "用户修改Email地址成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO",  SessionUtils.getAccount(request).getUserName(), "报警配置",msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(),msg,"报警配置","info","004","1",new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "修改Email地址失败,请稍后再试!";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg + e.getMessage());
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'"+msg+"'}";
		base.actionEnd(response, json ,result);
        return null;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
