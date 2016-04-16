package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.Server;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:34
 * To change this template use File | Settings | File Templates.
 */
public interface ServerDao extends BaseDao{

    PageResult listByPage(int pageIndex, int limit);

    public boolean add(Server server)throws Exception;

    public boolean modify(Server server)throws Exception;

    public boolean delete(Server server)throws Exception;

    public Server find()throws Exception;

    public boolean merge(Server server)throws Exception;
}
