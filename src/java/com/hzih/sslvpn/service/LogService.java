package com.hzih.sslvpn.service;

import cn.collin.commons.domain.PageResult;
//import com.hzih.sslvpn.domain.EquipmentLog;

import java.util.Date;

public interface LogService {
    /**
     * 分页查询SysLog
     *
     * @param pageIndex
     * @param pageLength
     * @param startDate
     * @param endDate
     * @param logLevel
     * @return
     */
  /*  public PageResult listSysLogByPage(int pageIndex, int pageLength,
                                       Date startDate, Date endDate, String logLevel);*/

    /**
     * 分页查询-用户日志审计
     *
     * @param pageIndex
     * @param pageLength
     * @param startDate
     * @param endDate
     * @param logLevel
     * @param userName
     * @return
     */
    public PageResult listUserOperLogByPage(int pageIndex, int pageLength,
                                            Date startDate, Date endDate, String logLevel, String userName);

    /**
     * 分页查询-业务日志审计
     *
     */
    /*public PageResult listBusinessLogByPage(int pageIndex, int pageLength,
                                            Date startDate, Date endDate, String logLevel, String businessName) throws Exception;*/

    /**
     * 添加用户日志
     * @param level
     * @param userName
     * @param auditModule
     * @param auditInfo
     */
    public void newLog(String level, String userName, String auditModule, String auditInfo);

    /**
     * 添加设备日志
//     * @param equipmentLog
     * @throws Exception
     */
//    public void newLog(EquipmentLog equipmentLog);

//    public void newEquipmentLog(EquipmentLog equipmentLog);

   /* public void newSysLog(String level, String auditModule, String auditAction, String auditinfo);*/
}
