package com.cscc01.demo.Models.SchemaBeans;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
public class File {
	
	@Id
    private String id;
	private String fileName;
	private String ContentType;
	private List<String> tags;
	private String owner;
	private String time;
	private String date;
	private String ViewType; // instructor or Student
	private Set<String> groupIds = new HashSet<>();

	public File() {}
	
	public File(String id, String fileName, String contentType, List<String> tags, String owner, String time, String date) {
		this.id = id;
		this.fileName = fileName;
		ContentType = contentType;
		this.tags = tags;
		this.owner = owner;
		this.time = time;
		this.date = date;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContentType() {
		return ContentType;
	}
	public void setContentType(String contentType) {
		ContentType = contentType;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getViewType() {
		return ViewType;
	}
	public void setViewType(String viewType) {
		ViewType = viewType;
	}

	public Set<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(Set<String> groupIds) {
		this.groupIds = groupIds;
	}

	public void addGroup(Group group) {
		this.groupIds.add(group.getId());
	}
}
