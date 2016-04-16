package com.hzih.sslvpn.service;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.UserGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午10:54
 * To change this template use File | Settings | File Templates.
 */
public interface UserGroupService {
    //得到角色对应的用户列表
    public String getUsersByRoleId(int roleId, int start, int limit)throws Exception;

    //添加用户到角色
    public void addUsersToRoleId(String uIds, int roleId)throws Exception;

    public void addUserToRoleId(int uId, int roleId)throws Exception;
    //找出去除roleId的其他的用户
    public PageResult findCaUserByOtherRoleId(int start, int limit);

    //删除所有roleId 关联
    public boolean delAllByRoleId(int roleId)throws Exception;

    void delByRoleIdAndUserId(int i, int i1)throws Exception;


    void delByUserId(int i)throws Exception;

    List<UserGroup> findByUserId(int id)throws Exception;
}
