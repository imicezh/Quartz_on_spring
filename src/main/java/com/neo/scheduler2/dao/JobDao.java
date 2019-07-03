package com.neo.scheduler2.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neo.scheduler2.bean.JobBean;

public interface JobDao extends BaseMapper<JobBean> {

	int updateJobsByPrimaryKey(String column,Object value,Integer[] primaryKeys);
}
