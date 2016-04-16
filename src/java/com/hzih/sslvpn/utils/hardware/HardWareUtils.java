package com.hzih.sslvpn.utils.hardware;

import com.hzih.sslvpn.domain.EquipmentLog;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 15-8-5.
 */
public class HardWareUtils {
    private Logger logger  = Logger.getLogger(HardWareUtils.class);

    private static final String disk_info = StringContext.systemPath+"/hardware/disk.info";

    private static final String memory_info = StringContext.systemPath+"/hardware/memory.info";

    private static final String pcie_info = StringContext.systemPath+"/hardware/pcie.info";

    public void read_hardware() {
        Proc proc = new Proc();
        String command = null;
        if (OSInfo.getOSInfo().isWin()) {
            command = StringContext.systemPath + "/script/hardware_read.bat ";
        } else {
            command = StringContext.systemPath + "/script/hardware_read.sh ";
        }
        proc.exec(command);
    }

    public EquipmentLog checkDisk()  {
        File disk_file = new File(disk_info);
        if(!disk_file.exists()){
            read_hardware();
        }else {
            Runtime rt = Runtime.getRuntime();
            Process p = null;
            String[] cmd = {"sh", "-c", "fdisk -l|grep Disk|awk -F, '{print $1}'"};
            try {
                p = rt.exec(cmd);
            } catch (IOException e) {
                return null;
            }
            if (p != null) {
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String str = null;
                String str_l1 = null;
                String str_l2 = null;
                int line = 0;
                try {
                    while ((str = in.readLine()) != null) {
                        line++;
                        if (line == 1) {
                            str_l1 = str;
                        }
                        if (line == 2) {
                            str_l2 = str;
                        }
                    }
                } catch (IOException e) {
                    return null;
                }
                ArrayList<String> strings = null;
                try {
                    strings = FileUtil.readFile(disk_info);
                } catch (Exception e) {
                    return null;
                }
                if(strings!=null&&strings.size()==2){
                    if(!(strings.get(0).equals(str_l1)&&strings.get(1).equals(str_l2))){
                        EquipmentLog equipmentLog = new EquipmentLog();
                        equipmentLog.setLog_time(new Date());
                        equipmentLog.setEquipment_name("sslvpn");
                        equipmentLog.setLevel("WARN");
                        equipmentLog.setLog_info("硬盘更改:原硬盘:" + strings.get(0)+",原硬盘序列号:"+strings.get(1)+",更改为:硬盘:"+str_l1+",序列号:"+str_l2);
                        return equipmentLog;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public EquipmentLog checkMem() {
        File disk_file = new File(memory_info);
        if(!disk_file.exists()){
            read_hardware();
        }else {
            Runtime rt = Runtime.getRuntime();
            Process p = null;
            String[] cmd = {"sh", "-c", "cat /proc/meminfo | grep MemTotal |awk '{print $2$3}'"};
            try {
                p = rt.exec(cmd);
            } catch (IOException e) {
                return null;
            }
            if (p != null) {
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String str = null;
                String str_l1 = null;
                int line = 0;
                try {
                    while ((str = in.readLine()) != null) {
                        line++;
                        if (line == 1) {
                            str_l1 = str;
                        }
                    }
                } catch (IOException e) {
                    return null;
                }
                ArrayList<String> strings = null;
                try {
                    strings = FileUtil.readFile(memory_info);
                } catch (Exception e) {
                    return null;
                }
                if(strings!=null&&strings.size()==1){
                    if(!strings.get(0).equals(str_l1)){
                        EquipmentLog equipmentLog = new EquipmentLog();
                        equipmentLog.setLog_time(new Date());
                        equipmentLog.setEquipment_name("sslvpn");
                        equipmentLog.setLevel("WARN");
                        equipmentLog.setLog_info("内存更改:原内存容量:" + strings.get(0)+",更改为:现有容量:"+str_l1);
                        return equipmentLog;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    /*public EquipmentLog checkPcie() {
        File disk_file = new File(pcie_info);
        if(!disk_file.exists()){
            read_hardware();
        }else {
            Runtime rt = Runtime.getRuntime();
            Process p = null;
            String cmd = StringContext.script_path+"/pcie_serial";
            try {
                p = rt.exec(cmd);
            } catch (IOException e) {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setFlag(false);
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("PCIE卡更改:未找到现有PCIE卡！");
                return equipmentLog;
            }
            if (p != null) {
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String str = null;
                String str_l1 = null;
                int line = 0;
                try {
                    while ((str = in.readLine()) != null) {
                        line++;
                        if (line == 1) {
                            str_l1 = str;
                        }
                    }
                } catch (IOException e) {
                    EquipmentLog equipmentLog = new EquipmentLog();
                    equipmentLog.setLog_time(new Date());
                    equipmentLog.setEquipment_name("sslvpn");
                    equipmentLog.setFlag(false);
                    equipmentLog.setLevel("WARN");
                    equipmentLog.setLog_info("PCIE卡更改:未找到现有PCIE卡！");
                    return equipmentLog;
                }
                logger.info("读取PCIE卡信息:"+str_l1);
                if(str_l1.equals("Open Device Error.")) {
                    EquipmentLog equipmentLog = new EquipmentLog();
                    equipmentLog.setLog_time(new Date());
                    equipmentLog.setEquipment_name("sslvpn");
                    equipmentLog.setFlag(false);
                    equipmentLog.setLevel("WARN");
                    equipmentLog.setLog_info("PCIE卡更改:打开PCIE卡失败！");
                    return equipmentLog;
                }
                if(str_l1.equals("Get Device Info Fail.")){
                    EquipmentLog equipmentLog = new EquipmentLog();
                    equipmentLog.setLog_time(new Date());
                    equipmentLog.setEquipment_name("sslvpn");
                    equipmentLog.setFlag(false);
                    equipmentLog.setLevel("WARN");
                    equipmentLog.setLog_info("PCIE卡更改:获取PCIE卡信息失败！");
                    return equipmentLog;
                }
                ArrayList<String> strings = null;
                try {
                    strings = FileUtil.readFile(pcie_info);
                } catch (Exception e) {
                    return null;
                }
                if(strings!=null&&strings.size()==1){
                    if(!strings.get(0).equals(str_l1)){
                        EquipmentLog equipmentLog = new EquipmentLog();
                        equipmentLog.setLog_time(new Date());
                        equipmentLog.setFlag(false);
                        equipmentLog.setEquipment_name("sslvpn");
                        equipmentLog.setLevel("WARN");
                        equipmentLog.setLog_info("PCIE卡更改:原序列号:" + strings.get(0)+",更改为:现序列号:"+str_l1);
                        return equipmentLog;
                    }
                }
                return null;
            }else {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setFlag(false);
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("PCIE卡更改:未找到现有PCIE卡！");
                return equipmentLog;
            }
        }
        return null;
    }*/


}
