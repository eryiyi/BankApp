package com.ruiping.BankApp.huanxin.mine;


import com.hyphenate.chat.EMConversation;
import com.ruiping.BankApp.entiy.BankEmpBean;

/**
 * Created by Administrator on 2015/3/5.
 * 继承环信提供的EMConversation，增加emp属性，以及getter and setter 方法
 */
public class MyEMConversation {

    private BankEmpBean emp;

    private EMConversation emConversation;

    public EMConversation getEmConversation() {
        return emConversation;
    }

    public void setEmConversation(EMConversation emConversation) {
        this.emConversation = emConversation;
    }

    public BankEmpBean getEmp() {
        return emp;
    }

    public void setEmp(BankEmpBean emp) {
        this.emp = emp;
    }
}
