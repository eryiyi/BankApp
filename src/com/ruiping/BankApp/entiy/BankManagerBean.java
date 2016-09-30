package com.ruiping.BankApp.entiy;


public class BankManagerBean {
	
	private String managerId;//管理员id 32 位 uuid
	private String managerName ;// 用户名 
	private String managerPassword; // 用户密码 长度 6-18 位
	private String managerMobile; //用户手机号 唯一
	private String managerCover; //管理员头像 
	private char isUse ;//是否可用 0 可用 1 不可用
	private String dateLine;
	private int roleid;
	private String rolename;
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerPassword() {
		return managerPassword;
	}
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	public String getManagerMobile() {
		return managerMobile;
	}
	public void setManagerMobile(String managerMobile) {
		this.managerMobile = managerMobile;
	}
	
	public char getIsUse() {
		return isUse;
	}
	public void setIsUse(char isUse) {
		this.isUse = isUse;
	}
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	public void setManagerCover(String managerCover) {
		this.managerCover = managerCover;
	}
	public String getManagerCover() {
		return managerCover;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
	

}
