package com.ruiping.BankApp.data;

import com.ruiping.BankApp.entiy.BankJobTask;
import com.ruiping.BankApp.entiy.BankNoteBean;
import com.ruiping.BankApp.entiy.BankNoticesBean;

import java.util.List;

/**
 * Created by zhl on 2016/9/29.
 */
public class NoteJobTaskObj extends Data {
    private int noticesList;
    private int Note;
    private int JobTask;

    public int getNoticesList() {
        return noticesList;
    }

    public void setNoticesList(int noticesList) {
        this.noticesList = noticesList;
    }

    public int getNote() {
        return Note;
    }

    public void setNote(int note) {
        Note = note;
    }

    public int getJobTask() {
        return JobTask;
    }

    public void setJobTask(int jobTask) {
        JobTask = jobTask;
    }
}
