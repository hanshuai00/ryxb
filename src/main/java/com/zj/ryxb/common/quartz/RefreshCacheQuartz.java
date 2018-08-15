package com.zj.ryxb.common.quartz;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.zj.ryxb.service.DictionaryService;

/**
 * 
 * @author zxf
 *
 */
public class RefreshCacheQuartz  implements Job {
	
	
	@Autowired
	private DictionaryService dictionaryService;
	
	
	@Override
	public void execute(JobExecutionContext paramJobExecutionContext)
			throws JobExecutionException {
		
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("platformCache");
		
		//判断缓存为空，如果为空说明数据已清空
		//cache.removeAll();
		
		//部门数据放入缓存
		//Element element1 = this.getCategory();
		//cache.put(element1);
	}


	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	

}
