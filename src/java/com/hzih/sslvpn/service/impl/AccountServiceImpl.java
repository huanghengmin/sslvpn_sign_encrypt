package com.hzih.sslvpn.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.AccountDao;
import com.hzih.sslvpn.dao.RoleDao;
import com.hzih.sslvpn.domain.Account;
import com.hzih.sslvpn.domain.Role;
import com.hzih.sslvpn.service.AccountService;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-11
 * Time: 下午1:01
 * To change this template use File | Settings | File Templates.
 * 用户管理业务逻辑处理
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
    private RoleDao roleDao;

    /**
     * 分页查找用户，并以json形式返回
     * @param start   开始页
     * @param limit   每页大小
     * @return
     */
    public String select(String userName, String status,int start,int limit) {
        PageResult pageResult = accountDao.listByPage(userName,status,start/limit+1,limit);
        int total = pageResult.getAllResultsAmount();
        List<Account> accounts = pageResult.getResults();
        String json = "{success:true,total:" + total + ",rows:[";
        for (Account a : accounts) {
            Set<Role> roles = a.getRoles();
            Iterator<Role> iterator = roles.iterator();
            String roleName = null;
            Long roleId = null;
            while (iterator.hasNext()){
                Role role = iterator.next();
                roleName = role.getName();
                roleId = role.getId();
            }
            json +="{id:'"+a.getId()+"',name:'"+a.getName()+"',userName:'"+a.getUserName()+
                    "',status:'"+a.getStatus()+"',sex:'"+a.getSex()+"',description:'"+a.getDescription()+
                    "',password:'',depart:'"+a.getDepart()+"',ipType:"+ (a.getIpType()==1?true:false)+
                    ",startIp:'"+a.getStartIp()+"',endIp:'"+a.getEndIp()+
                    "',startHour:'"+a.getStartHour()+"',endHour:'"+a.getEndHour()+
                    "',phone:'"+a.getPhone()+"',remoteIp:'"+(a.getRemoteIp()==null?"":a.getRemoteIp())+
                    "',email:'"+a.getEmail()+"',desc:'"+a.getDescription()+"',mac:'"+(a.getMac()==null?"":a.getMac())+
                    "',roleId:'"+roleId+"',roleName:'"+roleName+"',title:'"+a.getTitle()+
                    "',createTime:'"+a.getCreatedTime()+"',modifiedTime:'"+a.getModifiedTime()+"'},";
        }
        json += "]}";
        return json;
    }

    /**
     * 更新用户信息，并以json形式返回处理结果
     * @param account  用户信息
     * @param rIds     用户对应角色ID
     * @return
     * @throws Exception
     */
    public String update(Account account,long[] rIds) throws Exception {
        Account old = (Account) accountDao.getById(account.getId());
        old.setPassword(account.getPassword());
        old.setDepart(account.getDepart());
        old.setDescription(account.getDescription());
        old.setEmail(account.getEmail());
        old.setEndHour(account.getEndHour());
        if(account.getIpType()==1){
            old.setStartIp(account.getStartIp());
            old.setEndIp(account.getEndIp());
        }else {
            old.setRemoteIp(account.getRemoteIp());
            old.setMac(account.getMac());
        }
        old.setModifiedTime(new Date());
        old.setName(account.getName());
        old.setPhone(account.getPhone());
        old.setSex(account.getSex());
        old.setStartHour(account.getStartHour());
        old.setStatus(account.getStatus());
        old.setTitle(account.getTitle());
        old.setUserName(account.getUserName());
        old.setIpType(account.getIpType());
        if(rIds.length > 0){
            Set<Role> roles = new HashSet<Role>();
            for (int i = 0; i < rIds.length; i++) {
                Role role = (Role) roleDao.getById(rIds[i]);
                roles.add(role);
            }
            old.setRoles(roles);
        }
        accountDao.update(old);
        return "<font color=\"green\">修改成功,点击[确定]返回列表!</font>";
    }

    /**
     * 删除用户信息，并以json形式返回处理结果
     * @param id   用户ID
     * @return
     * @throws Exception
     */
    public String delete(Long id) throws Exception {
        accountDao.delete(id);
        return "<font color=\"green\">删除成功,点击[确定]返回列表!</font>";
    }

    /**
     * 新增用户信息，并以json形式返回处理结果
     * @param account  新增用户信息
     * @param rIds     用户对应角色ID
     * @return
     * @throws Exception
     */
    public String insert(Account account, long[] rIds) throws Exception {
        Set<Role> roles = new HashSet<Role>();
        for (int i = 0; i < rIds.length; i++) {
            Role role = (Role) roleDao.getById(rIds[i]);
            roles.add(role);
        }
        account.setRoles(roles);
        accountDao.create(account);
        return "<font color=\"green\">保存成功,点击[确定]返回列表!</font>";
    }

    /**
     * 新增信息时，检查用户名是否已经使用，并以json形式返回处理结果
     * @param userName    用户名
     * @return
     */
    public String checkUserName(String userName) throws Exception{
        String msg = null;
        Account account = accountDao.findByName(userName);
        if(account!=null){
            msg = "用户名已经存在";
        } else {
            msg = "true";
        }
        return "{success:true,msg:'"+msg+"'}";
    }

    /**
     * 组织账号名成为一组 {key:'',value:''}  列表
     * @return
     * @throws Exception
     */
    public String selectUserNameKeyValue() throws Exception {
        List<Account> accounts = accountDao.findAll();
        int total = accounts.size();
        String json = "{success:true,total:"+total + ",rows:[";
        for (Account a : accounts) {
            json += "{key:'"+a.getUserName()+"',value:'"+a.getUserName()+"'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public Account getAccountById(long id) throws Exception {
        return accountDao.getAccountById(id);
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
