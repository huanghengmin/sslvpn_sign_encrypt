package com.hzih.sslvpn.web.action.config;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.Dom4jUtil;
import com.hzih.sslvpn.utils.KeepAlivedConfigUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-5-12
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class HotBackupKeepAlivedAction extends ActionSupport {

    private Logger logger = Logger.getLogger(HotBackupKeepAlivedAction.class);
    public static final String keepalived_xml = StringContext.config_path + "/keepalived.xml";
    private static final String keepalived_config =  "/etc/keepalived/keepalived.conf";
    private LogService logService;

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        try {
            Document doc = Dom4jUtil.getDocument(keepalived_xml);
            if(doc!=null) {
                Element del_url = (Element) doc.selectSingleNode("/config");
                if(del_url!=null) {
                    Element device_type_element = del_url.element("device_type");
                    String device_type =null;
                    if(device_type_element!=null)
                        device_type_element.getText();
                    Element listen_inet_element = del_url.element("listen_inet");
                    Element back_inet_element = del_url.element("back_inet");
                    Element virt_ip_element = del_url.element("virt_ip");
                    Dom4jUtil.writeDocumentToFile(doc, keepalived_xml);
                    json = "{success:true,total:1,rows:[{device_type:'" + device_type_element.getText() +
                            "',listen_inet:'" + listen_inet_element.getText() +
                            "',back_inet:'" + back_inet_element.getText() +
                            "',virt_ip:'" + virt_ip_element.getText() + "'}]}";
                    //if (AuditFlagAction.getAuditFlag()) {
                        logService.newLog("info", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户查找双机热备配置信息成功");
                    //}
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            msg = "用户查找双机热备配置信息失败";
           // if (AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
            //}
            json = "{success:true,total:0,rows:[]}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String device_type = request.getParameter("device_type");
        String listen_inet = request.getParameter("listen_inet");
        String back_inet = request.getParameter("back_inet");
        String virt_ip = request.getParameter("virt_ip");
        Document doc = Dom4jUtil.getDocument(keepalived_xml);
        if(doc!=null) {
            Element del_url = (Element) doc.selectSingleNode("/config");
            try {
                Element device_type_element = del_url.element("device_type");
                if(device_type_element==null)
                    device_type_element = del_url.addElement("device_type");
                device_type_element.setText(device_type);
                Element listen_inet_element = del_url.element("listen_inet");
                if(listen_inet_element==null)
                    listen_inet_element = del_url.addElement("listen_inet");
                listen_inet_element.setText(listen_inet);
                Element virt_ip_element = del_url.element("virt_ip");
                if(virt_ip_element==null)
                    virt_ip_element = del_url.addElement("virt_ip");
                virt_ip_element.setText(virt_ip);
                Element back_inet_element = del_url.element("back_inet");
                if(back_inet_element==null)
                    back_inet_element = del_url.addElement("back_inet");
                back_inet_element.setText(back_inet);
                Dom4jUtil.writeDocumentToFile(doc, keepalived_xml);
                KeepAlivedConfigUtil.configServer(keepalived_config);
                msg ="用户更新双机热备配置信息成功";
                logger.info(msg+",时间："+new Date());
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户更新双机热备配置信息成功!");
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
                msg = "用户更新双机热备配置信息失败";
                //if (AuditFlagAction.getAuditFlag()) {
                    logger.error(msg, e);
                    logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                    //String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                    //SiteContextLoaderServlet.sysLogService.offer(log);
               //}
            }
        }else {
            Document document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("config");
            Element device_type_el = rootElement.addElement("device_type");
            Element listen_inet_el = rootElement.addElement("listen_inet");
            Element back_inet_el = rootElement.addElement("back_inet");
            Element virt_ip_el = rootElement.addElement("virt_ip");
            device_type_el.setText(device_type);
            listen_inet_el.setText(listen_inet);
            back_inet_el.setText(back_inet);
            virt_ip_el.setText(virt_ip);
            try {
                Dom4jUtil.writeDocumentToFile(document,keepalived_xml);
                KeepAlivedConfigUtil.configServer(keepalived_config);
                msg ="用户更新双机热备配置信息成功";
                logger.info(msg+",时间："+new Date());
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户更新双机热备配置信息成功!");
            } catch (IOException e) {
                msg ="用户更新双机热备配置信息失败";
                logger.error("用户更新双机热备配置信息失败:" + e);
            }
        }
        actionBase.actionEnd(response, "{success:true,msg:'" + msg + "'}", result);
        return null;
    }

    public String add_virtserver() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String v_ip = request.getParameter("v_ip");
        String v_port = request.getParameter("v_port");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        Document doc = Dom4jUtil.getDocument(keepalived_xml);
        if(doc!=null) {
            Element del_url = (Element) doc.selectSingleNode("/config/virtservers/virtserver[@ip='" + ip + "'][@port='" + port + "']");
            try {
                if(del_url!=null){
                    msg = "监控服务已存在,示允许再次添加";
                    logger.info(msg+",时间："+new Date());
                    actionBase.actionEnd(response, "{success:false,msg:'" + msg + "'}", result);
                }else {
                    Element config_el = (Element) doc.selectSingleNode("/config");
                    Element virtservers_el = config_el.element("virtservers");
                    Element virtserver_el;
                    if(virtservers_el==null){
                        virtservers_el = config_el.addElement("virtservers");

                    }
                    virtserver_el = virtservers_el.addElement("virtserver");
                    virtserver_el.addAttribute("ip",ip);
                    virtserver_el.addAttribute("port",port);
                    virtserver_el.addAttribute("v_ip",v_ip);
                    virtserver_el.addAttribute("v_port",v_port);
                    Dom4jUtil.writeDocumentToFile(doc, keepalived_xml);
                    KeepAlivedConfigUtil.configServer(keepalived_config);
                    msg = "用户添加监控服务信息成功";
                    logger.info(msg+",时间："+new Date());
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户添加监控服务信息成功!");
                }

            } catch (Exception e) {
                //logger.info(e.getMessage(), e);
                msg = "用户添加监控服务配置信息失败";
                //if (AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                //String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                //SiteContextLoaderServlet.sysLogService.offer(log);
                //}
                actionBase.actionEnd(response, "{success:false,msg:'" + msg + "'}", result);
            }
        }else {
            Document document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("config");
            Element virtservers_el = rootElement.element("virtservers");
            Element virtserver_el;
            if(virtservers_el==null){
                virtservers_el = rootElement.addElement("virtservers");

            }
            virtserver_el = virtservers_el.addElement("virtserver");
            virtserver_el.addAttribute("ip",ip);
            virtserver_el.addAttribute("port",port);
            virtserver_el.addAttribute("v_ip",v_ip);
            virtserver_el.addAttribute("v_port",v_port);
            try {
                Dom4jUtil.writeDocumentToFile(document,keepalived_xml);
                KeepAlivedConfigUtil.configServer(keepalived_config);
                msg ="用户更新双机热备配置信息成功";
                logger.info(msg+",时间："+new Date());
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户更新双机热备配置信息成功!");
            } catch (IOException e) {
                msg ="用户更新双机热备配置信息失败";
                logger.error("用户更新双机热备配置信息失败:" + e);
            }
        }
        actionBase.actionEnd(response, "{success:true,msg:'" + msg + "'}", result);
        return null;
    }

    public String update_virtserver() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String v_ip = request.getParameter("v_ip");
        String v_port = request.getParameter("v_port");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        Document doc = Dom4jUtil.getDocument(keepalived_xml);
        if(doc!=null) {
            Element del_url = (Element) doc.selectSingleNode("/config/virtservers/virtserver[@ip='" + ip + "'][@port='" + port + "']");
            try {
                del_url.attribute("v_ip").setValue(v_ip);
                del_url.attribute("v_port").setValue(v_port);
                Dom4jUtil.writeDocumentToFile(doc, keepalived_xml);
                KeepAlivedConfigUtil.configServer(keepalived_config);
                msg = "用户更新监控服务信息成功";
                logger.info(msg+",时间："+new Date());
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户更新监控服务信息成功!");
            } catch (Exception e) {
                //logger.info(e.getMessage(), e);
                msg = "用户更新监控服务配置信息失败";
                //if (AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                //String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                //SiteContextLoaderServlet.sysLogService.offer(log);
                //}
            }
        }
        actionBase.actionEnd(response, "{success:true,msg:'" + msg + "'}", result);
        return null;
    }

    public String delete_virtserver() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String v_ip = request.getParameter("v_ip");
        String v_port = request.getParameter("v_port");
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        Document doc = Dom4jUtil.getDocument(keepalived_xml);
        if(doc!=null) {
            try {
                Element del_url = (Element) doc.selectSingleNode("/config/virtservers/virtserver[@ip='" + ip + "'][@port='" + port + "']");
                del_url.getParent().remove(del_url);
                Dom4jUtil.writeDocumentToFile(doc, keepalived_xml);
                KeepAlivedConfigUtil.configServer(keepalived_config);
                msg = "用户删除监控服务信息成功";
                logger.info(msg+",时间："+new Date());
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户删除监控服务信息成功!");
            } catch (Exception e) {
                //logger.info(e.getMessage(), e);
                msg = "用户删除监控服务配置信息失败";
                //if (AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                //String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                //SiteContextLoaderServlet.sysLogService.offer(log);
                //}
            }
        }
        actionBase.actionEnd(response, "{success:true,msg:'" + msg + "'}", result);
        return null;
    }

    public String find_virtserver() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        Document doc = Dom4jUtil.getDocument(keepalived_xml);
        List<Element> del_url = doc.selectNodes("/config/virtservers/virtserver");
        try {
            if (del_url != null) {
                 json = "{success:true,total:" + del_url.size() + ",rows:[";
                Iterator<Element> raUserIterator = del_url.iterator();
                while (raUserIterator.hasNext()) {
                    Element log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "ip:'" + log.attributeValue("ip") +
                                "',port:'" + log.attributeValue("port") +
                                "',v_ip:'" + log.attributeValue("v_ip") +
                                "',v_port:'" + log.attributeValue("v_port") + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "ip:'" + log.attributeValue("ip") +
                                "',port:'" + log.attributeValue("port") +
                                "',v_ip:'" + log.attributeValue("v_ip") +
                                "',v_port:'" + log.attributeValue("v_port") + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        } catch (Exception e) {
            //logger.info(e.getMessage(), e);
            msg = "用户查找双机热备配置信息失败";
            //if (AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
           // }
            json = "{success:true,total:0,rows:[]}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public LogService getLogService() {
        return logService;
    }
}
