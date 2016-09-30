package com.ruiping.BankApp.entiy;

import java.io.Serializable;
import java.util.ArrayList;

public class BankGroupBean implements Serializable {
	
	private String groupId;//部门id 32位uuid 
	private String groupName;//部门名称 
	private char isUse ;// 是否可用 0 可用 1 不可用 
	private String dateLine;//时间戳  毫秒值
	private ArrayList<BankEmpBean> empList;//会员信息
	
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public ArrayList<BankEmpBean> getEmpList() {
		return empList;
	}
	public void setEmpList(ArrayList<BankEmpBean> empList) {
		this.empList = empList;
	}
	
	

}
