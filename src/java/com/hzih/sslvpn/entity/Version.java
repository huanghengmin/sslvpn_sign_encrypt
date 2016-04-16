package com.hzih.sslvpn.entity;

/**
 * Created by Administrator on 15-3-9.
 */
public class Version {
    private String version;
    private String name;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Version(String version, String name) {
        this.version = version;
        this.name = name;
    }
}
