package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankEmpBean;

import java.util.List;

/**
 * Created by zhl on 2016/9/7.
 */
public class BankEmpData extends Data {
    private List<BankEmpBean> data;

    public List<BankEmpBean> getData() {
        return data;
    }

    public void setData(List<BankEmpBean> data) {
        this.data = data;
    }
}
