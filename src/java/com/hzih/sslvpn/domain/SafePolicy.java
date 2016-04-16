package com.hzih.sslvpn.domain;

/**
 * 安全策略
 * 
 * @author collin.code@gmail.com
 * @hibernate.class table="safe_policy"
 */
public class SafePolicy {
	/**
	 * @hibernate.id column="id" generator-class="increment" type="long"
	 */
	Long id;

	/**
	 * 用户登录超时时间，单位秒
	 * @hibernate.property name="timeout" type="int"
	 */
	int timeout;
	
	/**
	 * 密码长度
	 * @hibernate.property name="password_length" type="int"
	 */
	int passwordLength;
	
	/**
	 * 密码正则表达式限制
	 * @hibernate.property name="password_rules" type="string"
	 */
	String passwordRules;
	
	/**
	 * 用户登录最多失败次数
	 * @hibernate.property name="error_limit" type="int"
	 */
	int errorLimit;
	
	/**
	 * 是否启用远程登录限制，如果是，则启用IP过滤。
	 * @hibernate.property name="remote_disabled" type="boolean"
	 */
	boolean remoteDisabled;

    /**
     * 是否启用MAC地址校验
     */
    boolean macDisabled;
    /**
     * 锁定时间(小时)
     */
    int lockTime;

	public SafePolicy(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getErrorLimit() {
		return errorLimit;
	}

	public void setErrorLimit(int errorLimit) {
		this.errorLimit = errorLimit;
	}

	public boolean getRemoteDisabled() {
		return remoteDisabled;
	}

	public void setRemoteDisabled(boolean remoteDisabled) {
		this.remoteDisabled = remoteDisabled;
	}

	public int getPasswordLength() {
		return passwordLength;
	}

	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public String getPasswordRules() {
		return passwordRules;
	}

	public void setPasswordRules(String passwordRules) {
		this.passwordRules = passwordRules;
	}

    public boolean getMacDisabled() {
        return macDisabled;
    }

    public void setMacDisabled(boolean macDisabled) {
        this.macDisabled = macDisabled;
    }

    public int getLockTime() {
        return lockTime;
    }

    public void setLockTime(int lockTime) {
        this.lockTime = lockTime;
    }

}
