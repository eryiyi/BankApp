package com.ruiping.BankApp.entiy;

public class BankTaskShareBean {
	private String taskShareId;
	private String taskid;
	private String empid;
	private int flag;
	private String recordDate;
	private String name;
	private BankJobTask bankJobTask;
	private BankEmpBean bankEmp;
	public String getTaskShareId() {
		return taskShareId;
	}
	public void setTaskShareId(String taskShareId) {
		this.taskShareId = taskShareId;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
}
