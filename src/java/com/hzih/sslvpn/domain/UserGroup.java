package com.hzih.sslvpn.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 14-4-16
 * Time: 上午9:15
 * To change this template use File | Settings | File Templates.
 */
public class UserGroup   implements Serializable {

    private int  user_id;
    private int group_id;

    public UserGroup() {
    }

    public UserGroup(int user_id, int group_id) {
        this.user_id = user_id;
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
