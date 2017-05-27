package com.ruiping.BankApp.entiy;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * 工作表  bank_job_task
 * @author 刘柯
 *
 */
public class BankJobTask implements Serializable{
	private String taskId;//工作表id 32 位 uuid
	private String empIdZf;//主负责人ID
	private String empId;//会员id 创建者 32 位uuid
	private String empName;//创建人
	private String empIdF;//会员id 负责人 32位uuid
	private String empFName;//负责人
	private String joinEmpId;//参与人
	private String shareEmpId;
	private String shareEmpName;
	private String shareFlags;
	private String reason;
	private String isExceed;//0未超期 1超期
	private String taskTitle;//任务标题
	private String taskCont;//任务内容
	private String taskFile;//任务附件 10个内
	private String taskType;//任务分类
	private String dateLineEnd; // 到期日期
	private String taskProgress;// 进度拜	private char isType;//是否王城 0 否 2 
	private char isUse;//是否可用 0 可用 1 不可用
	private String isType;// 是否完成 0 否1 是
	private String dateLine;
	private String recordDate;//录入当前日期
	private String finishDate;//完成日期
	private String taskRemindDay1;//到期日期前几天 
	private char isEmpOne1;// 是否提醒创建者 0 否 1 是 
	private char isEmpTwo1;//是否提醒负责人 0 否 1 是
	private char isEmpthree1;// 是否提醒参与人 0 否 1 是
	private String remindId;
	BankJobTaskRemind bankJobTaskRemind;
	private String pid;// 父类  0 为主任务，不为0时为子任务
	private int status;
	private int taskcount;
	private String riqi;
	private int num;
	private String delDate;//删除时间
	private String delName;//删除人
	private BankEmpBean bankEmp;//创建人
	private BankEmpBean bankEmpf;//负责人
	private BankEmpBean bankEmpZf;//主任务负责人

	private ArrayList<BankJobTaskComment> bankJobTaskCommentList;
	private ArrayList<BankJobTaskDoneBean> bankJobTaskDoneList;
	private ArrayList<BankJobTaskRemind> bankJobTaskRemindList;
	private ArrayList<BankJobTaskEmp> bankJobTaskEmpList;
	//执行任务完成情况相关属性
	private int total;
	private int donerate;
	private int doingtask;
	private int donetask;
	private int delaytask;
	private int normaltask;
	private int urgenttask;
	private int expecturgenttask;
	private int newtask;
	private int mytask;
	private int isshare;//是否共享


	public BankEmpBean getBankEmpZf() {
		return bankEmpZf;
	}

	public void setBankEmpZf(BankEmpBean bankEmpZf) {
		this.bankEmpZf = bankEmpZf;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BankJobTaskRemind getBankJobTaskRemind() {
		return bankJobTaskRemind;
	}
	public void setBankJobTaskRemind(BankJobTaskRemind bankJobTaskRemind) {
		this.bankJobTaskRemind = bankJobTaskRemind;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getTaskCont() {
		return taskCont;
	}

	public BankEmpBean getBankEmp() {
		return bankEmp;
	}
	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}
	public BankEmpBean getBankEmpf() {
		return bankEmpf;
	}
	public void setBankEmpf(BankEmpBean bankEmpf) {
		this.bankEmpf = bankEmpf;
	}
	public ArrayList<BankJobTaskComment> getBankJobTaskCommentList() {
		return bankJobTaskCommentList;
	}
	public void setBankJobTaskCommentList(
			ArrayList<BankJobTaskComment> bankJobTaskCommentList) {
		this.bankJobTaskCommentList = bankJobTaskCommentList;
	}
	public ArrayList<BankJobTaskDoneBean> getBankJobTaskDoneList() {
		return bankJobTaskDoneList;
	}
	public void setBankJobTaskDoneList(
			ArrayList<BankJobTaskDoneBean> bankJobTaskDoneList) {
		this.bankJobTaskDoneList = bankJobTaskDoneList;
	}
	public ArrayList<BankJobTaskRemind> getBankJobTaskRemindList() {
		return bankJobTaskRemindList;
	}
	public void setBankJobTaskRemindList(
			ArrayList<BankJobTaskRemind> bankJobTaskRemindList) {
		this.bankJobTaskRemindList = bankJobTaskRemindList;
	}
	public ArrayList<BankJobTaskEmp> getBankJobTaskEmpList() {
		return bankJobTaskEmpList;
	}
	public void setBankJobTaskEmpList(ArrayList<BankJobTaskEmp> bankJobTaskEmpList) {
		this.bankJobTaskEmpList = bankJobTaskEmpList;
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
	public String getEmpIdF() {
		return empIdF;
	}
	public void setEmpIdF(String empIdF) {
		this.empIdF = empIdF;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	
	public String getTaskFile() {
		return taskFile;
	}
	public void setTaskFile(String taskFile) {
		this.taskFile = taskFile;
	}
	
	public String getDateLineEnd() {
		return dateLineEnd;
	}
	public void setDateLineEnd(String dateLineEnd) {
		this.dateLineEnd = dateLineEnd;
	}
	public String getTaskProgress() {
		return taskProgress;
	}
	public void setTaskProgress(String taskProgress) {
		this.taskProgress = taskProgress;
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
	public void setTaskCont(String taskCont) {
		this.taskCont = taskCont;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpFName() {
		return empFName;
	}
	public void setEmpFName(String empFName) {
		this.empFName = empFName;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getIsType() {
		return isType;
	}
	public void setIsType(String isType) {
		this.isType = isType;
	}
	
	public String getJoinEmpId() {
		return joinEmpId;
	}
	public void setJoinEmpId(String joinEmpId) {
		this.joinEmpId = joinEmpId;
	}
	
	public char getIsEmpOne1() {
		return isEmpOne1;
	}
	public void setIsEmpOne1(char isEmpOne1) {
		this.isEmpOne1 = isEmpOne1;
	}
	public char getIsEmpTwo1() {
		return isEmpTwo1;
	}
	public void setIsEmpTwo1(char isEmpTwo1) {
		this.isEmpTwo1 = isEmpTwo1;
	}
	public char getIsEmpthree1() {
		return isEmpthree1;
	}
	public void setIsEmpthree1(char isEmpthree1) {
		this.isEmpthree1 = isEmpthree1;
	}
	public void setTaskRemindDay1(String taskRemindDay1) {
		this.taskRemindDay1 = taskRemindDay1;
	}
	public String getTaskRemindDay1() {
		return taskRemindDay1;
	}
	public String getRemindId() {
		return remindId;
	}
	public void setRemindId(String remindId) {
		this.remindId = remindId;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public int getTaskcount() {
		return taskcount;
	}
	public void setTaskcount(int taskcount) {
		this.taskcount = taskcount;
	}
	public String getRiqi() {
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getDonerate() {
		return donerate;
	}
	public void setDonerate(int donerate) {
		this.donerate = donerate;
	}
	public int getDoingtask() {
		return doingtask;
	}
	public void setDoingtask(int doingtask) {
		this.doingtask = doingtask;
	}
	public int getDonetask() {
		return donetask;
	}
	public void setDonetask(int donetask) {
		this.donetask = donetask;
	}
	public int getDelaytask() {
		return delaytask;
	}
	public void setDelaytask(int delaytask) {
		this.delaytask = delaytask;
	}
	public int getNormaltask() {
		return normaltask;
	}
	public void setNormaltask(int normaltask) {
		this.normaltask = normaltask;
	}
	public int getUrgenttask() {
		return urgenttask;
	}
	public void setUrgenttask(int urgenttask) {
		this.urgenttask = urgenttask;
	}
	public int getExpecturgenttask() {
		return expecturgenttask;
	}
	public void setExpecturgenttask(int expecturgenttask) {
		this.expecturgenttask = expecturgenttask;
	}
	public int getNewtask() {
		return newtask;
	}
	public void setNewtask(int newtask) {
		this.newtask = newtask;
	}
	public String getDelDate() {
		return delDate;
	}
	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}
	public String getDelName() {
		return delName;
	}
	public void setDelName(String delName) {
		this.delName = delName;
	}
	public int getIsshare() {
		return isshare;
	}
	public void setIsshare(int isshare) {
		this.isshare = isshare;
	}
	public String getShareEmpId() {
		return shareEmpId;
	}
	public void setShareEmpId(String shareEmpId) {
		this.shareEmpId = shareEmpId;
	}
	public String getShareFlags() {
		return shareFlags;
	}
	public void setShareFlags(String shareFlags) {
		this.shareFlags = shareFlags;
	}
	public String getShareEmpName() {
		return shareEmpName;
	}
	public void setShareEmpName(String shareEmpName) {
		this.shareEmpName = shareEmpName;
	}
	public int getMytask() {
		return mytask;
	}
	public void setMytask(int mytask) {
		this.mytask = mytask;
	}

	public String getEmpIdZf() {
		return empIdZf;
	}

	public void setEmpIdZf(String empIdZf) {
		this.empIdZf = empIdZf;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getIsExceed() {
		return isExceed;
	}

	public void setIsExceed(String isExceed) {
		this.isExceed = isExceed;
	}
}
