package com.hzih.sslvpn.entity;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: hhm
 * Date: 12-8-13
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
public class LdapInfo implements Serializable {
    private String baseDn;
    private String password;
    private String hostIp;
    private int port;
    private String admin;

    @Override
    public String toString() {
        return "LdapInfo{" +
                "baseDn='" + baseDn + '\'' +
                ", password='" + password + '\'' +
                ", hostIp='" + hostIp + '\'' +
                ", port=" + port +
                ", admin='" + admin + '\'' +
                '}';
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public LdapInfo(String baseDn, String password, String hostIp, int port, String admin) {
        this.baseDn = baseDn;
        this.password = password;
        this.hostIp = hostIp;
        this.admin = admin;
        this.port = port;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
