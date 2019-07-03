package com.neo.scheduler2.web.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neo.scheduler2.bean.JobBean;
import com.neo.scheduler2.bean.TriggerBean;
import com.neo.scheduler2.service.JobService;
import com.neo.scheduler2.service.TriggerService;
import com.neo.scheduler2.util.JobProducer;
import com.neo.scheduler2.util.SchedulerDispacher;
import com.neo.scheduler2.util.TriggerProducer;
import com.neo.scheduler2.web.msg.Msg;

import static com.neo.scheduler2.web.dict.Status.*;

/**
 * console
 *   - get trigger state
 *   - pause a trigger
 *   - resume a trigger
 *   - load a new trigger
 *   - unload a trigger
 *   - fire a trigger instantly
 */
@RestController
@RequestMapping("/console")
public class ConsoleController {
	
	@Autowired
	private SchedulerDispacher scheDispacher;
	@Autowired
	private TriggerService triggerService;
	@Autowired
	private JobService jobService;
	
	/**
	 * 获取某一个触发器当前状态
	 * @author zhou
	 */
	@GetMapping(value="/triggerState")
	public Msg getATriggerState(
			@RequestParam("triggerName") String triggerName,
			@RequestParam("triggerGroup") String triggerGroup
			) throws SchedulerException{
		Msg msg = new Msg();
		
		Scheduler scheduler = scheDispacher.getNativeScheduler();
		TriggerState state = scheduler.getTriggerState(new TriggerKey(triggerName,triggerGroup));
		
		msg.setCode(SUCCESS.code());
		msg.setMsg(SUCCESS.msg());
		msg.setData(state.toString());
		return msg;
	}
	
	/**
	 * 获取多个触发器当前状态
	 * @author zhou
	 */
	@GetMapping(value="/triggerStates")
	public Msg getTriggerState(
			@RequestParam("triggerIds") Integer[] triggerIds
			) throws SchedulerException{
		Msg msg = new Msg();
		
		QueryWrapper query = new QueryWrapper();
		query.in("trigger_id", triggerIds);
		List<TriggerBean> triggers = triggerService.selectTriggers(query);
		
		Scheduler scheduler = scheDispacher.getNativeScheduler();
		Map<Integer,String> states = new HashMap<Integer, String>();
		TriggerKey key = null;
		for(TriggerBean qtb : triggers){
			key = new TriggerKey(qtb.getTriggerName(), qtb.getTriggerGroup());
			states.put(qtb.getTriggerId(), scheduler.getTriggerState(key).toString());
		}
		
		msg.setCode(SUCCESS.code());
		msg.setMsg(SUCCESS.msg());
		msg.setData(states);
		return msg;
	}
	
	/**
	 * 暂停某一个触发器
	 * @author zhou
	 */
	@PutMapping(value="/trigger/pause")
	public Msg pauseTriggerImmediately(
			@RequestParam("triggerName") String triggerName,
			@RequestParam("triggerGroup") String triggerGroup
			) throws SchedulerException{
		Msg msg = new Msg();
		
		TriggerKey key = new TriggerKey(triggerName, triggerGroup);
		
		if(scheDispacher.ifTriggerAlreadyExist(key)){	//存在
			scheDispacher.getNativeScheduler().pauseTrigger(key);
			msg.setCode(SUCCESS.code());
			msg.setMsg("暂停成功！");
		}else{
			msg.setCode(FAIL.code());		//不存在
			msg.setMsg("触发器不存在！暂停失败！");
		}
		
		return msg;
	}
	
	/**
	 * 唤醒某一个触发器
	 * @author zhou
	 */
	@PutMapping(value="/trigger/awaken")
	public Msg awakenTriggerImmediately(
			@RequestParam("triggerName") String triggerName,
			@RequestParam("triggerGroup") String triggerGroup
			) throws SchedulerException{
		Msg msg = new Msg();
		
		TriggerKey key = new TriggerKey(triggerName, triggerGroup);
		Scheduler scheduler = scheDispacher.getNativeScheduler();
		if("PAUSED".equals(scheduler.getTriggerState(key).toString())){
			scheduler.resumeTrigger(key);
			msg.setCode(SUCCESS.code());
			msg.setMsg("唤醒成功");
		}
		else{
			msg.setCode(FAIL.code());
			msg.setMsg("触发器不是暂停状态！无法唤醒！");
		}
		return msg;
	}
	
	/**
	 * 卸载触发器
	 * @author zhou
	 */
	@DeleteMapping(value="/trigger/unload")
	public Msg unloadTriggerImmediately(
			@RequestParam("triggerName") String triggerName,
			@RequestParam("triggerGroup") String triggerGroup
			) throws SchedulerException{
		Msg msg = new Msg();
		
		TriggerKey key = new TriggerKey(triggerName, triggerGroup);
		Scheduler scheduler = scheDispacher.getNativeScheduler();

		boolean bol = scheduler.unscheduleJob(key);
		if(bol){
			//将触发器置为无效
			QueryWrapper query = new QueryWrapper();
			query.eq("trigger_name", triggerName);
			query.eq("trigger_group", triggerGroup);
			TriggerBean trigger = triggerService.selectOneTrigger(query);
			trigger.setState("0");
			triggerService.updateOneTrigger(trigger);
			
			msg.setCode(SUCCESS.code());
			msg.setMsg("卸载成功！");
		}
		else{
			msg.setCode(FAIL.code());
			msg.setMsg("卸载失败！");
		}
		return msg;
	}
	
	/**
	 * 装载触发器
	 * @author zhou
	 */
	@PostMapping(value="/trigger/load")
	public Msg loadTriggerImmediately(
			@RequestParam("triggerName") String triggerName,
			@RequestParam("triggerGroup") String triggerGroup
			) throws SchedulerException, ClassNotFoundException{
		
		Msg msg = new Msg();
		
		QueryWrapper query = new QueryWrapper();
		query.eq("trigger_name", triggerName);
		query.eq("trigger_group", triggerGroup);
		TriggerBean trigDefine = triggerService.selectOneTrigger(query);
		
		if(trigDefine==null){
			msg.setCode(FAIL.code());
			msg.setMsg("找不到触发器！");
			return msg;
		}
		JobBean jobDefine = jobService.selectOneJob(trigDefine.getJobId());
		
		if(jobDefine == null){
			msg.setCode(FAIL.code());
			msg.setMsg("找不到JOB！");
			return msg;
		}
		
		Trigger trigger = TriggerProducer.produceTrigger(trigDefine);
		JobDetail job   = JobProducer.produceJob(jobDefine);
		
		Scheduler scheduler = scheDispacher.getNativeScheduler();
		scheduler.scheduleJob(job, trigger);
		
		//修改触发器的状态
		trigDefine.setState("1");
		triggerService.updateOneTrigger(trigDefine);
		msg.setCode(SUCCESS.code());
		msg.setMsg("装配触发器成功！");
		
		return msg;
	}
	
	/**
	 * 让某一个触发器对应的Job执行一次
	 * 可以通过trigger将参数传给job使用
	 * @author zhou
	 * @throws SchedulerException 
	 */
	@PostMapping("/trigger/runOnce/{triggerId}")
	public Msg fireOneTriggerOnce(
			@PathVariable(name="triggerId") Integer triggerId,
			@RequestBody Map<String,Object> triggerDataMap
			) throws SchedulerException{
		Msg msg = new Msg();
		
		TriggerBean triggerBean = triggerService.selectOneTrigger(triggerId);
		JobBean jobBean = jobService.selectOneJob(triggerBean.getJobId());
		
		Scheduler scheduler = scheDispacher.getNativeScheduler();
		Trigger trigger = scheduler.getTrigger(new TriggerKey(triggerBean.getTriggerName(),triggerBean.getTriggerGroup()));
		
		if(trigger==null){
			msg.setCode(FAIL.code());
			msg.setMsg("失败：没有找到触发器！");
			return msg;
		}
		
		//将参数传入trigger，可以在job中获取到
		if(!triggerDataMap.isEmpty()){
			JobDataMap jMap = new JobDataMap(triggerDataMap);
			trigger.getTriggerBuilder().usingJobData(jMap);
		}
		
		//执行JOB
		scheduler.triggerJob(new JobKey(jobBean.getJobName(),jobBean.getJobGroup()));
		
		msg.setCode(SUCCESS.code());
		msg.setMsg("触发成功!");
		return msg;
	}
}
