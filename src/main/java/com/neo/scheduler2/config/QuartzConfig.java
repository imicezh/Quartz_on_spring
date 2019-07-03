package com.neo.scheduler2.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.neo.scheduler2.util.SchedulerDispacher;


/**
 * Quartz配置信息
 * @author zhou
 */
@Configuration
public class QuartzConfig {

	@Bean(name="schedulerFactoryBean")
	@DependsOn(value={"dataSource"})
	public SchedulerFactoryBean schedulerFactoryBean(
			DataSource dataSource
			){
		SchedulerFactoryBean sfb = new SchedulerFactoryBean();
		sfb.setDataSource(dataSource);		//quartz使用spring环境提供的数据源
		sfb.setJobFactory(jobFactory());
		
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		sfb.setConfigLocation(resourceResolver.getResource("classpath:quartz.properties"));
		
		sfb.setOverwriteExistingJobs(true);
		sfb.setStartupDelay(3);
		
		return sfb;
	}
	
	@Bean("schedulerDispacher")
	public SchedulerDispacher schedulerDispatcher(){
		SchedulerDispacher sd = new SchedulerDispacher();
		return sd;
	}
	
	@Bean("myJobFactory")
	public AutowiredJobFactory jobFactory(){
		return new AutowiredJobFactory();
	}

}
