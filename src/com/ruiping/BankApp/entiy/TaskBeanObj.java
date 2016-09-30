package com.ruiping.BankApp.entiy;

import java.util.List;

/**
 * Created by zhl on 2016/9/8.
 */
public class TaskBeanObj {
//    private List<BankJobTaskDoneBean> taskView;//查看
//    private List<BankJobTaskEmp> taskEmp;//参与人员
//    private List<BankEmpBean> taskManager;//全部人员
    private List<BankJobTask> bankJobTask;//任务
    private String joinEmpNum;
    private String subTaskNum;

    public String getJoinEmpNum() {
        return joinEmpNum;
    }

    public void setJoinEmpNum(String joinEmpNum) {
        this.joinEmpNum = joinEmpNum;
    }

    public String getSubTaskNum() {
        return subTaskNum;
    }

    public void setSubTaskNum(String subTaskNum) {
        this.subTaskNum = subTaskNum;
    }

    //    private List<BankJobTaskDoneBean> taskDone;//操作
//    private List<BankJobTaskRemind> remindSetting;//提醒
//    private List<BankJobTaskComment> taskComment;//评论

    public List<BankJobTask> getBankJobTask() {
        return bankJobTask;
    }

    public void setBankJobTask(List<BankJobTask> bankJobTask) {
        this.bankJobTask = bankJobTask;
    }
}
