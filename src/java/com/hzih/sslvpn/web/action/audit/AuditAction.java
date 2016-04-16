package com.hzih.sslvpn.web.action.audit;


import com.hzih.sslvpn.service.AuditService;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.service.LoginService;
import com.hzih.sslvpn.utils.DateUtils;
import com.hzih.sslvpn.utils.StringUtils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午9:55
 * To change this template use File | Settings | File Templates.
 * 日志审计
 */
public class AuditAction extends ActionSupport{
    private static final Logger logger = LoggerFactory.getLogger(AuditAction.class);
    private LogService logService;
    private AuditService auditService;
    private LoginService loginService;
    private int start;
    private int limit;

    public String selectUserAudit() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json =  "{'success':true,'total':0,'rows':[]}";
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String logLevel = request.getParameter("logLevel");
            String userName = request.getParameter("userName");
            Date startDate = StringUtils.isBlank(startDateStr) ? null : DateUtils
            				.parse(startDateStr, "yyyy-MM-dd");
            Date endDate = StringUtils.isBlank(endDateStr) ? null : DateUtils
            				.parse(endDateStr, "yyyy-MM-dd");

            json = auditService.selectUserAudit(start/limit+1, limit,startDate,endDate,logLevel,userName );
            if(AuditFlagAction.getAuditFlag()) {
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "管理员日志审计", "用户读取管理员日志审计信息成功");
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(e.getMessage());
                logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "管理员日志审计", "用户读取管理员日志审计信息失败");
            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    /**
     * 检查startDate和endDate,endDate必须大于startDate
     * @return
     * @throws Exception
     */
    public String checkDate() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = null;
        boolean isClear = false;
        try {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            isClear = DateUtils.checkDate(startDate, endDate, "yyyy-MM-dd");
            msg = "校验成功";
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            msg = "校验失败"+e.getMessage();
            if(AuditFlagAction.getAuditFlag()) {
                logger.error(msg);
            }
        }
        String json =  "{success:true,msg:'"+msg+"',clear:"+isClear+"}";
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public AuditService getAuditService() {
        return auditService;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

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

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
}
