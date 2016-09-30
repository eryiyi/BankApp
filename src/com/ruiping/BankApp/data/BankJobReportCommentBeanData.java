package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankJobReportCommentBean;

import java.util.List;

/**
 * Created by zhl on 2016/8/28.
 */
public class BankJobReportCommentBeanData extends Data {
    private List<BankJobReportCommentBean> data;

    public List<BankJobReportCommentBean> getData() {
        return data;
    }

    public void setData(List<BankJobReportCommentBean> data) {
        this.data = data;
    }
}
