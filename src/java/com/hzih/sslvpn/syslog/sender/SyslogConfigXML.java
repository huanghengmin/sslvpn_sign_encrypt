package com.hzih.sslvpn.syslog.sender;

import com.hzih.sslvpn.entity.StatusMsg;
import com.hzih.sslvpn.entity.SysLogServer;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class SyslogConfigXML {
    private static Logger logger = Logger.getLogger(SyslogConfigXML.class);
    private static final String host = "host";
    private static final String port = "port";
    private static final String syslog = "syslog";
    private static final String syslogs = "syslogs";
    private static final String charset = "utf-8";

    /**
     * @param xml
     * @return
     */
    public List<SysLogServer> findAll(String xml) {
        List<SysLogServer> ipPortsLists = null;
        Document document = buildFromFile(xml);
        if (document != null) {
            Element ipPorts = document.getRootElement();
            if (ipPorts != null) {
                List<Element> ipPortObject = ipPorts.getChildren(SyslogConfigXML.syslog);
                if (ipPortObject != null) {
                    ipPortsLists = new ArrayList<SysLogServer>();
                    Iterator<Element> its = ipPortObject.iterator();
                    while (its.hasNext()) {
                        Element obj = its.next();
                        SysLogServer newIpPort = new SysLogServer(obj.getAttributeValue(SyslogConfigXML.host),
                                Integer.parseInt(obj.getAttributeValue(SyslogConfigXML.port)));
                        ipPortsLists.add(newIpPort);
                    }
                }
            }
        }else {
            return null;
        }
        return ipPortsLists;
    }

    /**
     * 判断是否存在
     *
     * @param xml
     * @param sysLogServer
     * @return
     */
    public boolean exist(String xml, SysLogServer sysLogServer) {
        boolean flag = false;
        Document document = buildFromFile(xml);
        if (document != null) {
            Element ipPorts = document.getRootElement();
            List<Element> ipPortObject = ipPorts.getChildren(SyslogConfigXML.syslog);
            if (!ipPortObject.isEmpty() && ipPortObject != null) {
                Iterator<Element> its = ipPortObject.iterator();
                while (its.hasNext()) {
                    Element obj = its.next();
                    if ((obj.getAttributeValue(SyslogConfigXML.host).equals(sysLogServer.getHost())) && (obj.getAttributeValue(SyslogConfigXML.port).equals(String.valueOf(sysLogServer.getPort())))) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 删除记录
     *
     * @param xml
     * @param sysLogServer
     * @return
     */
    public StatusMsg delete(String xml, SysLogServer sysLogServer) {
        String msg = "删除日志服务器失败";
        Document document = buildFromFile(xml);
        if (document != null) {
            Element ipPorts = document.getRootElement();
            List<Element> ipPortObject = ipPorts.getChildren(SyslogConfigXML.syslog);
            if (!ipPortObject.isEmpty() && ipPortObject != null) {
                Iterator<Element> its = ipPortObject.iterator();
                while (its.hasNext()) {
                    Element obj = its.next();
                    if ((obj.getAttributeValue(SyslogConfigXML.host).equals(sysLogServer.getHost())) && (obj.getAttributeValue(SyslogConfigXML.port).equals(String.valueOf(sysLogServer.getPort())))) {
                        ipPorts.removeContent(obj);
                        outputToFile(document, xml, charset);
                        msg = "删除日志服务器成功";
                        return new StatusMsg(true,msg);
                    }
                }
            }
        }
       return new StatusMsg(false,msg);
    }

    /**
     * 更新
     *
     * @param xml
     * @param sysLogServer
     * @param newSysLogServer
     * @return
     */
    public StatusMsg update(String xml, SysLogServer sysLogServer, SysLogServer newSysLogServer) {
        String msg = "更新日志服务器失败";
        Document document = buildFromFile(xml);
        if (document != null) {
            Element ipPorts = document.getRootElement();
            List<Element> ipPortObject = ipPorts.getChildren(SyslogConfigXML.syslog);
            if (!ipPortObject.isEmpty() && ipPortObject != null) {
                Iterator<Element> its = ipPortObject.iterator();
                while (its.hasNext()) {
                    Element obj = its.next();
                    Attribute ipAttribute = obj.getAttribute(SyslogConfigXML.host);
                    Attribute portAttribute = obj.getAttribute(SyslogConfigXML.port);
                    if ((ipAttribute.getValue().equals(sysLogServer.getHost())) && (portAttribute.getValue().equals(String.valueOf(sysLogServer.getPort())))) {
                        if (!exist(xml, newSysLogServer)) {
                            Attribute newIp = new Attribute(SyslogConfigXML.host, newSysLogServer.getHost());
                            Attribute newPort = new Attribute(SyslogConfigXML.port, String.valueOf(newSysLogServer.getPort()));
                            obj.setAttribute(newIp);
                            obj.setAttribute(newPort);
                            outputToFile(document, xml, charset);
                            msg = "更新日志服务器成功";
                            return new StatusMsg(true,msg);
                        } else {
                            msg = "记录已存在";
                            return new StatusMsg(false,msg);
                        }
                    }
                }
            }
        }
        return new StatusMsg(false,msg);
    }

    /**
     * 添加记录到XML文件
     *
     * @param xml
     * @param ipPort
     * @return
     */
    public StatusMsg add(String xml, SysLogServer ipPort) {
        String msg = "添加日志服务器失败";
        Document document = buildFromFile(xml);
        if (document != null) {
            if (!exist(xml, ipPort)) {
                Element ipPorts = document.getRootElement();
                Element ipPortObject = new Element(SyslogConfigXML.syslog);
                Attribute ip = new Attribute(SyslogConfigXML.host, ipPort.getHost());
                Attribute port = new Attribute(SyslogConfigXML.port, String.valueOf(ipPort.getPort()));
                ipPortObject.setAttribute(ip);
                ipPortObject.setAttribute(port);
                ipPorts.addContent(ipPortObject);
                outputToFile(document, xml, charset);
                msg = "添加日志服务器成功";
                return new StatusMsg(true,msg);
            } else {
                msg = "记录已存在";
                return new StatusMsg(false,msg);
            }
        } else {
            Element ipPorts = new Element(SyslogConfigXML.syslogs);
            Element ipPortObject = new Element(SyslogConfigXML.syslog);
            Attribute ip = new Attribute(SyslogConfigXML.host, ipPort.getHost());
            Attribute port = new Attribute(SyslogConfigXML.port, String.valueOf(ipPort.getPort()));
            ipPortObject.setAttribute(ip);
            ipPortObject.setAttribute(port);
            ipPorts.addContent(ipPortObject);
            Document doc = new Document(ipPorts);
            outputToFile(doc, xml, charset);
            msg = "添加日志服务器成功";
            return new StatusMsg(true,msg);
        }
    }

    /**
     * 从指定文件获取Document对象
     *
     * @param filePath
     * @return
     */
    public Document buildFromFile(String filePath) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document anotherDocument = builder.build(new File(filePath));
            return anotherDocument;
        } catch (JDOMException e) {
            logger.error(e.getMessage(),e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(),e);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
     * 指定编码写入XML文件
     *
     * @param doc
     * @param filePath
     * @param encoding
     */
    public void outputToFile(Document doc, String filePath, String encoding) {
        try {
            XMLOutputter out = new XMLOutputter();
            Format fm = Format.getPrettyFormat();
            fm.setExpandEmptyElements(true);
            fm.setEncoding(encoding);
            fm.setIndent("\t");
            out.setFormat(fm);
            FileWriter writer = new FileWriter(filePath);
            out.output(doc, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.info(e.getMessage(),e);
        }
    }
}