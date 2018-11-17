package com.cscc01.demo.Controllers.PublicOutputBeans;

import java.util.List;

import com.cscc01.demo.Models.SchemaBeans.Comment;

public class CommentsResponseAjax {
	String message;
	List<Comment> result;
	
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<Comment> getResult() {
		return result;
	}
	
	public void setResult(List<Comment> result) {
		this.result = result;
	}
	
	
	
}
