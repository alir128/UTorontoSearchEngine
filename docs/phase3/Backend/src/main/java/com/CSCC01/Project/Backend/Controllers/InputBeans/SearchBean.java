package com.CSCC01.Project.Backend.Controllers.InputBeans;

import java.util.ArrayList;

public class SearchBean {

    private ArrayList<String> title = new ArrayList<>();
    private String ext = "";
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> accessGroups = new ArrayList<>();
    private ArrayList<String> body = new ArrayList<>();

    public SearchBean() {}

    public SearchBean(ArrayList<String> title, String ext, ArrayList<String> tags, ArrayList<String> accessGroups, ArrayList<String> body) {
        this.title = title;
        this.ext = ext;
        this.tags = tags;
        this.accessGroups = accessGroups;
        this.body = body;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "title=" + title +
                ", ext='" + ext + '\'' +
                ", tags=" + tags +
                ", accessGroups=" + accessGroups +
                ", body=" + body +
                '}';
    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getAccessGroups() {
        return accessGroups;
    }

    public void setAccessGroups(ArrayList<String> accessGroups) {
        this.accessGroups = accessGroups;
    }

    public ArrayList<String> getBody() {
        return body;
    }

    public void setBody(ArrayList<String> body) {
        this.body = body;
    }
}
