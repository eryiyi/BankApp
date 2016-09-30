package com.ruiping.BankApp.entiy;

import java.io.Serializable;

/**
 * 计划报告操作表 bank_job_report_done
 * @author liuke
 *
 */

public class BankJobReportDoneBean implements Serializable {
	private String reportDoneId;
	private String reportId;
	private String empId;
	private String commentCont;
	private String dateLine;
	private int doneType;//操作类型 1：查询 2：添加 3：修改 4：删除
	private String count;
	private BankEmpBean bankEmp;
	private BankJobReport bankJobReport;
	
	
	
	
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public int getDoneType() {
		return doneType;
	}
	public void setDoneType(int i) {
		this.doneType = i;
	}
	public BankEmpBean getBankEmp() {
		return bankEmp;
	}
	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}
	public BankJobReport getBankJobReport() {
		return bankJobReport;
	}
	public void setBankJobReport(BankJobReport bankJobReport) {
		this.bankJobReport = bankJobReport;
	}
	public String getReportDoneId() {
		return reportDoneId;
	}
	public void setReportDoneId(String reportDoneId) {
		this.reportDoneId = reportDoneId;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getCommentCont() {
		return commentCont;
	}
	public void setCommentCont(String commentCont) {
		this.commentCont = commentCont;
	}
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	
	
	
	
}
