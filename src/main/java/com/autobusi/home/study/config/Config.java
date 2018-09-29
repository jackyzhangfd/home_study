package com.autobusi.home.study.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;

@Configuration
@ComponentScan(basePackages = "com.autobusi.home.study")
@EnableAutoConfiguration
public class Config {
	
	/*
	 * Solve context registered dataSource issue
	 * https://stackoverflow.com/questions/28295503/migration-to-tomcat-8-instancealreadyexistsexception-datasource
	 */
	@Bean
	public AnnotationMBeanExporter annotationMBeanExporter() {
	    AnnotationMBeanExporter annotationMBeanExporter = new AnnotationMBeanExporter();
	    annotationMBeanExporter.addExcludedBean("dataSource");
	    return annotationMBeanExporter;
	}
}
