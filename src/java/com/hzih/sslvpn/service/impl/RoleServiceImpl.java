package com.hzih.sslvpn.service.impl;

import com.hzih.sslvpn.dao.PermissionDao;
import com.hzih.sslvpn.dao.RoleDao;
import com.hzih.sslvpn.domain.Permission;
import com.hzih.sslvpn.domain.Role;
import com.hzih.sslvpn.service.RoleService;
import com.hzih.sslvpn.utils.LicenseUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-6-8
 * Time: 下午12:23
 * To change this template use File | Settings | File Templates.
 * 角色管理、用户管理业务逻辑处理
 */
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;
    private PermissionDao permissionDao;

    public String formatDate(Date date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(date!=null){
            return  format.format(date);
        }
        return "";
    }

    /**
     * 分页查找--角色信息，并以json形式返回
     * @param start    开始页
     * @param limit    分页大小
     * @return
     * @throws Exception
     */
    public String select(int start, int limit) throws Exception{
        String json = "{success:true,total:";
        List<Role> roles = roleDao.findAll();
        int total = roles.size();
        json += total + ",rows:[";
        int index = 0;
        int count = 0;
        for (Role role : roles) {
            if(index == start && count < limit){
                json +="{id:'"+role.getId()+"',name:'"+role.getName()+"',description:'"+role.getDescription()+
                        "',createTime:'"+formatDate(role.getCreatedTime())+"',modifiedTime:'"+formatDate(role.getModifiedTime())+"'},";
                count ++;
                start ++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    /**
     * 新增角色时，权限信息
     * @return
     * @throws Exception
     * @param start
     * @param limit
     */
    public String getPermissionInsert(int start, int limit) throws Exception {
        List<Permission> permissions = permissionDao.findAll();
        boolean isExistLicense = false;                        //是否存在usb-key控制权限
        List<String> lps = new LicenseUtils().getNeedsLicenses(isExistLicense);
        List<Permission> licensePermissions = new ArrayList<Permission>();
        for (Permission p : permissions) {
            for (String str : lps) {
                if(str.equals(p.getCode())){
                    licensePermissions.add(p);
                }
            }
        }
        int index = 0;
        int count = 0;
        String json = "{success:true,total:"+licensePermissions.size()+",rows:[";
        for (Permission p : licensePermissions) {
            if(index == start && count < limit){
                Long parentId = p.getId();
                if(p.getParentId() == 0){
                    json +="{topName:'"+p.getName() +"',secondName:'',id:'"+p.getId()+"',parentId:'"+parentId+"',checked:false},";
                } else {
                    json +="{topName:'',secondName:'"+p.getName() +"',id:'"+p.getId()+"',parentId:'"+parentId+"',checked:false},";
                }
                count ++;
                start ++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    /**
     *   更新角色时，权限信息
     * @param id    角色对应ID
     * @return
     * @throws Exception
     */
    public String getPermissionUpdate(int start, int limit,long id) throws Exception {
        List<Permission> permissions = permissionDao.findAll();
        boolean isExistLicense = false;              //是否存在usb-key控制权限
        List<String> lps = new LicenseUtils().getNeedsLicenses(isExistLicense);
        List<Permission> licensePermissions = new ArrayList<Permission>();
        for (Permission p : permissions) {
            for (String str : lps) {
                if(str.equals(p.getCode())){
                    licensePermissions.add(p);
                }
            }
        }
        int index = 0;
        int count = 0;
        String json = "{success:true,total:"+licensePermissions.size()+",rows:[";
        for (Permission p : licensePermissions) {
            if(index == start && count < limit){
                Long parentId = p.getId();
                boolean isChecked = check(p,id);    //判断是否存在权限，true表示存在，在界面上就表示改权限需要打钩
                if(p.getParentId() == 0){
                    json +="{topName:'"+p.getName() +"',secondName:'',id:'"+p.getId()+"',parentId:'"+parentId+"',checked:"+isChecked+"},";
                } else {
                    json +="{topName:'',secondName:'"+p.getName() +"',id:'"+p.getId()+"',parentId:'"+parentId+"',checked:"+isChecked+"},";
                }
                count ++;
                start ++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    /**
     * 判断id对应角色中是否存在permission
     * @param permission  一个权限
     * @param id         角色ID
     * @return     true表示存在，false表示不存在
     */
    private boolean check(Permission permission, long id) {
        Set<Role> roles = permission.getRoles();
        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            if(role.getId() == id){
                return true;
            }
        }
        return false;
    }

    /**
     * 组织成一对的信息，用于界面上combox数据
     * @return
     * @throws Exception
     */
    public String getNameKeyValue() throws Exception{
        String json = "{success:true,total:";
        List<Role> roles = roleDao.findAll();
        int total = roles.size();
        json += total + ",rows:[";
        for (Role role : roles) {
            json +="{value:'"+role.getId()+"',key:'"+role.getName()+"'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public String checkRoleName(String name) throws Exception {
        String msg = null;
        Role role = roleDao.findByName(name);
        if(role!=null){
            msg = "角色名已经存在";
        } else {
            msg = "true";
        }
        return "{success:true,msg:'"+msg+"'}";
    }

    /**
     *  新增角色，并以json形式返回
     * @param role    选择角色信息
     * @param pIds     对应权限ID
     * @return
     * @throws Exception
     */
    public String insert(Role role, String[] pIds) throws Exception{
        Set<Permission> permissions = new HashSet<Permission>();
        List<Long> parentIds = new ArrayList<Long>();
        for ( int i = 0; i < pIds.length; i ++){
            Permission p = (Permission) permissionDao.getById(Long.parseLong(pIds[i]));
            if(p.getParentId()>0 ) {           //罗列一级权限ID,用于防止[存在二级权限时不存在一级权限的情况]发生
                parentIds.add(p.getParentId());
            }
            permissions.add(p);
        }
        boolean isExist = false; //表示一级权限不存在
        for(Long id : parentIds){
            isExist = isExist(id,permissions);
            if(!isExist){
                Permission p = (Permission) permissionDao.getById(id);
                permissions.add(p);
            }
        }
        role.setPermissions(permissions);
        roleDao.create(role);
        return "{success:true,msg:'新增成功,点击[确定]返回列表!'}";
    }

    /**
     * 是否存在 id对应的权限
     * @param id         权限ID
     * @param permissions  角色所有权限
     * @return    true表示存在,false表示不存在
     */
    private boolean isExist(Long id, Set<Permission> permissions) {
        Iterator<Permission> iterator = permissions.iterator();
        while (iterator.hasNext()){
            Permission p = iterator.next();
            if(id.equals(p.getId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 删除角色,并以json形式返回
     * @param id    角色ID
     * @return
     * @throws Exception
     */
    public String delete(long id) throws Exception {
        roleDao.delete(id);
        return "{success:true,msg:'删除成功,点击[确定]返回列表!'}";
    }

    /**
     * 更新角色,并以json形式返回:
     * 如果二级权限存在的同时对应的一级权限不存在,则需要自动添加对应的一级权限;
     * @param role  更新角色信息
     * @param pIds  角色对应的权限ID
     * @return
     * @throws Exception
     */
    public String update(Role role, String[] pIds) throws Exception {
        Role old = (Role) roleDao.getById(role.getId());
        old.setName(role.getName());
        old.setDescription(role.getDescription());
        old.setModifiedTime(new Date());
        List<Long> parentIds = new ArrayList<Long>();
        Set<Permission> permissions = new HashSet<Permission>();
        for (int i = 0; i < pIds.length; i ++){
            Permission p = (Permission) permissionDao.getById(Long.parseLong(pIds[i]));
            if(p.getParentId()>0 ) {           //罗列一级权限ID,用于防止[存在二级权限时不存在一级权限的情况]发生
                parentIds.add(p.getParentId());
            }
            permissions.add(p);
        }
        boolean isExist = false; //表示一级权限不存在
        for(Long id : parentIds){
            isExist = isExist(id,permissions);
            if(!isExist){
                Permission p = (Permission) permissionDao.getById(id);
                permissions.add(p);
            }
        }
        old.setPermissions(permissions);
        roleDao.update(old);
        return "{success:true,msg:'更新成功,点击[确定]返回列表!'}";
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public PermissionDao getPermissionDao() {
        return permissionDao;
    }

    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }
}
