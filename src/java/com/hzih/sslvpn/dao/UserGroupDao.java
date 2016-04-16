package com.hzih.sslvpn.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.domain.UserGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午9:50
 * To change this template use File | Settings | File Templates.
 */
public interface UserGroupDao extends BaseDao{

    //得到角色对应的用户列表
    public PageResult getUsersByRoleId(int groupId, int start, int limit)throws Exception;

    //添加用户到角色
    public void addUsersToRoleId(String uIds, int groupI)throws Exception;

    public void addUserToRoleId(int uId, int groupI)throws Exception;

    //删除所有roleId 关联
    public boolean delAllByRoleId(int groupI)throws Exception;

    public PageResult findCaUserByOtherRoleId(int start, int limit);

    void delByRoleIdAndUserId(int i, int i1)throws Exception;

    void delByUserId(int i)throws Exception;

    List<UserGroup> findByUserId(int id)throws Exception;
}
