package com.hzih.sslvpn.domain;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-2
 * Time: 下午8:05
 * To change this template use File | Settings | File Templates.
 */
public class Groups {
    private Set<User> userSet;
//    private Set<PrivateNet> privateNets;

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

 /*   public Set<PrivateNet> getPrivateNets() {
        return privateNets;
    }

    public void setPrivateNets(Set<PrivateNet> privateNets) {
        this.privateNets = privateNets;
    }*/

    private int id;
    private String group_name;
//    private String group_code;
    private int deny_access;
//    private String dynamic_ip_range;
//    private String assign_nets;
//    private String allow_group_ids;
    private Set<SourceNet> sourceNets;
//    private List<User> users;
    private String group_desc;
//    private int level = 0;


    public Set<SourceNet> getSourceNets() {
        return sourceNets;
    }

    public void setSourceNets(Set<SourceNet> sourceNets) {
        this.sourceNets = sourceNets;
    }
/*
    public String getDynamic_ip_range() {
        return dynamic_ip_range;
    }

    public void setDynamic_ip_range(String dynamic_ip_range) {
        this.dynamic_ip_range = dynamic_ip_range;
    }

    public String getAssign_nets() {
        return assign_nets;
    }

    public void setAssign_nets(String assign_nets) {
        this.assign_nets = assign_nets;
    }

    public String getAllow_group_ids() {
        return allow_group_ids;
    }

    public void setAllow_group_ids(String allow_group_ids) {
        this.allow_group_ids = allow_group_ids;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    /*public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }*/

    public int getDeny_access() {
        return deny_access;
    }

    public void setDeny_access(int deny_access) {
        this.deny_access = deny_access;
    }

   /* public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }*/

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

//    public int getLevel() {
//        return level;
//    }

//    public void setLevel(int level) {
//        this.level = level;
//    }
}
