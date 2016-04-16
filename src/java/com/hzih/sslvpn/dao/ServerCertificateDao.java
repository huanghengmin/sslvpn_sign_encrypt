package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.ServerCertificate;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
public interface ServerCertificateDao extends BaseDao{

    PageResult listByPage(int pageIndex, int limit);

    public boolean add(ServerCertificate serverCertificate)throws Exception;

    public boolean modify(ServerCertificate serverCertificate)throws Exception;

    public boolean delete(ServerCertificate serverCertificate)throws Exception;

    public ServerCertificate findByName(String name)throws Exception;

    ServerCertificate findById(int id) throws Exception;
}
