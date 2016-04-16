package com.hzih.sslvpn.domain;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 操作权限
 * 
 * @hibernate.class table="permission"
 */
public class Permission {

	/**
	 * @hibernate.id column="id" generator-class="increment" type="long"
	 */
	Long id;

	/**
	 * 编码
	 * 
	 * @hibernate.property column="code" type="string" length="50"
	 */
	String code;

	/**
	 * 名称
	 * 
	 * @hibernate.property column="name" type="string" length="30"
	 */
	String name;

	/**
	 * 描述
	 * 
	 * @hibernate.property column="description" type="string"
	 */
	String description;

	/**
	 * 上级权限
	 * 
	 * @hibernate.property column="parent_id" type="long"
	 */
	Long parentId;

	/**
	 * 排序，默认为1
	 * 
	 * @hibernate.property column="seq" type="int"
	 */
	int order;

	/**
	 * 角色
	 * 
	 * @hibernate.set cascade="none" table="role_permission" lazy="false"
	 * @hibernate.key column="permission_id"
	 * @hibernate.many-to-many column="role_id"
	 *                         class="com.hzjava.monitorcenter.domain.Role"
	 */
	Set<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Permission() {

	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Permission)) {
			return false;
		}
		Permission rhs = (Permission) object;
		return new EqualsBuilder().append(this.id, rhs.id).isEquals();
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-580636485, -1010118751).append(this.id)
				.toHashCode();
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
