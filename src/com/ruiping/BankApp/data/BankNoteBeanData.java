package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankNoteBean;

import java.util.List;

/**
 * Created by zhl on 2016/9/5.
 */
public class BankNoteBeanData extends Data {
    private List<BankNoteBean> data;

    public List<BankNoteBean> getData() {
        return data;
    }

    public void setData(List<BankNoteBean> data) {
        this.data = data;
    }
}
