package com.hzih.sslvpn.web.servlet;

import com.hzih.sslvpn.utils.Dom4jUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.web.action.sslvpn.crl.HttpCRLDownLoad;
import com.hzih.sslvpn.web.action.sslvpn.ldap.LdapCRLDownload;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.*;
import java.util.Date;
import java.util.TimerTask;

public class CRLUpdateTask extends TimerTask {

    private Logger logger = Logger.getLogger(CRLUpdateTask.class);

    @Override
    public void run() {
        logger.info("*********************启动CRL自动更新服务********************************");
        Document doc = Dom4jUtil.getDocument(StringContext.crl_xml);
        if (doc != null) {
            Element selectSingleNode = (Element) doc.selectSingleNode("/config/crldownload[@status='" + 1 + "']");
            if (selectSingleNode != null) {
                String name = selectSingleNode.attributeValue("name");
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
                            String msg = "下载CRL http下载点" + name + "成功";
                            logger.info("时间:" + new Date() + ",信息:" + msg);
                        } catch (Exception e) {
                            String msg = "下载CRL http下载点" + name + "失败";
                            logger.info("时间:" + new Date() + ",信息:" + msg,e);
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
                    byte[] bytes = crlDownload.getByte(ldap_host_el.getText(),
                            ldap_port_el.getText(),
                            ldap_adm_el.getText(),
                            ldap_pwd_el.getText(),
                            ldap_search_path_el.getText(),
                            ldap_filter_string_el.getText(),
                            ldap_attribute_name_el.getText());
                    if (bytes != null) {
                        FileOutputStream outputFileStream = null;
                        try {
                            outputFileStream = new FileOutputStream(new File(StringContext.crl_path + "/" + name + ".crl"));
                            outputFileStream.write(bytes);
                            outputFileStream.flush();
                            outputFileStream.close();
                            String msg = "下载CRL LDAP下载点" + name + "成功";
                            logger.info("时间:" + new Date() + ",信息:" + msg);
                        } catch (IOException e) {
                            String msg = "下载CRL LDAP下载点" + name + "失败";
                            logger.info("时间:" + new Date() + ",信息:" + msg);
                        }
                    }
                }
            }
        }
    }
}