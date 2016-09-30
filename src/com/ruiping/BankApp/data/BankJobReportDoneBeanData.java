package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankJobReportDoneBean;

import java.util.List;

/**
 * Created by zhl on 2016/8/29.
 */
public class BankJobReportDoneBeanData extends Data{
    private List<BankJobReportDoneBean> data;

    public List<BankJobReportDoneBean> getData() {
        return data;
    }

    public void setData(List<BankJobReportDoneBean> data) {
        this.data = data;
    }
}
