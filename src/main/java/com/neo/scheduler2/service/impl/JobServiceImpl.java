package com.neo.scheduler2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.neo.scheduler2.bean.JobBean;
import com.neo.scheduler2.dao.JobDao;
import com.neo.scheduler2.dao.TriggerDao;
import com.neo.scheduler2.service.JobService;

@Service
@Transactional
public class JobServiceImpl implements JobService {

	@Autowired
	private JobDao jobDao;
	@Autowired
	private TriggerDao triggerDao;
	
	@Override
	public List<JobBean> selectJobs(Wrapper<JobBean> condition) {
		return jobDao.selectList(condition);
	}

	@Override
	public JobBean selectOneJob(Integer primaryKey) {
		return jobDao.selectById(primaryKey);
	}
	
	@Override
	public JobBean selectOneJob(Wrapper<JobBean> condition) {
		return jobDao.selectOne(condition);
	}
	@Override
	public int addOneJob(JobBean bean) {
		return jobDao.insert(bean);
	}

	@Override
	public int updateOneJob(JobBean bean) {
		return jobDao.updateById(bean);
	}

	@Override
	public int updateJobs(String column, Object value, Integer[] primaryKeys) {
		return jobDao.updateJobsByPrimaryKey(column, value, primaryKeys);
	}

	@Override
	public int deleteOneJob(Integer primaryKey) {
		return jobDao.deleteById(primaryKey);
	}

}
