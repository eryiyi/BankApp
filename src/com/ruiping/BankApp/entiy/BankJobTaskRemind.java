package com.ruiping.BankApp.entiy;

import java.io.Serializable;

/**
 * 
 * @author 刘柯 
 *任务提醒表 bank_job_task_remind
 */
public class BankJobTaskRemind implements Serializable {

	private String taskRemindId;//提醒表id 32位uuid 
	private String taskId;// 任务表id 32位
	private int taskRemindDay;//到期日期前几天 
	private char isEmpOne;// 是否提醒创建者 0 否 1 是 
	private char isEmpTwo;//是否提醒负责人 0 否 1 是
	private char isEmpthree;// 是否提醒参与人 0 否 1 是
	private String remindId;
	private String dateLine;// 时间戳  毫秒值
	
	private BankJobTask bankJobTask;
	
	public BankJobTask getBankJobTask() {
		return bankJobTask;
	}
	public void setBankJobTask(BankJobTask bankJobTask) {
		this.bankJobTask = bankJobTask;
	}
	public String getTaskRemindId() {
		return taskRemindId;
	}
	public void setTaskRemindId(String taskRemindId) {
		this.taskRemindId = taskRemindId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public int getTaskRemindDay() {
		return taskRemindDay;
	}
	public void setTaskRemindDay(int taskRemindDay) {
		this.taskRemindDay = taskRemindDay;
	}
	public char getIsEmpOne() {
		return isEmpOne;
	}
	public void setIsEmpOne(char isEmpOne) {
		this.isEmpOne = isEmpOne;
	}
	public char getIsEmpTwo() {
		return isEmpTwo;
	}
	public void setIsEmpTwo(char isEmpTwo) {
		this.isEmpTwo = isEmpTwo;
	}
	public char getIsEmpthree() {
		return isEmpthree;
	}
	public void setIsEmpthree(char isEmpthree) {
		this.isEmpthree = isEmpthree;
	}
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	public String getRemindId() {
		return remindId;
	}
	public void setRemindId(String remindId) {
		this.remindId = remindId;
	}
	
	
}
