package com.cscc01.demo.TestPublicOutputBeans;

import org.junit.Test;

import com.cscc01.demo.Controllers.PublicOutputBeans.ResultObject;

import junit.framework.TestCase;

public class TestResultObject extends TestCase {
	private ResultObject ro;
	
	public TestResultObject() {
		String id = "id";
		String content = "content";
		String title = "title";
		String time = "time";
		String owner = "owner";
		String date = "date";
		String filetype = "filetype";
		ro = new ResultObject(id, content, title, time, owner, date, filetype);
	}
	
	@Test
	public void test_setOwner_setFromConstrcutor() {
		assertEquals(ro.getOwner(), "owner");
	}
	
	@Test
	public void test_setDate_setFromConstructor() {
		assertEquals(ro.getDate(), "date");
	}
	
	@Test
	public void test_toString_dataFromConstructor() {
		assertEquals("ResultObject{" +
                "id='" + ro.getId() + '\'' +
                ", content='" + ro.getContent() + '\'' +
                ", title='" + ro.getTitle() + '\'' +
                ", time='" + ro.getTime() + '\'' +
                ", owner='" + ro.getOwner() + '\'' +
                ", date='" + ro.getDate() + '\'' +
                ", fileType='" + ro.getFileType() + '\'' +
                '}', ro.toString());
	}
}
