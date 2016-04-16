package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.ServerCertificateDao;
import com.hzih.sslvpn.domain.ServerCertificate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-17
 * Time: 下午1:26
 * To change this template use File | Settings | File Templates.
 */
public class ServerCertificateDaoImpl extends MyDaoSupport implements ServerCertificateDao {

    @Override
    public ServerCertificate findById(int id) throws Exception {
        String hql = new String("from ServerCertificate where id ="+id);
        List list = null;
        try {
            list = getHibernateTemplate().find(hql);
        } catch (Exception e) {
            logger.error("ServerCertificate 查找出错!",e);
        }
        ServerCertificate serverManager = (ServerCertificate) list.get(0);
        return serverManager;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = ServerCertificate.class;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from ServerCertificate s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(ServerCertificate serverCertificate) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(serverCertificate);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(ServerCertificate serverCertificate) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(serverCertificate);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(ServerCertificate serverCertificate) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(serverCertificate);
        flag = true;
        return flag;
    }

    @Override
    public ServerCertificate findByName(String name) throws Exception {
        String hql = " from ServerCertificate s where s.name='"+name+"'";
        List<ServerCertificate> list = null;
        try {
            list = getHibernateTemplate().find(hql);
            if(null!=list)
                return list.get(0);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
        return null;
    }
}
