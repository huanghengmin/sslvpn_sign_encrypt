package com.hzih.sslvpn.web.action.sslvpn.encrypt.config;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-3
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
public class X509UserConfigUtils {

    public static String applyUser(X509Entity x509Certificate){
        StringBuilder config = new StringBuilder();
        config.append("[ req ]").append("\n");
        config.append("default_keyfile         = " + x509Certificate.getCN() + X509Context.keyName).append("\n");
        config.append("prompt                  = no").append("\n");
        config.append("string_mask             = utf8only").append("\n");
        config.append("distinguished_name      = req_distinguished_name").append("\n");

        config.append("[ req_distinguished_name ]").append("\n");
        config.append("C                       = " + X509Context.default_country_code).append("\n");
        if(x509Certificate.getST()!=null)
        config.append("ST                      = " + x509Certificate.getST()).append("\n");
        if(x509Certificate.getStateOrProvinceName()!=null)
        config.append("stateOrProvinceName     = " + x509Certificate.getStateOrProvinceName()).append("\n");
        if(x509Certificate.getL()!=null)
        config.append("L                       = " + x509Certificate.getL()).append("\n");
        if(x509Certificate.getLocalityName()!=null)
        config.append("localityName            = " + x509Certificate.getLocalityName()).append("\n");
        if(x509Certificate.getO()!=null)
        config.append("O                       = " + x509Certificate.getO()).append("\n");
        if(x509Certificate.getOU()!=null)
        config.append("OU                      = " + x509Certificate.getOU()).append("\n");
        if(x509Certificate.getOrganizationalUnitName()!=null)
        config.append("organizationalUnitName  = " + x509Certificate.getOrganizationalUnitName()).append("\n");
        if(x509Certificate.getEmail()!=null)
        config.append("emailAddress            = " + x509Certificate.getEmail()).append("\n");
        config.append("CN                      = " + x509Certificate.getCN()).append("\n");
        return config.toString();
    }


    public static boolean buildUser(X509Entity x509Certificate,String storeDirectory){
        String text = applyUser(x509Certificate);
        File file = new File(storeDirectory + "/" + x509Certificate.getCN() + X509Context.config_type_certificate);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        OutputStreamWriter osw  = null;
        try {
            osw = new OutputStreamWriter(fos, X509Context.charset);
            try {
                osw.write(text);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                osw.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!file.exists()) {
            return false;
        }
        return true;
    }

}
