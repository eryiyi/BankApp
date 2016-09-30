package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankNoticesBean;

import java.util.List;

/**
 * Created by zhl on 2016/9/26.
 */
public class BankNoticesBeanData  extends Data{
    private List<BankNoticesBean> data;

    public List<BankNoticesBean> getData() {
        return data;
    }

    public void setData(List<BankNoticesBean> data) {
        this.data = data;
    }
}
