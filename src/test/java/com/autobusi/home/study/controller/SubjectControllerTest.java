package com.autobusi.home.study.controller;

import java.io.File;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class SubjectControllerTest {
	
	@Test
	public void testUpload() throws Exception { 
		/*
	    String url = "http://127.0.0.1:8080/study/api/v1/subject/uploadSubjectImage";  
	    String filePath = "D:/test.jpg";  
	  
	    RestTemplate rest = new RestTemplate();  
	    FileSystemResource resource = new FileSystemResource(new File(filePath));  
	    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();  
	    param.add("imageFile", resource);  
	    param.add("fileName", "test.jpg");  
	  
	    String string = rest.postForObject(url, param, String.class);  
	    System.out.println(string);
	    */  
	} 
}
