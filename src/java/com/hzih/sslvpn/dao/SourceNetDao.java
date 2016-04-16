package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.SourceNet;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public interface SourceNetDao extends BaseDao{
    PageResult listByPage(int pageIndex, int limit);

    public boolean add(SourceNet net)throws Exception;

    public boolean modify(SourceNet net)throws Exception;

    public boolean delete(SourceNet net)throws Exception;
    
    public SourceNet findByNet(String net)throws Exception;

    public SourceNet findById(int id)throws Exception;

}
