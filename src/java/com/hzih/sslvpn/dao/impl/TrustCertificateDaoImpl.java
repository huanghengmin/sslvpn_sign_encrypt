package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.TrustCertificate;
import com.hzih.sslvpn.dao.TrustCertificateDao;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class TrustCertificateDaoImpl extends MyDaoSupport implements TrustCertificateDao {
    @Override
    public TrustCertificate findById(int id) throws Exception {
        String hql = new String("from TrustCertificate where id ="+id);
        List list = null;
        try {
            list = getHibernateTemplate().find(hql);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            logger.error("TrustCertificate 根据Id查询出错!",e);
        }
        TrustCertificate caManager = (TrustCertificate) list.get(0);
        return caManager;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from TrustCertificate s where 1=1";
        List paramsList = new ArrayList();
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(), pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(TrustCertificate trustCertificate) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(trustCertificate);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(TrustCertificate trustCertificate) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(trustCertificate);
        flag = true;
        return flag;
    }
    

    @Override
    public boolean delete(TrustCertificate trustCertificate) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(trustCertificate);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify_check_no() throws Exception {
        String s ="update TrustCertificate trustCertificate set trustCertificate.status = 0";
        Session session = super.getSession();
        session.beginTransaction();
        Query query = session.createQuery(s);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean modify_check_on(int id) throws Exception {
        String s ="update TrustCertificate trustCertificate set trustCertificate.status = 1 where trustCertificate.id="+id;
        Session session = super.getSession();
        session.beginTransaction();
        Query query = session.createQuery(s);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public TrustCertificate find_name(String name) throws Exception {
        String hql = " from TrustCertificate s where s.name='"+name+"'";
        List<TrustCertificate> list = null;
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

    @Override
    public List<TrustCertificate> findAllCheck() {
        String hql = " from TrustCertificate s where s.status=1";
        List<TrustCertificate> list = null;
        try {
            list = getHibernateTemplate().find(hql);
            if(null!=list)
                return list;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
        return null;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = TrustCertificate.class;
    }
}
