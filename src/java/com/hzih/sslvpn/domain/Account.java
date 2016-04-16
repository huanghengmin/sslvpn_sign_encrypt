package com.hzih.sslvpn.domain;

import java.util.Date;
import java.util.Set;

/**
 * 帐号，用户
 *
 * @author collin.code@gmail.com
 * @hibernate.class table="account"
 */
public class Account {
	/**
	 *
	 */
	private String lastMsg;
	/**
	 * @hibernate.id column="id" generator-class="increment" type="long"
	 *               length="11"
	 */
	Long id;

	/**
	 * 用户名
	 *
	 * @hibernate.property column="user_name" type="string" length="20"
	 */
	String userName;

	/**
	 * 密码
	 *
	 * @hibernate.property column="password" type="string" length="20"
	 */
	String password;

	/**
	 * 性别
	 *
	 * @hibernate.property column="sex" type="string" length="10"
	 */
	String sex;

	/**
	 * 角色
	 *
	 * @hibernate.set cascade="save-update" table="account_role" lazy="false"
	 * @hibernate.key column="account_id"
	 * @hibernate.many-to-many column="role_id"
	 *                         class="com.hzjava.monitorcenter.domain.Role"
	 */
	Set<Role> roles;

	/**
	 * 电话
	 *
	 * @hibernate.property column="phone" type="string" length="20"
	 */
	String phone;

	/**
	 * 创建时间
	 *
	 * @hibernate.property column="created_time" type="java.util.Date"
	 */
	Date createdTime;

	/**
	 * 更新时间
	 *
	 * @hibernate.property column="modified_time" type="java.util.Date"
	 */
	Date modifiedTime;

	/**
	 * @hibernate.property column="modifie_password_time" type="java.util.Date"
	 */
	Date modifiedPasswordTime;

	/**
	 * 状态，有效，無效
	 *
	 * @hibernate.property column="status" type="string" length="20"
	 */
	String status;

	/**
	 * 部门
	 *
	 * @hibernate.property column="depart" type="string" length="20"
	 */
	String depart;

	/**
	 * 职务
	 *
	 * @hibernate.property column="title" type="string" length="20"
	 */
	String title;

	/**
	 * 真实姓名
	 *
	 * @hibernate.property column="name" type="string" length="20"
	 */
	String name;

	/**
	 * 邮箱
	 *
	 * @hibernate.property column="email" type="string" length="30"
	 */
	String email;

	/**
	 * 开始IP
	 *
	 * @hibernate.property column="start_ip" type="string" length="20"
	 */
	String startIp;

	/**
	 * 结束IP
	 *
	 * @hibernate.property column="end_ip" type="string" length="20"
	 */
	String endIp;

	/**
	 * 开始时间
	 *
	 * @hibernate.property column="start_hour" type="int" length="2"
	 */
	int startHour;

	/**
	 * 结束时间
	 *
	 * @hibernate.property column="end_hour" type="int" length="2"
	 */
	int endHour;

	/**
	 * 描述
	 *
	 * @hibernate.property column="description" type="text"
	 */
	String description;

	/**
	 * 权限，当用户登录的时候初始化
	 */
	Set<Permission> permissions;

	/**
	 * 允许访问的IP地址
	 *
	 * @hibernate.property column="remote_ip" type="string"
	 */
	String remoteIp;

	String mac;

	/**
	 * 选择是IP段还是IP/MAC:true(1)选IP段 false(0)选IP/MAC
	 */
	int ipType;

	public Account() {
	}

	public String getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getIpType() {
		return ipType;
	}

	public void setIpType(int ipType) {
		this.ipType = ipType;
	}

	public Date getModifiedPasswordTime() {
		return modifiedPasswordTime;
	}

	public void setModifiedPasswordTime(Date modifiePasswordTime) {
		this.modifiedPasswordTime = modifiePasswordTime;
	}
}
