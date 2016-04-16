package com.hzih.sslvpn.utils;

import com.hzih.sslvpn.entity.Version;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * Created by Administrator on 15-3-11.
 */
public class VersionUtils {

   /* 涉及到客户端的系统中经常需要用到比较版本号的功能，但是比较版本号又不能完全按照字符串比较的方式去用compareTo之类的方法；
    这就需要我们总结版本号的通用规则，设计一个比较算法并封装成通用方法来使用：
    通常版本好如：1.3.20.8，6.82.20160101，8.5a/8.5c等；
    通用规则就是，先将版本字符串按照点号分割，然后主版本与主版本比较、此版本与此版本比较，如此按序一级一级往后比较，直到有分出大小；
    值得注意的是，很多比较版本号的方法都先将字符串转换成int或者double类型，这样做未必通用，因为可能含有字母，如8.5c这样的版本号；
    通用的方式依然是将分割后的字符串当做字符串来比较，不过，比较字符串之前，先比较位数；
    比较版本号的方法示例：*/

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            return -2;
        }
        String[] versionArray1 = null;
        if (version1.contains("."))
            versionArray1 = version1.split("\\.");
        //注意此处为正则匹配，不能用.；
        String[] versionArray2 = null;
        if (version2.contains("."))
            versionArray2 = version2.split("\\.");
        if (versionArray1 != null && versionArray2 != null) {
            int idx = 0;
            int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
            int diff = 0;
            while (idx < minLength
                    && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                    && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
                ++idx;
            }
            //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
            diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
            return diff;
        } else {
            return -2;
        }
    }


    public static Version readInfo(File xml) {
        Document doc = null;
        try {
            SAXReader saxReader = new SAXReader();
            doc = saxReader.read(xml);
            Element rootElt = doc.getRootElement(); // 获取根节点
            Element versionEl = rootElt.element("version");
            String version = versionEl.getText();
            Element nameEl = rootElt.element("name");
            String name = nameEl.getText();
            Version v = new Version(version, name);
            return v;
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }


    /*public static void main(String arg[])throws Exception{
        String version1= "2.0.1a";
        String version2 = "2.0.1b";
        compareVersion(version1,version2);
    }*/
}
