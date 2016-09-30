package com.ruiping.BankApp.entiy;

import java.io.Serializable;

/**
 * 提醒设置实体
 * @author lk
 *
 */
public class SettingsBean implements Serializable {
	
	private String settingsId;//提醒设置id uuid 32 位
	private String settings;//提醒设置内容 
	private String managerId;// 操作人 id  uuid  32 位
	private BankManagerBean bankManager;// 管理员（操作人）对象
	private String type;//时间类型 1：分钟 2：小时 ：3：天  4：周  5：月  6：年
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getSettingsId() {
		return settingsId;
	}
	public void setSettingsId(String settingsId) {
		this.settingsId = settingsId;
	}
	public String getSettings() {
		return settings;
	}
	public void setSettings(String settings) {
		this.settings = settings;
	}
	public BankManagerBean getBankManager() {
		return bankManager;
	}
	public void setBankManager(BankManagerBean bankManager) {
		this.bankManager = bankManager;
	}
	
	

}
