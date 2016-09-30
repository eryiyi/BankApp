package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankJobTaskEmp;

import java.util.List;

/**
 * Created by zhl on 2016/9/9.
 */
public class BankJobTaskEmpData extends Data {
    private List<BankJobTaskEmp> data;

    public List<BankJobTaskEmp> getData() {
        return data;
    }

    public void setData(List<BankJobTaskEmp> data) {
        this.data = data;
    }
}
