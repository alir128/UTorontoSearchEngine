package com.CSCC01.Project.Backend.Controllers.InputBeans;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public class UploadBean {

    public UploadBean() {
    }

    private ArrayList<String> tags;
    private MultipartFile file;
    private ArrayList<String> accessGroups;

    public UploadBean(ArrayList<String> tags, MultipartFile file, ArrayList<String> accessGroups) {
        this.tags = tags;
        this.file = file;
        this.accessGroups = accessGroups;
    }

    @Override
    public String toString() {
        return "UploadBean{" +
                "tags=" + tags +
                ", file=" + file +
                ", accessGroups=" + accessGroups +
                '}';
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public ArrayList<String> getAccessGroups() {
        return accessGroups;
    }

    public void setAccessGroups(ArrayList<String> accessGroups) {
        this.accessGroups = accessGroups;
    }
}
