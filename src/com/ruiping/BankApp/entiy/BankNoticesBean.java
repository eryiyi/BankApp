package com.ruiping.BankApp.entiy;


import java.io.Serializable;

public class BankNoticesBean implements Serializable {
	private String noticesId;//公告id
	private String title;//标题
	private String content;//内容
	private String releaseTime;//发布时间
	private String publisher;//发布人
	private int readNumber;//已读人数
	private String commentFile;//附件地址
	private int status;//状态 
	private String givePerson;//推送给的人
	
	public String getNoticesId() {
		return noticesId;
	}
	public void setNoticesId(String noticesId) {
		this.noticesId = noticesId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getReadNumber() {
		return readNumber;
	}
	public void setReadNumber(int readNumber) {
		this.readNumber = readNumber;
	}
	public String getCommentFile() {
		return commentFile;
	}
	public void setCommentFile(String commentFile) {
		this.commentFile = commentFile;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getGivePerson() {
		return givePerson;
	}
	public void setGivePerson(String givePerson) {
		this.givePerson = givePerson;
	}
	
	

}
