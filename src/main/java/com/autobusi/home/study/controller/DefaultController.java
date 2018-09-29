package com.autobusi.home.study.controller;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/*")
public class DefaultController {
	
	@GetMapping
	public String DefaultGetHandler() {
		return "Server Ok";
	}
	
	@GetMapping("/api/*")
	public String ApiGetHandler() {
		return "Server Ok";
	}
	
	@GetMapping("/api/v1/*")
	public String ApiV1GetHandler() {
		return "Server Ok";
	}
}
