package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.ServerDao;
import com.hzih.sslvpn.domain.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:54
 * To change this template use File | Settings | File Templates.
 */
public class ServerDaoImpl extends MyDaoSupport implements ServerDao {

    @Override
    public void setEntityClass() {
        this.entityClass = Server.class;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from Server s where 1=1";
        List paramsList = new ArrayList();
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(), pageIndex, limit);
        return ps;
    }


    @Override
    public Server find() throws Exception{
        String hql="from Server where id = "+1;
        List<Server> servers  = super.getHibernateTemplate().find(hql);
        if(servers!=null&&servers.size()>0)
            return servers.get(0);
        else
            return null;
    }

    @Override
    public boolean merge(Server server) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().merge(server);
        flag = true;
        return flag;
    }

    @Override
    public boolean add(Server server) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(server);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(Server server) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(server);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(Server server) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(server);
        flag = true;
        return flag;
    }
}
