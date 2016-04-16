package com.hzih.sslvpn.web.action.alert;

import com.hzih.sslvpn.constant.AppConstant;
import com.hzih.sslvpn.dao.EquipmentLogDao;
import com.hzih.sslvpn.domain.EquipmentLog;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.SiteContext;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.device.CpuUsage;
import com.hzih.sslvpn.web.action.device.MemUsage;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 15-5-11.
 */
public class SystemAlertAction extends ActionSupport {
    private Logger logger = Logger.getLogger(SystemAlertAction.class);
    private LogService logService;
    private EquipmentLogDao equipmentLogDao;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public EquipmentLogDao getEquipmentLogDao() {
        return equipmentLogDao;
    }

    public void setEquipmentLogDao(EquipmentLogDao equipmentLogDao) {
        this.equipmentLogDao = equipmentLogDao;
    }

    public String saveConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result = base.actionBegin(request);
        String msg = null;
        SAXReader reader = new SAXReader();
        String fileName = SiteContext.getInstance().contextRealPath + AppConstant.XML_ALERT_DEVICE_PATH;
        Document document = reader.read(new File(fileName));
        request.getCharacterEncoding();
        response.setCharacterEncoding("UTF-8");
        Element root = document.getRootElement();
        XMLWriter writer = null;
        try {
            Node cpuNode = document.selectSingleNode("//config/cpu");
            Node memNode = document.selectSingleNode("//config/mem");
            Node storageNode = document.selectSingleNode("//config/storage");
            cpuNode.setText(request.getParameter("cpu"));
            memNode.setText(request.getParameter("mem"));
            storageNode.setText(request.getParameter("storage"));
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer = new XMLWriter(new FileOutputStream(fileName), format);
            writer.write(document);
            writer.close();

            //CPU
            double cpuUse = CpuUsage.getCpuUsage();
            //内存
            double mem = MemUsage.getMemUsage();
            //Disk
            int use = getAtDiskUse();

            if (cpuUse >= Double.parseDouble(request.getParameter("cpu"))) {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("CPU使用超过阀值,CPU当前使用:" + cpuUse);
                try {
                    equipmentLogDao.add(equipmentLog);
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("CPU使用超过阀值,CPU当前使用:" + cpuUse);
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage(),e);
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.error("保存设备日志出错:" + e.getMessage());
                    }
                }

            } else if (mem >= Double.parseDouble(request.getParameter("mem"))) {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("内存使用超过阀值,内存当前使用:" + mem);
                try {
                    equipmentLogDao.add(equipmentLog);
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("内存使用超过阀值,内存当前使用:" + mem);
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage(),e);
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.error("保存设备日志出错:" + e.getMessage());
                    }
                }
            } else if (use >= Integer.parseInt(request.getParameter("storage"))) {
                EquipmentLog equipmentLog = new EquipmentLog();
                equipmentLog.setLog_time(new Date());
                equipmentLog.setEquipment_name("sslvpn");
                equipmentLog.setLevel("WARN");
                equipmentLog.setLog_info("磁盘使用超过阀值,磁盘当前使用:" + use);
                try {
                    equipmentLogDao.add(equipmentLog);
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("磁盘使用超过阀值,磁盘当前使用:" + use);
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage(),e);
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.error("保存设备日志出错:" + e.getMessage());
                    }
                }
            }
            msg = "用户修改设备报警配置信息成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e){
            logger.info(e.getMessage(),e);
            msg = "用户修改设备报警配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("报警配置"+e.getMessage());
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "报警配置", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        String json = "{success:true,msg:'" + msg + "'}";
        base.actionEnd(response, json, result);
        return null;
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

    public String loadConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase base = new ActionBase();
        String result = base.actionBegin(request);
        String json = null;
        try {
            SAXReader reader = new SAXReader();
            String fileName = SiteContext.getInstance().contextRealPath + AppConstant.XML_ALERT_DEVICE_PATH;
            Document document = reader.read(new File(fileName));
            Map<Object, Object> model = new HashMap<Object, Object>();
            Node tempNode = document.selectSingleNode("//config/cpu");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/mem");
            model.put(tempNode.getName(), tempNode.getText());
            tempNode = document.selectSingleNode("//config/storage");
            model.put(tempNode.getName(), tempNode.getText());
            String modelStr = model.toString();
            json = "{totalCount:1,root:[{success:true,";
            String[] fields = modelStr.split("\\{")[1].split("\\}")[0].split(",");
            int index = 0;
            for (int i = 0; i < fields.length; i++) {
                if (index == fields.length - 1) {
                    json += "'" + fields[i].split("=")[0].trim() + "':'" + fields[i].split("=")[1].trim() + "'";
                } else {
                    json += "'" + fields[i].split("=")[0].trim() + "':'" + fields[i].split("=")[1].trim() + "',";
                }
                index++;
            }
            json += "}]}";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户获取设备报警配置信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("报警配置"+e.getMessage());
                logService.newLog("ERROE", SessionUtils.getAccount(request).getUserName(), "报警配置", "用户获取设备报警配置信息失败");
            }
        }
        base.actionEnd(response, json, result);
        return null;
    }

}
