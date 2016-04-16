package com.hzih.sslvpn.utils;

import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.domain.Role;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 11-10-11
 * Time: 下午5:09
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    public String trim(String str){
        String[] ary = str.split(",]");
        if(ary.length > 1 ){
            str = ary[0]+"]"+ary[1];
        }
        return str;
    }

    public String trimLast(String str) {
        int index = str.lastIndexOf('/');
        str = str.substring(0,index);
        return str;
    }

    //jdbc:jtds:sqlserver://192.168.1.113:1433/jsontest;tds=8.0;lastupdatecount=true	sqlserver
    //jdbc:oracle:thin:@192.168.1.104:1521:testnei		oracle
    //jdbc:jtds:sybase://192.168.1.104:5003/jsontest		sybase
    //jdbc:db2://192.168.1.14:5000/sample				db2
    //jdbc:jtds:sqlserver://192.168.11.25:1433:jsontest   	mssql
    public String trimUrl(String dbUrl) {
        String dbName = null;
        String db = dbUrl.split(":")[1];
        String[] dbs = dbUrl.split(";");
        if(dbs.length>1){
            if("jtds".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1,dbUrl.indexOf(';'));
            }else if("oracle".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf(':')+1,dbUrl.indexOf(';'));
            }else if("sybase".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1,dbUrl.indexOf(';'));
            }else if("db2".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1,dbUrl.indexOf(';'));
            }else if("microsoft".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1,dbUrl.indexOf(';'));
            }
        }else{
            if("jtds".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1);
            }else if("oracle".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf(':')+1);
            }else if("sybase".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1);
            }else if("db2".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1);
            }else if("microsoft".equals(db)){
                dbName = dbUrl.substring(dbUrl.lastIndexOf('/')+1);
            }
        }
        return dbName;						//test2 or testnei
    }

    public String appendString(String[] arrayIp, String ip) {
        String ips = arrayIp[0];
        if(arrayIp.length>1){
            for (int i = 1; i < arrayIp.length; i++) {
                ips += ","+arrayIp[i];
            }
        }
        ips += ip==null?"":","+ip;
        String[] str = ips.split(",");
        String[] str1 = array_unique(str);
        String ip_str = str1[0];
        for (int i = 1; i < str1.length; i++) {
            ip_str += ","+str1[i];
        }
        return ip_str;
    }

    //去除数组中重复的记录  
    public String[] array_unique(String[] a) {
        // array_unique  
        List<String> list = new LinkedList<String>();
        for(int i = 0; i < a.length; i++) {
            if(!list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        return (String[])list.toArray(new String[list.size()]);
    }

    public String updateString(String[] ips, String ip, String oldIp) {//用ip替换ips中的oldIp
        for (int i = 0; i < ips.length; i++) {
            if(oldIp.equals(ips[i])){
                ips[i] = ip;
            }
        }
        String ip_str = ips[0];
        for (int i = 1; i < ips.length; i++) {
            ip_str += ","+ips[i];
        }
        return ip_str;
    }

    public String deleteString(String[] ips, String ip) { //去掉ips中等于ip的数据
        String ip_str = "";
        for (int i = 0; i < ips.length; i++) {
            if(!ip.equals(ips[i])){
                ip_str += ","+ips[i];
            }
        }
        return ip_str.substring(1);
    }

    /**
     * 去掉ips中等于或者存在ip的ip或者ip段
     * @param ips 192.168.1.15-192.168.1.30,192.168.1.50,192.168.1.60-192.168.1.100
     * @param ip 192.168.1.15-192.168.1.30,192.168.1.50
     * @return 192.168.1.60-192.168.1.100
     */
    public String deleteArray(String[] ips, String[] ip) {
        String[] ipArray = getArray(ips,ip);
        String str = "";
        if(ipArray.length>1){
            str = appendString(ipArray, "");
        }
        return str;
    }

    /**
     *
     * @param appNamesThatS 	["a","b","c","d","e"]
     * @param appNamesThisT		["a","b"]
     * @return appNamesT		["c","d","e"]
     */
    public String[] getArray(String[] appNamesThatS, String[] appNamesThisT) {
        List<String> appNamesThatSList = new ArrayList<String>();
        List<String> appNamesThisTList = new ArrayList<String>();
        for (int i = 0; i < appNamesThatS.length; i++) {
            appNamesThatSList.add(appNamesThatS[i]);
        }
        for (int i = 0; i < appNamesThisT.length; i++) {
            appNamesThisTList.add(appNamesThisT[i]);
        }
        appNamesThatSList.removeAll(appNamesThisTList);
        String[] appNamesTs = (String[])appNamesThatSList.toArray(new String[appNamesThatSList.size()]);
        return appNamesTs;
    }

    /**
     * when str is null or "" return true
     *
     * @param str
     * @return
     */
    public static Boolean isBlank(String str) {
        if (str == null || str.equals("") || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * @param str
     * @return
     */
    public static Boolean isNotBlank(String str) {
        if (str == null || str.equals("") || str.length() == 0)
            return false;
        else
            return true;
    }
}
