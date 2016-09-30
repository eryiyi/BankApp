package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankJobTask;

import java.util.List;

/**
 * Created by zhl on 2016/9/7.
 */
public class BankJobTaskData extends Data {
    private List<BankJobTask> data;

    public List<BankJobTask> getData() {
        return data;
    }

    public void setData(List<BankJobTask> data) {
        this.data = data;
    }
}
