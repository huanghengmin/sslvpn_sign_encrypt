package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.TrustCertificate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public interface TrustCertificateDao extends BaseDao{
    public TrustCertificate findById(int id)throws Exception;

    PageResult listByPage(int pageIndex, int limit);

    public boolean add(TrustCertificate caManager)throws Exception;

    public boolean modify(TrustCertificate caManager)throws Exception;

    public boolean delete(TrustCertificate caManager)throws Exception;

    public boolean modify_check_no()throws Exception;

    boolean modify_check_on(int id) throws Exception;

    TrustCertificate find_name(String name)throws Exception;

    List<TrustCertificate> findAllCheck();
}
