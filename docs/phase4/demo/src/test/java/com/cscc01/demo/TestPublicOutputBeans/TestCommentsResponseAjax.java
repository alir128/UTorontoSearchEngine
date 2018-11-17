package com.cscc01.demo.TestPublicOutputBeans;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cscc01.demo.Controllers.PublicOutputBeans.CommentsResponseAjax;
import com.cscc01.demo.Models.SchemaBeans.Comment;

import junit.framework.TestCase;

public class TestCommentsResponseAjax extends TestCase {
	
	CommentsResponseAjax cra = new CommentsResponseAjax();
	
	@Test
	public void test_getMessage_notSet() {
		assertEquals(cra.getMessage(), null);
	}
	
	@Test
	public void test_getMessage_set() {
		cra.setMessage("i am set");
		assertEquals(cra.getMessage(), "i am set");
	}
	
	@Test
	public void test_getResult_emptyCommentList() {
		assertNull(cra.getResult());
	}
	
	@Test
	public void test_getResult_nonEmptyCommentList() {
		List<Comment> lst = new ArrayList<>(); 
		lst.add(new Comment());
		cra.setResult(lst);
		assertEquals(lst.size(), 1);
	}

}
