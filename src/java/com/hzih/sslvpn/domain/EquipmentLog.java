package com.hzih.sslvpn.domain;

import java.util.Date;

/**
 * 设备日志
 * 
 * @author collin.code@gmail.com
 * @hibernate.class table="equipment_log"
 */
public class EquipmentLog {
	/**
	 * @hibernate.id column="Id" generator-class="identity"
	 *               type="java.lang.Long"
	 */
	int id;

	/**
	 * 日志时间
	 * 
	 * @hibernate.property column="log_time" type="java.util.Date"
	 */
	Date log_time;

	/**
	 * 日志等级
	 * 
	 * @hibernate.property column="level" type="java.lang.String"
	 */
	String level;

	/**
	 * 设备名
	 * 
	 * @hibernate.property column="equipment_name" type="java.lang.String"
	 */
	String equipment_name;

	/**
	 * 日志内容
	 * 
	 * @hibernate.property column="log_info" type="java.lang.String"
	 */
	String log_info;

	boolean flag = true;

	public EquipmentLog() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getLog_time() {
		return log_time;
	}

	public void setLog_time(Date log_time) {
		this.log_time = log_time;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getEquipment_name() {
		return equipment_name;
	}

	public void setEquipment_name(String equipment_name) {
		this.equipment_name = equipment_name;
	}

	public String getLog_info() {
		return log_info;
	}

	public void setLog_info(String log_info) {
		this.log_info = log_info;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
