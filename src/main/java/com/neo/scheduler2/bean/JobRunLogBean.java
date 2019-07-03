package com.neo.scheduler2.bean;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName(value="sys_qrtz_job_runlog")
public class JobRunLogBean {

	private String jobKey;
	private String triggerKey;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp startTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp endTime;
	
	private String host;
	private String runResult;
	
	public String getJobKey() {
		return jobKey;
	}
	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}
	public String getTriggerKey() {
		return triggerKey;
	}
	public void setTriggerKey(String triggerKey) {
		this.triggerKey = triggerKey;
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getRunResult() {
		return runResult;
	}
	public void setRunResult(String runResult) {
		this.runResult = runResult;
	}
	
}
