package com.hzih.sslvpn.entity;

/**
 * Created by Administrator on 14-8-25.
 */
public class StatusMsg {
    private boolean flag;
    private String msg;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public StatusMsg(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public StatusMsg() {
    }
}
