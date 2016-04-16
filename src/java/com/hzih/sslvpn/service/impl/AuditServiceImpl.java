package com.hzih.sslvpn.service.impl;

import cn.collin.commons.domain.PageResult;
//import com.hzih.sslvpn.dao.BusinessLogDao;
//import com.hzih.sslvpn.dao.EquipmentLogDao;
//import com.hzih.sslvpn.dao.SysLogDao;
import com.hzih.sslvpn.dao.UserOperLogDao;
import com.hzih.sslvpn.domain.*;
import com.hzih.sslvpn.service.AuditService;
import com.hzih.sslvpn.utils.DateUtils;
import com.hzih.sslvpn.utils.FileUtil;
import com.hzih.sslvpn.utils.StringContext;
import com.hzih.sslvpn.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-19
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class AuditServiceImpl implements AuditService{
//    private BusinessLogDao businessLogDao;
//    private SysLogDao sysLogDao;
    private UserOperLogDao userOperLogDao;
//    private EquipmentLogDao equipmentLogDao;

    /**
     * 分页读取用户日志--并以json形式返回
     */
    public String selectUserAudit(int pageIndex, int limit, Date startDate, Date endDate,
                                  String logLevel, String userName) throws Exception {
        PageResult pageResult = userOperLogDao.listLogsByParams(pageIndex,limit,startDate, endDate, logLevel, userName);
        List<UserOperLog> userOperLogs = pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (UserOperLog u : userOperLogs) {
            json +="{id:'"+u.getId()+"',userName:'"+u.getUserName()+"',level:'"+u.getLevel()+
                    "',auditModule:'"+u.getAuditModule()+"',auditInfo:'"+u.getAuditInfo()+
                    "',logTime:'"+ DateUtils.formatDate(u.getLogTime(), "yy-MM-dd HH:mm:ss")+"'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public UserOperLog selectModelLast(String userName, String auditModel) throws Exception {
        return userOperLogDao.selectModelLast(userName,auditModel);
    }
/*
    *//**
     * 删除符合条件的数据,如果没有条件则清空设备日志表的日志
     * @param startDate
     * @param endDate
     * @param logLevel
     * @param equipmentName
     * @throws Exception
     *//*
    public void deleteEquipment(String startDate, String endDate, String logLevel, String equipmentName) throws Exception {
        if(StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate) &&
                StringUtils.isBlank(logLevel) && StringUtils.isBlank(equipmentName)) {
            equipmentLogDao.truncate();
        } else {
            equipmentLogDao.delete(startDate,endDate,logLevel,equipmentName);
        }
    }*/

    /**
     * 删除符合条件的数据,如果没有条件则清空业务日志表的日志
     */
  /*  public void deleteBusiness(String startDate, String endDate, String businessType, String businessName) throws Exception {
        if(StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate) &&
                StringUtils.isBlank(businessType) && StringUtils.isBlank(businessName)) {
            businessLogDao.truncate();
        } else {
            businessLogDao.delete(startDate,endDate,businessType,businessName);
        }
    }*/

   /* *//**
     * 读取审计信息比对
     * @param pageIndex
     * @param limit
     * @param startDate
     * @param endDate
     * @param businessType
     * @param businessName
     * @return
     * @throws Exception
     *//*
    public String selectBusinessCompareAudit(int pageIndex, int limit, Date startDate, Date endDate,
                                             String businessType, String businessName) throws Exception {
        PageResult pageResult = businessLogDao.listCompareLogsByParams(pageIndex, limit, startDate, endDate, businessType, businessName);
        String json = "{success:true,total:0,rows:[]}";
        if(pageResult!=null){
            int total = 0;
            List<BusinessLog> list = pageResult.getResults();
            String rows = "rows:[";
            for (BusinessLog log : list) {
                int internalCount = businessLogDao.getCount(log.getBusinessName(), log.getBusinessType(), log.getFileName(), StringContext.INTERNAL);
                int externalCount = businessLogDao.getCount(log.getBusinessName(), log.getBusinessType(), log.getFileName(), StringContext.EXTERNAL);
                if(internalCount!=externalCount){
                    total ++;
                    rows +="{businessName:'"+log.getBusinessName()+
                            "',businessType:'"+log.getBusinessType()+
                            "',fileName:'"+log.getFileName()+
                            "',internalCount:'"+FileUtil.setLength(internalCount)+
                            "',externalCount:'"+FileUtil.setLength(externalCount)+"'},";
                }
            }
            json = "{success:true,total:" + total +","+ rows + "]}";
        }
        return json;
    }*/

    /**
     * 业务需要重传的文件导出后,改变业务日志比对标记flag
//     * @param list
     * @throws Exception
     */
   /* public void updateBusinessLogFlag(List<AuditReset> list) throws Exception {
        for (AuditReset a : list){
            List<BusinessLog> olds = businessLogDao.findByNameTypeFileName(a.getBusinessName(),a.getBusinessType(),a.getFileName());
            for (BusinessLog log : olds){
                log.setFlag(1);
                businessLogDao.update(log);
            }
        }
    }*/

    /*@Override
    public List<AuditReset> selectBusinessCompareAudit(String businessName) throws Exception {
        List<AuditReset> auditResets = new ArrayList<AuditReset>();
        List<BusinessLog> list = businessLogDao.listCompareLogsByNameAndType(businessName, "file");
        for (BusinessLog log : list) {
            int internalCount = businessLogDao.getCount(log.getBusinessName(), log.getBusinessType(), log.getFileName(), StringContext.INTERNAL);
            int externalCount = businessLogDao.getCount(log.getBusinessName(), log.getBusinessType(), log.getFileName(), StringContext.EXTERNAL);
            if(internalCount!=externalCount){
                AuditReset a = new AuditReset();
                a.setBusinessName(log.getBusinessName());
                a.setBusinessType("file");
                a.setFileName(log.getFileName());
                a.setFileSize(FileUtil.setLength(externalCount));
                auditResets.add(a);
            }
        }
        return auditResets;
    }*/

    /*@Override
    public String selectOSAudit(int pageIndex, int limit, Date startDate, Date endDate, String logLevel) {
        PageResult pageResult = sysLogDao.listLogsByParams(pageIndex,limit,startDate, endDate, logLevel);
        List<SysLog> list = pageResult.getResults();
        int total = pageResult.getAllResultsAmount();
        String json = "{success:true,total:"+ total + ",rows:[";
        for (SysLog o : list) {
            json +="{id:'"+o.getId()+"',auditAction:'"+o.getAuditAction()+"',level:'"+o.getLevel()+
                    "',auditModule:'"+o.getAuditModule()+"',auditInfo:'"+o.getAuditInfo()+
                    "',logTime:'"+DateUtils.formatDate(o.getLogTime(),"yy-MM-dd HH:mm:ss")+"'},";
        }
        json += "]}";
        return json;
    }*/

    /**
     *  分页读取用户日志--并以json形式返回
     */
   /* public String selectBusinessAudit(int pageIndex, int limit, Date startDate,
                                      Date endDate, String businessType, String businessName) throws Exception {
        PageResult pageResult = businessLogDao.listLogsByParams(pageIndex,limit,startDate,endDate,businessType,businessName);
        int total = pageResult.getAllResultsAmount();
        List<BusinessLog> list = pageResult.getResults();
        String json = "{success:true,total:" + total + ",rows:[";
        for (BusinessLog log : list) {
            json +="{id:"+log.getId()+",businessName:'"+log.getBusinessName()+"',level:'"+log.getLevel()+
                    "',businessType:'"+log.getBusinessType()+"',businessDesc:'"+log.getBusinessDesc()+
                    "',auditCount:"+log.getAuditCount()+",logTime:'"+DateUtils.formatDate(log.getLogTime(),"yy-MM-dd HH:mm:ss")+
                    "',sourceJdbc:'"+log.getSourceJdbc()+"',destJdbc:'"+log.getDestJdbc()+
                    "',sourceIp:'"+log.getSourceIp()+"',sourcePort:'"+log.getSourcePort()+
                    "',destIp:'"+log.getDestIp()+"',destPort:'"+log.getDestPort()+
                    "',fileName:'"+log.getFileName()+"',plugin:'"+log.getPlugin()+"'},";
        }
        json += "]}";
        return json;
    }*/

    /**
     *    分页读取设备日志--并以json形式返回
     */
    /*public String selectEquipmentAudit(int pageIndex, int limit, Date startDate, Date endDate,
                                       String logLevel, String equipmentName) throws Exception {
        PageResult pageResult = equipmentLogDao.listLogsByParams(pageIndex,limit,startDate,endDate,logLevel,equipmentName);
        int total = pageResult.getAllResultsAmount();
        List<EquipmentLog> list = pageResult.getResults();
        String json = "{success:true,total:" + total + ",rows:[";
        for (EquipmentLog log : list) {
            json +="{id:'"+log.getId()+"',equipmentName:'"+log.getEquipmentName()+"',level:'"+log.getLevel()+
                    "',linkName:'"+log.getLinkName()+"',auditInfo:'"+log.getLogInfo()+
                    "',logTime:'"+DateUtils.formatDate(log.getLogTime(),"yy-MM-dd HH:mm:ss")+"'},";
        }
        json += "]}";
        return json;
    }*/
/*
    public BusinessLogDao getBusinessLogDao() {
        return businessLogDao;
    }

    public void setBusinessLogDao(BusinessLogDao businessLogDao) {
        this.businessLogDao = businessLogDao;
    }*/
/*
    public SysLogDao getSysLogDao() {
        return sysLogDao;
    }

    public void setSysLogDao(SysLogDao sysLogDao) {
        this.sysLogDao = sysLogDao;
    }*/

    public UserOperLogDao getUserOperLogDao() {
        return userOperLogDao;
    }

    public void setUserOperLogDao(UserOperLogDao userOperLogDao) {
        this.userOperLogDao = userOperLogDao;
    }
/*
    public EquipmentLogDao getEquipmentLogDao() {
        return equipmentLogDao;
    }

    public void setEquipmentLogDao(EquipmentLogDao equipmentLogDao) {
        this.equipmentLogDao = equipmentLogDao;
    }*/
}
