package com.hzih.sslvpn.domain;

/**
 * Created by IntelliJ IDEA.
 * User: cx
 * Date: 12-11-29
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class RoleUser {
    private int id;
    private int roleid;
    private int userid;

    public RoleUser() {
    }

    public RoleUser(int roleid, int userid) {
        this.roleid = roleid;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
