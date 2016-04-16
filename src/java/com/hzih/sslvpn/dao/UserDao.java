package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.User;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:32
 * To change this template use File | Settings | File Templates.
 */
public interface UserDao extends BaseDao{
    public User findBySerialNumber(String serialNumber)throws Exception;

    public boolean add(User user)throws Exception;

    public boolean modify(User user)throws Exception;

    public boolean delete(User user)throws Exception;

    public User findById(int id)throws Exception;

    public PageResult findByPages(String username,int disabled , int start, int limit)throws Exception;

    public User checkCn(String username)throws Exception;

    public User findByCommonName(String cn)throws Exception;
    
    public boolean disableUser(int id)throws Exception;

    boolean enableUser(int id) throws Exception;

    public void flushSession() throws Exception;

    boolean merge(User user);
}
