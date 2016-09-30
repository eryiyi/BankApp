package com.ruiping.BankApp.entiy;

import java.io.Serializable;

/**
 * 计划报告评论表 bank_job_report_comment
 * @author 刘柯
 *
 */
public class BankJobReportCommentBean implements Serializable {
	private String reportCommentId;//计划评论表ID 32位 uuid
	private String reportId;//计划报告表id 32 位uuid
	private String empId;// 会员信息ID 32 位uuid
	private String  commentCont;// 评论内容 200字以内
 	private String commentFile; // 附件路径 10个以内
	private String dateLine;// 时间戳 毫秒值 
	private String commentId;//评论id
	private String count;//
	private BankJobReport bankJobReport;
	private BankJobReportCommentBean  JobReportComment;
	private BankEmpBean bankEmp;
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public BankJobReportCommentBean getJobReportComment() {
		return JobReportComment;
	}
	public void setJobReportComment(BankJobReportCommentBean jobReportComment) {
		JobReportComment = jobReportComment;
	}
	public BankJobReport getBankJobReport() {
		return bankJobReport;
	}
	public void setBankJobReport(BankJobReport bankJobReport) {
		this.bankJobReport = bankJobReport;
	}
	public BankEmpBean getBankEmp() {
		return bankEmp;
	}
	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}
	public String getReportCommentId() {
		return reportCommentId;
	}
	public void setReportCommentId(String reportCommentId) {
		this.reportCommentId = reportCommentId;
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
	public String getCommentFile() {
		return commentFile;
	}
	public void setCommentFile(String commentFile) {
		this.commentFile = commentFile;
	}
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	
	
}
