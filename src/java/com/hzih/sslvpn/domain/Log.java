package com.hzih.sslvpn.domain;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午1:59
 * To change this template use File | Settings | File Templates.
 */
public class Log {
    private int id;
    private String cn;
    private String serial_number;
    private String subject_dn;
    private Date start_time;
    private Date end_time;
    private String trusted_ip;
    private int trusted_port;
    private String protocol;
    private String remote_ip;
    private String remote_netmask;
    private long bytes_received;
    private long bytes_sent;
    private int status;
    private String description;

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

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getSubject_dn() {
        return subject_dn;
    }

    public void setSubject_dn(String subject_dn) {
        this.subject_dn = subject_dn;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getTrusted_ip() {
        return trusted_ip;
    }

    public void setTrusted_ip(String trusted_ip) {
        this.trusted_ip = trusted_ip;
    }

    public int getTrusted_port() {
        return trusted_port;
    }

    public void setTrusted_port(int trusted_port) {
        this.trusted_port = trusted_port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRemote_ip() {
        return remote_ip;
    }

    public void setRemote_ip(String remote_ip) {
        this.remote_ip = remote_ip;
    }

    public String getRemote_netmask() {
        return remote_netmask;
    }

    public void setRemote_netmask(String remote_netmask) {
        this.remote_netmask = remote_netmask;
    }

    public long getBytes_received() {
        return bytes_received;
    }

    public void setBytes_received(long bytes_received) {
        this.bytes_received = bytes_received;
    }

    public long getBytes_sent() {
        return bytes_sent;
    }

    public void setBytes_sent(long bytes_sent) {
        this.bytes_sent = bytes_sent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
