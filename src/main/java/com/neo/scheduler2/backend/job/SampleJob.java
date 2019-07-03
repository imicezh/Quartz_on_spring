package com.neo.scheduler2.backend.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.neo.scheduler2.bean.LoggableJob;

/**
 * extends LoggableJob or implements Job interface to fulfill your needs.
 * (Quartz native annotation stil works,like @DisallowConcurrentExecution)
 */


public class SampleJob extends LoggableJob {

	//@autowire your spring bean ,like Service,Dao and so on.
	
	@Override
	public void doExecute(JobExecutionContext context)
			throws JobExecutionException {

		//TODO your job logic
		System.out.println("=========== sample job running ===========");
	}

}
