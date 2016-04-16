package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.BackUp;

public interface BackUpDao extends BaseDao {

	public boolean add(BackUp backUp)throws Exception;

	public boolean delete(BackUp backUp)throws Exception;

	BackUp findById(int id) throws Exception;

	public PageResult findByPages(int start, int limit)throws Exception;
}
