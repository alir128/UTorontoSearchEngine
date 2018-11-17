package com.cscc01.demo.TestControllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.cscc01.demo.Controllers.AdvancedSearchController;
import com.cscc01.demo.Models.Repositories.FileRepository;
import com.cscc01.demo.Models.SchemaBeans.File;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class TestAdvancedSearchController extends TestCase {
	
	@Mock
	private FileRepository mockFP;
	
	@InjectMocks
	private AdvancedSearchController asc = new AdvancedSearchController();
	
	@Test
	public void test_GetAllTags_Empty() throws Exception {
		Mockito.when(mockFP.findAll()).thenReturn(new ArrayList<File>());
		assertEquals(asc.getAllTags().size(), 0);
	}
	
	@Test
	public void test_GetAllTags_NonEmpty() throws Exception {
		List<File> fileArray = new ArrayList<>();
		List<String> tagArray = new ArrayList<>();
		File mockFile = Mockito.mock(File.class);
		
		tagArray.add("tag1");
		tagArray.add("tag2");
		Mockito.when(mockFile.getTags()).thenReturn(tagArray);
		fileArray.add(mockFile);
		Mockito.when(mockFP.findAll()).thenReturn(fileArray);
		
		assertEquals(asc.getAllTags().contains("tag1"), true);
		assertEquals(asc.getAllTags().contains("tag2"), true);
		assertEquals(asc.getAllTags().contains("tag3"), false);
		assertEquals(asc.getAllTags().size(), 2);
	}
	
	@Test
	public void test_GetAllFileTypes_Empty() throws Exception {
		Mockito.when(mockFP.findAll()).thenReturn(new ArrayList<File>());
		assertEquals(asc.getAllFileTypes().size(), 0);
	}
	
	@Test
	public void test_GetAllFileTypes_NonEmpty() throws Exception {
		List<File> fileArray = new ArrayList<>();
		File mockFile = Mockito.mock(File.class);
		
		fileArray.add(mockFile);
		Mockito.when(mockFP.findAll()).thenReturn(fileArray);
		assertEquals(asc.getAllFileTypes().size(), 1);
	}
	
	@Test
	public void test_GetAllTag_Empty() throws Exception {
		Mockito.when(mockFP.findAll()).thenReturn(new ArrayList<File>());
		assertEquals(asc.getAllTag().size(), 0);
	}
	
	@Test
	public void test_GetAllTag_NonEmpty() throws Exception {
		List<File> fileArray = new ArrayList<>();
		List<String> tagArray = new ArrayList<>();
		File mockFile = Mockito.mock(File.class);
		
		tagArray.add("tag1");
		tagArray.add("tag2");
		Mockito.when(mockFile.getTags()).thenReturn(tagArray);
		fileArray.add(mockFile);
		Mockito.when(mockFP.findAll()).thenReturn(fileArray);
		
		assertEquals(asc.getAllTag().contains("tag1"), true);
		assertEquals(asc.getAllTag().contains("tag2"), true);
		assertEquals(asc.getAllTag().contains("tag3"), false);
		assertEquals(asc.getAllTag().size(), 2);
	}
}
