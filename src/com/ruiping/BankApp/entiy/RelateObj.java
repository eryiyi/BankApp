package com.ruiping.BankApp.entiy;

/**
 * Created by zhl on 2017/2/17.
 *  "relationId": "5beb7954c213496295a5dd5021ac43c5",
 "empId": "18da5ff3f1a748e291fd247f5edb0bdf",
 "empUpId": "a1cb7c808d614e5985900f70649331cc",
 "title": "测试1",
 "recordId": "27efe5c02d004605ae329ffb8c680315",
 "recordType": 2,
 "isRead": "1",
 "recordTime": "2017-02-17",
 "bankemp": {
 "empId": "18da5ff3f1a748e291fd247f5edb0bdf",
 "empMobile": "13181038185",
 "empName": "张海龙",
 "empSex": "\u0000",
 "dateLine": "1482987262567",
 "channelId": "4571284451586481359",
 "hx_name": "18da5ff3f1a748e291fd247f5edb0bdf",
 "isMeeting": "2",
 "roleid": 0,
 "tasknum": 0,
 "dayreport": 0,
 "weekreport": 0,
 "yearreport": 0,
 "loginnum": 0
 }
 },
 {
 "relationId": "b71e5dd4e01e485392177af127154a08",
 "empId": "53e2d0eba00d476bb61a92413f6d2fe2",
 "empUpId": "a1cb7c808d614e5985900f70649331cc",
 "title": "测试2",
 "recordId": "cd0ca3e1113846aea86d3dd06d570ad8",
 "recordType": 2,
 "isRead": "0",
 "recordTime": "2017-02-17",
 "bankemp": {
 "empId": "53e2d0eba00d476bb61a92413f6d2fe2",
 "empMobile": "15266788746",
 "empName": "糖糖",
 "empSex": "\u0000",
 "dateLine": "1475546203503",
 "channelId": "3702116158726198239",
 "hx_name": "53e2d0eba00d476bb61a92413f6d2fe2",
 "isMeeting": "2",
 "roleid": 0,
 "tasknum": 0,
 "dayreport": 0,
 "weekreport": 0,
 "yearreport": 0,
 "loginnum": 0
 },
 "bankJobReport": {
 "reportId": "cd0ca3e1113846aea86d3dd06d570ad8",
 "reportType": "1",
 "reportTitle": "今天抓紧时间进行流程测试</br>今天风和日丽，心情还可以吧！特附加美女照片一枚！",
 "reportCont": "今天抓紧时间进行流程测试</br>今天风和日丽，心情还可以吧！特附加美女照片一枚！",
 "dateLine": "1475547070646",
 "status": 1,
 "commentCount": 0,
 "doneCount": 0,
 "reportCount": 0,
 "pagecurrent": 0,
 "contion": 0
 }
 }
 ],
 "success": true,
 "code": 200

 */
public class RelateObj {
    private String relationId;
    private String empId;
    private String empUpId;
    private String title;
    private String recordId;
    private String recordType;
    private String isRead;
    private String recordTime;

    private BankEmpBean bankemp;
    private BankJobReport bankJobReport;
    private BankJobTask bankJobTask;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpUpId() {
        return empUpId;
    }

    public void setEmpUpId(String empUpId) {
        this.empUpId = empUpId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public BankEmpBean getBankemp() {
        return bankemp;
    }

    public void setBankemp(BankEmpBean bankemp) {
        this.bankemp = bankemp;
    }

    public BankJobReport getBankJobReport() {
        return bankJobReport;
    }

    public void setBankJobReport(BankJobReport bankJobReport) {
        this.bankJobReport = bankJobReport;
    }

    public BankJobTask getBankJobTask() {
        return bankJobTask;
    }

    public void setBankJobTask(BankJobTask bankJobTask) {
        this.bankJobTask = bankJobTask;
    }
}
