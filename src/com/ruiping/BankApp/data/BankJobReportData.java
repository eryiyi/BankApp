package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankJobReport;

import java.util.List;

/**
 * Created by zhl on 2016/8/28.
 */
public class BankJobReportData extends Data {
    private List<BankJobReport> data;

    public List<BankJobReport> getData() {
        return data;
    }

    public void setData(List<BankJobReport> data) {
        this.data = data;
    }
}
