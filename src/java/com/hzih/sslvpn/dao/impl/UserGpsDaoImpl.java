package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.UserGpsDao;
import com.hzih.sslvpn.domain.UserGps;

import java.util.List;

/**
 * Created by hhm on 2014/12/17.
 */
public class UserGpsDaoImpl extends MyDaoSupport implements UserGpsDao {
    @Override
    public boolean add(UserGps userGps) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(userGps);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(UserGps userGps) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(userGps);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(UserGps userGps) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(userGps);
        flag = true;
        return flag;
    }

    @Override
    public UserGps findById(int id) throws Exception {
        String hql="from UserGps userGps where userGps.id ="+id;
        List<UserGps> userGpses  = super.getHibernateTemplate().find(hql);
        if(userGpses.size()>0&&userGpses!=null){
            return userGpses.get(0);
        }else {
            return null;
        }
    }

    @Override
    public void setEntityClass() {
        this.entityClass = UserGps.class;
    }
}
