package com.neo.scheduler2.bean;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@TableName(value="sys_qrtz_trigger")
public class TriggerBean {

	@TableId(type=IdType.AUTO)
	private Integer triggerId;
	private String  triggerTitle;
	private String  triggerName;
	private String  triggerGroup;
	
	private String  triggerType;
	
	private Integer simpleRepeatCount;
	private String  simpleRepeatType;
	
	private Integer simpleInterval;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp startTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;
	
	private String  simpleMisfirePolicy;
	
	private String  cronMisfirePolicy;
	private String  cronFormula;
	
	private Integer jobId;
	
	private String  state;
	
	@TableField(exist=false)
	@JsonIgnore					//此字段无需序列化
	private JobBean job;
	
	
	public Integer getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(Integer triggerId) {
		this.triggerId = triggerId;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public Integer getSimpleRepeatCount() {
		return simpleRepeatCount;
	}

	public void setSimpleRepeatCount(Integer simpleRepeatCount) {
		this.simpleRepeatCount = simpleRepeatCount;
	}

	public String getSimpleRepeatType() {
		return simpleRepeatType;
	}

	public void setSimpleRepeatType(String simpleRepeatType) {
		this.simpleRepeatType = simpleRepeatType;
	}

	public Integer getSimpleInterval() {
		return simpleInterval;
	}

	public void setSimpleInterval(Integer simpleInterval) {
		this.simpleInterval = simpleInterval;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getSimpleMisfirePolicy() {
		return simpleMisfirePolicy;
	}

	public void setSimpleMisfirePolicy(String simpleMisfirePolicy) {
		this.simpleMisfirePolicy = simpleMisfirePolicy;
	}

	public String getCronMisfirePolicy() {
		return cronMisfirePolicy;
	}

	public void setCronMisfirePolicy(String cronMisfirePolicy) {
		this.cronMisfirePolicy = cronMisfirePolicy;
	}

	public String getCronFormula() {
		return cronFormula;
	}

	public void setCronFormula(String cronFormula) {
		this.cronFormula = cronFormula;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return state;
	}
	public String getTriggerTitle() {
		return triggerTitle;
	}

	public void setTriggerTitle(String triggerTitle) {
		this.triggerTitle = triggerTitle;
	}

	public JobBean getJob() {
		return job;
	}

	public void setJob(JobBean job) {
		this.job = job;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
}
