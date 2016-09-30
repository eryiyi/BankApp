package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankJobTaskComment;

import java.util.List;

/**
 * Created by zhl on 2016/9/7.
 */
public class BankJobTaskCommentData extends Data {
    private List<BankJobTaskComment> data;

    public List<BankJobTaskComment> getData() {
        return data;
    }

    public void setData(List<BankJobTaskComment> data) {
        this.data = data;
    }
}
