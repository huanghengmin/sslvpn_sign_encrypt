package com.hzih.sslvpn.web.servlet;

import com.hzih.sslvpn.constant.AppConstant;
import com.hzih.sslvpn.dao.EquipmentLogDao;
import com.hzih.sslvpn.domain.EquipmentLog;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.utils.hardware.HardWareUtils;
import com.hzih.sslvpn.web.SiteContext;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.device.CpuUsage;
import com.hzih.sslvpn.web.action.device.MemUsage;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Date;
import java.util.TimerTask;

public class EquipmentLogTask extends TimerTask {

    private Logger logger = Logger.getLogger(EquipmentLogTask.class);

    private EquipmentLogDao equipmentLogDao;

    public EquipmentLogTask(EquipmentLogDao equipmentLogDao) {
        this.equipmentLogDao = equipmentLogDao;
    }

    private int getAtDiskUse() {
        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isLinux()) {
            Proc proc = new Proc();
            proc.exec("df  /");
            String result = proc.getOutput();
            String[] lines = result.split("\n");
            String[] str = lines[1].split("\\s");
            for (int i = 0; i < str.length; i++) {
                if (str[i].endsWith("%")) {
                    return Integer.parseInt(str[i].split("%")[0]);
                }
            }
        }
        return 0;
    }

    @Override
    public void run() {
        logger.info("*********************启动设备检查服务********************************");
        //CPU
        double cpuUse = 0;
        try {
            cpuUse = CpuUsage.getCpuUsage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //内存
        double mem = 0;
        try {
            mem = MemUsage.getMemUsage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Disk
        int use = getAtDiskUse();

        SAXReader reader = new SAXReader();
        String fileName = SiteContext.getInstance().contextRealPath + AppConstant.XML_ALERT_DEVICE_PATH;
        Document document = null;
        try {
            document = reader.read(new File(fileName));
        } catch (DocumentException e) {
            logger.error(e.getMessage(),e);
        }
        if(document!=null) {
            Node tempNode = document.selectSingleNode("//config/cpu");
            int cpuConfigUse = Integer.parseInt(tempNode.getText());
            tempNode = document.selectSingleNode("//config/mem");
            int memConfigUse = Integer.parseInt(tempNode.getText());
            tempNode = document.selectSingleNode("//config/storage");
            int storageConfigUse = Integer.parseInt(tempNode.getText());
            if (cpuUse >= cpuConfigUse) {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("CPU使用超过阀值,CPU当前使用:" + cpuUse);
                try {
                    equipmentLogDao.add(equipmentLog);
                } catch (Exception e) {
                    logger.error("保存设备日志出错:"+e.getMessage(),e);
                }

            } else if (mem >= memConfigUse) {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("内存使用超过阀值,内存当前使用:" + mem);
                try {
                    equipmentLogDao.add(equipmentLog);
                } catch (Exception e) {
                    logger.error("保存设备日志出错:"+e.getMessage(),e);
                }
            } else if (use >= storageConfigUse) {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("磁盘使用超过阀值,磁盘当前使用:" + use);
                try {
                    equipmentLogDao.add(equipmentLog);
                } catch (Exception e) {
                    logger.error("保存设备日志出错:"+e.getMessage(),e);
                }
            }
        }

        //硬盘
        HardWareUtils hardWareUtils = new HardWareUtils();
        EquipmentLog disk_log = hardWareUtils.checkDisk();
        if(disk_log!=null){
            try {
                String logInfo = disk_log.getLog_info();
                String log = AccountLogUtils.getResult("boot", logInfo, "软硬件篡改", "ERROR", "013", "0", new Date());
                SyslogSender.sysLog(log);
                equipmentLogDao.add(disk_log);
            } catch (Exception e) {
                logger.error("保存硬盘更改日志出错:" + e.getMessage(), e);
            }
        }

        //内存
        EquipmentLog mem_log = hardWareUtils.checkMem();
        if(mem_log!=null){
            try {
                String logInfo = mem_log.getLog_info();
                String log = AccountLogUtils.getResult("boot",  logInfo,"软硬件篡改", "ERROR", "013", "0", new Date());
                SyslogSender.sysLog(log);
                equipmentLogDao.add(mem_log);
            } catch (Exception e) {
                logger.error("保存内存更改日志出错:"+e.getMessage(),e);
            }
        }
        /**
         * 加密卡
         */
       /* EquipmentLog pcie_log = hardWareUtils.checkPcie();
        if(pcie_log!=null){
            try {
                String logInfo = pcie_log.getLog_info();
                String log = AccountLogUtils.getResult("boot",  logInfo,"软硬件篡改", "ERROR", "013", "0", new Date());
                SyslogSender.sysLog(log);
                equipmentLogDao.add(pcie_log);
            } catch (Exception e) {
                logger.error("保存PCIE卡更改日志出错:"+e.getMessage(),e);
            }
        }*/

        /**
         * 备份log
         */
       /* String format = DateUtils.format(new Date(),"yyyy-MM-dd");
        BackUpLog.bak(StringContext.systemPath,format+"_log");*/
    }
}