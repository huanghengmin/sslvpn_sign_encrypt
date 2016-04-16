package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.LogDao;
import com.hzih.sslvpn.domain.Log;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:54
 * To change this template use File | Settings | File Templates.
 */
public class LogDaoImpl extends MyDaoSupport implements LogDao{
    @Override
    public PageResult listByPage(int pageIndex,String common_name,String trust_ip, int limit) {
        String hql = " from Log s where 1=1";
        List paramsList = new ArrayList();
        if (null!=common_name && common_name.length() > 0) {
            hql += " and cn like ?";
            paramsList.add("%" + common_name + "%");
        }
        if (null!=trust_ip && trust_ip.length() > 0) {
            hql += " and trusted_ip like ?";
            paramsList.add("%" + trust_ip + "%");
        }
        hql +=" order by id desc";
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(Log net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(Log net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(Log net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(net);
        flag = true;
        return flag;
    }

    @Override
    public void clearLogs() {
        String s = String.format("delete Log log where TO_DAYS(NOW()) - TO_DAYS(log.end_time) >= %d", 3);
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query = session.createQuery(s);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        } finally {
            session.close();
        }
    }

    @Override
    public void setEntityClass() {
        this.entityClass = Log.class;
    }
}
