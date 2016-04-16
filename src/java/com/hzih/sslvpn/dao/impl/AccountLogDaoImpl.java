package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.AccountLogDao;
import com.hzih.sslvpn.domain.AccountLog;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountLogDaoImpl extends MyDaoSupport implements AccountLogDao {

    @Override
    public void setEntityClass() {
        this.entityClass = AccountLog.class;
    }


    @Override
    public PageResult listByPage(String name,String audittype, Date startDate, Date endDate, int pageIndex, int limit) {
        StringBuilder hql = new StringBuilder(" from AccountLog where 1=1 ");
        List paramsList = new ArrayList();
        if (name != null && name.length() > 0) {
            hql.append(" and account like ?");
            paramsList.add("%" + name + "%");
        }
        if(audittype!=null&&audittype.length()>0){
            hql.append(" and audittype =" +audittype);
        }
        if (startDate != null) {
            hql.append(" and date_format(datetime,'%Y-%m-%d')>= date_format(?,'%Y-%m-%d')");
            paramsList.add(startDate);
        }
        if (endDate != null) {
            hql.append(" and date_format(datetime,'%Y-%m-%d')<= date_format(?,'%Y-%m-%d')");
            paramsList.add(endDate);
        }
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql.toString(), countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public List<AccountLog> findByCode(String code) {
        String queryString = " from AccountLog c where c.audittype = '" + code + "'";
        Session session = this.getSession();
        Query query = session.createQuery(queryString);
        List list = query.list();
        return list;
    }

    @Override
    public List<AccountLog> findByYearCode(String year, String code) {
        String queryString = " from AccountLog c where date_format (c.datetime,'%Y')= '" + year + "' and c.audittype = '" + code + "'";
        Session session = this.getSession();
        Query query = session.createQuery(queryString);
        List list = query.list();
        return list;
    }

    @Override
    public List<AccountLog> findByYearMonthCode(String year, String month, String code) {
        String queryString = " from AccountLog c where date_format(c.datetime,'%Y-%m')= '" + year + "-" + month + "' and c.audittype='" + code + "'";
        Session session = this.getSession();
        Query query = session.createQuery(queryString);
        List list = query.list();
        return list;
    }

    @Override
    public List<AccountLog> findByYearMonthDayCode(String year, String month, String day, String code) {
        String queryString = "  from AccountLog c where date_format(c.datetime,'%Y-%m-&d')= '" + year + "-" + month + "-" + day + "' and c.audittype='" + code + "'";
        Session session = this.getSession();
        Query query = session.createQuery(queryString);
        List list = query.list();
        return list;
    }

    @Override
    public List<AccountLog> findByYearMonthDayHourCode(String year, String month, String day, String hour, String code) {
        String queryString = "  from AccountLog c where date_format(c.datetime,'%Y-%m-&d %H')= '" + year + "-" + month + "-" + day + " " + hour + "" + "' and c.audittype='" + code + "'";
        Session session = this.getSession();
        Query query = session.createQuery(queryString);
        List list = query.list();
        return list;
    }

    @Override
    public void removeLists(long[] ids) {
        for (long id : ids) {
            AccountLog accountLog = new AccountLog(id);
            this.getHibernateTemplate().delete(accountLog);
        }
    }

    @Override
    public void remove(long id) {
        AccountLog accountLog = new AccountLog(id);
        this.getHibernateTemplate().delete(accountLog);
    }
}
