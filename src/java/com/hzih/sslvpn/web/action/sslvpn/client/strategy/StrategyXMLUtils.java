package com.hzih.sslvpn.web.action.sslvpn.client.strategy;

import com.hzih.sslvpn.utils.StringContext;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

public class StrategyXMLUtils {

    private static Logger logger = Logger.getLogger(StrategyXMLUtils.class);
    public static final String root = "strategy";
    public static final String gprs = "gprs";
    public static final String wifi = "wifi";
    public static final String bluetooth = "bluetooth";
    public static final String gps = "gps";
    public static final String gps_interval = "gps_interval";
    public static final String view = "view";
    public static final String view_interval = "view_interval";
    public static final String strategy_interval = "strategy_interval";
    public static final String threeyards = "threeyards";
    public static final String charset = "utf-8";

    /**
     * @param name
     * @return
     */
    public static String getValue(String name) {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        String result = null;
        try {
            doc = saxReader.read(new File(StringContext.strategy_xml));
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

    /**
     *
     * @param gps
     * @param gps_interval
     * @param view
     * @param view_interval
     * @return
     */
    public static boolean save(String gps, String gps_interval,
                               String view, String view_interval,String threeyards,String strategy_interval,
                               String gprs,String wifi,String bluetooth) {
        boolean flag = false;
        Document doc = DocumentHelper.createDocument();

        Element root = doc.addElement(StrategyXMLUtils.root);

        Element gps_el = root.addElement(StrategyXMLUtils.gps);
        gps_el.addText(gps);

        Element gprs_el = root.addElement(StrategyXMLUtils.gprs);
        gprs_el.addText(gprs);

        Element wifi_el = root.addElement(StrategyXMLUtils.wifi);
        wifi_el.addText(wifi);

        Element bluetooth_el = root.addElement(StrategyXMLUtils.bluetooth);
        bluetooth_el.addText(bluetooth);

        Element gps_interval_el = root.addElement(StrategyXMLUtils.gps_interval);
        gps_interval_el.addText(gps_interval);

        Element view_el = root.addElement(StrategyXMLUtils.view);
        view_el.addText(view);

        Element view_interval_el = root.addElement(StrategyXMLUtils.view_interval);
        view_interval_el.addText(view_interval);

        Element threeyards_el = root.addElement(StrategyXMLUtils.threeyards);
        threeyards_el.addText(threeyards);

        Element strategy_interval_el = root.addElement(StrategyXMLUtils.strategy_interval);
        strategy_interval_el.addText(strategy_interval);

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        format.setIndent(true);
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.strategy_xml)), format);
            try {
                xmlWriter.write(doc);
                flag = true;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                try {
                    xmlWriter.flush();
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(),e);
        }
        return flag;
    }
}


