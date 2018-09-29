package com.autobusi.home.study.config;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ContextResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiObjectFactoryBean;

import com.autobusi.home.study.models.SubjectRepository;

@Configuration
@EnableJpaRepositories(basePackageClasses = SubjectRepository.class)
@EntityScan(basePackages="com.autobusi.home.study.models")
public class DBConfig {
	
	
	@Bean(name="dataSource")
	public DataSource getDataSource(){
		try {
			Context    context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/LocalDB");
			ds.getConnection();
			if(ds == null){
				System.out.println("empty data source");
			}
			return ds;
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
