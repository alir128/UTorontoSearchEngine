package com.cscc01.demo.TestPublicOutputBeans;

import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import org.mockito.Mockito;

import com.cscc01.demo.Controllers.PublicOutputBeans.ChartsResponse;

import junit.framework.TestCase;

public class TestChartResponse extends TestCase {
	private ChartsResponse cr;
	
	public TestChartResponse() {
		cr = new ChartsResponse();
	}
	
	@Test
	public void test_setMessage_notNull() {
		cr.setMessage("i am not null");
		assertNotNull(cr.getMessage());
	}
	
	@Test
	public void test_getMessage_Null() {
		assertNull(cr.getMessage());
	}
	
	@Test
	public void test_setFileType_emptyMap() {
		@SuppressWarnings("unchecked")
		Map<String, Integer> mockMap = Mockito.mock(Map.class);
		cr.setFiletype(mockMap);
		assertEquals(cr.getFiletypes().size(), 0);
	}
	
	@Test
	public void test_setFileType_nonEmptyMap() {
		Map<String, Integer> mockMap = new HashMap<>();
		mockMap.put("key1", 1);
		cr.setFiletype(mockMap);
		assertEquals(cr.getFiletypes().size(), 1);
		assertEquals((Object) cr.getFiletypes().get("key1"), 1);
	}
}

