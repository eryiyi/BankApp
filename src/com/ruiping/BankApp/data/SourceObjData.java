package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.SourceObj;

import java.util.List;

/**
 * Created by zhl on 2017/4/30.
 */
public class SourceObjData extends Data {
    private List<SourceObj> data;

    public List<SourceObj> getData() {
        return data;
    }

    public void setData(List<SourceObj> data) {
        this.data = data;
    }
}
