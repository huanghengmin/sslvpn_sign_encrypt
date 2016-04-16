package com.hzih.sslvpn.utils;

import com.hzih.sslvpn.domain.Groups;
import com.hzih.sslvpn.domain.Server;
import com.hzih.sslvpn.domain.SourceNet;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.web.action.sslvpn.encrypt.config.X509Context;
import com.inetec.common.util.Proc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-10
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class VPNConfigUtil {

    /**
     * 构建android 客户端配置文件
     *
     * @param server        服务器端配置对象
     * @param androidConfig android 客户端配置文件保存路径
     * @throws Exception
     */
    public static void androidConfig(Server server, String androidConfig) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("client").append("\n");
//        sb.append("max-routes 5000").append("\n");
        sb.append("dev tun").append("\n");
        //服务监听协议
        sb.append("proto " + server.getProtocol()).append("\n");
        if (server.getListen().equals("0.0.0.0")) {
            //服务监听地址
            sb.append("remote www.sslvpn.net " + server.getPort()).append("\n");
        } else {
            //服务端口
            sb.append("remote " + server.getListen() + " " + server.getPort()).append("\n");
        }
//        sb.append("resolv-retry infinite" ).append("\n");
        sb.append("persist-key").append("\n");
        sb.append("persist-tun").append("\n");
        //证书相关配置
        sb.append("ca ca.crt").append("\n");
        sb.append("cert client.crt").append("\n");
        sb.append("key client.key").append("\n");
        //加密tls文件
        //sb.append("tls-auth ta.key 1").append("\n");
        //加密算法
        sb.append("cipher " + server.getCipher()).append("\n");
        //支持客户端数据压缩
        if (server.getComp_lzo() == 1)
            sb.append("comp-lzo").append("\n");
        //日志级别
        sb.append("verb " + server.getVerb()).append("\n");
        //重复日志记录数
        sb.append("mute " + server.getMute()).append("\n");
        File file = new File(androidConfig);
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
    }

    /**
     * 构建windows 客户端配置文件
     *
     * @param server        服务器端配置对象
     * @param windowsConfig windows 客户端配置文件保存路径
     * @throws Exception
     */
    public static void windowsConfig(Server server, String windowsConfig) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("client").append("\n");
        sb.append("max-routes 5000").append("\n");
        sb.append("dev tun").append("\n");
        //服务监听协议
        sb.append("proto " + server.getProtocol()).append("\n");
        if (server.getListen().equals("0.0.0.0")) {
            //服务监听地址
            sb.append("remote www.sslvpn.net " + server.getPort()).append("\n");
        } else {
            //服务端口
            sb.append("remote " + server.getListen() + " " + server.getPort()).append("\n");
        }
        sb.append("resolv-retry infinite").append("\n");
        sb.append("persist-key").append("\n");
        sb.append("persist-tun").append("\n");
        //证书相关配置
        sb.append("ca ca.crt").append("\n");
        //加密tls文件
        //sb.append("tls-auth ta.key 1").append("\n");
        sb.append("cert client.crt").append("\n");
        sb.append("key client.key").append("\n");
        //加密算法
        sb.append("cipher " + server.getCipher()).append("\n");
        //支持客户端数据压缩
        if (server.getComp_lzo() == 1)
            sb.append("comp-lzo").append("\n");
        //日志级别
        sb.append("verb " + server.getVerb()).append("\n");
        //重复日志记录数
        sb.append("mute " + server.getMute()).append("\n");
        File file = new File(windowsConfig);
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
    }

    /**
     * @param server           服务器端配置对象服务器端配置对象
     * @param server_file_path 服务端配置文件保存路径
     * @throws Exception
     */
    public static void configServer(Server server, String server_file_path) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (!server.getListen().equals("0.0.0.0")) {
            //服务监听地址
            sb.append("local " + server.getListen()).append("\n");
        }
        //服务监听协议
        sb.append("proto " + server.getProtocol()).append("\n");
        //服务端口
        sb.append("port " + server.getPort()).append("\n");
        sb.append("dev tun").append("\n");
        sb.append("management localhost 7505").append("\n");
        //证书相关配置
        sb.append("ca " + X509Context.encrypt_ca_file).append("\n");
        sb.append("cert " + X509Context.encrypt_server_file).append("\n");
        sb.append("key " + X509Context.encrypt_key_file).append("\n");
        //crl 检查
        /*if (server.getCheck_crl() == 1) {
            File crl = new File(StringContext.crl_file);
            if (crl.exists() && crl.length() > 0) {
                sb.append("crl-verify " + StringContext.crl_file).append("\n");
            }
        }*/
        sb.append("dh " + StringContext.dh_file).append("\n");
        //动态ip分配地址
        sb.append("server " + server.getServer_net() + " " + server.getServer_mask()).append("\n");
        // 可访问子网
        Set<SourceNet> sourceNets = server.getSourceNets();
        //加入所有子网路由表
        if (null != sourceNets && sourceNets.size() > 0) {
            for (SourceNet net : sourceNets) {
                sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
            }
        }
        //此处配置客户端相互通信即所有客户端可以相互通信
        if (server.getClient_to_client() == 1) {
            sb.append("client-to-client").append("\n");
        }
        //客户端可以使用同一证书多次连接
        if (server.getDuplicate_cn() == 1) {
            sb.append("duplicate-cn").append("\n");
        }
        sb.append("ifconfig-pool-persist " + StringContext.pool_file).append("\n");

        //客户端权限配置地址
        sb.append("client-config-dir " + StringContext.ccd).append("\n");
        //加入后必须在client-config-dir 目录中有对应的文件才能连接,否则不能连接
        //sb.append(";ccd-exclusive ").append("\n");
        //保持存活时间
        sb.append("keepalive " + server.getKeep_alive_interval() + " " + server.getKeep_alive()).append("\n");
        //支持客户端数据压缩
        if (server.getComp_lzo() == 1)
            sb.append("comp-lzo").append("\n");

        //配置客户端所有流量都通过VPN服务器
        if (server.getTraffic_server() == 1) {
            sb.append("push ").append("\"redirect-gateway def1 bypass-dhcp" + "\"").append("\n");
        }
        //dns 类型
        if (server.getClient_dns_type() == 2) {
            if (null != server.getClient_first_dns() && "" != server.getClient_first_dns()) {
                sb.append("push ").append("\"dhcp-option DNS " + server.getClient_first_dns() + "\"").append("\n");
            }
            if (null != server.getClient_second_dns() && "" != server.getClient_second_dns()) {
                sb.append("push ").append("\"dhcp-option DNS " + server.getClient_second_dns() + "\"").append("\n");
            }
        } else if (server.getClient_dns_type() == 1) {
            Proc proc = new Proc();
            if (proc.exec("more /etc/resolv.conf |awk '/nameserver/{split($2,x,\" \");print x[1]}'")) {
                String[] dns = proc.getOutput().split("\n");
                String dns_1 = null;
                String dns_2 = null;
                for (int k = 0; k < dns.length; k++) {
                    if (dns[k].startsWith("nameserver")) {
                        dns_1 = dns[k].split("nameserver")[1].trim();
                        if(dns_1!=null&&dns_1.length()>0)
                        sb.append("push ").append("\"dhcp-option DNS " + dns_1 + "\"").append("\n");
                        if (k < dns.length - 1 && dns[k + 1].startsWith("nameserver")) {
                            dns_2 = dns[k + 1].split("nameserver")[1].trim();
                            if(dns_2!=null&&dns_2.length()>0)
                            sb.append("push ").append("\"dhcp-option DNS " + dns_2 + "\"").append("\n");
                            break;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        //默认域名后缀
        if (null != server.getDefault_domain_suffix()) {
            sb.append("push ").append("\"dhcp-option DOMAIN " + server.getDefault_domain_suffix() + "\"").append("\n");
        }
        //加密tls文件
        //sb.append("tls-auth " + StringContext.ta_key_file + " 0").append("\n");
        //加密算法
        sb.append("cipher " + server.getCipher()).append("\n");
        //最大连接数
        sb.append("max-clients " + server.getMax_clients()).append("\n");
       /* int max = 0;
        try {
            max = License.getMaxConnect();
        } catch (Exception e) {

        }*/
//        sb.append("max-clients " + max).append("\n");
        sb.append("persist-key").append("\n");
        sb.append("persist-tun").append("\n");
        //状态文件不记录
        //sb.append("status " + StringContext.serverLogPath + "/" + "server_status.log").append("\n");
        if (server.getLog_flag() == 1) {
            if (server.getLog_append() == 1)
                sb.append("log-append  " + StringContext.serverLogPath + "/" + "server.log").append("\n");
            else
                sb.append("log " + StringContext.serverLogPath + "/" + "server.log").append("\n");
            //日志级别
            sb.append("verb " + server.getVerb()).append("\n");
            //重复日志记录数
            sb.append("mute " + server.getMute()).append("\n");
        }
        //
        sb.append("script-security 2").append("\n");
        if (server.getUse_learn_address_script() == 1)
            sb.append("learn-address " + StringContext.script_path + "/learn_address_script.sh").append("\n");
        if (server.getUse_connect_script() == 1)
            sb.append("client-connect " + StringContext.script_path + "/client_connect.sh").append("\n");
        if (server.getUse_disconnect_script() == 1)
            sb.append("client-disconnect " + StringContext.script_path + "/client_disconnect.sh").append("\n");

        File file = new File(server_file_path);
        FileOutputStream out = new FileOutputStream(file);
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
        androidConfig(server, StringContext.android_config_file);
        windowsConfig(server, StringContext.windows_config_file);
    }

    /**
     * 校验group是否禁用
     *
     * @param groupses
     * @return
     */
    public static boolean checkDisable(Set<Groups> groupses) {
        if (groupses != null) {
            for (Groups groups : groupses) {
                if (groups.getDeny_access() == 1) {
                    //禁用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param user              用户
     * @param manager_user_path 用户配置文件路径
     * @throws Exception
     */
    public static void configUser(User user, String manager_user_path) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (user.getEnabled() == 0) {
            sb.append("disable ").append("\n");
            File file = new File(manager_user_path + "/" + user.getCn());
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
        } else {
            boolean flag = checkDisable(user.getGroupsSet());
            if (flag) {
                sb.append("disable ").append("\n");
                File file = new File(manager_user_path + "/" + user.getCn());
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream out = new FileOutputStream(file);
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
            } else {
                int client_to_client = user.getAllow_all_client();
                int dynamic = user.getDynamic_ip();
                if (dynamic == 0) {
                    String static_ip = user.getStatic_ip();
                    String start_ip = static_ip.substring(0, static_ip.lastIndexOf("."));
                    int end = Integer.parseInt(static_ip.substring(static_ip.lastIndexOf(".") + 1, static_ip.length()));
                    sb.append("ifconfig-push ").append(user.getStatic_ip()).append(" ").append(start_ip).append(end + 1).append("\n");
                }
                if (client_to_client == 1) {
                    sb.append("client-to-client").append("\n");
                }
                Set<SourceNet> sourceNets = new HashSet<>();
                Set<SourceNet> sourceNets_u = user.getSourceNets();
                if (sourceNets_u != null && sourceNets_u.size() > 0) {
                    for (SourceNet net : sourceNets_u) {
                        if (!sourceNets.contains(net)) {
                            sourceNets.add(net);
                        }
                    }
                }
                if (null != sourceNets && sourceNets.size() > 0) {
                    for (SourceNet net : sourceNets) {
                        sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
                    }
                }
                File file = new File(manager_user_path + "/" + user.getCn());
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream out = new FileOutputStream(file);
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
            }
        }
    }

    /**
     * @param manager_user_path 用户配置文件路径
     * @param groups            用户组
     * @throws Exception
     */
    public static void configGroup(String manager_user_path, Groups groups) throws Exception {
        StringBuilder sb = new StringBuilder();
        /**
         * 如果禁用，所有用户全部禁用
         */
        if (groups.getDeny_access() == 1) {
            Set<User> users = groups.getUserSet();
            if (users != null && users.size() > 0) {
                for (User u : users) {
                    sb.append("disable ").append("\n");
                    File file = new File(manager_user_path + "/" + u.getCn());
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(sb.toString().getBytes());
                    out.flush();
                    out.close();
                }
            }
        } else {
            /**
             * 非禁用状态
             */
            Set<User> users = groups.getUserSet();
            if (users != null && users.size() > 0) {
                for (User user : users) {
                    if (user.getEnabled() == 0) {
                        sb.append("disable ").append("\n");
                        File file = new File(manager_user_path + "/" + user.getCn());
                        if (file.exists()) {
                            file.delete();
                        }
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(sb.toString().getBytes());
                        out.flush();
                        out.close();
                    } else {
                        int client_to_client = user.getAllow_all_client();
                        int dynamic = user.getDynamic_ip();
                        if (dynamic == 0) {
                            String static_ip = user.getStatic_ip();
                            String start_ip = static_ip.substring(0, static_ip.lastIndexOf("."));
                            int end = Integer.parseInt(static_ip.substring(static_ip.lastIndexOf(".") + 1, static_ip.length()));
                            sb.append("ifconfig-push ").append(user.getStatic_ip()).append(" ").append(start_ip).append(end + 1).append("\n");
                        }
                        if (client_to_client == 1) {
                            sb.append("client-to-client").append("\n");
                        }
                        Set<SourceNet> sourceNets = new HashSet<>();
                        if (groups != null) {
                            Set<SourceNet> netSet = groups.getSourceNets();
                            if (netSet != null) {
                                for (SourceNet net : netSet) {
                                    if (!sourceNets.contains(net))
                                        sourceNets.add(net);
                                }
                            }
                            Set<SourceNet> u_sourceNets = user.getSourceNets();
                            if (null != u_sourceNets && u_sourceNets.size() > 0) {
                                for (SourceNet net : u_sourceNets) {
                                    if (!sourceNets.contains(net)) {
                                        sourceNets.add(net);
                                    }
                                }
                            }
                            if (sourceNets != null && sourceNets.size() > 0) {
                                for (SourceNet net : sourceNets) {
                                    sb.append("push ").append("\"route " + net.getNet() + " " + net.getNet_mask() + "\"").append("\n");
                                }
                            }
                        }
                        File file = new File(manager_user_path + "/" + user.getCn());
                        if (file.exists()) {
                            file.delete();
                        }
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(sb.toString().getBytes());
                        out.flush();
                        out.close();
                    }
                }
            }
        }
    }
}
