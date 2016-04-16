package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.Groups;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public interface GroupDao extends BaseDao{

    PageResult listByPage(String group_name,int pageIndex, int limit);

    public boolean add(Groups group)throws Exception;

    public boolean modify(Groups group)throws Exception;

    public boolean delete(Groups group)throws Exception;
    
    public Groups findById(int id)throws Exception;
    
    public Groups findByName(String group_name)throws Exception;

    boolean merge(Groups group);

    boolean disable(int i);

    boolean enable(int i);
}
