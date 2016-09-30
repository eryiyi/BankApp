package com.ruiping.BankApp.entiy;

/**
 * Created by zhl on 2016/7/1.
 * 个人主页 日报 周报 任务类
 */
public class IndexObj {
    private String title;
    private int pic;
    private String number;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public IndexObj(String title, int pic, String number) {
        this.title = title;
        this.pic = pic;
        this.number = number;
    }
}
