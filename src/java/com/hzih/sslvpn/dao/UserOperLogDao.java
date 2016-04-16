package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.UserOperLog;

import java.util.Date;

public interface UserOperLogDao extends BaseDao {
	/**
	 * 分页查询
	 * 
	 */
	public PageResult listLogsByParams(int pageIndex, int pageLength,
                                       Date startDate, Date endDate, String logLevel, String userName);

	public UserOperLog selectModelLast(String userName, String auditModel);
}
