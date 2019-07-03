package com.neo.scheduler2.config;

import java.beans.PropertyVetoException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.neo.scheduler2.config.bean.C3p0Param;

@Configuration
public class C3p0DataSouceConfig {
	
	@Bean
	@ConfigurationProperties("spring.datasource.c3p0")	//将application.yml中的属性值注入到bean中
	public C3p0Param c3p0Param(){
		return new C3p0Param();
	}
	
	@Bean("dataSource")
	public ComboPooledDataSource createDevDataSource() throws PropertyVetoException{
		C3p0Param c3p0 = c3p0Param();
		ComboPooledDataSource  cDataSource = new ComboPooledDataSource();
		cDataSource.setDriverClass(c3p0.getDriverclass());
		cDataSource.setJdbcUrl(c3p0.getUrl());
		cDataSource.setUser(c3p0.getUsername());
		cDataSource.setPassword(c3p0.getPassword());
		
		//TODO 添加数据源链接池的其他参数，优化
		
		
		return cDataSource;
	}
	
}
