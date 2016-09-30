package com.ruiping.BankApp.entiy;

import java.io.Serializable;
import java.util.Date;





public class BankNoteBean  implements Serializable {
	
	private String noteId;
	private String noteContent;
	private String dateLine;
	private String noteDate;
	private Date date;
	private int noteStatus;
	private int count;
	private String empId;
	private String settingsId;
	private BankEmpBean bankEmp;
	private SettingsBean remindsettings;
	
	
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getSettingsId() {
		return settingsId;
	}
	public void setSettingsId(String settingsId) {
		this.settingsId = settingsId;
	}
	public SettingsBean getRemindsettings() {
		return remindsettings;
	}
	public void setRemindsettings(SettingsBean remindsettings) {
		this.remindsettings = remindsettings;
	}
	public String getNoteId() {
		return noteId;
	}
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	public String getDateLine() {
		return dateLine;
	}
	public void setDateLine(String dateLine) {
		this.dateLine = dateLine;
	}
	
	public String getNoteDate() {
		return noteDate;
	}
	public void setNoteDate(String noteDate) {
		this.noteDate = noteDate;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getNoteStatus() {
		return noteStatus;
	}
	public void setNoteStatus(int noteStatus) {
		this.noteStatus = noteStatus;
	}
	public BankEmpBean getBankEmp() {
		return bankEmp;
	}
	public void setBankEmp(BankEmpBean bankEmp) {
		this.bankEmp = bankEmp;
	}
	
	
	

}
