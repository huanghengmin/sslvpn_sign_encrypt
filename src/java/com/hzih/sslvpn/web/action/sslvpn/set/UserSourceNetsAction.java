package com.hzih.sslvpn.web.action.sslvpn.set;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.SourceNetDao;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.SourceNet;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.VPNConfigUtil;
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
import java.util.Set;

/**
 * Created by Administrator on 15-4-14.
 */
public class UserSourceNetsAction extends ActionSupport {
    private Logger logger = Logger.getLogger(UserSourceNetsAction.class);
    private SourceNetDao sourceNetDao;
    private LogService logService;
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
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

    public String update() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String msg = "保存失败";
        String json = "{success:false,msg:'" + msg + "'}";
        String id = request.getParameter("id");
        String[] ids = request.getParameterValues("ids");
        User user = null;
        if (id != null)
            user = userDao.findById(Integer.parseInt(id));
        if (user != null) {
            Set<SourceNet> sourceNetSet = user.getSourceNets();
            sourceNetSet.clear();
            if (null != ids && !"".equals(ids) && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    String idy = ids[i];
                    if (!"".equals(idy)) {
                        SourceNet sourceNet = sourceNetDao.findById(Integer.parseInt(idy));
                        if (!sourceNetSet.contains(sourceNet)) {
                            sourceNetSet.add(sourceNet);
                        }
                    }
                }
            }
            user.setSourceNets(sourceNetSet);
            userDao.merge(user);
            VPNConfigUtil.configUser(user, StringContext.ccd);
            msg = "保存成功";
            json = "{success:true,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户资源", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户资源", "info", "004", "1", new Date());
                SyslogSender.sysLog(log);
            }
        }else {
            msg = "保存失败,未找到指定用户";
            json = "{success:false,msg:'" + msg + "'}";
            if(AuditFlagAction.getAuditFlag()) {
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "用户资源", msg);
                String log = AccountLogUtils.getResult(SessionUtils.getAccount(request).getUserName(), msg, "用户资源", "info", "004", "0", new Date());
                SyslogSender.sysLog(log);
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public boolean existObject(Set<SourceNet> sourceNets, SourceNet sourceNet) {
        if (sourceNets != null && sourceNets.size() > 0) {
            for (SourceNet s : sourceNets) {
                if (s.getNet().equals(sourceNet.getNet()) && s.getNet_mask().equals(sourceNet.getNet_mask())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String findNets() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        int pageIndex = start / limit + 1;
        String id = request.getParameter("id");
        User user = null;
        if (id != null) {
            user = userDao.findById(Integer.parseInt(id));
        }
        if (user != null) {
            PageResult pageResult = sourceNetDao.listByPage(pageIndex, limit);
            if (pageResult != null) {
                Set<SourceNet> sourceNetSet = null;
                sourceNetSet = user.getSourceNets();
                List<SourceNet> list = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (list != null) {
                    String json = "{success:true,total:" + count + ",rows:[";
                    Iterator<SourceNet> raUserIterator = list.iterator();
                    while (raUserIterator.hasNext()) {
                        SourceNet log = raUserIterator.next();
                        boolean flag = existObject(sourceNetSet, log);
                        if (sourceNetSet != null && flag) {
                            if (raUserIterator.hasNext()) {
                                json += "{" +
                                        "id:'" + log.getId() +
                                        "',net:'" + log.getNet() +
//                                        "',level:'" + log.getLevel() +
                                        "',checked:" + true +
                                        ",net_mask:'" + log.getNet_mask() + "'" +
                                        "},";
                            } else {
                                json += "{" +
                                        "id:'" + log.getId() +
                                        "',net:'" + log.getNet() +
//                                        "',level:'" + log.getLevel() +
                                        "',checked:" + true +
                                        ",net_mask:'" + log.getNet_mask() + "'" +
                                        "}";
                            }
                        } else {
                            if (raUserIterator.hasNext()) {
                                json += "{" +
                                        "id:'" + log.getId() +
                                        "',net:'" + log.getNet() +
//                                        "',level:'" + log.getLevel() +
                                        "',checked:" + false +
                                        ",net_mask:'" + log.getNet_mask() + "'" +
                                        "},";
                            } else {
                                json += "{" +
                                        "id:'" + log.getId() +
                                        "',net:'" + log.getNet() +
//                                        "',level:'" + log.getLevel() +
                                        "',checked:" + false +
                                        ",net_mask:'" + log.getNet_mask() + "'" +
                                        "}";
                            }
                        }

                    }
                    json += "]}";
                    actionBase.actionEnd(response, json, result);
                }
            }
        }

        return null;
    }
}
