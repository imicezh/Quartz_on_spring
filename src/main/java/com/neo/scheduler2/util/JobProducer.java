package com.neo.scheduler2.util;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

import com.neo.scheduler2.bean.JobBean;

/**
 * produce quartz jobdetail instance
 * @author zhou
 */
public class JobProducer {

	public static JobDetail produceJob(JobBean definition) throws ClassNotFoundException{
		
		Class jobClazz = Class.forName(definition.getJobClassName());
		if(!Job.class.isAssignableFrom(jobClazz)){
			throw new RuntimeException(jobClazz.getName()+" must implements interface [org.quartz.Job] ! ");
		}
		
		JobDetail job = JobBuilder.newJob((Class<? extends Job>) Class.forName(definition.getJobClassName()))
								  .withIdentity(definition.getJobName(), definition.getJobGroup())
								  .build();
		return job;
	}
}
