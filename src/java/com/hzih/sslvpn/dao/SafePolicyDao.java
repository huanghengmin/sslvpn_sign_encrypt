package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.sslvpn.domain.SafePolicy;

public interface SafePolicyDao extends BaseDao{

	SafePolicy getData();

}
