package com.hzih.sslvpn.service;

import com.hzih.sslvpn.domain.Role;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-8
 * Time: 下午12:24
 * To change this template use File | Settings | File Templates.
 */
public interface RoleService {
    public String insert(Role role, String[] pIds) throws Exception;

    public String delete(long id) throws Exception;

    public String update(Role role, String[] pIds) throws Exception;

    public String select(int start, int limit) throws Exception;

    public String getPermissionInsert(int start, int limit) throws Exception;

    public String getPermissionUpdate(int start, int limit,long id) throws Exception;

    public String getNameKeyValue() throws Exception;

    public String checkRoleName(String name) throws Exception;
}
