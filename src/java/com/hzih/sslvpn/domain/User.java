package com.hzih.sslvpn.domain;

import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-2
 * Time: 下午8:02
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private Set<Groups> groupsSet;

    public Set<Groups> getGroupsSet() {
        return groupsSet;
    }

    public void setGroupsSet(Set<Groups> groupsSet) {
        this.groupsSet = groupsSet;
    }

    private int id;
    private String cn;
    private String subject;
    private int dynamic_ip = 1;
    private String static_ip;
    private int allow_all_client = 0;
    private String serial_number;
    private int enabled = 1;
    private String real_address;
    private long byte_received = 0;
    private long byte_send = 0;
    private Date connected_since;
    private String virtual_address;
    private Date last_ref;
    private String net_id;
    private String terminal_id;
    private int view_flag = 0;
    private int gps_flag = 0;
    private String description;

    private Set<SourceNet> sourceNets;

    private Set<UserGps> userGpses;

    public Set<SourceNet> getSourceNets() {
        return sourceNets;
    }

    public void setSourceNets(Set<SourceNet> sourceNets) {
        this.sourceNets = sourceNets;
    }

    public Set<UserGps> getUserGpses() {
        return userGpses;
    }

    public void setUserGpses(Set<UserGps> userGpses) {
        this.userGpses = userGpses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getNet_id() {
        return net_id;
    }

    public void setNet_id(String net_id) {
        this.net_id = net_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public int getView_flag() {
        return view_flag;
    }

    public void setView_flag(int view_flag) {
        this.view_flag = view_flag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDynamic_ip() {
        return dynamic_ip;
    }

    public void setDynamic_ip(int dynamic_ip) {
        this.dynamic_ip = dynamic_ip;
    }

    public String getStatic_ip() {
        return static_ip;
    }

    public void setStatic_ip(String static_ip) {
        this.static_ip = static_ip;
    }

    public int getAllow_all_client() {
        return allow_all_client;
    }

    public void setAllow_all_client(int allow_all_client) {
        this.allow_all_client = allow_all_client;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getReal_address() {
        return real_address;
    }

    public void setReal_address(String real_address) {
        this.real_address = real_address;
    }

    public long getByte_received() {
        return byte_received;
    }

    public void setByte_received(long byte_received) {
        this.byte_received = byte_received;
    }

    public long getByte_send() {
        return byte_send;
    }

    public void setByte_send(long byte_send) {
        this.byte_send = byte_send;
    }

    public Date getConnected_since() {
        return connected_since;
    }

    public void setConnected_since(Date connected_since) {
        this.connected_since = connected_since;
    }

    public String getVirtual_address() {
        return virtual_address;
    }

    public void setVirtual_address(String virtual_address) {
        this.virtual_address = virtual_address;
    }

    public Date getLast_ref() {
        return last_ref;
    }

    public void setLast_ref(Date last_ref) {
        this.last_ref = last_ref;
    }

    public int getGps_flag() {
        return gps_flag;
    }

    public void setGps_flag(int gps_flag) {
        this.gps_flag = gps_flag;
    }

}
