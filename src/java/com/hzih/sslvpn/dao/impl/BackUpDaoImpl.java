package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.BackUpDao;
import com.hzih.sslvpn.domain.BackUp;

import java.util.ArrayList;
import java.util.List;

public class BackUpDaoImpl extends MyDaoSupport implements BackUpDao {

    @Override
    public void setEntityClass() {
        this.entityClass = BackUp.class;
    }

    @Override
    public boolean add(BackUp backUp) throws Exception {
        boolean flag = false;
        super.getHibernateTemplate().save(backUp);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(BackUp backUp) throws Exception {
        boolean flag = false;
        super.getHibernateTemplate().delete(backUp);
        flag = true;
        return flag;
    }


    @Override
    public BackUp findById(int id) throws Exception {
        String hql="from BackUp b where b.id =id";
        List<BackUp> backUps  = super.getHibernateTemplate().find(hql);
        if(null!=backUps&&backUps.size()>0){
            return backUps.get(0);
        }else {
            return null;
        }
    }

    @Override
    public PageResult findByPages(int start, int limit) throws Exception {
        int pageIndex = start / limit + 1;
        String hql = " from BackUp s where 1=1";
        List paramsList = new ArrayList();
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(), pageIndex, limit);
        return ps;
    }
}
