package com.hzih.sslvpn.web.action.sslvpn.crl;

import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.Dom4jUtil;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.MergeFiles;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.ActionBase;
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
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-6
 * Time: 下午1:33
 * To change this template use File | Settings | File Templates.
 */
public class RevokeFileAction extends ActionSupport {
    private Logger logger = Logger.getLogger(RevokeFileAction.class);

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }


    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        File file = new File(StringContext.crl_path);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".crl")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        if (files != null && files.length > 0) {
            String json = "{success:true,total:" + files.length + ",rows:[";
            for (File f : files) {
                json += "{" +
                        "name:'" + f.getName() +
                        "',filesize:'" + f.length() +
                        "',path:'" + file.getAbsolutePath() + "'" +
                        "},";
            }
            if (json.endsWith(","))
                json = json.substring(0, json.length() - 1);
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }else {
            String json = "{success:true,total:" + 0 + ",rows:[";
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }


    public String findStatus() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        File file = new File(StringContext.crl_path);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".crl")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        if (files != null && files.length > 0) {
            String json = "{success:true,total:" + files.length + ",rows:[";
            for (File f : files) {
                json += "{" +
                        "name:'" + f.getName() +
                        "',filesize:'" + f.length();
                if (check_status(f.getName())) {
                    json += "',status:'" + 1;
                } else {
                    json += "',status:'" + 0;
                }
                json += "',path:'" + file.getAbsolutePath() + "'" +
                        "},";
            }
            if (json.endsWith(","))
                json = json.substring(0, json.length() - 1);
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }else {
            String json = "{success:true,total:" + 0 + ",rows:[";
            json += "]}";
            actionBase.actionEnd(response, json, result);
        }
        return null;
    }

    public String config() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "更新黑名单校验配置失败";
        String json = "{success:false,msg:'" + msg + "'}";
        String[] names = request.getParameterValues("names");
        if (null != names && !"".equals(names) && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                if (!"".equals(name))
                    check(name);
            }
        }
        List<Element> checks = findAllCheck();
        if (null != checks && checks.size() > 0) {
            String[] ss = null;
            for (int i = 0; i < checks.size(); i++) {
                if (ss == null)
                    ss = new String[checks.size()];
                Element element = checks.get(i);
                String path = StringContext.crl_path + "/" + element.attributeValue("name");
                File file = new File(path);
                if (file.exists())
                    ss[i] = path;
            }
            if (ss != null && ss.length > 0){
                File crl_file = new File(StringContext.crl_file);
                if(crl_file.exists())
                    crl_file.delete();
                MergeFiles.mergeFiles(StringContext.crl_file, ss);
            }

            msg = "更新黑名单校验配置成功";
            json = "{success:true,msg:'" + msg + "'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public void check(String name) throws Exception {
        Document doc = Dom4jUtil.getDocument(StringContext.crl_check_xml);
        if (doc != null) {
            List<Element> services = doc.selectNodes("/config/crl[@name='" + name + "']");
            if (services != null && services.size() > 0) {
                for (int i = 0; i < services.size(); i++) {
                    Element element = services.get(i);
                    Attribute status = element.attribute("status");
                    status.setValue("1");
                }
            } else {
                Element rootElement = doc.getRootElement();
                Element crl = rootElement.addElement("crl");
                crl.addAttribute("name", name);
                crl.addAttribute("status", "1");
                Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_check_xml);
            }
        } else {
            Document document = DocumentHelper.createDocument();
            Element config = document.addElement("config");
            Element crl = config.addElement("crl");
            crl.addAttribute("name", name);
            crl.addAttribute("status", "1");
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            format.setIndent(true);
            try {
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.crl_check_xml)), format);
                try {
                    xmlWriter.write(document);
                } catch (IOException e) {
                    logger.info(e.getMessage(),e);
                } finally {
                    try {
                        xmlWriter.flush();
                        xmlWriter.close();
                    } catch (IOException e) {
                        logger.info(e.getMessage(),e);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                logger.info(e.getMessage(),e);
            } catch (FileNotFoundException e) {
                logger.info(e.getMessage(),e);
            }
        }
    }

    public List<Element> findAllCheck() throws Exception {
        Document doc = Dom4jUtil.getDocument(StringContext.crl_check_xml);
        if (doc != null) {
            List<Element> services = doc.selectNodes("/config/crl[@status='" + 1 + "']");
            if (services != null && services.size() > 0) {
                return services;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean check_status(String name) throws Exception {
        Document doc = Dom4jUtil.getDocument(StringContext.crl_check_xml);
        if (doc != null) {
            List<Element> services = doc.selectNodes("/config/crl[@name='" + name + "'][@status='" + 1 + "']");
            if (services != null && services.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean del_element(String name) throws Exception {
        Document doc = Dom4jUtil.getDocument(StringContext.crl_check_xml);
        if (doc != null) {
            List<Element> services = doc.selectNodes("/config/crl[@name='" + name + "']");
            if (services != null && services.size() > 0) {
                Element element = services.get(0);
                Element root = doc.getRootElement();
                boolean flag = root.remove(element);
                if(flag)
                    Dom4jUtil.writeDocumentToFile(doc, StringContext.crl_check_xml);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String down()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false}";
        String name = request.getParameter("name");
        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent, ";");
        st.nextToken();
        //得到用户的浏览器名  MSIE  Firefox
        String userBrowser = st.nextToken();
        File file = new File(StringContext.crl_path+"/"+name);
        FileUtil.downType(response, name, userBrowser);
        response = FileUtil.copy(file, response);
        json = "{success:true}";
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String del()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "删除黑名单文件失败";
        String json = "{success:false,msg:'"+msg+"'}";
        String name = request.getParameter("name");
        File file = new File(StringContext.crl_path+"/"+name);
        if(file.exists()){
            del_element(name);
            file.delete();
            msg ="删除黑名单文件成功";
            json = "{success:true,msg:'"+msg+"'}";
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

}
