package com.hzih.sslvpn.web.action.sslvpn.crl;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.Dom4jUtil;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.sslvpn.ldap.LdapCRLDownload;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-6
 * Time: 下午1:33
 * To change this template use File | Settings | File Templates.
 */
public class RevokeAction extends ActionSupport {
    private Logger logger = Logger.getLogger(RevokeAction.class);
    private LogService logService;
//    private UserDao userDao;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    private File crlFile;
    private String crlFileFileName;
    // 使用列表保存多个上传文件的MIME类型
    private String crlFileContentType;

    public File getCrlFile() {
        return crlFile;
    }

    public void setCrlFile(File crlFile) {
        this.crlFile = crlFile;
    }

    public String getCrlFileFileName() {
        return crlFileFileName;
    }

    public void setCrlFileFileName(String crlFileFileName) {
        this.crlFileFileName = crlFileFileName;
    }

    public String getCrlFileContentType() {
        return crlFileContentType;
    }

    public void setCrlFileContentType(String crlFileContentType) {
        this.crlFileContentType = crlFileContentType;
    }

  /*  public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }*/

    /*public String get_revoke() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        StringBuilder sb = new StringBuilder();
        File crl_file = new File(StringContext.crl_file);
        if (crl_file.exists() && crl_file.length() > 0) {
            FileInputStream fis = new FileInputStream(crl_file);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509CRL aCrl = (X509CRL) cf.generateCRL(fis);
            Set tSet = aCrl.getRevokedCertificates();
            Iterator tIterator = tSet.iterator();
            sb.append("{success:true,total:" + tSet.size() + ",rows:[");
            while (tIterator.hasNext()) {
                X509CRLEntry tEntry = (X509CRLEntry) tIterator.next();
                String sn = tEntry.getSerialNumber().toString(16).toUpperCase();
                String issName = aCrl.getIssuerDN().toString();
                String time = new SimpleDateFormat("yyyy年MM月dd日HH日mm分ss秒").format(tEntry.getRevocationDate());
                User user = userDao.findBySerialNumber(sn);
                if (null != user) {
                    sb.append("{");
                    sb.append("username:'" + user.getCn() + "'").append(",");
                    sb.append("serial:'" + sn + "'").append(",");
                    sb.append("id_card:'" + user.getId_card() + "'").append(",");
                    sb.append("revoke_time:'" + time + "'").append(",");
                    sb.append("iss_name:'" + issName + "'").append("");
                    sb.append("},");
                }
            }
            fis.close();
            String json = "";
            if (sb.toString().endsWith(",")) {
                json = sb.toString().substring(0, sb.length() - 1);
            }
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }*/

    public String update_crl() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        File f = new File(StringContext.crl_path + "/" + crlFileFileName);
        if (f.exists()) {
            msg = "文件已在在,请改名后上传";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        } else {
//            boolean crlFlag = FileUtil.saveUploadFile(crlFile, StringContext.crl_file);
            boolean crlFlag = FileUtil.saveUploadFile(crlFile, StringContext.crl_path + "/" + crlFileFileName);
            if (crlFlag) {
                msg = "上传CRL列表成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                msg = "上传CRL列表失败";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /*public String down_crl() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String url = request.getParameter("url");
        if (url != null) {
            CrlTimingUpdate crlTimingUpdate = new CrlTimingUpdate();
            boolean flag = crlTimingUpdate.down_crl(url);
            if (flag) {
                msg = "下载CRL列表成功";
                json = "{success:true,msg:'" + msg + "'}";
            } else {
                msg = "下载CRL列表失败";
                json = "{success:false,msg:'" + msg + "'}";
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }*/

    public String addLdapPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String name = request.getParameter("name");
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                msg = "添加LDAP下载点失败,LDAP配置名称已在在,请更换";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                String ldap_host = request.getParameter("ldap_host");
                String ldap_port = request.getParameter("ldap_port");
                String ldap_adm = request.getParameter("ldap_adm");
                String ldap_pwd = request.getParameter("ldap_pwd");
                String ldap_search_path = request.getParameter("ldap_search_path");
                String ldap_filter_string = request.getParameter("ldap_filter_string");
                String ldap_attribute_name = request.getParameter("ldap_attribute_name");

                Element config = doc.getRootElement();
                Element crlDownload_el = config.addElement("crldownload");
                crlDownload_el.addAttribute("name", name);
                crlDownload_el.addAttribute("type", "ldap");
                crlDownload_el.addAttribute("late_time", "");
                crlDownload_el.addAttribute("status", "0");

                Element ldap_host_el = crlDownload_el.addElement("ldap_host");
                ldap_host_el.setText(ldap_host);
                Element ldap_port_el = crlDownload_el.addElement("ldap_port");
                ldap_port_el.setText(ldap_port);
                Element ldap_adm_el = crlDownload_el.addElement("ldap_adm");
                ldap_adm_el.setText(ldap_adm);
                Element ldap_pwd_el = crlDownload_el.addElement("ldap_pwd");
                ldap_pwd_el.setText(ldap_pwd);
                Element ldap_search_path_el = crlDownload_el.addElement("ldap_search_path");
                ldap_search_path_el.setText(ldap_search_path);
                Element ldap_filter_string_el = crlDownload_el.addElement("ldap_filter_string");
                ldap_filter_string_el.setText(ldap_filter_string);
                Element ldap_attribute_name_el = crlDownload_el.addElement("ldap_attribute_name");
                ldap_attribute_name_el.setText(ldap_attribute_name);
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "添加LDAP下载点成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {

            String ldap_host = request.getParameter("ldap_host");
            String ldap_port = request.getParameter("ldap_port");
            String ldap_adm = request.getParameter("ldap_adm");
            String ldap_pwd = request.getParameter("ldap_pwd");
            String ldap_search_path = request.getParameter("ldap_search_path");
            String ldap_filter_string = request.getParameter("ldap_filter_string");
            String ldap_attribute_name = request.getParameter("ldap_attribute_name");

            Document document = DocumentHelper.createDocument();
            Element config = document.addElement("config");
            Element crlDownload_el = config.addElement("crldownload");
            crlDownload_el.addAttribute("name", name);
            crlDownload_el.addAttribute("type", "ldap");
            crlDownload_el.addAttribute("late_time", "");
            crlDownload_el.addAttribute("status", "0");

            Element ldap_host_el = crlDownload_el.addElement("ldap_host");
            ldap_host_el.setText(ldap_host);
            Element ldap_port_el = crlDownload_el.addElement("ldap_port");
            ldap_port_el.setText(ldap_port);
            Element ldap_adm_el = crlDownload_el.addElement("ldap_adm");
            ldap_adm_el.setText(ldap_adm);
            Element ldap_pwd_el = crlDownload_el.addElement("ldap_pwd");
            ldap_pwd_el.setText(ldap_pwd);
            Element ldap_search_path_el = crlDownload_el.addElement("ldap_search_path");
            ldap_search_path_el.setText(ldap_search_path);
            Element ldap_filter_string_el = crlDownload_el.addElement("ldap_filter_string");
            ldap_filter_string_el.setText(ldap_filter_string);
            Element ldap_attribute_name_el = crlDownload_el.addElement("ldap_attribute_name");
            ldap_attribute_name_el.setText(ldap_attribute_name);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            format.setIndent(true);
            try {
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.crl_xml)), format);
                try {
                    xmlWriter.write(document);
                    msg = "添加规则成功";
                    json = "{success:true,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                        SyslogSender.sysLog(log);
                    }

                } catch (IOException e) {
//                    logger.info(e.getMessage());
                    msg = "添加规则失败,出现异常";
                    json = "{success:false,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info(e.getMessage(),e);
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                } finally {
                    try {
                        xmlWriter.flush();
                        xmlWriter.close();
                    } catch (IOException e) {
//                        logger.info(e.getMessage());
                        msg = "添加规则失败,出现异常";
                        json = "{success:false,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info(e.getMessage(),e);
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
//                logger.info(e.getMessage());
                msg = "添加规则失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            } catch (FileNotFoundException e) {
//                logger.info(e.getMessage());
                msg = "添加规则失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String addHttpPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String name = request.getParameter("name");
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                msg = "添加HTTP下载点失败,HTTP配置名称已在在,请更换";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                String url = request.getParameter("url");

                Element config = doc.getRootElement();
                Element crlDownload_el = config.addElement("crldownload");
                crlDownload_el.addAttribute("name", name);
                crlDownload_el.addAttribute("type", "http");
                crlDownload_el.addAttribute("late_time", "");
                crlDownload_el.addAttribute("status", "0");

                Element url_el = crlDownload_el.addElement("url");
                url_el.setText(url);
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "添加LDAP下载点成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            String url = request.getParameter("url");
            Document document = DocumentHelper.createDocument();
            Element config = document.addElement("config");
            Element crlDownload_el = config.addElement("crldownload");
            crlDownload_el.addAttribute("name", name);
            crlDownload_el.addAttribute("type", "http");
            crlDownload_el.addAttribute("late_time", "");
            crlDownload_el.addAttribute("status", "0");

            Element url_el = crlDownload_el.addElement("url");
            url_el.setText(url);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            format.setIndent(true);
            try {
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.crl_xml)), format);
                try {
                    xmlWriter.write(document);
                    msg = "添加规则成功";
                    json = "{success:true,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                        SyslogSender.sysLog(log);
                    }
                } catch (IOException e) {

                    msg = "添加规则失败,出现异常";
                    json = "{success:false,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info(e.getMessage(),e);
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                } finally {
                    try {
                        xmlWriter.flush();
                        xmlWriter.close();
                    } catch (IOException e) {
//                        logger.info(e.getMessage(),e);
                        msg = "添加规则失败,出现异常";
                        json = "{success:false,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info(e.getMessage(),e);
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
//                logger.info(e.getMessage());
                msg = "添加规则失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            } catch (FileNotFoundException e) {
//                logger.info(e.getMessage());
                msg = "添加规则失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        StringBuilder sb = new StringBuilder();
        String json = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            List<Element> selectNodes = doc.selectNodes("/config/crldownload");
            if (selectNodes != null && selectNodes.size() > 0) {
                sb.append("{success:true,total:" + selectNodes.size() + ",rows:[");
                for (Element element : selectNodes) {
                    sb.append("{");
                    sb.append("name:'" + element.attributeValue("name") + "'").append(",");
                    sb.append("type:'" + element.attributeValue("type") + "'").append(",");
                    sb.append("late_time:'" + element.attributeValue("late_time") + "'").append(",");
                    sb.append("status:'" + element.attributeValue("status") + "'").append("");
                    sb.append("},");
                }
            }
            if (sb.toString().endsWith(",")) {
                json = sb.toString().substring(0, sb.length() - 1);
            }
            json += "]}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findLdapPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            Element ldap_host_el = selectSingleNode.element("ldap_host");
            Element ldap_port_el = selectSingleNode.element("ldap_port");
            Element ldap_adm_el = selectSingleNode.element("ldap_adm");
            Element ldap_pwd_el = selectSingleNode.element("ldap_pwd");
            Element ldap_search_path_el = selectSingleNode.element("ldap_search_path");
            Element ldap_filter_string_el = selectSingleNode.element("ldap_filter_string");
            Element ldap_attribute_name_el = selectSingleNode.element("ldap_attribute_name");
            StringBuilder sb = new StringBuilder("[");
            sb.append("{");
            sb.append("name:'" + name + "',");
            sb.append("type:'" + selectSingleNode.attributeValue("type") + "',");
            sb.append("status:'" + selectSingleNode.attributeValue("status") + "',");
            sb.append("late_time:'" + selectSingleNode.attributeValue("late_time") + "',");
            sb.append("ldap_host:'" + ldap_host_el.getText() + "',");
            sb.append("ldap_port:'" + ldap_port_el.getText() + "',");
            sb.append("ldap_adm:'" + ldap_adm_el.getText() + "',");
            sb.append("ldap_pwd:'" + ldap_pwd_el.getText() + "',");
            sb.append("ldap_search_path:'" + ldap_search_path_el.getText() + "',");
            sb.append("ldap_filter_string:'" + ldap_filter_string_el.getText() + "',");
            sb.append("ldap_attribute_name:'" + ldap_attribute_name_el.getText() + "'");
            sb.append("}");
            sb.append("]");
            json = sb.toString();
        } else {
            msg = "查找指定记录失败";
            json = "{success:false,msg:'" + msg + "'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findHttpPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            Element url_el = selectSingleNode.element("url");
            StringBuilder sb = new StringBuilder("[");
            sb.append("{");
            sb.append("name:'" + name + "',");
            sb.append("type:'" + selectSingleNode.attributeValue("type") + "',");
            sb.append("status:'" + selectSingleNode.attributeValue("status") + "',");
            sb.append("late_time:'" + selectSingleNode.attributeValue("late_time") + "',");
            sb.append("url:'" + url_el.getText() + "'");
            sb.append("}");
            sb.append("]");
            json = sb.toString();
        } else {
            msg = "未找到指定记录";
            json = "{success:false,msg:'" + msg + "'}";
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String deletePoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                Element config = doc.getRootElement();
                config.remove(selectSingleNode);
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "删除成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                msg = "未找到指定记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            msg = "未找到指定记录";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String updateLdapPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                String ldap_host = request.getParameter("ldap_host");
                String ldap_port = request.getParameter("ldap_port");
                String ldap_adm = request.getParameter("ldap_adm");
                String ldap_pwd = request.getParameter("ldap_pwd");
                String ldap_search_path = request.getParameter("ldap_search_path");
                String ldap_filter_string = request.getParameter("ldap_filter_string");
                String ldap_attribute_name = request.getParameter("ldap_attribute_name");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String date = simpleDateFormat.format(new Date());
                Attribute attribute = selectSingleNode.attribute("late_time");
                if (attribute == null)
                    selectSingleNode.addAttribute("late_time", date);
                else
                    attribute.setValue(date);

                Element ldap_host_el = selectSingleNode.element("ldap_host");
                ldap_host_el.setText(ldap_host);
                Element ldap_port_el = selectSingleNode.element("ldap_port");
                ldap_port_el.setText(ldap_port);
                Element ldap_adm_el = selectSingleNode.element("ldap_adm");
                ldap_adm_el.setText(ldap_adm);
                Element ldap_pwd_el = selectSingleNode.element("ldap_pwd");
                ldap_pwd_el.setText(ldap_pwd);
                Element ldap_search_path_el = selectSingleNode.element("ldap_search_path");
                ldap_search_path_el.setText(ldap_search_path);
                Element ldap_filter_string_el = selectSingleNode.element("ldap_filter_string");
                ldap_filter_string_el.setText(ldap_filter_string);
                Element ldap_attribute_name_el = selectSingleNode.element("ldap_attribute_name");
                ldap_attribute_name_el.setText(ldap_attribute_name);

                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "修改LDAP下载点成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }

            } else {
                msg = "修改LDAP下载点失败,未找到指定记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            msg = "查找指定记录失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String updateHttpPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                String url = request.getParameter("url");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String date = simpleDateFormat.format(new Date());
                Attribute attribute = selectSingleNode.attribute("late_time");
                if (attribute == null)
                    selectSingleNode.addAttribute("late_time", date);
                else
                    attribute.setValue(date);
                Element url_el = selectSingleNode.element("url");
                url_el.setText(url);
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "修改Http下载点成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                msg = "修改Http下载点失败,未找到指定记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            msg = "查找指定记录失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String enablePoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                Attribute attribute = selectSingleNode.attribute("status");
                attribute.setValue("1");
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "启用下载点成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                msg = "启用下载点失败,未找到指定记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            msg = "查找指定记录失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String disablePoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                Attribute attribute = selectSingleNode.attribute("status");
                attribute.setValue("0");
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "禁用下载点成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                msg = "禁用下载点失败,未找到指定记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            msg = "查找指定记录失败";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String downPoint() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String name = request.getParameter("name");
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@name='" + name + "']");
            if (selectSingleNode != null) {
                String type = selectSingleNode.attributeValue("type");
                if (type.equals("http")) {
                    String url = selectSingleNode.element("url").getText();
                    HttpCRLDownLoad crlDownLoad = new HttpCRLDownLoad();
                    InputStream in = crlDownLoad.download(url);
                    if (null != in) {
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(StringContext.crl_path + "/" + name + ".crl");
                            byte[] content = new byte[1024 * 1024];
                            int length;
                            while ((length = in.read(content, 0, content.length)) != -1) {
                                out.write(content, 0, length);
                                out.flush();
                            }
                            in.close();
                            out.flush();
                            out.close();
                            msg = "下载成功";
                            json = "{success:true,msg:'" + msg + "'}";
                            if(AuditFlagAction.getAuditFlag()) {

                                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                                SyslogSender.sysLog(log);
                            }
                        } catch (Exception e) {
                            msg = "下载CRL列表失败";
                            if(AuditFlagAction.getAuditFlag()) {
                                logger.info(e.getMessage(),e);
                                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                                SyslogSender.sysLog(log);
                            }
                        }
                    }
                } else {
                    Element ldap_host_el = selectSingleNode.element("ldap_host");
                    Element ldap_port_el = selectSingleNode.element("ldap_port");
                    Element ldap_adm_el = selectSingleNode.element("ldap_adm");
                    Element ldap_pwd_el = selectSingleNode.element("ldap_pwd");
                    Element ldap_search_path_el = selectSingleNode.element("ldap_search_path");
                    Element ldap_filter_string_el = selectSingleNode.element("ldap_filter_string");
                    Element ldap_attribute_name_el = selectSingleNode.element("ldap_attribute_name");

                    LdapCRLDownload crlDownload = new LdapCRLDownload();
                    byte[] bytes = crlDownload.getByte(ldap_host_el.getText(), ldap_port_el.getText(), ldap_adm_el.getText(), ldap_pwd_el.getText(), ldap_search_path_el.getText(), ldap_filter_string_el.getText(), ldap_attribute_name_el.getText());
                    if (bytes != null) {
                        FileOutputStream outputFileStream = new FileOutputStream(new File(StringContext.crl_path + "/" + name + ".crl"));
                        outputFileStream.write(bytes);
                        outputFileStream.flush();
                        outputFileStream.close();
                        msg = "下载成功";
                        json = "{success:true,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                }
            } else {
                msg = "未找到指定记录";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            msg = "未找到指定记录";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }

        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String updateAutoCRL() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String json = null;
        String auto_flag = request.getParameter("auto_flag");
        if (auto_flag == null)
            auto_flag = "";
        String hours = request.getParameter("hours");
        if (hours == null)
            hours = "";
        String minutes = request.getParameter("minutes");
        if (minutes == null)
            minutes = "";
        String seconds = request.getParameter("seconds");
        if (seconds == null)
            seconds = "";
        //按日，周，月
        String conf_type = request.getParameter("conf_type");
        if (conf_type == null)
            conf_type = "";
        String conf_time = request.getParameter("conf_time");
        if (conf_time == null)
            conf_time = "";
        String conf_day = request.getParameter("conf_day");
        if (conf_day == null)
            conf_day = "";
        String conf_time2 = request.getParameter("conf_time2");
        if (conf_time2 == null)
            conf_time2 = "";
        String conf_month_day = request.getParameter("conf_month_day");
        if (conf_month_day == null)
            conf_month_day = "";
        String conf_time3 = request.getParameter("conf_time3");
        if (conf_time3 == null)
            conf_time3 = "";
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/auto_update");
            if (selectSingleNode != null) {
                Attribute attribute = selectSingleNode.attribute("enable");
                attribute.setValue(auto_flag);
                Element conf_type_el = selectSingleNode.element("conf_type");
                conf_type_el.setText(conf_type);
                Element hours_el = selectSingleNode.element("hours");
                hours_el.setText(hours);
                Element minutes_el = selectSingleNode.element("minutes");
                minutes_el.setText(minutes);
                Element seconds_el = selectSingleNode.element("seconds");
                seconds_el.setText(seconds);
                Element conf_time_el = selectSingleNode.element("conf_time");
                conf_time_el.setText(conf_time);
                Element conf_day_el = selectSingleNode.element("conf_day");
                conf_day_el.setText(conf_day);
                Element conf_time2_el = selectSingleNode.element("conf_time2");
                conf_time2_el.setText(conf_time2);
                Element conf_month_day_el = selectSingleNode.element("conf_month_day");
                conf_month_day_el.setText(conf_month_day);
                Element conf_time3_el = selectSingleNode.element("conf_time3");
                conf_time3_el.setText(conf_time3);
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "修改CRL自动更新配置成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                Element config = doc.getRootElement();
                Element auto_update_el = config.addElement("auto_update");
                auto_update_el.addAttribute("enable", auto_flag);
                Element conf_type_el = auto_update_el.addElement("conf_type");
                conf_type_el.setText(conf_type);
                Element hours_el = auto_update_el.addElement("hours");
                hours_el.setText(hours);
                Element minutes_el = auto_update_el.addElement("minutes");
                minutes_el.setText(minutes);
                Element seconds_el = auto_update_el.addElement("seconds");
                seconds_el.setText(seconds);
                Element conf_time_el = auto_update_el.addElement("conf_time");
                conf_time_el.setText(conf_time);
                Element conf_day_el = auto_update_el.addElement("conf_day");
                conf_day_el.setText(conf_day);
                Element conf_time2_el = auto_update_el.addElement("conf_time2");
                conf_time2_el.setText(conf_time2);
                Element conf_month_day_el = auto_update_el.addElement("conf_month_day");
                conf_month_day_el.setText(conf_month_day);
                Element conf_time3_el = auto_update_el.addElement("conf_time3");
                conf_time3_el.setText(conf_time3);
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_xml);
                msg = "修改CRL自动更新配置成功";
                json = "{success:true,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        } else {
            Document document = DocumentHelper.createDocument();
            Element config = document.addElement("config");
            Element auto_update_el = config.addElement("auto_update");
            auto_update_el.addAttribute("enable", auto_flag);
            Element conf_type_el = auto_update_el.addElement("conf_type");
            conf_type_el.setText(conf_type);
            Element hours_el = auto_update_el.addElement("hours");
            hours_el.setText(hours);
            Element minutes_el = auto_update_el.addElement("minutes");
            minutes_el.setText(minutes);
            Element seconds_el = auto_update_el.addElement("seconds");
            seconds_el.setText(seconds);
            Element conf_time_el = auto_update_el.addElement("conf_time");
            conf_time_el.setText(conf_time);
            Element conf_day_el = auto_update_el.addElement("conf_day");
            conf_day_el.setText(conf_day);
            Element conf_time2_el = auto_update_el.addElement("conf_time2");
            conf_time2_el.setText(conf_time2);
            Element conf_month_day_el = auto_update_el.addElement("conf_month_day");
            conf_month_day_el.setText(conf_month_day);
            Element conf_time3_el = auto_update_el.addElement("conf_time3");
            conf_time3_el.setText(conf_time3);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            format.setIndent(true);
            try {
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.crl_xml)), format);
                try {
                    xmlWriter.write(document);
                    msg = "修改CRL自动更新配置成功";
                    json = "{success:true,msg:'" + msg + "'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "1", new Date());
                        SyslogSender.sysLog(log);
                    }
                } catch (IOException e) {
//                    logger.info(e.getMessage());
                    msg = "修改CRL自动更新配置失败,出现异常";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info(e.getMessage(),e);
                        json = "{success:false,msg:'" + msg + "'}";
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                } finally {
                    try {
                        xmlWriter.flush();
                        xmlWriter.close();
                    } catch (IOException e) {
//                        logger.info(e.getMessage());
                        msg = "修改CRL自动更新配置失败,出现异常";
                        json = "{success:false,msg:'" + msg + "'}";
                        if(AuditFlagAction.getAuditFlag()) {
                            logger.info(e.getMessage(),e);
                            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                            SyslogSender.sysLog(log);
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
//                logger.info(e.getMessage());
                msg = "修改CRL自动更新配置失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info(e.getMessage(),e);
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            } catch (FileNotFoundException e) {
                msg = "修改CRL自动更新配置失败,出现异常";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "吊销管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "吊销管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String findAutoCRL() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int totalCount = 0;
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        StringBuilder json = new StringBuilder("[");
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/auto_update");
            if (selectSingleNode != null) {
                Attribute attribute = selectSingleNode.attribute("enable");
                Element conf_type_el = selectSingleNode.element("conf_type");
                Element hours_el = selectSingleNode.element("hours");
                Element minutes_el = selectSingleNode.element("minutes");
                Element seconds_el = selectSingleNode.element("seconds");
                Element conf_time_el = selectSingleNode.element("conf_time");
                Element conf_day_el = selectSingleNode.element("conf_day");
                Element conf_time2_el = selectSingleNode.element("conf_time2");
                Element conf_month_day_el = selectSingleNode.element("conf_month_day");
                Element conf_time3_el = selectSingleNode.element("conf_time3");
                json.append("{");
                json.append("enable:'" + attribute.getValue() + "'").append(",");
                json.append("conf_type:'" + conf_type_el.getText() + "'").append(",");
                json.append("hours:'" + hours_el.getText() + "'").append(",");
                json.append("minutes:'" + minutes_el.getText() + "'").append(",");
                json.append("seconds:'" + seconds_el.getText() + "'").append(",");
                json.append("conf_time:'" + conf_time_el.getText() + "'").append(",");
                json.append("conf_day:'" + conf_day_el.getText() + "'").append(",");
                json.append("conf_time2:'" + conf_time2_el.getText() + "'").append(",");
                json.append("conf_month_day:'" + conf_month_day_el.getText() + "'").append(",");
                json.append("conf_time3:'" + conf_time3_el.getText() + "'").append("");
                json.append("}");
            }
        }
        json.append("]");
        try {
            actionBase.actionEnd(response, json.toString(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
