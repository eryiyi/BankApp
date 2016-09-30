package com.ruiping.BankApp.entiy;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * 计划报告表 bank_job_report
 * @author 刘柯
 *
 */
public class BankJobReport implements Serializable {
	private String reportId;// 计划表id 32位uuid
	private String empId;//会员id 32位 uuid
	private String empName;
	private String reportType;// 类型 2：周报 3：月报 4：季报 5:年中  6：年度
	private String reportTitle;//工作成效
	private String reportCont;// 总结心得
	private String reportNext;//  下周工作计划
	private String reportFile;//附件    10个以内
	private String reportNumber;// 第几周/第几月
	private String dateStartEnd;//这周的开始和结束时间 08.03-08.09
	private String isUse;// 是否可以 0：可用 1：禁用
	private String dateLine;// 时间戳 毫秒值
	private int status;//报告说明 ：1 ：正常报告 2：补交报告
	private String createDate;//创建时间
	private String date;
	private String months;//月
	private String quarters;//季
	private String weeks;//周
	private int commentCount;//评论数
	private int doneCount;//查看数
	private int reportCount;
	private int pagecurrent;
	private int contion;
	private BankEmpBean bankEmp;
	private BankEmpBean bankEmp1;
	private ArrayList<BankJobReportCommentBean> bankJobReportCommentList;
	private ArrayList<BankJobReportDoneBean> bankJobReportDoneList;

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

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportCont() {
		return reportCont;
	}

	public void setReportCont(String reportCont) {
		this.reportCont = reportCont;
	}

	public String getReportNext() {
		return reportNext;
	}

	public void setReportNext(String reportNext) {
		this.reportNext = reportNext;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	public String getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	public String getDateStartEnd() {
		return dateStartEnd;
	}

	public void setDateStartEnd(String dateStartEnd) {
		this.dateStartEnd = dateStartEnd;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getDateLine() {
		return dateLine;
	}

	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public String getQuarters() {
		return quarters;
	}

	public void setQuarters(String quarters) {
		this.quarters = quarters;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(int doneCount) {
		this.doneCount = doneCount;
	}

	public int getReportCount() {
		return reportCount;
	}

	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}

	public int getPagecurrent() {
		return pagecurrent;
	}

	public void setPagecurrent(int pagecurrent) {
		this.pagecurrent = pagecurrent;
	}

	public int getContion() {
		return contion;
	}

	public void setContion(int contion) {
		this.contion = contion;
	}

	public BankEmpBean getBankEmp() {
		return bankEmp;
	}

	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}

	public BankEmpBean getBankEmp1() {
		return bankEmp1;
	}

	public void setBankEmp1(BankEmpBean bankEmp1) {
		this.bankEmp1 = bankEmp1;
	}

	public ArrayList<BankJobReportCommentBean> getBankJobReportCommentList() {
		return bankJobReportCommentList;
	}

	public void setBankJobReportCommentList(ArrayList<BankJobReportCommentBean> bankJobReportCommentList) {
		this.bankJobReportCommentList = bankJobReportCommentList;
	}

	public ArrayList<BankJobReportDoneBean> getBankJobReportDoneList() {
		return bankJobReportDoneList;
	}

	public void setBankJobReportDoneList(ArrayList<BankJobReportDoneBean> bankJobReportDoneList) {
		this.bankJobReportDoneList = bankJobReportDoneList;
	}
}
