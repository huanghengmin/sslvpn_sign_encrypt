package com.hzih.sslvpn.domain;

import java.util.Date;

/**
 * 用户操作审计表
 * 
 * @author collin.code@gmail.com
 * @hibernate.class table="user_oper_log"
 */
public class UserOperLog {
	/**
	 * @hibernate.id column="Id" generator-class="identity"
	 *               type="java.lang.Long"
	 */
	Long id;

	/**
	 * 审计时间
	 * 
	 * @hibernate.property column="log_time" type="java.util.Date"
	 */
	Date logTime;

	/**
	 * 日志级别
	 * 
	 * @hibernate.property column="level" type="java.lang.String"
	 */
	String level;

	/**
	 * 用户名
	 * 
	 * @hibernate.property column="username" type="java.lang.String"
	 */
	String userName;

	/**
	 * 审计位置
	 * 
	 * @hibernate.property column="audit_module" type="java.lang.String"
	 */
	String auditModule;

	/**
	 * 审计内容
	 * 
	 * @hibernate.property column="audit_info" type="java.lang.String"
	 */
	String auditInfo;

	public UserOperLog() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAuditModule() {
		return auditModule;
	}

	public void setAuditModule(String auditModule) {
		this.auditModule = auditModule;
	}

	public String getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo;
	}
}
