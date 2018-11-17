package com.cscc01.demo.Controllers.PublicOutputBeans;

public class ResultObject {
    private String id;
    private String content;
    private String title;
    private String time;
    private String owner;
    private String date;
    private String fileType;

    public ResultObject() {}

    public ResultObject(String id, String content, String title, String time, String Owner, String date, String fileType) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.time = time;
        this.setOwner(Owner);
        this.setDate(date);
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "ResultObject{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", owner='" + owner + '\'' +
                ", date='" + date + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
