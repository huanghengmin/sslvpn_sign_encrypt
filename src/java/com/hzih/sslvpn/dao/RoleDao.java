package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.sslvpn.domain.Role;

public interface RoleDao extends BaseDao {

    public Role findByName(String name) throws Exception;
}
