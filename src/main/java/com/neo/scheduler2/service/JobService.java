package com.neo.scheduler2.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.neo.scheduler2.bean.JobBean;

public interface JobService {

	List<JobBean> selectJobs(Wrapper<JobBean> condition);
	
	JobBean selectOneJob(Integer primaryKey);
	
	JobBean selectOneJob(Wrapper<JobBean> condition);
	
	int addOneJob(JobBean bean);
	
	int updateOneJob(JobBean bean);
	
	int updateJobs(String column,Object value,Integer[] primaryKeys);
	
	int deleteOneJob(Integer primaryKey);
}
