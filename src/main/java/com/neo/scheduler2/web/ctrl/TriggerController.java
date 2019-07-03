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
import com.neo.scheduler2.bean.TriggerBean;
import com.neo.scheduler2.service.TriggerService;
import com.neo.scheduler2.web.msg.Msg;

import static com.neo.scheduler2.web.dict.Status.*;

/**
 * sys_qrtz_trigger table CRUD
 */
@RestController
@RequestMapping("/triggerCtrl")
public class TriggerController {
	
	@Autowired
	private TriggerService triggerService;
	
	/**
	 * 查询许多
	 * @author zhou
	 */
	@GetMapping(value="/triggers")
	public Msg queryTriggers(
			@RequestParam("state") String state
			){
		Msg msg = new Msg();
		
		QueryWrapper<TriggerBean> condition = new QueryWrapper<TriggerBean>();
		condition.eq("state", state);
		
		msg.setData(triggerService.selectTriggers(condition));
		msg.setCode(SUCCESS.code());
		msg.setMsg("查询成功!");
		
		return msg;
	}
	
	/**
	 * 查询一个
	 * @author zhou
	 */
	@GetMapping(value="/trigger")
	public Msg queryOneTrigger(
			@RequestParam("triggerName") String triggerName,
			@RequestParam("triggerGroup") String triggerGroup
			){
		Msg msg = new Msg();
		
		QueryWrapper<TriggerBean> condition = new QueryWrapper<TriggerBean>();
		condition.eq("trigger_name", triggerName);
		condition.eq("trigger_group", triggerGroup);
		msg.setData(triggerService.selectOneTrigger(condition));
		msg.setCode(SUCCESS.code());
		msg.setMsg("查询成功!");
		
		return msg;
	}
	
	/**
	 * 新增一个
	 * @author zhou
	 */
	@PostMapping(value="/trigger")
	public Msg addOneTrigger(
			@RequestBody TriggerBean bean
			){
		Msg msg = new Msg();
		
		bean.setTriggerGroup("default");
		bean.setState("0");
		msg.setData(triggerService.addOneTrigger(bean));
		msg.setCode(SUCCESS.code());
		msg.setMsg("新增成功!");
		
		return msg;
	}
	
	/**
	 * 修改一个
	 * @author zhou
	 */
	@PutMapping(value="/trigger")
	public Msg updateOneTrigger(
			@RequestBody TriggerBean bean
			){
		Msg msg = new Msg();
		
		bean.setState(null);	//不允许在这里修改triggre的状态
		msg.setData(triggerService.updateOneTrigger(bean));
		msg.setCode(SUCCESS.code());
		msg.setMsg("修改成功!");
		
		return msg;
	}
	
	/**
	 * 删除一个
	 * @author zhou
	 */
	@DeleteMapping(value="/trigger/{triggerId}")
	public Msg deleteOneTrigger(
			@PathVariable(name="triggerId") Integer triggerId
			){
		Msg msg = new Msg();
		
		msg.setData(triggerService.deleteOneTrigger(triggerId));
		msg.setCode(SUCCESS.code());
		msg.setMsg("删除成功!");
		
		return msg;
	}
	
}
