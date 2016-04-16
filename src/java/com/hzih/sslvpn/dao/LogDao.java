package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public interface LogDao extends BaseDao{
    PageResult listByPage(int pageIndex,String common_name,String trust_ip, int limit);

    public boolean add(Log log)throws Exception;

    public boolean modify(Log log)throws Exception;

    public boolean delete(Log log)throws Exception;

    void clearLogs();

}
