package com.ruiping.BankApp.entiy;

import java.io.Serializable;

public class BankJobTaskEmp implements Serializable {
	private String taskEmpId;//任务参与人员表id 32 位uuid
	private String taskId;//任务表id 32 位uuid
	private String empId;//会员id 32 位uuid
	private char isRead;//是否已经阅读 0 否 1  是 
	private String  dateLineRead;// 时间戳 阅读时间
	private String dateLine;//时间戳 毫秒值
	
	private int taskCount;//任务数统计
	private int empCount;//参与人数统计
	
	private BankJobTask bankJobTask;
	private BankEmpBean bankEmp;
	
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public int getEmpCount() {
		return empCount;
	}
	public void setEmpCount(int empCount) {
		this.empCount = empCount;
	}
	
	
	public BankJobTask getBankJobTask() {
		return bankJobTask;
	}
	public void setBankJobTask(BankJobTask bankJobTask) {
		this.bankJobTask = bankJobTask;
	}
	public BankEmpBean getBankEmp() {
		return bankEmp;
	}
	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}
	public String getTaskEmpId() {
		return taskEmpId;
	}
	public void setTaskEmpId(String taskEmpId) {
		this.taskEmpId = taskEmpId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public char getIsRead() {
		return isRead;
	}
	public void setIsRead(char isRead) {
		this.isRead = isRead;
	}
	public String getDateLineRead() {
		return dateLineRead;
	}
	public void setDateLineRead(String dateLineRead) {
		this.dateLineRead = dateLineRead;
	}
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	
	

}
