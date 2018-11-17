package com.cscc01.demo.Models.SchemaBeans;

import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "comment")
public class Comment {
	
	@Id
    private String id;
	
	private String fileId;
	private String groupCode;
	private String fullname;
	private String time;
	private String date;
	
	@NotEmpty(message = "comment can't be empty")
	private String content;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String file) {
		this.fileId = file;
	}
	public String getOwner() {
		return fullname;
	}
	public void setOwner(String username) {
		this.fullname = username;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
}
