package com.neo.scheduler2.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.neo.scheduler2.bean.TriggerBean;
import com.neo.scheduler2.dao.JobDao;
import com.neo.scheduler2.dao.TriggerDao;
import com.neo.scheduler2.service.TriggerService;

@Service
@Transactional
public class TriggerServiceImpl implements TriggerService {

	@Autowired
	private TriggerDao triggerDao;
	@Autowired
	private JobDao jobDao;
	
	@Override
	public List<TriggerBean> selectTriggers(
			Wrapper<TriggerBean> condition) {
		return triggerDao.selectList(condition);
	}

	@Override
	public TriggerBean selectOneTrigger(Integer primaryKey) {
		return triggerDao.selectById(primaryKey);
	}
	
	@Override
	public TriggerBean selectOneTrigger(Wrapper<TriggerBean> condition) {
		return triggerDao.selectOne(condition);
	}
	
	@Override
	public int addOneTrigger(TriggerBean bean) {
		return triggerDao.insert(bean);
	}

	@Override
	public int updateOneTrigger(TriggerBean bean) {
		return triggerDao.updateById(bean);
	}

	@Override
	public int updateTriggers(String column, Object value,@Param("primaryKeys") Integer[] primaryKeys) {
		return triggerDao.updateTriggersByPrimaryKey(column, value, primaryKeys);
	}

	@Override
	public int deleteOneTrigger(Integer primaryKey) {
		
		TriggerBean trigger = triggerDao.selectById(primaryKey);
		if(!StringUtils.isEmpty(trigger.getJobId())){
			jobDao.deleteById(trigger.getJobId());
		}
		return triggerDao.deleteById(primaryKey);
	}

}
