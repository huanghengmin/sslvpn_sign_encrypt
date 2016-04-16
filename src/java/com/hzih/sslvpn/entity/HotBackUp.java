package com.hzih.sslvpn.entity;

import com.hzih.sslvpn.utils.Configuration;
import com.hzih.sslvpn.utils.StringContext;
import com.inetec.common.exception.Ex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-5-12
 * Time: 下午3:43
 * To change this template use File | Settings | File Templates.
 */
public class HotBackUp {
    public boolean isActive;
    public boolean isMainSystem;
    private String mainIp;
    private String backupIp;
    private int mainPort;
    private int backupPort;
    private String mainStatus;
    private String backupStatus;
    List<String> pings = new ArrayList<String>();
    List<String> telnets = new ArrayList<String>();
    List<String> others = new ArrayList<String>();

    public boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public boolean isMainSystem() {
        return isMainSystem;
    }

    public void setIsMainSystem(Boolean isMainSystem) {
        this.isMainSystem = isMainSystem;
    }

    public String getMainIp() {
        return mainIp;
    }

    public void setMainIp(String mainIp) {
        this.mainIp = mainIp;
    }

    public String getBackupIp() {
        return backupIp;
    }

    public void setBackupIp(String backupIp) {
        this.backupIp = backupIp;
    }

    public int getMainPort() {
        return mainPort;
    }

    public void setMainPort(int mainPort) {
        this.mainPort = mainPort;
    }

    public int getBackupPort() {
        return backupPort;
    }

    public void setBackupPort(int backupPort) {
        this.backupPort = backupPort;
    }

    public String getMainStatus() {
        return mainStatus;
    }

    public void setMainStatus(String mainStatus) {
        this.mainStatus = mainStatus;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(String backupStatus) {
        this.backupStatus = backupStatus;
    }

    public List<String> getPings() {
        return pings;
    }

    public void setPings(List<String> pings) {
        this.pings = pings;
    }

    public List<String> getTelnets() {
        return telnets;
    }

    public void setTelnets(List<String> telnets) {
        this.telnets = telnets;
    }

    public List<String> getOthers() {
        return others;
    }

    public void setOthers(List<String> others) {
        this.others = others;
    }

    public static HotBackUp readBackUp() throws Ex {
        Configuration config = new Configuration(StringContext.hotConfig);
        return config.initBackUp();
    }

    public static void updateBase(HotBackUp backUp) throws Ex {
        Configuration config = new Configuration(StringContext.hotConfig);
        config.editBackUpBase(backUp);
    }

    public static void update(List<String> list,String type) throws Ex {
        Configuration config = new Configuration(StringContext.hotConfig);
        config.editBackupList(list, type);
    }

    public static void insert(List<String> list,String type) throws Ex {
        Configuration config = new Configuration(StringContext.hotConfig);
        config.addBackupList(list, type);
    }

    public static void delete(List<String> list,String type) throws Ex {
        Configuration config = new Configuration(StringContext.hotConfig);
        config.deleteBackupList(list, type);
    }


}
