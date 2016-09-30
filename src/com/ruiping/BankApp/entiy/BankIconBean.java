package com.ruiping.BankApp.entiy;

import java.io.Serializable;

public class BankIconBean implements Serializable {

	public BankIconBean() {
		// TODO Auto-generated constructor stub
	}
    private int id;
    private String iconclass;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIconclass() {
		return iconclass;
	}
	public void setIconclass(String iconclass) {
		this.iconclass = iconclass;
	}
    
}
