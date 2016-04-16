package com.hzih.sslvpn.web.thread;

import com.hzih.sslvpn.entity.NetInfo;
import com.hzih.sslvpn.entity.SysInfoBean;
import com.hzih.sslvpn.utils.GetListNetInfo;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by 钱晓盼 on 15-5-11.
 */
public class SystemStatusService extends Thread {
    final static Logger logger = Logger.getLogger(SystemStatusService.class);
    public static Map<String,String> cpuMap = new LinkedHashMap<String, String>();
    public static Map<String,String> memMap = new LinkedHashMap<String, String>();
    public static Map<String,String> netMap = new LinkedHashMap<String, String>();
    public static Map<String,String> diskMap = new LinkedHashMap<String, String>();
    private boolean isSend = false;

    private double txBytes = 0;
    private double rxBytes = 0;

    private Calendar calendar;

    @Override
    public void run() {
        //TODO check and send mail
        int x = 30;
        int t = 2;
        try{
            for (int i = 1; i <= x; i++){
                String key = "" + i;
                cpuMap.put(key,cpuMap.get(i + 1));
                calendar = Calendar.getInstance();
                String time = getTime();
                SysInfoBean bean = getSysInfoForLinux(t);
                String cpu = getCpu(time,bean);
                cpuMap.put(key,cpu);
                String mem = getMem(time,bean);
                memMap.put(key,mem);
                String net = getNet(time,bean);
                netMap.put(key,net);
                String disk = getDisk(time);
                diskMap.put(key,disk);
                if(i < x){
                    try {
                        Thread.sleep(1000 * 60 * t);
                    } catch (InterruptedException e) {e.printStackTrace();
                    }
                }
            }
        } catch (Exception e){
            logger.error("",e);
        } finally {
            try {
                Thread.sleep(1000 * 60 * t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true){
            try{
                for (int i = 1; i < x; i++){
                    String key = "" + i;
                    cpuMap.put(key,cpuMap.get(""+(i+1)));
                    memMap.put(key,memMap.get(""+(i+1)));
                    netMap.put(key,netMap.get(""+(i+1)));
                    diskMap.put(key,diskMap.get(""+(i+1)));
                }

                String key = "" + x;
                calendar = Calendar.getInstance();
                String time = getTime();
                SysInfoBean bean = getSysInfoForLinux(t);
                String cpu = getCpu(time,bean);
                cpuMap.put(key,cpu);
                String mem = getMem(time,bean);
                memMap.put(key,mem);
                String net = getNet(time,bean);
                netMap.put(key,net);
                String disk = getDisk(time);
                diskMap.put(key,disk);
            } catch (Exception e){
                logger.error("",e);
            } finally {
                try {
                    Thread.sleep(1000 * 60 * t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getTime() {

        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        if(m < 10){
            return h + ":0" + m;
        } else {
            return  h + ":" + m;
        }
    }

    private String getDisk(String time) {
        double root = getDataBaseAtDisk("");
        double var = getDataBaseAtDisk("/var");
        double data = getDataBaseAtDisk("/data/mysql");
        return "{name:'"+time+"',num1:"+root+",num2:"+var+",num3:"+data+"},";
    }

    private String getNet(String time, SysInfoBean bean) {
        double inAll = change(bean.getInAll() / (1024 * 1024) );
        double outAll = change(bean.getOutAll() / (1024 * 1024) );
        double inFlux = change(bean.getInFlux() / (1024 * 1024) );
        double outFlux = change(bean.getOutFlux() / (1024 * 1024) );
        double inAverage = change(bean.getInAverage() / (1024 * 1024) );
        double outAverage = change(bean.getOutAverage() / (1024 * 1024) );
        return "{name:'"+time+"',num1:"+inAll+",num2:"+outAll+
                ",num3:"+inFlux+",num4:"+outFlux+
                ",num5:"+inAverage+",num6:"+outAverage+"},";
    }

    private double getEthByte(String command) {
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            Proc proc = new Proc();
            proc.exec(command);
            String result = proc.getOutput();
            StringTokenizer tokenizer = new StringTokenizer(result, "\n");
            while (tokenizer.hasMoreTokens()) {
                String line = tokenizer.nextToken().trim();
                return Double.parseDouble(line);
            }
        }
        return 0;
    }

    private String getMem(String time, SysInfoBean bean) {
        /*
        free -m
                     total       used       free     shared    buffers     cached
        Mem:          3832       1642       2190          0         91        801
        -/+ buffers/cache:        749       3083
        Swap:          207          0        207
         */
        double num1 = change(bean.getMemTotal() / 1024);
        double num2 = change(bean.getMemUse() / 1024);
        double num3 = change(bean.getMemFree() / 1024);
        double num4 = change(bean.getMenBuffers() / 1024);
        double num5 = change(bean.getMemCanBeUse() / 1024);
        return "{name:'"+ time +"',num1:"+num1+",num2:"+num2+
                ",num3:"+num3+",num4:"+num4+
                ",num5:"+num5+"},";
    }

    /**
     * 四舍五入 double保留小数点后2位
     * @param l
     * @return
     */
    private double change(double l ){
        return Double.parseDouble(new java.text.DecimalFormat("#.00").format(l));
    }

    private String getCpu(String time, SysInfoBean bean) {
        /*
        top -b -n 1
        top - 11:03:09 up 20 days, 17:54,  1 user,  load average: 1.63, 1.71, 1.67
        Tasks:  82 total,   2 running,  80 sleeping,   0 stopped,   0 zombie
        %Cpu(s):  0.9 us,  2.1 sy,  0.0 ni, 97.0 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
        KiB Mem:   3924976 total,  1684376 used,  2240600 free,    93292 buffers
        KiB Swap:   212988 total,        0 used,   212988 free,   820676 cached
         */
        double num1 = bean.getCpuUserUse();
        double num2 = bean.getCpuSysUse();
        double num3 = bean.getCpuNiceUse();
        double num4 = bean.getCpuIdleUse();
        double num5 = bean.getCpuIoUse();
        double num6 = bean.getCpuHiUse();
        double num7 = bean.getCpuSiUse();
        return "{name:'"+ time +
                "',num1:"+num1+",num2:"+num2+
                ",num3:"+num3+",num4:"+num4+
                ",num5:"+num5+",num6:"+num6+
                ",num7:"+num7+"},";
    }


    public SysInfoBean getSysInfoForLinux(int interval){
        SysInfoBean bean = new SysInfoBean();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        String line = null;
        try{
            if(OSInfo.getOSInfo().isLinux()){
                Process process = Runtime.getRuntime().exec("top -b -n 1");
                is = process.getInputStream();
            } else {
                is = new FileInputStream("D:/test/cpu.txt");
            }
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);


            line = brStat.readLine();
            tokenStat = new StringTokenizer(line);
            logger.info(line);       //top - 11:53:13 up 1 day,  2:02,  1 user,  load average: 0.25, 0.30, 0.16
            if (line.indexOf("day")>-1) {
                tokenStat.nextToken();
                tokenStat.nextToken();
                String sysTime = tokenStat.nextToken();
                tokenStat.nextToken();
                String sysUpDays = tokenStat.nextToken();
                tokenStat.nextToken();
                String sysUdpHM = tokenStat.nextToken();
                String users = tokenStat.nextToken();
                try{
                    int u = Integer.parseInt(users);
                    bean.setSysUpTime("在线" + sysUpDays + "天" + sysUdpHM.split(":")[0] + "小时" + sysUdpHM.split(":")[1].split(",")[0] + "分钟");
                } catch (Exception e){
                    e.printStackTrace();
                    if(users.indexOf("m")>-1){
                        bean.setSysUpTime("在线" + sysUpDays + "天" + sysUdpHM + "分钟");
                    } else {
                        bean.setSysUpTime("在线" + sysUpDays + "天" + sysUdpHM + "秒");
                    }
                    users = tokenStat.nextToken();
                }
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                String loadAverage_1 = tokenStat.nextToken();
                String loadAverage_5 = tokenStat.nextToken();
                String loadAverage_15 = tokenStat.nextToken();
                bean.setSysTime(sysTime);

                bean.setUsers(Integer.parseInt(users));
                bean.setLoadAverage_1(Double.parseDouble(loadAverage_1.split(",")[0]));
                bean.setLoadAverage_5(Double.parseDouble(loadAverage_5.split(",")[0]));
                bean.setLoadAverage_15(Double.parseDouble(loadAverage_15));
            } else {
                tokenStat.nextToken();
                tokenStat.nextToken();
                String sysTime = tokenStat.nextToken();
//                tokenStat.nextToken();
                String sysUpDays = "0";
                tokenStat.nextToken();
                String sysUdpHM = tokenStat.nextToken();
                String users = tokenStat.nextToken();
                try{
                    int u = Integer.parseInt(users);
                    bean.setSysUpTime("在线" + sysUpDays + "天" + sysUdpHM.split(":")[0] + "小时" + sysUdpHM.split(":")[1].split(",")[0] + "分钟");
                } catch (Exception e){
                    e.printStackTrace();
                    if(users.indexOf("m")>-1){
                        bean.setSysUpTime("在线" + sysUpDays + "天" + sysUdpHM + "分钟");
                    } else {
                        bean.setSysUpTime("在线" + sysUpDays + "天" + sysUdpHM + "秒");
                    }
                    users = tokenStat.nextToken();
                }
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                String loadAverage_1 = tokenStat.nextToken();
                String loadAverage_5 = tokenStat.nextToken();
                String loadAverage_15 = tokenStat.nextToken();
                bean.setSysTime(sysTime);

                bean.setUsers(Integer.parseInt(users));
                bean.setLoadAverage_1(Double.parseDouble(loadAverage_1.split(",")[0]));
                bean.setLoadAverage_5(Double.parseDouble(loadAverage_5.split(",")[0]));
                bean.setLoadAverage_15(Double.parseDouble(loadAverage_15));
            }



            line = brStat.readLine();
            tokenStat = new StringTokenizer(line);
            logger.info(line);
            tokenStat.nextToken();
            String taskTotal = tokenStat.nextToken();
            tokenStat.nextToken();
            String taskRunning = tokenStat.nextToken();
            tokenStat.nextToken();
            String taskSleeping = tokenStat.nextToken();
            tokenStat.nextToken();
            String taskStopped = tokenStat.nextToken();
            tokenStat.nextToken();
            String taskZombie = tokenStat.nextToken();
            tokenStat.nextToken();
            bean.setTaskTotal(Integer.parseInt(taskTotal));
            bean.setTaskRunning(Integer.parseInt(taskRunning));
            bean.setTaskSleeping(Integer.parseInt(taskSleeping));
            bean.setTaskStopped(Integer.parseInt(taskStopped));
            bean.setTaskZombie(Integer.parseInt(taskZombie));


            line = brStat.readLine();
            tokenStat = new StringTokenizer(line);
            logger.info(line);
            tokenStat.nextToken();
            String cpuUserUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuSysUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuNiceUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuIdleUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuIoUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuHiUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuSiUse = tokenStat.nextToken();
            tokenStat.nextToken();


            bean.setCpuUserUse(Double.parseDouble(cpuUserUse));
            bean.setCpuSysUse(Double.parseDouble(cpuSysUse));
            bean.setCpuNiceUse(Double.parseDouble(cpuNiceUse));
            bean.setCpuIdleUse(Double.parseDouble(cpuIdleUse));
            bean.setCpuIoUse(Double.parseDouble(cpuIoUse));
            bean.setCpuHiUse(Double.parseDouble(cpuHiUse));
            bean.setCpuSiUse(Double.parseDouble(cpuSiUse));

            line = brStat.readLine();
            tokenStat = new StringTokenizer(line);
            logger.info(line);

            tokenStat.nextToken();
            tokenStat.nextToken();
            String memTotal = tokenStat.nextToken();
            tokenStat.nextToken();
            String memUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String memFree = tokenStat.nextToken();
            tokenStat.nextToken();
            String memBuffers = tokenStat.nextToken();
            tokenStat.nextToken();

            bean.setMemTotal(Double.parseDouble(memTotal));
            bean.setMemUse(Double.parseDouble(memUse));
            bean.setMemFree(Double.parseDouble(memFree));
            bean.setMenBuffers(Double.parseDouble(memBuffers));

            line = brStat.readLine();
            tokenStat = new StringTokenizer(line);
            logger.info(line);
            tokenStat.nextToken();
            tokenStat.nextToken();
            String swapTotal = tokenStat.nextToken();
            tokenStat.nextToken();
            String swapUse = tokenStat.nextToken();
            tokenStat.nextToken();
            String swapFree = tokenStat.nextToken();
            tokenStat.nextToken();
            String swapCached = tokenStat.nextToken();
            tokenStat.nextToken();

            bean.setSwapTotal(Double.parseDouble(swapTotal));
            bean.setSwapUsed(Double.parseDouble(swapUse));
            bean.setSwapFree(Double.parseDouble(swapFree));
            bean.setSwapCached(Double.parseDouble(swapCached));

            bean.setMemCanBeUse(Double.parseDouble(memFree) + Double.parseDouble(memBuffers) + Double.parseDouble(swapCached));

        } catch(IOException ioe){
            logger.error("" + line, ioe);
//            freeResource(is, isr, brStat);
        } finally{
            freeResource(is, isr, brStat);
        }

        try {
            List<NetInfo> list = new GetListNetInfo().readInterfaces();
            double inAll = 0.0;
            double outAll = 0.0;
            double inFlux = 0.0;
            double outFlux = 0.0;
            double inAverage = 0.0;
            double outAverage = 0.0;
            for (NetInfo netInfo : list) {
                String eth = netInfo.getInterfaceName();
                if(eth.indexOf(":")>-1){
                    continue;
                }
                String txBytesCommand = "cat /sys/class/net/"+eth+"/statistics/tx_bytes";
                double txBytesNow = getEthByte(txBytesCommand);
                String rxBytesCommand = "cat /sys/class/net/"+eth+"/statistics/rx_bytes";
                double rxBytesNow = getEthByte(rxBytesCommand);
                outAll += txBytesNow;
                inAll += rxBytesNow;
                outFlux = (outAll - txBytes);
                inFlux = (inAll - rxBytes);
                txBytes = outAll;
                rxBytes = inAll;
            }

            bean.setInAll(inAll);
            bean.setOutAll(outAll);
            bean.setInFlux(inFlux);
            bean.setOutFlux(outFlux);
            bean.setInAverage(inFlux / (interval * 60));
            bean.setOutAverage(outFlux / (interval * 60));
        } catch (Exception e) {
            logger.error("",e);
        }

        return bean;

    }

    private static void freeResource(InputStream is, InputStreamReader isr, BufferedReader br){
        try{
            if(is!=null)
                is.close();
            if(isr!=null)
                isr.close();
            if(br!=null)
                br.close();
        }catch(IOException ioe){
            logger.error("", ioe);
        }
    }

    private int getDataBaseAtDisk(String dataDir) {

        int used = 100;
        Proc proc;
        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isWin()) {
        }
        if (osinfo.isLinux()) {
            proc = new Proc();
            proc.exec("df "+dataDir);
            String result = proc.getOutput();
            String[] lines = result.split("\n");
            String[] str = lines[1].split("\\s");
            for (int i = 0;i<str.length;i++){
                if(str[i].endsWith("%") ){
                    used = Integer.parseInt(str[i].split("%")[0]);
                }
            }
        }
        return used;
    }
}
