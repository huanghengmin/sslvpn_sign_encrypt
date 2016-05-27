package com.hzih.sslvpn.web.action.sslvpn.service;

import com.hzih.sslvpn.dao.EquipmentLogDao;
import com.hzih.sslvpn.dao.ServerDao;
import com.hzih.sslvpn.domain.EquipmentLog;
import com.hzih.sslvpn.domain.Server;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.IPPoolUtil;
import com.hzih.sslvpn.utils.ShellUtils;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.hardware.HardWareUtils;
import com.hzih.sslvpn.utils.md5.MD5Utils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-6
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class ServerStatusAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(ServerStatusAction.class);

    private ServerDao serverDao;

    private EquipmentLogDao equipmentLogDao;

    public ServerDao getServerDao() {
        return serverDao;
    }

    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public LogService getLogService() {
        return logService;
    }

    public EquipmentLogDao getEquipmentLogDao() {
        return equipmentLogDao;
    }

    public void setEquipmentLogDao(EquipmentLogDao equipmentLogDao) {
        this.equipmentLogDao = equipmentLogDao;
    }

    public String findConfig() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        Server server = serverDao.find();
        StringBuilder sb = new StringBuilder("{'success':true,'totalCount':1,'root':[");
        if (server != null) {
            sb.append("{");
            sb.append("protocol:'" + server.getProtocol() + "',");
            sb.append("port:'" + server.getPort() + "',");
            if (null != server.getListen() && server.getListen().equals("0.0.0.0"))
                sb.append("interface:'0.0.0.0'");
            else
                sb.append("interface:'" + server.getListen() + "'");
            sb.append("}");
        }
        sb.append("]}");
        actionBase.actionEnd(response, sb.toString(), result);
        return null;
    }

    public String openServer() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn start");
            Thread.sleep(1000 * 2);
            proc.exec("service openvpn status");
            String msg_on = proc.getOutput();
//            if (msg_on.contains("is running")) {
                if (msg_on.contains("running")) {
//            if (msg_on.contains("active")) {
                msg = "1";
                String dys_net = null;
                Server server = serverDao.find();
                String server_net = server.getServer_net();
                String server_mask = server.getServer_mask();
                if (server_mask != null) {
                    int mask = IPPoolUtil.getNetMask(server_mask);
                    dys_net = server_net + "/" + mask;
                }
                if (dys_net != null)
                    ShellUtils.firewall_start(dys_net, server.getProtocol(), String.valueOf(server.getPort()));
            } else {
                msg = "0";
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String closeServer() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn stop");
            Thread.sleep(1000 * 2);
            proc.exec("service openvpn status");
            String msg_on = proc.getOutput();
//            if (msg_on.contains("is running")) {
                if (msg_on.contains("running")) {
//            if (msg_on.contains("active")) {
                msg = "1";
            } else {
                msg = "0";
                String dys_net = null;
                Server server = serverDao.find();
                String server_net = server.getServer_net();
                String server_mask = server.getServer_mask();
                if (server_mask != null) {
                    int mask = IPPoolUtil.getNetMask(server_mask);
                    dys_net = server_net + "/" + mask;
                }
                if (dys_net != null)
                    ShellUtils.firewall_stop(dys_net, server.getProtocol(), String.valueOf(server.getPort()));
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String checkServerStatus() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn status");
            String msg_on = proc.getOutput();
//            if (msg_on.contains("is running")) {
            if (msg_on.contains("running")) {
//            if (msg_on.contains("active")) {
                msg = "1";
            } else {
                msg = "0";
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String checkDeviceStatus() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String soft_status = "正常";
        String config_status = "正常";
//        String pcie_status = "正常";
        /**
         * 加密卡
         */
        /*HardWareUtils hardWareUtils = new HardWareUtils();
        EquipmentLog pcie_log = hardWareUtils.checkPcie();
        if(pcie_log!=null){
            pcie_status = "PCIE卡被篡改";
            try {
                equipmentLogDao.add(pcie_log);
                String logInfo = pcie_log.getLog_info();
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), logInfo, "软硬件篡改", "ERROR", "013", "0", new Date());
                SyslogSender.sysLog(log);
            } catch (Exception e) {
                logger.error("保存PCIE卡更改日志出错:"+e.getMessage(),e);
            }
        }*/


        MD5Utils md5Utils = new MD5Utils();
        Boolean flag = md5Utils.checkMd5(StringContext.server_config_file_md5);
        if(!flag){
            config_status = "配置文件被篡改";
            String msg = "检测配置文件完整性出错,已被篡改";
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg,"软硬件篡改", "ERROR", "013", "0", new Date());
            SyslogSender.sysLog(log);
        }else {
            String msg = "检测配置文件完整性完成..............";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
        }


        String md5_value = md5Utils.getMd5ByFile(new File(StringContext.software_bin));
        String md5_bak_value = md5Utils.getMd5ByFile(new File(StringContext.software_bin_bak));
        if(md5_value.equals(md5_bak_value)){
            String msg = "检测重要软件完整性完成,软件未发生异常";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
        }else {
            soft_status = "软件被篡改";
            String msg = "检测软件完整性出错,已被篡改";
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "软硬件篡改","ERROR", "013", "0", new Date());
            SyslogSender.sysLog(log);
        }

//        String json = "{success:true,pcie_status:'" + pcie_status + "',soft_status:'" + soft_status + "',config_status:'" + config_status + "'}";
        String json = "{success:true,soft_status:'" + soft_status + "',config_status:'" + config_status + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /*public String reloadServer() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "0";
        try {
            Proc proc = new Proc();
            proc.exec("service openvpn restart");
            Thread.sleep(1000 * 2);
            try {
                proc.exec("service openvpn status");
                String msg_on = proc.getOutput();
                if (msg_on.contains("is running")) {
//               if (msg_on.contains("active")) {
                    msg = "1";
                    String dys_net = null;
                    Server server = serverDao.find();
                    String server_net = server.getServer_net();
                    String server_mask = server.getServer_mask();
                    if (server_mask != null) {
                        int mask = IPPoolUtil.getNetMask(server_mask);
                        dys_net = server_net + "/" + mask;
                    }
                    if (dys_net != null)
                        ShellUtils.firewall_start(dys_net, server.getProtocol(), String.valueOf(server.getPort()));
                } else {
                    msg = "0";
                }
            } catch (Exception e) {
                msg = "0";
            }
        } catch (Exception e) {
            msg = "0";
        }
        String json = "{success:true,msg:'" + msg + "'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }*/
}
