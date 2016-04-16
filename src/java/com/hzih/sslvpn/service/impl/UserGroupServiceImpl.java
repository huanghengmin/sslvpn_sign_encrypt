package com.hzih.sslvpn.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.GroupDao;
import com.hzih.sslvpn.dao.UserDao;
import com.hzih.sslvpn.dao.UserGroupDao;
import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.domain.UserGroup;
import com.hzih.sslvpn.service.UserGroupService;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-27
 * Time: 下午10:58
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupServiceImpl implements UserGroupService {
    private UserGroupDao userGroupDao;
    private UserDao userDao;
    private GroupDao groupDao;

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String getUsersByRoleId(int roleId, int start, int limit) throws Exception {
        PageResult ps = userGroupDao.getUsersByRoleId(roleId,start,limit);
        StringBuilder sb = new StringBuilder();
        if(ps!=null){
            List<UserGroup> list = ps.getResults();
            int count =  ps.getAllResultsAmount();
            if(list!=null){
                sb.append("{success:true,total:" + count + ",rows:[");
                Iterator<UserGroup> raUserIterator = list.iterator();
                while (raUserIterator.hasNext()){
                    UserGroup caUserRole = raUserIterator.next();
                    if(caUserRole!=null){
                        User log = userDao.findById(caUserRole.getUser_id());
                        if(raUserIterator.hasNext()){
                            sb.append("{");
                            sb.append("id:").append("'").append(log.getId()).append("'").append(",");

                            if(null==log.getCn())
                                sb.append("cn:").append("'").append("").append("'").append(",");
                            else
                                sb.append("cn:").append("'").append(log.getCn()).append("'").append(",");

                            if(null==log.getSubject())
                                sb.append("subject:").append("'").append("").append("'").append(",");
                            else
                                sb.append("subject:").append("'").append(log.getSubject()).append("'").append(",");

                            sb.append("dynamic_ip:").append("'").append(log.getDynamic_ip()).append("'").append(",");

                            if(null==log.getStatic_ip())
                                sb.append("static_ip:").append("'").append("").append("'").append(",");
                            else
                                sb.append("static_ip:").append("'").append(log.getStatic_ip()).append("'").append(",");

                            sb.append("allow_all_client:").append("'").append(log.getAllow_all_client()).append("'").append(",");

                            if(null==log.getSerial_number())
                                sb.append("serial_number:").append("'").append("").append("'").append(",");
                            else
                                sb.append("serial_number:").append("'").append(log.getSerial_number()).append("'").append(",");

                            sb.append("enabled:").append("'").append(log.getEnabled()).append("'").append(",");

                            if(null==log.getReal_address())
                                sb.append("real_address:").append("'").append("").append("'").append(",");
                            else
                                sb.append("real_address:").append("'").append(log.getReal_address()).append("'").append(",");

                            sb.append("byte_received:").append("'").append(log.getByte_received()).append("'").append(",");
                            sb.append("byte_send:").append("'").append(log.getByte_send()).append("'").append(",");

                            sb.append("connected_since:").append("'").append(log.getConnected_since()).append("'").append(",");

                            if(null==log.getVirtual_address())
                                sb.append("virtual_address:").append("'").append("").append("'").append(",");
                            else
                                sb.append("virtual_address:").append("'").append(log.getVirtual_address()).append("'").append(",");


                            if(null==log.getNet_id())
                                sb.append("net_id:").append("'").append("").append("'").append(",");
                            else
                                sb.append("net_id:").append("'").append(log.getNet_id()).append("'").append(",");

                            if(null==log.getTerminal_id())
                                sb.append("terminal_id:").append("'").append("").append("'").append(",");
                            else
                                sb.append("terminal_id:").append("'").append(log.getTerminal_id()).append("'").append(",");

                            if(null==log.getDescription())
                                sb.append("description:").append("'").append("").append("'").append(",");
                            else
                                sb.append("description:").append("'").append(log.getDescription()).append("'").append(",");

                            sb.append("view_flag:").append("'").append(log.getView_flag()).append("'").append(",");
                            sb.append("gps_flag:").append("'").append(log.getGps_flag()).append("'").append(",");
//                            sb.append("level:").append("'").append(log.getLevel()).append("'").append(",");
                            sb.append("last_ref:").append("'").append(log.getLast_ref()).append("'");
                            sb.append("}");
                            sb.append(",");
                        } else {
                            sb.append("{");
                            sb.append("id:").append("'").append(log.getId()).append("'").append(",");

                            if(null==log.getCn())
                                sb.append("cn:").append("'").append("").append("'").append(",");
                            else
                                sb.append("cn:").append("'").append(log.getCn()).append("'").append(",");

                            if(null==log.getSubject())
                                sb.append("subject:").append("'").append("").append("'").append(",");
                            else
                                sb.append("subject:").append("'").append(log.getSubject()).append("'").append(",");

                            sb.append("dynamic_ip:").append("'").append(log.getDynamic_ip()).append("'").append(",");

                            if(null==log.getStatic_ip())
                                sb.append("static_ip:").append("'").append("").append("'").append(",");
                            else
                                sb.append("static_ip:").append("'").append(log.getStatic_ip()).append("'").append(",");

                            sb.append("allow_all_client:").append("'").append(log.getAllow_all_client()).append("'").append(",");

                            if(null==log.getSerial_number())
                                sb.append("serial_number:").append("'").append("").append("'").append(",");
                            else
                                sb.append("serial_number:").append("'").append(log.getSerial_number()).append("'").append(",");

                            sb.append("enabled:").append("'").append(log.getEnabled()).append("'").append(",");

                            if(null==log.getReal_address())
                                sb.append("real_address:").append("'").append("").append("'").append(",");
                            else
                                sb.append("real_address:").append("'").append(log.getReal_address()).append("'").append(",");

                            sb.append("byte_received:").append("'").append(log.getByte_received()).append("'").append(",");
                            sb.append("byte_send:").append("'").append(log.getByte_send()).append("'").append(",");

                            sb.append("connected_since:").append("'").append(log.getConnected_since()).append("'").append(",");

                            if(null==log.getVirtual_address())
                                sb.append("virtual_address:").append("'").append("").append("'").append(",");
                            else
                                sb.append("virtual_address:").append("'").append(log.getVirtual_address()).append("'").append(",");


                            if(null==log.getNet_id())
                                sb.append("net_id:").append("'").append("").append("'").append(",");
                            else
                                sb.append("net_id:").append("'").append(log.getNet_id()).append("'").append(",");

                            if(null==log.getTerminal_id())
                                sb.append("terminal_id:").append("'").append("").append("'").append(",");
                            else
                                sb.append("terminal_id:").append("'").append(log.getTerminal_id()).append("'").append(",");

                            if(null==log.getDescription())
                                sb.append("description:").append("'").append("").append("'").append(",");
                            else
                                sb.append("description:").append("'").append(log.getDescription()).append("'").append(",");

                            sb.append("view_flag:").append("'").append(log.getView_flag()).append("'").append(",");
                            sb.append("gps_flag:").append("'").append(log.getGps_flag()).append("'").append(",");
//                            sb.append("level:").append("'").append(log.getLevel()).append("'").append(",");
                            sb.append("last_ref:").append("'").append(log.getLast_ref()).append("'");
                            sb.append("}");
                        }
                    }
                }
            }
            sb.append("]}");
        }
        return sb.toString();
    }

    @Override
    public void addUsersToRoleId(String uIds, int roleId) throws Exception {
        userGroupDao.addUsersToRoleId(uIds,roleId);
    }

    @Override
    public void addUserToRoleId(int uId, int roleId) throws Exception {
        userGroupDao.addUserToRoleId(uId,roleId);
    }

    @Override
    public PageResult findCaUserByOtherRoleId(int start, int limit) {
        return userGroupDao.findCaUserByOtherRoleId(start,limit);
    }

    @Override
    public boolean delAllByRoleId(int roleId) throws Exception {
        return userGroupDao.delAllByRoleId(roleId);
    }

    @Override
    public void delByRoleIdAndUserId(int i, int i1)throws Exception {
        userGroupDao.delByRoleIdAndUserId(i,i1);
    }

    @Override
    public void delByUserId(int i) throws Exception {
        userGroupDao.delByUserId(i);
    }

    @Override
    public List<UserGroup> findByUserId(int id) throws Exception {
        return userGroupDao.findByUserId(id);
    }
}
