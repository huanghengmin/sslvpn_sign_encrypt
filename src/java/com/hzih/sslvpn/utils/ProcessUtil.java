package com.hzih.sslvpn.utils;

import com.hzih.sslvpn.entity.NetInfo;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cx
 * Date: 13-3-25
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class ProcessUtil {
    private Logger log = Logger.getLogger(getClass());

    public static void main(String[] args) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BufferedReader brs = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public String getProcessInfo(String command,String processName) {
        String netFlow = "0";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader brs = new BufferedReader(new InputStreamReader(process.getInputStream()));
            brs.readLine();
            while(true){
                String line = brs.readLine();
                if(line==null){
                    break;
                }else {
                    if(line.indexOf(processName)!=-1) {
                        netFlow = line;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return netFlow;
    }

    public String getLinuxByCommand( String command ) {
        String netFlow="";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader brs = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while(true){
                String line = brs.readLine();
                if(line==null){
                    break;
                }else {
                    netFlow = netFlow+line+"\n";
                }
            }
        } catch (IOException e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return netFlow;
    }

    public String getLinuxNetFlow(String eth ) {
        String netFlow="";
        try {
            Process process = Runtime.getRuntime().exec("/sbin/ifconfig "+eth+" | grep bytes");
            BufferedReader brs = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while(true){
                String line = brs.readLine();
                if(line==null){
                    break;
                }else {
                    netFlow = netFlow+line;
                }
            }
        } catch (IOException e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return netFlow;
    }


    public String getLinuxNetFlow() {
        String netFlow="";
        try {
            Process process = Runtime.getRuntime().exec("/sbin/ifconfig eth11 | grep bytes");
            BufferedReader brs = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while(true){
                String line = brs.readLine();
                if(line==null){
                    break;
                }else {
                    netFlow = netFlow+line;
                }
            }
        } catch (IOException e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return netFlow;
    }

    public Double getRxBytesNum1() {
        String netFlow = getLinuxNetFlow();
        String[] lines = netFlow.split(":");
        String[] aa = lines[1].split(" ");
        String rxbytes = aa[0];
        return Double.valueOf(rxbytes)/1024/1024;
    }

    public Double getRxBytesNum() {
        GetListNetInfo getListNetInfo = new GetListNetInfo();
        Double rxnum=0.0;
        try {
            List<NetInfo> netInfos = getListNetInfo.readInterfaces();
            for(NetInfo netInfo : netInfos) {
                String netFlow = getLinuxNetFlow(netInfo.getInterfaceName());
                String[] lines = netFlow.split(":");
                String[] aa = lines[1].split(" ");
                String rxbytes = aa[0];
                rxnum = rxnum + Double.valueOf(rxbytes);
            }
        } catch (Exception e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return rxnum/1024/1024;
    }

    public Double getTxBytesNum1() {
        String netFlow = getLinuxNetFlow();
        String[] lines = netFlow.split(":");
        String[] aa = lines[2].split(" ");
        String rxbytes = aa[0];
        return Double.valueOf(rxbytes)/1024/1024;
    }

    public Double getTxBytesNum() {
        GetListNetInfo getListNetInfo = new GetListNetInfo();
        Double txnum=0.0;
        try {
            List<NetInfo> netInfos = getListNetInfo.readInterfaces();
            for(NetInfo netInfo : netInfos) {
                String netFlow = getLinuxNetFlow(netInfo.getInterfaceName());
                String[] lines = netFlow.split(":");
                String[] aa = lines[2].split(" ");
                String rxbytes = aa[0];
                txnum = txnum + Double.valueOf(rxbytes);
            }
        } catch (Exception e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return txnum/1024/1024;
    }

    public Double getCPU() {
        Double cpuUse = 0.0;
        try {
            Process process = Runtime.getRuntime().exec("ps -aux");
            BufferedReader brs = new BufferedReader(new InputStreamReader(process.getInputStream()));
            brs.readLine();
            while(true){
                String line = brs.readLine();
                if(line==null){
                    break;
                }else {
                    String[] aa = line.split("\\s{1,}");
                    cpuUse = cpuUse + Double.valueOf(aa[2]);
                }
            }

        } catch (IOException e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return cpuUse;
    }

    public Double getMem() {
        Double cpuUse = 0.0;
        try {
            Process process = Runtime.getRuntime().exec("ps -aux");
            BufferedReader brs = new BufferedReader(new InputStreamReader(process.getInputStream()));
            brs.readLine();
            while(true){
                String line = brs.readLine();
                if(line==null){
                    break;
                }else {
                    String[] aa = line.split("\\s{1,}");
                    cpuUse = cpuUse + Double.valueOf(aa[5]);
                }
            }
        } catch (IOException e) {
            log.error("获得本地的linux方法命令:", e);
        }
        return cpuUse;
    }

    public Double getRX() {
        double drx = 0.0;
        String lines = getLinuxByCommand("ifconfig");
        String[] rxs = lines.split("RX bytes:");
        for(int i=1;i<rxs.length;i++) {
            String[] aa = rxs[i].split(" ");
            drx = drx + Double.valueOf(aa[0]);
        }
        drx = drx/1024/1024;
        return drx;
    }

    public Double getRXKB() {
        double drx = 0.0;
        String lines = getLinuxByCommand("ifconfig");
        String[] rxs = lines.split("RX bytes:");
        for(int i=1;i<rxs.length;i++) {
            String[] aa = rxs[i].split(" ");
            drx = drx + Double.valueOf(aa[0]);
        }
        drx = drx/1024;
        return drx;
    }

    public Double getTX() {
        double drx = 0.0;
        String lines = getLinuxByCommand("ifconfig");
        String[] rxs = lines.split("TX bytes:");
        for(int i=1;i<rxs.length;i++) {
            String[] aa = rxs[i].split(" ");
            drx = drx + Double.valueOf(aa[0]);
        }
        drx = drx/1024/1024;
        return drx;
    }

    public Double getTXKB() {
        double drx = 0.0;
        String lines = getLinuxByCommand("ifconfig");
        String[] rxs = lines.split("TX bytes:");
        for(int i=1;i<rxs.length;i++) {
            String[] aa = rxs[i].split(" ");
            drx = drx + Double.valueOf(aa[0]);
        }
        drx = drx/1024;
        return drx;
    }
}

