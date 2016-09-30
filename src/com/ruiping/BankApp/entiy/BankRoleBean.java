package com.ruiping.BankApp.entiy;

import java.util.ArrayList;

/**
 * 角色表 bank_role
 * @author liuke
 *
 */
public class BankRoleBean {
	private String id;//id 主键 自增
	private String name;// 角色名称 
	private int roleClass;//角色分类
	private String  description;// 描述
	private ArrayList<BankPrivilegeBean> bankPrivilegeList;
	private ArrayList<BankEmpBean> banEmpList;
	
	public ArrayList<BankPrivilegeBean> getBankPrivilegeList() {
		return bankPrivilegeList;
	}
	public void setBankPrivilegeList(ArrayList<BankPrivilegeBean> bankPrivilegeList) {
		this.bankPrivilegeList = bankPrivilegeList;
	}
	public ArrayList<BankEmpBean> getBanEmpList() {
		return banEmpList;
	}
	public void setBanEmpList(ArrayList<BankEmpBean> banEmpList) {
		this.banEmpList = banEmpList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public int getRoleClass() {
		return roleClass;
	}
	public void setRoleClass(int roleClass) {
		this.roleClass = roleClass;
	}
	
	
}
