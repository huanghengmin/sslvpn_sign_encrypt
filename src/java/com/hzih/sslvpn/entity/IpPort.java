package com.hzih.sslvpn.entity;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: hhm
 * Date: 12-10-26
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
public class IpPort implements Serializable {

    public IpPort() {
    }

    private String ip;
    private int port;

    @Override
    public String toString() {
        return "IpPort{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        boolean flag = false;
        if(obj instanceof IpPort){
           IpPort ipPort = (IpPort)obj;
            if(this.getIp().equals(ipPort.getIp())&&this.getPort()==ipPort.getPort()){
               flag =true;
            }
        }
        return flag;
    }

    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
