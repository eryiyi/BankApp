package com.ruiping.BankApp.entiy;

import java.io.Serializable;
import java.util.ArrayList;


public class BankEmpBean implements Serializable {
	private String empId;//会员id 32位uuid
	private String empMobile;//会员手机号   唯一
	private String newPhone;//新手机号
	private String empPassword;//会员密码 长度6-18位 
	private String newempPassword;
	private String empName;//会员真实姓名 
	private char empSex;//性别 0男 1女
	private String empIdUp;//上级领导id
	private String empCover;//会员头像
	private String isUse; //是否可用  0在职 1 禁用 2 离职
	private String isCheck; //是否审核 0 默认 1 是 2 不通过
	private String dateLine;//时间戳 毫秒值
	private String groupId;//部门id 32 为uuid
	private String pushId;//用户唯一标示
	private String deviceId;//用户设备类型；1：web 2：pc 3：Android 4：ios 5：wp
	private String channelId;//
	private String hx_name;//环信名称
	private String isMeeting;//1不允许 2允许

	public String getIsMeeting() {
		return isMeeting;
	}

	public void setIsMeeting(String isMeeting) {
		this.isMeeting = isMeeting;
	}

	private BankGroupBean bankGroup;
	private BankEmpBean bankemp;
	private int roleid;
	private String email;//邮箱
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	private ArrayList<BankJobLogBean> bankJobLogList;
	private ArrayList<BankJobReport> bankJobReportList;
	private ArrayList<BankJobReportCommentBean> bankJobReportCommentList;
	private ArrayList<BankJobReportDoneBean> bankJobReportDoneList;
	private ArrayList<BankJobTask> bankJobTaskList;//创建任务
	private ArrayList<BankJobTask> bankJobTaskfList;//负责任务
	private ArrayList<BankJobTaskComment> bankJobTaskCommentList;
	private ArrayList<BankJobTaskDoneBean> bankJobTaskDoneList;
	private ArrayList<BankJobTaskEmp> bankJobTaskEmpList;
	private ArrayList<BankRoleBean> bankRoleList;
	
	//查询相关统计数量
	private int tasknum;
	private int dayreport;
	private int weekreport;
	private int yearreport;
	private int loginnum;
	

	public String getPushId() {
		return pushId;
	}
	public void setPushId(String pushId) {
		this.pushId = pushId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public BankEmpBean getBankemp() {
		return bankemp;
	}
	public void setBankemp(BankEmpBean bankemp) {
		this.bankemp = bankemp;
	}
	public ArrayList<BankJobReport> getBankJobReportList() {
		return bankJobReportList;
	}
	public void setBankJobReportList(ArrayList<BankJobReport> bankJobReportList) {
		this.bankJobReportList = bankJobReportList;
	}
	public ArrayList<BankJobReportCommentBean> getBankJobReportCommentList() {
		return bankJobReportCommentList;
	}
	public void setBankJobReportCommentList(
			ArrayList<BankJobReportCommentBean> bankJobReportCommentList) {
		this.bankJobReportCommentList = bankJobReportCommentList;
	}
	public ArrayList<BankJobReportDoneBean> getBankJobReportDoneList() {
		return bankJobReportDoneList;
	}
	public void setBankJobReportDoneList(
			ArrayList<BankJobReportDoneBean> bankJobReportDoneList) {
		this.bankJobReportDoneList = bankJobReportDoneList;
	}
	public ArrayList<BankJobTask> getBankJobTaskList() {
		return bankJobTaskList;
	}
	public void setBankJobTaskList(ArrayList<BankJobTask> bankJobTaskList) {
		this.bankJobTaskList = bankJobTaskList;
	}
	public ArrayList<BankJobTask> getBankJobTaskfList() {
		return bankJobTaskfList;
	}
	public void setBankJobTaskfList(ArrayList<BankJobTask> bankJobTaskfList) {
		this.bankJobTaskfList = bankJobTaskfList;
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
	public ArrayList<BankJobTaskEmp> getBankJobTaskEmpList() {
		return bankJobTaskEmpList;
	}
	public void setBankJobTaskEmpList(ArrayList<BankJobTaskEmp> bankJobTaskEmpList) {
		this.bankJobTaskEmpList = bankJobTaskEmpList;
	}
	public ArrayList<BankRoleBean> getBankRoleList() {
		return bankRoleList;
	}
	public void setBankRoleList(ArrayList<BankRoleBean> bankRoleList) {
		this.bankRoleList = bankRoleList;
	}
	public ArrayList<BankJobLogBean> getBankJobLogList() {
		return bankJobLogList;
	}
	public void setBankJobLogList(ArrayList<BankJobLogBean> bankJobLogList) {
		this.bankJobLogList = bankJobLogList;
	}
	public BankGroupBean getBankGroup() {
		return bankGroup;
	}
	public void setBankGroup(BankGroupBean bankGroup) {
		this.bankGroup = bankGroup;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpMobile() {
		return empMobile;
	}
	public void setEmpMobile(String empMobile) {
		this.empMobile = empMobile;
	}
	public String getEmpPassword() {
		return empPassword;
	}
	public void setEmpPassword(String empPassword) {
		this.empPassword = empPassword;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public char getEmpSex() {
		return empSex;
	}
	public void setEmpSex(char empSex) {
		this.empSex = empSex;
	}
	public String getEmpIdUp() {
		return empIdUp;
	}
	public void setEmpIdUp(String empIdUp) {
		this.empIdUp = empIdUp;
	}

	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getEmpCover() {
		return empCover;
	}
	public void setEmpCover(String empCover) {
		this.empCover = empCover;
	}
	public String getHx_name() {
		return hx_name;
	}
	public void setHx_name(String hx_name) {
		this.hx_name = hx_name;
	}
	public int getTasknum() {
		return tasknum;
	}
	public void setTasknum(int tasknum) {
		this.tasknum = tasknum;
	}
	public int getDayreport() {
		return dayreport;
	}
	public void setDayreport(int dayreport) {
		this.dayreport = dayreport;
	}
	public int getWeekreport() {
		return weekreport;
	}
	public void setWeekreport(int weekreport) {
		this.weekreport = weekreport;
	}
	public int getYearreport() {
		return yearreport;
	}
	public void setYearreport(int yearreport) {
		this.yearreport = yearreport;
	}
	public int getLoginnum() {
		return loginnum;
	}
	public void setLoginnum(int loginnum) {
		this.loginnum = loginnum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNewempPassword() {
		return newempPassword;
	}
	public void setNewempPassword(String newempPassword) {
		this.newempPassword = newempPassword;
	}
	public String getNewPhone() {
		return newPhone;
	}
	public void setNewPhone(String newPhone) {
		this.newPhone = newPhone;
	}
	
	
	

}
