package com.hzih.sslvpn.utils;

import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 15-5-5.
 */
public class ShellUtils {
    private static Logger logger = Logger.getLogger(ShellUtils.class);

    /**
     * 开启SSLVPN服务
     * @param net_mask
     * @param protocol
     * @param port
     * @return
     */
    public static boolean firewall_start(String net_mask,String protocol,String port) {
        Proc proc = new Proc();
        String command = null;
        logger.info("开启SSLVPN服务脚本 net_mask:"+net_mask+",protocol:"+protocol+",port:"+port);
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/script/firewall_start.bat " +
                    net_mask + " " +
                    protocol+ " " +
                    port;
        } else {
            command = StringContext.systemPath + "/script/firewall_start.sh " +
                    net_mask + " " +
                    protocol+ " " +
                    port;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
            if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }


    /**
     * 关闭SSLVPN服务
     * @param net_mask
     * @param protocol
     * @param port
     * @return
     */
    public static boolean firewall_stop(String net_mask,String protocol,String port) {
        Proc proc = new Proc();
        String command = null;
        logger.info("终止SSLVPN服务脚本 net_mask:"+net_mask+",protocol:"+protocol+",port:"+port);
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/script/firewall_stop.bat " +
                    net_mask + " " +
                    protocol+ " " +
                    port;
        } else {
            command = StringContext.systemPath + "/script/firewall_stop.sh " +
                    net_mask + " " +
                    protocol+ " " +
                    port;
        }
        proc.exec(command);
        if (proc.getResultCode() != -1) {
            if(!proc.getErrorOutput().contains("error")&&!proc.getErrorOutput().contains("Error")){
                return true;
            } else {
                logger.equals(proc.getErrorOutput());
            }
        }
        return false;
    }
}
