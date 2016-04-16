package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:42
 * To change this template use File | Settings | File Templates.
 */
public class UserDaoImpl extends MyDaoSupport implements UserDao{
    @Override
    public void setEntityClass() {
        this.entityClass = User.class;
    }

    @Override
    public User findBySerialNumber(String serialNumber) throws Exception {
        String hql="from User user where user.serial_number ='"+serialNumber+"'";
        List<User> users  = super.getHibernateTemplate().find(hql);
        if(users.size()>0&&users!=null){
            return users.get(0);
        }else {
            return null;
        }
    }

    @Override
    public boolean add(User user) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(user);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(User user) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(user);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(User user) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(user);
        flag = true;
        return flag;
    }

    @Override
    public User findById(int id) throws Exception {
        String hql="from User user where user.id ="+id;
        List<User> users  = super.getHibernateTemplate().find(hql);
        if(users.size()>0&&users!=null){
            return users.get(0);
        }else {
            return null;
        }
    }

    @Override
    public PageResult findByPages(String cn, int disabled, int start, int limit) throws Exception {
        int pageIndex = start/limit+1;
        String hql = " from User s where 1=1";
        List paramsList = new ArrayList();
        if (null!=cn && cn.length() > 0) {
            hql += " and cn like ?";
            paramsList.add("%" + cn + "%");
        }
        if (disabled!=-1) {
            hql += " and enabled = ?";
            paramsList.add(disabled);
        }
        String countHql = "select count(*) " + hql;
        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public User checkCn(String cn) throws Exception {
        String hql="from User user where user.cn ='"+cn+"'";
        List<User> users  = super.getHibernateTemplate().find(hql);
        if(users.size()>0&&users!=null){
            return users.get(0);
        }else {
            return null;
        }
    }

    @Override
    public User findByCommonName(String cn) throws Exception {
        String hql="from User user where user.cn ='"+cn+"'";
        List<User> users  = super.getHibernateTemplate().find(hql);
        if(null!=users&&users.size()>0){
            return users.get(0);
        }else {
            return null;
        }
    }

    @Override
    public boolean disableUser(int id) throws Exception {
        boolean flag =false;
        String s ="update User u set u.enabled= "+0+" where u.id = "+id;
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
    public boolean enableUser(int id) throws Exception {
        boolean flag =false;
        String s ="update User u set u.enabled= "+1+" where u.id = "+id;
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
    public void flushSession() throws Exception {
        Session session = this.getSession();
        if(session!=null|| session.isOpen()){
            session.flush();
            session.clear();
        }
    }

    @Override
    public boolean merge(User user) {
        boolean flag =false;
        super.getHibernateTemplate().merge(user);
        flag = true;
        return flag;
    }
}
