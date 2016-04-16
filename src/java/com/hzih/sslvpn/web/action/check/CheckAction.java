package com.hzih.sslvpn.web.action.check;

import com.hzih.sslvpn.domain.EquipmentLog;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.hardware.HardWareUtils;
import com.hzih.sslvpn.utils.md5.MD5Utils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 15-8-6.
 */
public class CheckAction extends ActionSupport {
    private Logger logger = Logger.getLogger(CheckAction.class);
    private LogService logService ;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public String check()throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        String json = null;
        logger.info("...........................................开始检测系统配置文件...........................................");
        MD5Utils md5Utils = new MD5Utils();
        Boolean flag = md5Utils.checkMd5(StringContext.server_config_file_md5);
        if(!flag){
            try {
                FileUtil.copy(new File(StringContext.server_config_file_bak),StringContext.server_config_file);
                msg = "检测配置文件完整性出错,已被篡改,执行自动恢复";
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg,"故障容错", "ERROR", "014", "0", new Date());
                SyslogSender.sysLog(log);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "故障容错", msg);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            } catch (IOException e) {
                msg = "检测配置文件完整性出错,已被篡改,执行自动恢复出错";
                logger.error(msg, e);
            }
        }else {
            msg = "检测配置文件完整性完成..............";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
        }
        logger.info("...........................................结束检测系统配置文件...........................................");

        Thread.sleep(500);

        logger.info("...........................................开始检测硬件信息更改...........................................");
        HardWareUtils hardWareUtils = new HardWareUtils();
        EquipmentLog equipmentLog = hardWareUtils.checkDisk();
        if(equipmentLog!=null){
            msg = equipmentLog.getLog_info();
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg,"软硬件篡改", "ERROR", "013", "0", new Date());
            SyslogSender.sysLog(log);
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "软硬件篡改", msg);
        }
        EquipmentLog equipmentLog_m = hardWareUtils.checkMem();
        if(equipmentLog_m!=null){
            msg = equipmentLog_m.getLog_info();
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg,"软硬件篡改", "ERROR", "013", "0", new Date());
            SyslogSender.sysLog(log);
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "软硬件篡改", msg);
        }

       /* EquipmentLog pcie_log = hardWareUtils.checkPcie();
        if(pcie_log!=null){
            msg = pcie_log.getLog_info();
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg,"软硬件篡改", "ERROR", "013", "0", new Date());
            SyslogSender.sysLog(log);
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "软硬件篡改", msg);
        }*/

        if(equipmentLog==null&&equipmentLog_m==null/*&&pcie_log==null*/){
            msg = "检测硬件信息完成........,硬件未发生异常";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
        }
        logger.info("...........................................结束检测硬件信息更改...........................................");

        Thread.sleep(500);

        logger.info("...........................................开始检测软件信息更改...........................................");
        String md5_value = md5Utils.getMd5ByFile(new File(StringContext.software_bin));
        String md5_bak_value = md5Utils.getMd5ByFile(new File(StringContext.software_bin_bak));
        if(md5_value.equals(md5_bak_value)){
            msg = "检测重要软件完整性完成,软件未发生异常";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
        }else {
            try {
                FileUtil.copy(new File(StringContext.software_bin_bak),StringContext.software_bin);
                msg = "检测软件完整性出错,已被篡改,执行自动恢复";
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "故障容错","ERROR", "014", "0", new Date());
                SyslogSender.sysLog(log);
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "故障容错", msg);
            } catch (IOException e) {
                msg = "检测软件完整性出错,已被篡改,执行自动恢复出错";
                logger.error(msg, e);
            }
        }
        logger.info("...........................................结束检测软件信息更改...........................................");
       /* if(pcie_log!=null&&!pcie_log.isFlag()) {
            json = "{success:false,flag:false,msg:'" + pcie_log.getLog_info() + "'}";
        }else {*/
            msg = "系统检测完成....";
            json = "{success:true,flag:true,msg:'" + msg + "'}";
//        }
        logger.info("返回信息:"+json);
        actionBase.actionEnd(response, json, result);
        return null;
    }
}
