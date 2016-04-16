package com.hzih.sslvpn.web.action.sslvpn.encrypt.config;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 12-11-8
 * Time: 下午11:25
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryUtils {

    /**
     * 根据实际路径获取上级结点路径
      * @param liunxPath
     * @return
     */
    public static String getSuperStoreDirectory(String liunxPath){
        String father_dir = null;
        if(liunxPath.contains("/")){
            father_dir =  liunxPath.substring(0,liunxPath.lastIndexOf("/"));
        }
        return father_dir;
    }
}
