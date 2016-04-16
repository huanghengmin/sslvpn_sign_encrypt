package com.hzih.sslvpn.entity;


public class NetInfo {
    private String interfaceName;
    private String encap;
    private String displayName;
    private String mac;
    private String ip;
    private Boolean isUp;
    private String subnetMask;
    private String broadCast;
    private String gateway;
    private String dhcp;
    private String dns_1;    //域名服务器地址
    private String dns_2;
    private boolean dhcpEnabled;

    @Override
    public String toString() {
        return "NetInfo{" +
                "interfaceName='" + interfaceName + '\'' +
                ", encap='" + encap + '\'' +
                ", displayName='" + displayName + '\'' +
                ", mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", isUp=" + isUp +
                ", subnetMask='" + subnetMask + '\'' +
                ", broadCast='" + broadCast + '\'' +
                ", gateway='" + gateway + '\'' +
                ", dhcp='" + dhcp + '\'' +
                ", dns_1='" + dns_1 + '\'' +
                ", dns_2='" + dns_2 + '\'' +
                ", dhcpEnabled=" + dhcpEnabled +
                ", autoConfigurationEnabled=" + autoConfigurationEnabled +
                ", leaseObtained='" + leaseObtained + '\'' +
                ", leaseExpires='" + leaseExpires + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }

    private boolean autoConfigurationEnabled;
    private String leaseObtained;
    private String leaseExpires;
    private String destination;

    public NetInfo() {

    }

    public NetInfo(String interfaceName, String subnetMask, String gateway, String destination) {
        this.interfaceName = interfaceName;
        this.subnetMask = subnetMask;
        this.gateway = gateway;
        this.destination = destination;
    }

    public NetInfo(String interfaceName, String ip, Boolean up, String subnetMask, String broadCast) {
        this.interfaceName = interfaceName;
        this.ip = ip;
        this.isUp = up;
        this.subnetMask = subnetMask;
        this.broadCast = broadCast;
    }

    public NetInfo(String interfaceName, String encap, String ip, Boolean up, String gateway, String subnetMask, String broadCast) {
        this.interfaceName = interfaceName;
        this.encap = encap;
        this.ip = ip;
        this.isUp = up;
        this.subnetMask = subnetMask;
        this.broadCast = broadCast;
        this.gateway = gateway;
    }

    public NetInfo(String interfaceName, String dns_1, String dns_2) {
        this.interfaceName = interfaceName;
        this.dns_1 = dns_1;
        this.dns_2 = dns_2;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getInterfaceName(){
        return interfaceName;
    }
    public void setInterfaceName(String interfaceName){
        this.interfaceName = interfaceName;
    }
    public String getEncap(){
        return encap;
    }
    public void setEncap(String encap){
        this.encap = encap;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getSubnetMask() {
        return subnetMask;
    }
    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }
    public String getBroadCast() {
        return broadCast;
    }
    public void setBroadCast(String broadCast) {
        this.broadCast = broadCast;
    }
    public String getGateway() {
        return gateway;
    }
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
    public String getDhcp() {
        return dhcp;
    }
    public void setDhcp(String dhcp) {
        this.dhcp = dhcp;
    }
    public String getDns_1() {
        return dns_1;
    }
    public void setDns_1(String dns_1) {
        this.dns_1 = dns_1;
    }
    public String getDns_2() {
        return dns_2;
    }
    public void setDns_2(String dns_2) {
        this.dns_2 = dns_2;
    }
    public boolean isDhcpEnabled() {
        return dhcpEnabled;
    }
    public void setDhcpEnabled(boolean dhcpEnabled) {
        this.dhcpEnabled = dhcpEnabled;
    }
    public boolean isAutoConfigurationEnabled() {
        return autoConfigurationEnabled;
    }
    public void setAutoConfigurationEnabled(boolean autoConfigurationEnabled) {
        this.autoConfigurationEnabled = autoConfigurationEnabled;
    }
    public String getLeaseObtained() {
        return leaseObtained;
    }
    public void setLeaseObtained(String leaseObtained) {
        this.leaseObtained = leaseObtained;
    }
    public String getLeaseExpires() {
        return leaseExpires;
    }
    public void setLeaseExpires(String leaseExpires) {
        this.leaseExpires = leaseExpires;
    }
    public Boolean getIsUp() {
        return isUp;
    }
    public void setIsUp(Boolean isUp) {
        this.isUp = isUp;
    }


}
