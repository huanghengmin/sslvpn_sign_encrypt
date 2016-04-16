package com.hzih.sslvpn.service;

import com.hzih.sslvpn.domain.Account;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:02
 * To change this template use File | Settings | File Templates.
 */
public interface AccountService {
    public String select(String userName, String status, int start, int limit) throws Exception;

    public String update(Account account, long[] rIds) throws Exception;

    public String delete(Long id) throws Exception;

    public String insert(Account account, long[] rIds) throws Exception;

    public String checkUserName(String userName) throws Exception;

    public String selectUserNameKeyValue() throws Exception;

    public Account getAccountById(long id)throws Exception;

}
