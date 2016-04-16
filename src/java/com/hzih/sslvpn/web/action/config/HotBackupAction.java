package com.hzih.sslvpn.web.action.config;

import com.hzih.sslvpn.entity.HotBackUp;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.hzih.sslvpn.web.action.boot.LoaderListener;
import com.hzih.sslvpn.web.servlet.SiteContextLoaderServlet;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-5-12
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class HotBackupAction extends ActionSupport {

    private static final Logger logger = LoggerFactory.getLogger(HotBackupAction.class);
    private LogService logService;
    private HotBackUp hotBackUp;
    public String query() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        try{
            HotBackUp bUp = HotBackUp.readBackUp();
            json = "{success:true,total:1,rows:[{isActive:"+bUp.isActive()+
                    ",isMainSystem:"+bUp.isMainSystem()+",mainIp:'"+bUp.getMainIp()+
                    "',mainPort:"+bUp.getMainPort()+",mainStatus:" + bUp.getMainStatus() +",backupIp:'"+bUp.getBackupIp()+
                    "',backupPort:"+bUp.getBackupPort()+",backupStatus:"+bUp.getBackupStatus()+"}]}";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("info", SessionUtils.getAccount(request).getUserName(), "双击热备", "用户查找双机热备配置信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户查找双机热备配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
            }
            json = "{success:true,total:0,rows:[]}";
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    public String update() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String active = request.getParameter("active");
            hotBackUp.setActive("on".equals(active));
            HotBackUp.updateBase(hotBackUp);
            msg = "用户更新双机热备配置信息成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("info", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户更新双机热备配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public String queryList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        try{
            String type = request.getParameter("type");
            int start = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("limit"));
            HotBackUp bUp = HotBackUp.readBackUp();
            List<String> list = null;
            if("ping".equals(type)) {
                list = bUp.getPings();
            }  else if("telnet".equals(type)) {
                list = bUp.getTelnets();
            }  else if ("other".equals(type)) {
                list = bUp.getOthers();
            }
            json = listToJson(list, start, limit);
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("info", SessionUtils.getAccount(request).getUserName(),"双击热备","用户查找双机热备配置信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户查找双机热备配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
            }
            json = "{success:true,total:0,rows:[]}";
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    private String listToJson(List<String> list, int start, int limit) {
        String json = "{success:true,total:"+list.size()+",rows:[";
        int index = 0;
        int count = 0;
        for(String ip:list) {
            if(index==start && count<=limit) {
                json += "{ip:'"+ip+"',flag:2},";
                start ++;
                count ++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    public String updateList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String type = request.getParameter("type");
            String old = request.getParameter("old");
            String l = request.getParameter("l");
            List<String> list = new ArrayList<String>();
            list.add(old);
            list.add(l);
            HotBackUp.delete(list,type);
            List<String> list2 = new ArrayList<String>();
            list2.add(l);
            HotBackUp.insert(list2,type);
            msg = "用户更新双机热备配置信息成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("info", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户更新双机热备配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public String insertList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String type = request.getParameter("type");
            String[] array = request.getParameterValues("array");
            List<String> list = new ArrayList<String>();
            for (int i=0;i<array.length;i++) {
                list.add(array[i]);
            }
            HotBackUp.delete(list,type);
            HotBackUp.insert(list,type);
            msg = "用户新增双机热备配置信息成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("info", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户新增双机热备配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public String deleteList() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = null;
        try{
            String type = request.getParameter("type");
            String[] array = request.getParameterValues("array");
            List<String> list = new ArrayList<String>();
            for (int i=0;i<array.length;i++) {
                list.add(array[i]);
            }
            HotBackUp.delete(list, type);
            msg = "用户删除双机热备配置信息成功";
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("info", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "1", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "用户删除双机热备配置信息失败";
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg, e);
                logService.newLog("error", SessionUtils.getAccount(request).getUserName(), "双击热备", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "双击热备", "info", "004", "0", new Date());
                SiteContextLoaderServlet.sysLogService.offer(log);
            }
        }
        actionBase.actionEnd(response,"{success:true,msg:'"+msg+"'}",result);
        return null;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public HotBackUp getHotBackUp() {
        return hotBackUp;
    }

    public void setHotBackUp(HotBackUp hotBackUp) {
        this.hotBackUp = hotBackUp;
    }
}
