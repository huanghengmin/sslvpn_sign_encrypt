package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupDao;
import com.hzih.sslvpn.domain.Groups;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class GroupDaoImpl extends MyDaoSupport implements GroupDao{
    @Override
    public PageResult listByPage(String group_name,int pageIndex, int limit) {
        String hql = "from Groups where 1=1 ";
        List paramsList = new ArrayList();
        if (null!=group_name && group_name.length() > 0) {
            hql += " and group_name like ?";
            paramsList.add("%" + group_name + "%");
        }
        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(Groups net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(Groups net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(Groups net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(net);
        flag = true;
        return flag;
    }

    @Override
    public Groups findById(int id) throws Exception {
        String hql="from Groups g where g.id ="+id;
        List<Groups> groups  = super.getHibernateTemplate().find(hql);
        if(groups.size()>0&&groups!=null){
            return groups.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Groups findByName(String group_name) throws Exception {
        String hql="from Groups  g where g.group_name ='"+group_name+"'";
        List<Groups> groups  = super.getHibernateTemplate().find(hql);
        if(groups.size()>0&&groups!=null){
            return groups.get(0);
        }else {
            return null;
        }
    }

    @Override
    public boolean merge(Groups group) {
        boolean flag =false;
        super.getHibernateTemplate().merge(group);
        flag = true;
        return flag;
    }

    @Override
    public boolean disable(int id) {
        boolean flag =false;
        String s ="update Groups g set g.deny_access= "+1+" where g.id = "+id;
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query = session.createQuery(s);
            query.executeUpdate();
            session.getTransaction().commit();
            flag = true;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        } finally {
            session.close();
        }
        return flag;
    }

    @Override
    public boolean enable(int id) {
        boolean flag =false;
        String s ="update Groups g set g.deny_access= "+0+" where g.id = "+id;
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query = session.createQuery(s);
            query.executeUpdate();
            session.getTransaction().commit();
            flag = true;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        } finally {
            session.close();
        }
        return flag;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = Groups.class;
    }
}
