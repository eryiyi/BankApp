package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankTaskShareBean;

import java.util.List;

/**
 * Created by zhl on 2016/9/29.
 */
public class BankTaskShareBeanData extends Data {
    private List<BankTaskShareBean> data;

    public List<BankTaskShareBean> getData() {
        return data;
    }

    public void setData(List<BankTaskShareBean> data) {
        this.data = data;
    }
}
