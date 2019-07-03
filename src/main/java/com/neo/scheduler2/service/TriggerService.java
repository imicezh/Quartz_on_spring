package com.neo.scheduler2.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.neo.scheduler2.bean.TriggerBean;

public interface TriggerService {

	List<TriggerBean> selectTriggers(Wrapper<TriggerBean> condition);
	
	TriggerBean selectOneTrigger(Integer primaryKey);
	
	TriggerBean selectOneTrigger(Wrapper<TriggerBean> condition);
	
	int addOneTrigger(TriggerBean bean);
	
	int updateOneTrigger(TriggerBean bean);
	
	int updateTriggers(String column,Object value,Integer[] primaryKeys);
	
	int deleteOneTrigger(Integer primaryKey);
}
