package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankNoticesBean;

/**
 * Created by zhl on 2016/9/26.
 */
public class BankNoticesSingleData extends Data{
    private BankNoticesBean data;

    public BankNoticesBean getData() {
        return data;
    }

    public void setData(BankNoticesBean data) {
        this.data = data;
    }
}
