package com.hzih.sslvpn.web.action.sslvpn.encrypt;


import com.hzih.sslvpn.web.action.sslvpn.encrypt.config.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by Administrator on 15-12-7.
 */
public class EncryptUtils {

    public static X509Entity getCA() {
        X509Entity x509Entity = new X509Entity();
        x509Entity.setCN("ZD");
        return x509Entity;
    }

    public static boolean findCA(X509Entity entity) {
        //CA 目录
        String storeDirectory = X509Context.store_path + "/" + entity.getCN();
        //CA 证书目录
        String selfDirectory = DirectoryUtils.getSuperStoreDirectory(storeDirectory);
        //证书文件
        File cerFile = new File(selfDirectory + "/" + entity.getCN() + X509Context.certName);
        File keyFile = new File(selfDirectory + "/" + entity.getCN() + X509Context.keyName);
        if (keyFile.exists() && keyFile.length() > 0 && cerFile.exists() && cerFile.length() > 0) {
            return true;
        }
        return false;
    }

    public static X509Entity getServer() {
        X509Entity x509Entity = new X509Entity();
        x509Entity.setCN("Server");
        return x509Entity;
    }

    public static boolean findServer(X509Entity entity) {
        //CA 信息
        X509Entity cAEntity = EncryptUtils.getCA();
        //CA 目录
        String storeDirectory = X509Context.store_path + "/" + cAEntity.getCN();
        //证书文件
        File cerFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.certName);
        File keyFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.keyName);
        if (keyFile.exists() && keyFile.length() > 0 && cerFile.exists() && cerFile.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean findClient(X509Entity entity) {
        //CA 信息
        X509Entity cAEntity = EncryptUtils.getCA();
        //CA 目录
        String storeDirectory = X509Context.store_path + "/" + cAEntity.getCN();
        //证书文件
        File cerFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.certName);
        File keyFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.keyName);
        if (keyFile.exists() && keyFile.length() > 0 && cerFile.exists() && cerFile.length() > 0) {
            CertificateFactory  cf = null;
            FileInputStream stream = null;
            try {
                cf = CertificateFactory.getInstance("X.509");
                stream = new FileInputStream(cerFile);
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                String subject = cert.getSubjectDN().toString();
                String[] subjects = subject.split(",");
                for (String s:subjects){
                    if(s.contains("CN")){
                        if(s.contains("=")){
                            String[] ss = s.split("=");
                            if(entity.getCN().equals(ss[1])){
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        return false;
    }

    public static boolean buildClient(X509Entity entity) {
        //CA 信息
        X509Entity cAEntity = EncryptUtils.getCA();
        //CA 目录
        String storeDirectory = X509Context.store_path + "/" + cAEntity.getCN();
        //CA 证书目录
        String selfDirectory = DirectoryUtils.getSuperStoreDirectory(storeDirectory);
        //构建用户请求文件
        boolean flag = X509UserConfigUtils.buildUser(entity, storeDirectory);
        if (flag) {
            //构建csr请求
            flag = X509ShellUtils.build_csr(X509Context.key_length, storeDirectory + "/" + entity.getCN() + X509Context.keyName, storeDirectory + "/" + entity.getCN() + X509Context.csrName, storeDirectory + "/" + entity.getCN() + X509Context.config_type_certificate);
            if (flag) {
                //签发用户CA
                flag = X509ShellUtils.build_sign_csr(storeDirectory + "/" + entity.getCN() + X509Context.csrName, storeDirectory + "/" + cAEntity.getCN() + X509Context.config_type_ca, X509Context.certificate_type_client, selfDirectory + "/" + cAEntity.getCN() + X509Context.certName, selfDirectory + "/" + cAEntity.getCN() + X509Context.keyName, storeDirectory + "/" + entity.getCN() + X509Context.certName, String.valueOf(X509Context.default_certificate_validity));
                if (flag) {
                    //构建pfx文件
                    flag = X509ShellUtils.build_pkcs12(storeDirectory + "/" + entity.getCN() + X509Context.keyName, storeDirectory + "/" + entity.getCN() + X509Context.certName, storeDirectory + "/" + entity.getCN() + X509Context.pkcsName);
                    if (flag) {
                        File cerFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.certName);
                        File keyFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.keyName);
                        if (keyFile.exists() && keyFile.length() > 0 && cerFile.exists() && cerFile.length() > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static File getCAFile(X509Entity entity) {
        //证书文件
        File cerFile = new File(X509Context.store_path + "/" + entity.getCN() + X509Context.certName);
        if (cerFile.exists() && cerFile.length() > 0) {
            return cerFile;
        }
        return null;
    }

    public static File getClientKey(X509Entity entity) {
        //CA 信息
        X509Entity cAEntity = EncryptUtils.getCA();
        //CA 目录
        String storeDirectory = X509Context.store_path + "/" + cAEntity.getCN();
        //证书文件
        File keyFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.keyName);
        if (keyFile.exists() && keyFile.length() > 0 ) {
            return keyFile;
        }
        return null;
    }

    public static File getClientCert(X509Entity entity) {
        //CA 信息
        X509Entity cAEntity = EncryptUtils.getCA();
        //CA 目录
        String storeDirectory = X509Context.store_path + "/" + cAEntity.getCN();
        //证书文件
        File certFile = new File(storeDirectory + "/" + entity.getCN() + X509Context.certName);
        if (certFile.exists() && certFile.length() > 0 ) {
            return certFile;
        }
        return null;
    }
}
