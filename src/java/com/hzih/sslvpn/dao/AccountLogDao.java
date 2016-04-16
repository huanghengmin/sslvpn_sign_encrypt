package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.AccountLog;

import java.util.Date;
import java.util.List;

public interface AccountLogDao extends BaseDao {

	PageResult listByPage(String userName, String audittype, Date startDate, Date endDate, int pageIndex, int limit);

	List<AccountLog>  findByCode(String code);

	List<AccountLog>  findByYearCode(String year, String code);

	List<AccountLog>  findByYearMonthCode(String year, String month, String code);

	List<AccountLog>  findByYearMonthDayCode(String year, String month, String day, String code);

	List<AccountLog>  findByYearMonthDayHourCode(String year, String month, String day, String hour, String code);

	void removeLists(long[] ids);

	void remove(long id);

}
