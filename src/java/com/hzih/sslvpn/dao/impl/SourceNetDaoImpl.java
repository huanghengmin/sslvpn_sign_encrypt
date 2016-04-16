package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.SourceNetDao;
import com.hzih.sslvpn.domain.SourceNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class SourceNetDaoImpl extends MyDaoSupport implements SourceNetDao {
    @Override
    public void setEntityClass() {
        this.entityClass = SourceNet.class;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from SourceNet s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(SourceNet net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(SourceNet net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(SourceNet net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(net);
        flag = true;
        return flag;
    }

    @Override
    public SourceNet findByNet(String net) throws Exception {
        String hql= String.format("from SourceNet s where s.net ='%s'", net);
        List<SourceNet> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }

    @Override
    public SourceNet findById(int id) throws Exception {
        String hql= String.format("from SourceNet s where s.id =%d", id);
        List<SourceNet> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }
}
