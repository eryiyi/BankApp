package com.ruiping.BankApp.entiy;

/**
 * Created by zhl on 2016/10/9.
 */
public class AboutUs {
//    "id": "32432534",
//            "projectName": "农商银行",
//            "projectContent": "农商银行农商银行农商银行农商银行农商银行农商银行农商银行",
//            "copyright": "农商银行 版权所有",
//            "CopyrightChina": "彻底放松对V型成本高"
    private String id;
    private String projectName;
    private String projectContent;
    private String copyright;
    private String CopyrightChina;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyrightChina() {
        return CopyrightChina;
    }

    public void setCopyrightChina(String copyrightChina) {
        CopyrightChina = copyrightChina;
    }
}
