package com.hzih.sslvpn.service.impl;

import cn.collin.commons.domain.PageResult;
//import com.hzih.sslvpn.dao.BusinessLogDao;
//import com.hzih.sslvpn.dao.EquipmentLogDao;
//import com.hzih.sslvpn.dao.SysLogDao;
import com.hzih.sslvpn.dao.UserOperLogDao;
//import com.hzih.sslvpn.domain.SysLog;
import com.hzih.sslvpn.domain.UserOperLog;
import com.hzih.sslvpn.service.LogService;
import org.apache.log4j.Logger;

import java.util.Date;

public class LogServiceImpl implements LogService {
    private final static Logger logger = Logger.getLogger(LogServiceImpl.class);
//    private SysLogDao sysLogDao;
    private UserOperLogDao userOperLogDao;
//    private BusinessLogDao businessLogDao;
//    private EquipmentLogDao equipmentLogDao;


//    public void setSysLogDao(SysLogDao sysLogDao) {
//        this.sysLogDao = sysLogDao;
//    }

    public void setUserOperLogDao(UserOperLogDao userOperLogDao) {
        this.userOperLogDao = userOperLogDao;
    }/*

    public void setBusinessLogDao(BusinessLogDao businessLogDao) {
        this.businessLogDao = businessLogDao;
    }

    public void setEquipmentLogDao(EquipmentLogDao equipmentLogDao) {
        this.equipmentLogDao = equipmentLogDao;
    }*/

  /*  public PageResult listSysLogByPage(int pageIndex, int pageLength,
                                       Date startDate, Date endDate, String logLevel) {
        logger.debug("pageIndex:" + pageIndex + "pageLength:" + pageLength
                + "startDate:" + startDate + " endDate:" + endDate);
        PageResult ps = this.sysLogDao.listLogsByParams(pageIndex, pageLength,
                startDate, endDate, logLevel);
        return ps;
    }*/


    public PageResult listUserOperLogByPage(int pageIndex, int pageLength,
                                            Date startDate, Date endDate, String logLevel, String userName) {
        logger.debug("startDate:" + startDate + " endDate:" + endDate);
        PageResult ps = this.userOperLogDao.listLogsByParams(pageIndex,
                pageLength, startDate, endDate, logLevel, userName);
        return ps;
    }

    /*public PageResult listBusinessLogByPage(int pageIndex, int pageLength,
                                            Date startDate, Date endDate, String logLevel, String businessName) throws Exception {
        logger.debug("startDate:" + startDate + " endDate:" + endDate);
        PageResult ps = this.businessLogDao.listLogsByParams(pageIndex,
                pageLength, startDate, endDate, logLevel, businessName);
        return ps;
    }*/

    public void newLog(String level, String userName, String auditModule,
                       String auditInfo) {
        UserOperLog entry = new UserOperLog();
        entry.setAuditInfo(auditInfo);
        entry.setAuditModule(auditModule);
        entry.setLevel(level);
        entry.setUserName(userName);
        entry.setLogTime(new Date());
        try{
            userOperLogDao.create(entry);
        } catch (Exception e){
            logger.error("新增用户日志失败",e);
        }
    }

    /**
     * 新增设备日志一次
     * @param equipmentLog
     */
    /*public void newLog(EquipmentLog equipmentLog){
        boolean isExist = false;
        try {
            isExist = equipmentLogDao.findByDateAndLogInfo(equipmentLog);
            if(!isExist){
                equipmentLogDao.create(equipmentLog);
            }
        } catch (Exception e) {
            logger.error("新增设备日志失败",e);
        }
    }*/

    /**
     * 新增设备日志
//     * @param equipmentLog
     */
    /*public void newEquipmentLog(EquipmentLog equipmentLog) {
        try{
            equipmentLogDao.create(equipmentLog);
        }catch (Exception e){
            logger.error("新增设备日志失败",e);
        }
    }*/

   /* @Override
    public void newSysLog(String level, String auditModule, String auditAction, String auditinfo) {
        SysLog sysLog = new SysLog();
        sysLog.setLogTime(new Date());
        sysLog.setLevel(level);
        sysLog.setAuditModule(auditModule);
        sysLog.setAuditAction(auditAction);
        sysLog.setAuditInfo(auditinfo);
        try{
            sysLogDao.create(sysLog);
        } catch (Exception e) {
            logger.error("新增系统日志",e);
        }
    }*/
}
