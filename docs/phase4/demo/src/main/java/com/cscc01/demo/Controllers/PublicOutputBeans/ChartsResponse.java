package com.cscc01.demo.Controllers.PublicOutputBeans;

import java.util.HashMap;
import java.util.Map;

public class ChartsResponse {
	String message;
	Map<String,Integer> filetypes =new HashMap<String,Integer>();
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Integer> getFiletypes() {
		return filetypes;
	}
	public void setFiletype(Map<String, Integer> filetypes) {
		this.filetypes = filetypes;
	}
	
	
}
