package com.neo.scheduler2.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * inject job instance Quartz created into Spring container,then you can inject 
 * spring component in your job implement.
 * @author zhou
 */
public class AutowiredJobFactory extends AdaptableJobFactory {

	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		capableBeanFactory.autowireBean(jobInstance);	//
		return jobInstance;
	}
}
