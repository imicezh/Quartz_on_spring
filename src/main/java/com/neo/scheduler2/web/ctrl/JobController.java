package com.neo.scheduler2.web.ctrl;


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
import com.neo.scheduler2.service.JobService;
import com.neo.scheduler2.service.TriggerService;
import com.neo.scheduler2.web.msg.Msg;

import static com.neo.scheduler2.web.dict.Status.*;

/**
 * sys_qrtz_job table CRUD
 */
@RestController
@RequestMapping("/jobCtrl")
public class JobController {
	
	@Autowired
	private JobService jobService;
	@Autowired
	private TriggerService triggerService;
	
	/**
	 * 查询许多
	 * @author zhou
	 */
	@GetMapping(value="/jobs")
	public Msg queryJobs(
			){
		Msg msg = new Msg();
		
		msg.setData(jobService.selectJobs(null));
		msg.setCode(SUCCESS.code());
		msg.setMsg("查询成功!");
		
		return msg;
	}
	
	/**
	 * 查询一个
	 * @author zhou
	 */
	@GetMapping(value="/job")
	public Msg queryOneJob(
			@RequestParam("jobName") String jobName,
			@RequestParam("jobGroup") String jobGroup
			){
		Msg msg = new Msg();
		
		QueryWrapper condition = new QueryWrapper();
		condition.eq("job_name", jobName);
		condition.eq("job_group", jobGroup);
		
		msg.setData(jobService.selectOneJob(condition));
		msg.setCode(SUCCESS.code());
		msg.setMsg("查询成功!");
		
		return msg;
	}
	
	/**
	 * 新增一个
	 * @author zhou
	 */
	@PostMapping(value="/job")
	public Msg addOneTrigger(
			@RequestBody JobBean bean
			){
		Msg msg = new Msg();
		
		try {
			this.getClass().getClassLoader().loadClass(bean.getJobClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			msg.setCode(FAIL.code());
			msg.setMsg("新增失败!系统内找不到Job的Class");
			return msg;
		}
		
		bean.setJobGroup("default");
		msg.setData(jobService.addOneJob(bean));
		msg.setCode(SUCCESS.code());
		msg.setMsg("新增成功!");
		
		return msg;
	}
	
	/**
	 * 修改一个
	 * @author zhou
	 */
	@PutMapping(value="/job")
	public Msg updateOneTrigger(
			@RequestBody JobBean bean
			){
		Msg msg = new Msg();
		
		msg.setData(jobService.updateOneJob(bean));
		msg.setCode(SUCCESS.code());
		msg.setMsg("修改成功!");
		
		return msg;
	}
	
	/**
	 * 删除一个
	 * @author zhou
	 */
	@DeleteMapping(value="/job/{jobId}")
	public Msg updateOneTrigger(
			@PathVariable(name="jobId") Integer jobId
			){
		Msg msg = new Msg();
		
		//检查是否已绑定trigger
		QueryWrapper condition = new QueryWrapper();
		condition.eq("job_id", jobId);
		if(!triggerService.selectTriggers(condition).isEmpty()){
			msg.setCode(FAIL.code());
			msg.setMsg("Job已绑定触发器,不能删除!");
			return msg;
		}
		
		msg.setData(jobService.deleteOneJob(jobId));
		msg.setCode(SUCCESS.code());
		msg.setMsg("新增成功!");
		
		return msg;
	}
	
}