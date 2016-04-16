package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.StaticIpDao;
import com.hzih.sslvpn.domain.StaticIp;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-13
 * Time: 下午4:13
 * To change this template use File | Settings | File Templates.
 */
public class StaticIpDaoImpl extends MyDaoSupport implements StaticIpDao{

    @Override
    public void setEntityClass() {
        this.entityClass = StaticIp.class;
    }

    @Override
    public StaticIp findById(int id) throws Exception {
        String hql="from StaticIp g where g.id ="+id;
        List<StaticIp> staticIps  = super.getHibernateTemplate().find(hql);
        if(staticIps.size()>0&&staticIps!=null){
            return staticIps.get(0);
        }else {
            return null;
        }
    }
}
