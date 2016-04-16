package com.hzih.sslvpn.domain;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-5
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    private int id;
    private String listen ;
    private int port=1194;
    private String protocol="tcp";
    private String server_net = "10.8.0.0";
    private String server_mask = "255.255.255.0";
    private int check_crl = 1;
    private int traffic_server = 0;
    private int client_to_client = 1;
    private int duplicate_cn =0;
    private int keep_alive=60;
    private int keep_alive_interval=10;
    private String cipher = "DES-EDE3-CBC";
    private int comp_lzo=1;
    private int max_clients=300;
    private int log_append=1;
    private int log_flag=1;
    private int verb=3;
    private int mute=5;
    private int client_dns_type=0;
    private String client_first_dns;
    private String client_second_dns;
    private String default_domain_suffix="sslvpn.com";
    private int use_connect_script=1;
    private int use_disconnect_script=1;
    private int use_learn_address_script=0;
    private Set<SourceNet> sourceNets;

    public String getServer_mask() {
        return server_mask;
    }

    public void setServer_mask(String server_mask) {
        this.server_mask = server_mask;
    }

    public int getCheck_crl() {
        return check_crl;
    }

    public void setCheck_crl(int check_crl) {
        this.check_crl = check_crl;
    }

    public Set<SourceNet> getSourceNets() {
        return sourceNets;
    }

    public void setSourceNets(Set<SourceNet> sourceNets) {
        this.sourceNets = sourceNets;
    }

    public int getLog_flag() {
        return log_flag;
    }

    public void setLog_flag(int log_flag) {
        this.log_flag = log_flag;
    }

    public int getUse_connect_script() {
        return use_connect_script;
    }

    public void setUse_connect_script(int use_connect_script) {
        this.use_connect_script = use_connect_script;
    }

    public int getUse_disconnect_script() {
        return use_disconnect_script;
    }

    public void setUse_disconnect_script(int use_disconnect_script) {
        this.use_disconnect_script = use_disconnect_script;
    }

    public int getUse_learn_address_script() {
        return use_learn_address_script;
    }

    public void setUse_learn_address_script(int use_learn_address_script) {
        this.use_learn_address_script = use_learn_address_script;
    }

    public int getClient_dns_type() {
        return client_dns_type;
    }

    public void setClient_dns_type(int client_dns_type) {
        this.client_dns_type = client_dns_type;
    }

    public String getClient_first_dns() {
        return client_first_dns;
    }

    public void setClient_first_dns(String client_first_dns) {
        this.client_first_dns = client_first_dns;
    }

    public String getClient_second_dns() {
        return client_second_dns;
    }

    public void setClient_second_dns(String client_second_dns) {
        this.client_second_dns = client_second_dns;
    }

    public String getDefault_domain_suffix() {
        return default_domain_suffix;
    }

    public void setDefault_domain_suffix(String default_domain_suffix) {
        this.default_domain_suffix = default_domain_suffix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getListen() {
        return listen;
    }

    public void setListen(String listen) {
        this.listen = listen;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServer_net() {
        return server_net;
    }

    public void setServer_net(String server_net) {
        this.server_net = server_net;
    }

    public int getTraffic_server() {
        return traffic_server;
    }

    public void setTraffic_server(int traffic_server) {
        this.traffic_server = traffic_server;
    }

    public int getClient_to_client() {
        return client_to_client;
    }

    public void setClient_to_client(int client_to_client) {
        this.client_to_client = client_to_client;
    }

    public int getDuplicate_cn() {
        return duplicate_cn;
    }

    public void setDuplicate_cn(int duplicate_cn) {
        this.duplicate_cn = duplicate_cn;
    }

    public int getKeep_alive() {
        return keep_alive;
    }

    public void setKeep_alive(int keep_alive) {
        this.keep_alive = keep_alive;
    }

    public int getKeep_alive_interval() {
        return keep_alive_interval;
    }

    public void setKeep_alive_interval(int keep_alive_interval) {
        this.keep_alive_interval = keep_alive_interval;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public int getComp_lzo() {
        return comp_lzo;
    }

    public void setComp_lzo(int comp_lzo) {
        this.comp_lzo = comp_lzo;
    }

    public int getMax_clients() {
        return max_clients;
    }

    public void setMax_clients(int max_clients) {
        this.max_clients = max_clients;
    }

    public int getLog_append() {
        return log_append;
    }

    public void setLog_append(int log_append) {
        this.log_append = log_append;
    }

    public int getVerb() {
        return verb;
    }

    public void setVerb(int verb) {
        this.verb = verb;
    }

    public int getMute() {
        return mute;
    }

    public void setMute(int mute) {
        this.mute = mute;
    }

}
