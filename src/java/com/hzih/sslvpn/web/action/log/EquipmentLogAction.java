package com.hzih.sslvpn.web.action.log;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.EquipmentLogDao;
import com.hzih.sslvpn.domain.EquipmentLog;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.utils.DateUtils;
import com.hzih.sslvpn.utils.StringUtils;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
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
 * Created by Administrator on 15-5-26.
 */
public class EquipmentLogAction extends ActionSupport{

    private Logger logger = Logger.getLogger(EquipmentLogAction.class);
    private EquipmentLogDao equipmentLogDao;
    private LogService logService;
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

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        StringBuilder json = new StringBuilder();
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String level = request.getParameter("level");
            String equipment_name = request.getParameter("equipment_name");
            Date startDate = StringUtils.isBlank(startDateStr) ? null : DateUtils.parse(startDateStr, "yyyy-MM-dd");
            Date endDate = StringUtils.isBlank(endDateStr) ? null : DateUtils.parse(endDateStr, "yyyy-MM-dd");

            PageResult pageResult = equipmentLogDao.listLogsByParams(start/limit+1, limit,startDate,endDate,level,equipment_name);
            if (pageResult != null) {
                List<EquipmentLog> list = pageResult.getResults();
                int count = pageResult.getAllResultsAmount();
                if (list != null) {
                    json.append("{success:true,total:" + count + ",rows:[");
                    Iterator<EquipmentLog> raUserIterator = list.iterator();
                    while (raUserIterator.hasNext()) {
                        EquipmentLog log = raUserIterator.next();
                        if (raUserIterator.hasNext()) {
                            json.append("{");
                            json.append("id:'").append(log.getId()).append("'").append(",");
                            json.append("equipment_name:'").append(log.getEquipment_name()).append("'").append(",");
                            json.append("level:'").append(log.getLevel()).append("'").append(",");
                            json.append("loginfo:'").append(log.getLog_info()).append("'").append(",");
                            json.append("datetime:'").append(DateUtils.formatDate(log.getLog_time(),"yyyy年MM月dd日HH时mm分ss秒"))
                                    .append("'");
                            json.append("}").append(",");
                        } else {
                            json.append("{");
                            json.append("id:'").append(log.getId()).append("'").append(",");
                            json.append("equipment_name:'").append(log.getEquipment_name()).append("'").append(",");
                            json.append("level:'").append(log.getLevel()).append("'").append(",");
                            json.append("loginfo:'").append(log.getLog_info()).append("'").append(",");
                            json.append("datetime:'").append(DateUtils.formatDate(log.getLog_time(), "yyyy年MM月dd日HH时mm分ss秒"))
                                    .append("'");
                            json.append("}");
                        }
                    }
                    json.append("]}");
                    actionBase.actionEnd(response, json.toString(), result);
                }
            }

        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            if(AuditFlagAction.getAuditFlag()) {
                logger.error("管理端日志审计", e);
                logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "管理端日志审计", "用户读取管理端审计日志信息失败");
            }
        }
        return null;
    }
}
