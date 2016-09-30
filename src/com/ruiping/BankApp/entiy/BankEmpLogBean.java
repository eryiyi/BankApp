package com.ruiping.BankApp.entiy;
/**
 * 会员操作日志表
 * @author hyp
 *
 */
public class BankEmpLogBean {
private String empLogId;//操作日志id
private String empId;
private String empLogCont;
private String waittime;
private String url;
private String recordDate;
private int flag;
public String getEmpLogId() {
	return empLogId;
}
public void setEmpLogId(String empLogId) {
	this.empLogId = empLogId;
}
public String getEmpId() {
	return empId;
}
public void setEmpId(String empId) {
	this.empId = empId;
}
public String getEmpLogCont() {
	return empLogCont;
}
public void setEmpLogCont(String empLogCont) {
	this.empLogCont = empLogCont;
}
public String getWaittime() {
	return waittime;
}
public void setWaittime(String waittime) {
	this.waittime = waittime;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getRecordDate() {
	return recordDate;
}
public void setRecordDate(String recordDate) {
	this.recordDate = recordDate;
}
public int getFlag() {
	return flag;
}
public void setFlag(int flag) {
	this.flag = flag;
}

}
