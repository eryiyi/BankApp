package com.ruiping.BankApp.entiy;

import java.io.Serializable;

/**
 * 
 * @author 刘柯
 * 工作任务操作表 bank_job_task_done
 *
 */
public class BankJobTaskDoneBean implements Serializable {
	private String taskDoneId;//操作表id 32位 uuid
	private String taskId;// 任务表id 32 位 uuid
	private String empId;// 会员表id 32 位uuid
	private String  commentCont;// 操作内容  100字以内
	private String dateLine;//时间戳 毫秒值
	private int flag;//标识
	private BankJobTask bankJobTask;
	private BankEmpBean bankEmp;
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

	
	
	public String getTaskDoneId() {
		return taskDoneId;
	}
	public void setTaskDoneId(String taskDoneId) {
		this.taskDoneId = taskDoneId;
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
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
	
	

}
