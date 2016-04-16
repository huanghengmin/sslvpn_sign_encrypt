package com.hzih.sslvpn.utils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

public class Dom4jUtil {
    private static Logger logger=Logger.getLogger(Dom4jUtil.class);
    /**
     * 根据xml文件的文件名，把xml文件转换成Document对象并返回。
     */
    public static Document getDocument(String fileUrl) {
        File file = new File(fileUrl);
        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        return document;
    }
    /**
     * 根据文件流得到document对象。
     */
    public static Document getInDocument(InputStream in) {
        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        Document document = null;
        try {
            document = reader.read(in);
        } catch (DocumentException e) {
            logger.info(e.getMessage(),e);
            return null;
        }
        return document;
    }
    /**
     * 把Document对象与filePath对应的物理文件进行同步。
     */
    public static void writeDocumentToFile(Document document, String filePath)throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        format.setIndent(true);
        XMLWriter writer = new XMLWriter(new FileOutputStream(filePath),format);
        writer.write(document);
        writer.flush();
        writer.close();
    }


    /**
     * 把Document对象与filePath对应的物理文件进行同步。
     */
    public static void writeDocumentForIn(Document document, OutputStream out)throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        format.setIndent(true);
        XMLWriter writer = new XMLWriter(out,format);
        writer.write(document);
        writer.flush();
        writer.close();
    }
}