package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankGroupBean;

import java.util.List;

/**
 * Created by zhl on 2016/9/29.
 */
public class BankGroupBeanData extends Data {
    private List<BankGroupBean> data;

    public List<BankGroupBean> getData() {
        return data;
    }

    public void setData(List<BankGroupBean> data) {
        this.data = data;
    }
}
