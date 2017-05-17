package com.hzih.sslvpn.utils;


import com.hzih.sslvpn.web.action.config.HotBackupKeepAlivedAction;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-10
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class KeepAlivedConfigUtil {
    /**
     * @param server_file_path 服务端配置文件保存路径
     * @throws Exception
     */
    public static void configServer(String server_file_path) throws Exception {

        Document doc = Dom4jUtil.getDocument(HotBackupKeepAlivedAction.keepalived_xml);
        if (doc != null) {
            Element del_url = doc.getRootElement();
            if (del_url != null) {
                String device_type_element = del_url.element("device_type").getText();
                String listen_inet_element = del_url.element("listen_inet").getText();
                String virt_ip_element = del_url.element("virt_ip").getText();
                String back_inet = del_url.element("back_inet").getText();
                StringBuilder sb = new StringBuilder();

                sb.append("! Configuration File for keepalived\n" +
                        "global_defs {\n" +
                        "   router_id HA_SSLVPN\n" +
                        " }\n" +
                        "vrrp_instance VI_1 {\n");

                if (device_type_element.equals("1")) {
                    sb.append("    state MASTER\n");
                } else {
                    sb.append("    state BACKUP\n");
                }

                sb.append("    interface "+back_inet+"\n" +
                        "    virtual_router_id 51\n" +
                        "    priority 100\n" +
                        "    advert_int 1\n" +
                        "    authentication {\n" +
                        "        auth_type PASS\n" +
                        "        auth_pass 1111\n" +
                        "    }\n" +
                        "    virtual_ipaddress {\n");
                sb.append(virt_ip_element).append("\n");

                sb.append("    }\n" +
                        "    track_interface {\n");
                String[] strings = listen_inet_element.split(",");
                for (String s : strings) {
                    sb.append(s).append("\n");
                }
                sb.append("     }\n" +
                        "}\n");

                Element virtservers_el = del_url.element("virtservers");
                if (virtservers_el != null) {
                    List<Element> elementList = virtservers_el.elements("virtserver");
                    if (elementList != null && elementList.size() > 0) {
                        for (Element element : elementList) {
                            String ip = element.attributeValue("ip");
                            String port = element.attributeValue("port");
                            String v_ip = element.attributeValue("v_ip");
                            String v_port = element.attributeValue("v_port");
                            if (ip != null && port != null && v_ip != null && v_port != null) {

                                sb.append(" virtual_server "+v_ip+" "+v_port+" {\n" +
                                        "     delay_loop 2\n" +
                                        "     lb_algo wrr\n" +
                                        "     lb_kind DR\n" +
                                        "     persistence_timeout 60\n" +
                                        "     protocol TCP\n" +
                                        "     real_server "+ip+" "+port+" {\n" +
                                        "         weight 3\n" +
                                        "         notify_down "+StringContext.script_path+"/keepalived_stop.sh"+"\n" +
                                        "         TCP_CHECK {\n" +
                                        "             connect_timeout 10\n" +
                                        "             nb_get_retry 3\n" +
                                        "             delay_before_retry 3\n" +
                                        "             connect_port 3306\n" +
                                        "         } \n" +
                                        "     }\n" +
                                        "} \n");
                            }
                        }
                    }
                }
                File file = new File(server_file_path);
                FileOutputStream out = new FileOutputStream(file);
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
            }
        }
    }
}
