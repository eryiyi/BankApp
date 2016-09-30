package com.ruiping.BankApp.entiy;

import java.io.Serializable;

public class BankNoticesLogBean implements Serializable {
	private String id;//表id 32为uuid
	private BankEmpBean bankEmp;//会员信息
	private BankNoticesBean bankNotices;//公告id
	private String operate;//操作
	private String dateline;// 时间戳 毫秒值
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BankEmpBean getBankEmp() {
		return bankEmp;
	}
	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}
	public BankNoticesBean getBankNotices() {
		return bankNotices;
	}
	public void setBankNotices(BankNoticesBean bankNotices) {
		this.bankNotices = bankNotices;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	
	

}
