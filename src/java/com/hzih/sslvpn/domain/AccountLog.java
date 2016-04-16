package com.hzih.sslvpn.domain;

import java.util.Date;

/**
 * Created by Administrator on 15-4-23.
 */
public class AccountLog {
    private long id;
    private String account;//操作的管理员
    private String action;//动作及返回状态
    private String auditmodel;//审计模块名称
    private String auditlevel;//审计级别
    private String audittype;//审计类别
    private String result;
    private Date datetime;//日期时间

    public AccountLog() {
    }

    public AccountLog(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAuditmodel() {
        return auditmodel;
    }

    public void setAuditmodel(String auditmodel) {
        this.auditmodel = auditmodel;
    }

    public String getAuditlevel() {
        return auditlevel;
    }

    public void setAuditlevel(String auditlevel) {
        this.auditlevel = auditlevel;
    }

    public String getAudittype() {
        return audittype;
    }

    public void setAudittype(String audittype) {
        this.audittype = audittype;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
