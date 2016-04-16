package com.hzih.sslvpn.domain;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public class SourceNet {

    private int id;
    private String net;
    private String net_mask;

    private Set<User> users;

    private Set<Groups> groupsSet;

    private Set<Server> serverSet;

    public SourceNet() {
    }

    public SourceNet(int id) {
        this.id = id;
    }

    public SourceNet(String net_mask, String net) {
        this.net_mask = net_mask;
        this.net = net;
    }

    public Set<Server> getServerSet() {
        return serverSet;
    }

    public void setServerSet(Set<Server> serverSet) {
        this.serverSet = serverSet;
    }

    public Set<Groups> getGroupsSet() {
        return groupsSet;
    }

    public void setGroupsSet(Set<Groups> groupsSet) {
        this.groupsSet = groupsSet;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getNet_mask() {
        return net_mask;
    }

    public void setNet_mask(String net_mask) {
        this.net_mask = net_mask;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (net != null ? net.hashCode() : 0);
        result = 31 * result + (net_mask != null ? net_mask.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || (this.getClass() != obj.getClass())) {
            return false;
        }

        SourceNet sourceNet = (SourceNet) obj;
        return (this.net != null && net.equals(sourceNet.net)) &&
                (this.net_mask != null && net_mask.equals(sourceNet.net_mask));
    }
}
