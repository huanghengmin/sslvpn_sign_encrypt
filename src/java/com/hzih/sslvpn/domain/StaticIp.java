package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-13
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class StaticIp {
    private int client_end;
    private int server_end;

    public int getClient_end() {
        return client_end;
    }

    public void setClient_end(int client_end) {
        this.client_end = client_end;
    }

    public int getServer_end() {
        return server_end;
    }

    public void setServer_end(int server_end) {
        this.server_end = server_end;
    }
}
