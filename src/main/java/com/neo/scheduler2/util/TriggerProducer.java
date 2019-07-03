package com.neo.scheduler2.util;


import org.quartz.CronScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.neo.scheduler2.bean.TriggerBean;



/**
 * produce trigger
 */
public class TriggerProducer {

	public static final String TRIGGER_SIMPLE = "1";
	public static final String TRIGGER_CRON = "2";
	
	public static final String INTERVAL_SECOND = "second";
	public static final String INTERVAL_MINUTE = "minute";
	public static final String INTERVAL_HOUR   = "hour";
	
	
	/**
	 * 生成trigger
	 *     支持两种：simple 和 cron
	 * 
	 * @author zhou
	 * @time   上午10:14:08
	 */
	public static Trigger produceTrigger(TriggerBean definition){
		if(definition==null){
			throw new RuntimeException("---- QrtzTriggerBean 不可为空!----");
		}
		
		if(TRIGGER_SIMPLE.equals(definition.getTriggerType())){
			return produceSimple(definition);
		}
		else if(TRIGGER_CRON.equals(definition.getTriggerType())){
			return produceCron(definition);
		}else{
			return null;
		}
		
	}
	
	
	public static Trigger produceSimple(TriggerBean definition){
		TriggerBuilder<Trigger> tirggerBuilder = TriggerBuilder.newTrigger();
		SimpleScheduleBuilder schedulerBuilder = SimpleScheduleBuilder.simpleSchedule();
		
		//重复次数 0不执行  <0无穷次
		schedulerBuilder.withRepeatCount(definition.getSimpleRepeatCount());
		
		//重复间隔
		switch(definition.getSimpleRepeatType()){
			case INTERVAL_SECOND:
				schedulerBuilder.withIntervalInSeconds(definition.getSimpleInterval());
				break;
			case INTERVAL_MINUTE:
				schedulerBuilder.withIntervalInMinutes(definition.getSimpleInterval());
				break;
			case INTERVAL_HOUR:
				schedulerBuilder.withIntervalInHours(definition.getSimpleInterval());
				break;
			default:
				break;
		}
		
		//熄火策略
		switch(definition.getSimpleMisfirePolicy()){
			case "1":
				schedulerBuilder.withMisfireHandlingInstructionFireNow();
				break;
			case "2":
				schedulerBuilder.withMisfireHandlingInstructionNowWithExistingCount();
				break;
			case "3":
				schedulerBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
				break;
			case "4":
				schedulerBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
				break;
			case "5":
				schedulerBuilder.withMisfireHandlingInstructionNextWithExistingCount();
				break;
			default:
				break;
		}
		
		tirggerBuilder.withIdentity(definition.getTriggerName(), "default");
		tirggerBuilder.startAt(definition.getStartTime());
		if(definition.getEndTime()!=null){
			tirggerBuilder.endAt(definition.getEndTime());			
		}
		tirggerBuilder.withSchedule(schedulerBuilder);
		
		return tirggerBuilder.build();
	}
	
	
	public static Trigger produceCron(TriggerBean definition){
		TriggerBuilder<Trigger> tirggerBuilder = TriggerBuilder.newTrigger();
		CronScheduleBuilder conScheduleBuilder = CronScheduleBuilder.cronSchedule(definition.getCronFormula());
		
		//熄火策略
		switch(definition.getCronMisfirePolicy()){
		case "1":
			conScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
			break;
		case "2":
			conScheduleBuilder.withMisfireHandlingInstructionDoNothing();
			break;
		default:
			break;
		}
		tirggerBuilder.withIdentity(definition.getTriggerName(), "default");
		tirggerBuilder.withSchedule(conScheduleBuilder);
		
		return tirggerBuilder.build();
	}
}
