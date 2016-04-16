package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.AccountDao;
import com.hzih.sslvpn.domain.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl extends MyDaoSupport implements AccountDao {

    @Override
    public void setEntityClass() {
        this.entityClass = Account.class;
    }

    /**
     * 分页查询用户列表
     *
     * @param userName
     * @param status
     * @param pageIndex
     * @param limit
     * @return
     */
    public PageResult listByPage(String userName, String status, int pageIndex, int limit) {
        String hql = "from Account where 1=1 ";
        List paramsList = new ArrayList();
        if (userName != null && userName.length() > 0) {
            hql += " and userName like ?";
            paramsList.add("%" + userName + "%");
        }
        if (status != null && status.length() > 0
                && !status.equalsIgnoreCase("全部")) {
            hql += " and status=?";
            paramsList.add(status);
        }
        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    /**
     * 通过用户名、密码和状态查找用户
     * @param name
     * @param pwd
     * @return
     */
    public Account findByNameAndPwd(String name, String pwd) {
//        String hql = new String("from Account where userName=? and password=? and status=?");
        String hql = new String("from Account where userName=? and password=? ");
        List list = getHibernateTemplate().find(hql,
                new String[] { name, pwd/*, "有效"*/ });
        if (list != null && list.size() > 0) {
            return (Account) list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 通过用户名查找用户
     * @param userName
     * @return
     */
    public Account findByName(String userName) {
        String hql = new String("from Account where userName=?");
        List<Account> list = getHibernateTemplate().find(hql,new String[] { userName });
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Account getAccountById(long id) {
        String hql = new String("from Account where id="+id);
        List<Account> list = getHibernateTemplate().find(hql);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

}
