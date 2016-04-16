package com.hzih.sslvpn.web.action.sslvpn.backup.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class MysqlProperties {
    private static final String config = "config.properties";

    public static String getProperties(String properties){
        Properties prop = new Properties();
        InputStream in = MysqlProperties.class.getResourceAsStream("/"+config);
        try {
            prop.load(in);
            return prop.getProperty(properties).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}