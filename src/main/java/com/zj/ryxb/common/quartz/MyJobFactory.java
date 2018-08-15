package com.zj.ryxb.common.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
/**
 * 自定义JobFactory，通过spring的AutowireCapableBeanFactory进行注入
 * 解决定时调度Job无法Autowired注入Service的方案 
 * @Author： xf   
 * @Date： 2015-11-24 下午1:59:21
 */
public class MyJobFactory extends SpringBeanJobFactory {
	
	 @Autowired
    private AutowireCapableBeanFactory beanFactory;

    /**
     * 这里覆盖了super的createJobInstance方法，对其创建出来的类再进行autowire。
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        beanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
