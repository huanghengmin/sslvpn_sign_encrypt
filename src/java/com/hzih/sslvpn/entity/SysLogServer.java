package com.hzih.sslvpn.entity;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: hhm
 * Date: 12-10-26
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
public class SysLogServer implements Serializable {

    public SysLogServer() {

    }

    private String host;
    private int port;

    @Override
    public boolean equals(Object obj) {
        boolean flag = false;
        if(obj instanceof SysLogServer){
           SysLogServer sysLogServer = (SysLogServer)obj;
            if(this.getHost().equals(sysLogServer.getHost())&&this.getPort()==sysLogServer.getPort()){
               flag =true;
            }
        }
        return flag;
    }

    public SysLogServer(String ip, int port) {
        this.host = ip;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
