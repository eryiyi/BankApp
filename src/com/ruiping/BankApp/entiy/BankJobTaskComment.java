package com.ruiping.BankApp.entiy;

import java.io.Serializable;

public class BankJobTaskComment implements Serializable {


	private String taskCommentId;//工作任务评论表id 32位 uuid
	private String taskId;//任务表id 32 位uuid
	private String empId;//会员id 评论者 32 位 uuid
	private String commentCont;//评论内容 200字以内
	private String commentFile;//附件路径 10个以内
	private String dateLine;//时间戳  毫秒值
	private BankJobTask bankJobTask;
	private BankEmpBean bankEmp;
	private String commentId;
	private BankJobTaskComment bankJobTaskComment;
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
	public String getTaskCommentId() {
		return taskCommentId;
	}
	public void setTaskCommentId(String taskCommentId) {
		this.taskCommentId = taskCommentId;
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
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public BankJobTaskComment getBankJobTaskComment() {
		return bankJobTaskComment;
	}
	public void setBankJobTaskComment(BankJobTaskComment bankJobTaskComment) {
		this.bankJobTaskComment = bankJobTaskComment;
	}




}
