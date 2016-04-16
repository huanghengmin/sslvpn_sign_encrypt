package com.hzih.sslvpn.domain;

/**
 * Created by Administrator on 15-3-31.
 */
public class BackUp {
    private int id;
    private long backup_account_id;
    private int backup_all;
    private int backup_server;
    private int backup_pki;
    private int backup_net;
    private String backup_file;
    private String backup_time;
    private String backup_desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBackup_account_id(long backup_account_id) {
        this.backup_account_id = backup_account_id;
    }

    public long getBackup_account_id() {
        return backup_account_id;
    }

    public int getBackup_all() {
        return backup_all;
    }

    public void setBackup_all(int backup_all) {
        this.backup_all = backup_all;
    }

    public int getBackup_server() {
        return backup_server;
    }

    public void setBackup_server(int backup_server) {
        this.backup_server = backup_server;
    }

    public int getBackup_pki() {
        return backup_pki;
    }

    public void setBackup_pki(int backup_pki) {
        this.backup_pki = backup_pki;
    }

    public int getBackup_net() {
        return backup_net;
    }

    public void setBackup_net(int backup_net) {
        this.backup_net = backup_net;
    }

    public String getBackup_file() {
        return backup_file;
    }

    public void setBackup_file(String backup_file) {
        this.backup_file = backup_file;
    }

    public String getBackup_time() {
        return backup_time;
    }

    public void setBackup_time(String backup_time) {
        this.backup_time = backup_time;
    }

    public String getBackup_desc() {
        return backup_desc;
    }

    public void setBackup_desc(String backup_desc) {
        this.backup_desc = backup_desc;
    }
}
