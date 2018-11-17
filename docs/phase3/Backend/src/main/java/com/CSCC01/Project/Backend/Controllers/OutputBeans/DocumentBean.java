package com.CSCC01.Project.Backend.Controllers.OutputBeans;

public class DocumentBean {

    private String title;
    private String content;

    public DocumentBean() {}

    public DocumentBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "DocumentBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
