package com.ruiping.BankApp.entiy;
/**
 * 系统功能配置表 bank_systemInfo
 * @author liuke
 *
 */
public class BankSystemInfoBean {
	private int id;// 主键
	private String description;//描述 
	private String  name;//名称
	private String jian;// 键
	private String zhi;//值
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJian() {
		return jian;
	}
	public void setJian(String jian) {
		this.jian = jian;
	}
	public String getZhi() {
		return zhi;
	}
	public void setZhi(String zhi) {
		this.zhi = zhi;
	}
	
	

}
