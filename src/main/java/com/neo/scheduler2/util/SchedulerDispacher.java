package com.neo.scheduler2.util;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neo.scheduler2.bean.JobBean;
import com.neo.scheduler2.bean.TriggerBean;
import com.neo.scheduler2.dao.JobDao;
import com.neo.scheduler2.dao.TriggerDao;


/**
 * search customized job, trigger and schedule them.
 * @author zhou
 */
public class SchedulerDispacher{

	private HashMap<String,TriggerBean> triggersMap = new HashMap<String, TriggerBean>();
	private HashMap<String,JobBean> jobsMap = new HashMap<String, JobBean>();
	
	//保存已注册的trigger和job
	private final HashMap<String, Trigger>   registedTriggers = new HashMap<String, Trigger>();
	private final HashMap<String, JobDetail> registedJobs     = new HashMap<String, JobDetail>();
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private JobDao jobDao;
	@Autowired
	private TriggerDao triggerDao;

	private Scheduler scheduler;
	
	@PostConstruct
	private void postConstruct() {
		
		System.out.println("-----------进入scheduler dispacher-----------");
		
		scheduler = schedulerFactoryBean.getObject();
		
		try {
			
			prepareQuartzEnvironment();		//做一些准备工作
			
			findTriggerEligible();			//找到需要运行的trigger
			
			scheduleJobAndTrigger();		//将job与trigger绑定到scheduler中去
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * do some preparation before find jobs and triggers.
	 */
	private void prepareQuartzEnvironment() throws SchedulerException{
		
		QueryWrapper<TriggerBean> queryCondition = new QueryWrapper<TriggerBean>();
		queryCondition.eq("state", "0");	//条件
		List<TriggerBean> trigBeans = triggerDao.selectList(queryCondition);
		
		for(TriggerBean tBean : trigBeans){
			
			TriggerKey triggerKey = new TriggerKey(tBean.getTriggerName(), tBean.getTriggerGroup());
			
			//不要运行的任务，如果quartz之前装载过，就卸载掉
			if( scheduler.checkExists(triggerKey) ){
				scheduler.unscheduleJob(triggerKey);
			}
			
		}
		
	}
	
	/**
	 * search eligible jobs and triggers
	 */
	private void findTriggerEligible() throws ClassNotFoundException, SchedulerException{
		QueryWrapper<TriggerBean> queryCondition = new QueryWrapper<TriggerBean>();
		queryCondition.eq("state", "1");	//条件
		
		List<TriggerBean> trigBeans = triggerDao.selectList(queryCondition);
		
		for(TriggerBean tBean : trigBeans){
			String tKey = 
				new StringBuffer().append(tBean.getTriggerGroup())
								  .append(".")
								  .append(tBean.getTriggerName())
								  .toString();
			
			triggersMap.put(tKey, tBean);
			
			//trigger绑定的job
			JobBean jBean = jobDao.selectById(tBean.getJobId());
			tBean.setJob(jBean);
			
			String jKey = new StringBuffer().append(jBean.getJobGroup())
					  .append(".")
					  .append(jBean.getJobName())
					  .toString();
			jobsMap.put(jKey, jBean);
			
		}
	}
	
	/**
	 * schedule jobs and triggers
	 * (not include the one which is already sheduled before but not unscheduled.)
	 */
	private void scheduleJobAndTrigger() throws ClassNotFoundException, SchedulerException{
		Set<String> keys = triggersMap.keySet();
		for(String key : keys){
			TriggerBean t = triggersMap.get(key);
			JobBean     j = jobsMap.get(t.getJob().getJobFullName());
			
			JobDetail job = null;
			Trigger trigger = null;
			
			JobKey     jKey = new JobKey(j.getJobName(), j.getJobGroup());
			TriggerKey tKey = new TriggerKey(t.getTriggerName(), t.getTriggerGroup());
			
			if( ifJobAlreadyExist(jKey) ){				//如果已注册
				job = scheduler.getJobDetail(jKey);
			}
			else{
				job = JobProducer.produceJob(j);
			}
			
			if( ifTriggerAlreadyExist(tKey) ){			//如果已注册
				trigger = scheduler.getTrigger(tKey);
			}
			else{
				trigger = TriggerProducer.produceTrigger(t);
			}
			
			if(!scheduler.checkExists(jKey) && !scheduler.checkExists(tKey)){
				scheduler.scheduleJob( job,trigger );	//只有新建的任务才需要绑定				
			}
			
			registedTriggers.put(t.getTriggerName(), trigger);
			registedJobs.put(j.getJobName(), job);
		}
	}

	
	/**
	 * if job is already sheduled
	 */
	private boolean ifJobAlreadyExist(JobKey jobKey) throws SchedulerException{
		return scheduler.checkExists(jobKey);
	}
	
	
	/**
	 * if trigger is already scheduled
	 */
	public boolean ifTriggerAlreadyExist(TriggerKey triggerKey) throws SchedulerException{
		return scheduler.checkExists(triggerKey);
	}
	

	/**
	 * get native Scheduler instance
	 * if you wanna do more then you need it
	 */
	public Scheduler getNativeScheduler(){
		return this.scheduler;
	}
}
