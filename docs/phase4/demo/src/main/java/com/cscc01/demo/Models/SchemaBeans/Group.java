package com.cscc01.demo.Models.SchemaBeans;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "groups")
public class Group {
	@Id
    private String id;

	@Indexed(unique = true)
	private String code;
	private String name;
	private String campus;
	private String info;
	private String type;
	private Set<String> userIds = new HashSet<>();

	private Set<String> adminsIds = new HashSet<>();

	private Set<String> fileIds = new HashSet<>();

	
	public Group() {};
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Set<String> getAdminsIds() {
		return adminsIds;
	}

	public void setAdminsIds(Set<String> adminsIds) {
		this.adminsIds = adminsIds;
	}

	public Set<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(Set<String> userIds) {
		this.userIds = userIds;
	}

	public Set<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(Set<String> fileIds) {
		this.fileIds = fileIds;
	}
	
	public void addFile(File file) {
		this.fileIds.add(file.getId());
	}
	
}
