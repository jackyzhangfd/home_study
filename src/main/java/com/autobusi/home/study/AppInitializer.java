package com.autobusi.home.study;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.autobusi.home.study.config.Config;

public class AppInitializer extends SpringBootServletInitializer {
	 
	/*
	 * This method is not necessary since we run the application in external web server
	 */
	public static void main(String[] args) {
		SpringApplication.run(AppInitializer.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		
		return builder.sources(Config.class);
	}
	
	/*
	 * Can we override this method just like what we do without spring boot to configure the server?
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		
		super.onStartup(servletContext);
	}
	
	
}
