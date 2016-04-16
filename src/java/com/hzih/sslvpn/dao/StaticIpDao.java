package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.sslvpn.domain.StaticIp;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-13
 * Time: 下午4:13
 * To change this template use File | Settings | File Templates.
 */
public interface StaticIpDao extends BaseDao{
    StaticIp findById(int id)throws Exception;
}
