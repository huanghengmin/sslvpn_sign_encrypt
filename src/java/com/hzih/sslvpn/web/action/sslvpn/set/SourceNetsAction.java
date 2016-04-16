package com.hzih.sslvpn.web.action.sslvpn.set;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.SourceNetDao;
import com.hzih.sslvpn.domain.SourceNet;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.AccountLogUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.syslog.sender.SyslogSender;
import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 15-4-14.
 */
public class SourceNetsAction extends ActionSupport {
    private Logger logger = Logger.getLogger(SourceNetsAction.class);
    private SourceNetDao sourceNetDao;
    private SourceNet sourceNet;
    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public SourceNet getSourceNet() {
        return sourceNet;
    }

    public void setSourceNet(SourceNet sourceNet) {
        this.sourceNet = sourceNet;
    }

    public SourceNetDao getSourceNetDao() {
        return sourceNetDao;
    }

    public void setSourceNetDao(SourceNetDao sourceNetDao) {
        this.sourceNetDao = sourceNetDao;
    }

    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String add() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'保存失败'}";
        String msg = null;
        if (sourceNet != null) {
            SourceNet old = sourceNetDao.findByNet(sourceNet.getNet());
            if (null != old) {
                msg = "资源已在在";
                json = "{success:false,msg:'" + msg + "'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "资源管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                sourceNetDao.add(sourceNet);
                msg = "资源添加成功";
                json = "{success:true,msg:'" + msg + "!'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "资源管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String modify() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'更新失败'}";
        String msg = null;
        String id = request.getParameter("id");
        SourceNet old = sourceNetDao.findById(Integer.parseInt(id));
        if (null != old) {
            if (old.getNet().equals(sourceNet.getNet()) && old.getNet_mask().equals(sourceNet.getNet_mask())) {
//                json = "{success:false,msg:'未修改数据,无需更新'}";
                msg = "未修改数据,无需更新";
                json = "{success:false,msg:'" + msg + "!'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "资源管理", "info", "004", "0", new Date());
                    SyslogSender.sysLog(log);
                }
            } else if (old.getNet().equals(sourceNet.getNet())) {
                old.setNet_mask(sourceNet.getNet_mask());
                sourceNetDao.modify(old);
//                json = "{success:true,msg:'更新成功'}";
                msg = "更新成功";
                json = "{success:true,msg:'" + msg + "!'}";
                if(AuditFlagAction.getAuditFlag()) {
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                    String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "资源管理", "info", "004", "1", new Date());
                    SyslogSender.sysLog(log);
                }
            } else {
                SourceNet net = sourceNetDao.findByNet(sourceNet.getNet());
                if (null != net) {
//                    json = "{success:false,msg:'网络已存在不允许更新到指定网络" + sourceNet.getNet() + "'}";
                    msg = "网络已存在不允许更新到指定网络" + sourceNet.getNet();
                    json = "{success:false,msg:'" + msg + "!'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "资源管理", "info", "004", "0", new Date());
                        SyslogSender.sysLog(log);
                    }
                } else {
                    old.setNet(sourceNet.getNet());
                    old.setNet_mask(sourceNet.getNet_mask());
                    sourceNetDao.modify(old);
//                    json = "{success:true,msg:'更新成功'}";
                    msg = "更新成功";
                    json = "{success:true,msg:'" + msg + "!'}";
                    if(AuditFlagAction.getAuditFlag()) {
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                        String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "资源管理", "info", "004", "1", new Date());
                        SyslogSender.sysLog(log);
                    }
                }
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        String msg = null;
        String id = request.getParameter("id");
        sourceNetDao.delete(new SourceNet(Integer.parseInt(id)));
//        json = "{success:true,msg:'删除成功'}";
        msg = "删除成功";
        json = "{success:true,msg:'" + msg + "!'}";
        if(AuditFlagAction.getAuditFlag()) {
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
            String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "资源管理", "info", "004", "1", new Date());
            SyslogSender.sysLog(log);
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int pageIndex = start / limit + 1;
        PageResult pageResult = sourceNetDao.listByPage(pageIndex, limit);
        if (pageResult != null) {
            List<SourceNet> list = pageResult.getResults();
            int count = pageResult.getAllResultsAmount();
            if (list != null) {
                String json = "{success:true,total:" + count + ",rows:[";
                Iterator<SourceNet> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()) {
                    SourceNet log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "id:'" + log.getId() +
                                "',net:'" + log.getNet() +
                                "',net_mask:'" + log.getNet_mask() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }
        }
        return null;
    }
}
