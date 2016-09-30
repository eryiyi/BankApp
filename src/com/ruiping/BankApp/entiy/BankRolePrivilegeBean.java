package com.ruiping.BankApp.entiy;
/**
 * 角色权限表 bank_roleprivilege
 * @author liuke
 *
 */

public class BankRolePrivilegeBean {
	private int releId;
	private int privilegeId;
	private BankRoleBean bankRoleBean;
	private BankPrivilegeBean bankPrivilegeBean;
	public int getReleId() {
		return releId;
	}
	public void setReleId(int releId) {
		this.releId = releId;
	}
	public int getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}
	public BankRoleBean getBankRoleBean() {
		return bankRoleBean;
	}
	public void setBankRoleBean(BankRoleBean bankRoleBean) {
		this.bankRoleBean = bankRoleBean;
	}
	public BankPrivilegeBean getBankPrivilegeBean() {
		return bankPrivilegeBean;
	}
	public void setBankPrivilegeBean(BankPrivilegeBean bankPrivilegeBean) {
		this.bankPrivilegeBean = bankPrivilegeBean;
	}
	
	
	
}
