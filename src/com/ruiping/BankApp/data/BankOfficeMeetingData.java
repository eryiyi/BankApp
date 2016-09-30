package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankOfficeMeeting;

import java.util.List;

/**
 * Created by zhl on 2016/9/30.
 */
public class BankOfficeMeetingData extends Data{
    private List<BankOfficeMeeting> data;

    public List<BankOfficeMeeting> getData() {
        return data;
    }

    public void setData(List<BankOfficeMeeting> data) {
        this.data = data;
    }
}
