package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.UserGroupDao;
import com.hzih.sslvpn.domain.UserGroup;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午10:21
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupDaoImpl extends MyDaoSupport implements UserGroupDao {
    @Override
    public PageResult getUsersByRoleId(int roleId, int start, int limit) throws Exception {
        int pageIndex = start/limit+1;
        String hql = " from UserGroup s where 1=1";
        List paramsList = new ArrayList();
        if (roleId > 0) {
            hql += " and group_id = ?";
            paramsList.add(roleId);
        }
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),pageIndex, limit);
        return ps;
    }

    @Override
    public void addUsersToRoleId(String uIds,int roleId) throws Exception {
        if(uIds!=null){
            String[] ida = uIds.split(",");
            for(String s : ida) {
                int id = Integer.parseInt(s);
                super.getHibernateTemplate().save(new UserGroup(id,roleId));
            }
        }
    }

    @Override
    public void addUserToRoleId(int uId, int roleId) throws Exception {
        super.getHibernateTemplate().save(new UserGroup(uId,roleId));
    }


    @Override
    public boolean delAllByRoleId(int roleId) throws Exception {
        boolean flag = false;
        String hql="delete from UserGroup u where u.group_id = "+roleId;
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query=session.createQuery(hql);
            query.executeUpdate();
            session.getTransaction().commit();
            flag=true;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        } finally {
            session.close();
        }
        return flag;
    }

    @Override
    public PageResult findCaUserByOtherRoleId(final int start, final int limit) {
//        final String hql = "from User u2 where u2.id not in (select u.id from User u,UserGroup r where u.id=r.ca_user_id) order by u2.id asc";
        final String hql = "from User u where u.id not in (select ug.user_id from UserGroup ug) order by u.id asc";
        int pageIndex = start/limit+1;
        List paramsList = new ArrayList();
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),pageIndex, limit);
        return ps;
    }

    @Override
    public void delByRoleIdAndUserId(int i, int i1)throws Exception{
        super.getHibernateTemplate().delete(new UserGroup(i1,i));
    }

    @Override
    public void delByUserId(int i) throws Exception {
        String hql="delete from UserGroup u where u.user_id = "+i;
        Session session = super.getSession();
        session.beginTransaction();
        Query query=session.createQuery(hql);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<UserGroup> findByUserId(int id) throws Exception {
        List<UserGroup> caUserRoles = null;
        String hql = " from UserGroup s where 1=1";
        List paramsList = new ArrayList();
        if (id >= 0) {
            hql += " and ca_user_id = ?";
            paramsList.add(id);
        }
        Session session = super.getSession();
        session.beginTransaction();
        Query query=session.createQuery(hql);
        caUserRoles = (List<UserGroup>) query.list();
        session.getTransaction().commit();
        session.close();
        return caUserRoles;

    }

    @Override
    public void setEntityClass() {
        this.entityClass = UserGroup.class;
    }


}
