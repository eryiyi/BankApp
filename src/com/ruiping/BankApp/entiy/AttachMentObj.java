package com.ruiping.BankApp.entiy;

import java.io.Serializable;

/**
 * Created by zhl on 2016/8/30.
 */
public class AttachMentObj implements Serializable {
    private String title;
    private String urlStr;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public AttachMentObj(String title, String urlStr) {
        this.title = title;
        this.urlStr = urlStr;
    }

}
