package com.ruiping.BankApp.entiy;

import java.util.ArrayList;

/**
 * 权限表 bank_privilege
 * @author liuke
 *
 */
public class BankPrivilegeBean {
	private int id;//主键 自增 
	private int pid;// 父类Id 
	private String description;// 描述
	private String name;//名称
	private int isLeaf;// 是否为叶子节点 0：否 1：是 
	private int display;//是否显示 0：不显示 1：显示
	private String constantName;//常量
	private String url;//链接地址
	private String target;// 链接目标
	private int orderNum;// 排序
	private int istree;// 是否是树
	private int issubmenu;//是否含有子菜单
	private String menuicon;//图标
	private ArrayList<BankRoleBean> bankRoleList;
	
	
	public ArrayList<BankRoleBean> getBankRoleList() {
		return bankRoleList;
	}
	public void setBankRoleList(ArrayList<BankRoleBean> bankRoleList) {
		this.bankRoleList = bankRoleList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
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
	public int getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	public String getConstantName() {
		return constantName;
	}
	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getIstree() {
		return istree;
	}
	public void setIstree(int istree) {
		this.istree = istree;
	}
	public int getIssubmenu() {
		return issubmenu;
	}
	public void setIssubmenu(int issubmenu) {
		this.issubmenu = issubmenu;
	}
	public String getMenuicon() {
		return menuicon;
	}
	public void setMenuicon(String menuicon) {
		this.menuicon = menuicon;
	}
	
	
	
	

}
