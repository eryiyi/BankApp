package com.ruiping.BankApp.entiy;

import java.io.Serializable;

/**
 * Created by zhl on 2016/9/30.
 */
public class BankOfficeMeeting implements Serializable {
    private String meetingId;
    private String filePath;
    private String createDate;
    private String meetingDate;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }
}
