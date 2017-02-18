package com.ruiping.BankApp.entiy;

public class BankVersion {

	public BankVersion() {
		// TODO Auto-generated constructor stub
	}
 private String codeid;
 private String version;
 private String version_package;
 private String download_url;
public String getCodeid() {
	return codeid;
}
public void setCodeid(String codeid) {
	this.codeid = codeid;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getVersion_package() {
	return version_package;
}
public void setVersion_package(String version_package) {
	this.version_package = version_package;
}
public String getDownload_url() {
	return download_url;
}
public void setDownload_url(String download_url) {
	this.download_url = download_url;
}

//	{"success":false,"code":2,"message":"当前已是最新版本！！！"}
//	{"data":{"codeid":"e6b92c5e7b7a45a7a08ffee163ce41cb","version":"1.3","version_package":"com.ruiping.BankApp",
// "download_url":"http://223.99.167.142:8088/Manage_ssm/download/index.html"},"success":true,"code":200}
}
