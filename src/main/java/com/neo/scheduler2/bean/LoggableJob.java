package com.neo.scheduler2.bean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.neo.scheduler2.dao.JobRunLogDao;

/**
 * 带日志记录功能的JOB
 * @author zhou
 */
public abstract class LoggableJob implements Job {

	private final ThreadLocal<JobRunLogBean> threadLocal = new ThreadLocal<JobRunLogBean>();
	
	@Autowired
	private JobRunLogDao jobRunlogDao;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		beforeExecute(context);
		try{
			doExecute(context);			
		}
		catch(Exception e){
			e.printStackTrace();
			afterExecute(context,e);			
		}
		afterExecute(context,null);
	}

	private void beforeExecute(JobExecutionContext context){
		JobRunLogBean jrlb = new JobRunLogBean();
		JobKey jkey = context.getJobDetail().getKey();
		TriggerKey tkey = context.getTrigger().getKey();
		jrlb.setJobKey(jkey.getGroup()+"."+jkey.getName());
		jrlb.setTriggerKey(tkey.getGroup()+"."+tkey.getName());
		try {
			jrlb.setHost(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			jrlb.setHost("unknown");
		}
		jrlb.setStartTime(new Timestamp(System.currentTimeMillis()));
		threadLocal.set(jrlb);
	}
	
	private void afterExecute(JobExecutionContext context,Exception e){
		JobRunLogBean jrlb = threadLocal.get();
		jrlb.setEndTime(new Timestamp(System.currentTimeMillis()));
		if(e == null)
			jrlb.setRunResult("1");
		else
			jrlb.setRunResult("2");
		
		try{
			jobRunlogDao.insert(jrlb);			
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	public abstract void doExecute(JobExecutionContext context) throws JobExecutionException;
	
}
