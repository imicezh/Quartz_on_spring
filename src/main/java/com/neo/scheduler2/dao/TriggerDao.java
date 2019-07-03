package com.neo.scheduler2.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neo.scheduler2.bean.TriggerBean;

public interface TriggerDao extends BaseMapper<TriggerBean>{

	int updateTriggersByPrimaryKey(String column,Object value,Integer[] primaryKeys);
}
