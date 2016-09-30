package com.ruiping.BankApp.entiy;

import java.io.Serializable;

public class BankJobLogBean implements Serializable {
	
	private String jobLogId;//工作日报表id 32 位uuid
	private String empId;//会员id 创建者id 
	private String jobLogCon;// 日报内容
	private String jobLogPic;//图片 最大9张
	private String jobLogFile;//附件文件 最大20个
	private char isUse;//是否可用 0 可用 1 不可用
	private String dateLine;//时间戳 毫秒值
	private BankEmpBean bankEmp;
	
	
	public String getJobLogCon() {
		return jobLogCon;
	}
	public BankEmpBean getBankEmp() {
		return bankEmp;
	}
	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}
	public String getJobLogId() {
		return jobLogId;
	}
	public void setJobLogId(String jobLogId) {
		this.jobLogId = jobLogId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	
	public String getJobLogPic() {
		return jobLogPic;
	}
	public void setJobLogPic(String jobLogPic) {
		this.jobLogPic = jobLogPic;
	}
	public String getJobLogFile() {
		return jobLogFile;
	}
	public void setJobLogFile(String jobLogFile) {
		this.jobLogFile = jobLogFile;
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
	public void setJobLogCon(String jobLogCon) {
		this.jobLogCon = jobLogCon;
	}
	
	
	
	
	

}
