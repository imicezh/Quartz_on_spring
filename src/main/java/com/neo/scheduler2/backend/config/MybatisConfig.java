package com.neo.scheduler2.backend.config;

import javax.sql.DataSource;

import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.neo.scheduler2.config.bean.MybatisParam;

@Configuration
public class MybatisConfig {

	@Bean
	@ConfigurationProperties("spring.mybatis")
	public MybatisParam mybatisParam(){
		return new MybatisParam();
	}
	
	@Bean("sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(
			 DataSource dataSource,
			 MybatisParam mybatisParam
			) throws Exception{
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
	    MybatisConfiguration config = new MybatisConfiguration();
	    
	    config.setCacheEnabled(false);	//关闭缓存
	    config.setLogImpl(Log4jImpl.class);	//打印sql
	    config.addInterceptor(new PerformanceInterceptor());  //通用CRUD已内置不需要写了
	    config.addInterceptor(new PaginationInterceptor());   //分页
	    
		factoryBean.setDataSource(dataSource);
		factoryBean.setConfiguration(config);
		
		//扫描mapper xml
		//mybatis框架自己是不支持解析mapperLocation的通配符写法的
		//这种方式加载mapperlocation，不支持Ant写法，必须自己去写解析
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		Resource[] mapperlocations = resourceResolver.getResources(mybatisParam.getMapperLocation());
		
		factoryBean.setMapperLocations(mapperlocations);
		
		return factoryBean.getObject();
	}
}
