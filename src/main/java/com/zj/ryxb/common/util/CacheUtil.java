package com.zj.ryxb.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.ryxb.model.BDictionary;
import com.zj.ryxb.service.DictionaryService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
/**
 * 缓存数据工具类
 * @ClassName: CacheUtil 
 * @Author： zhang   
 * @Date： 2016-8-12 上午11:05:33
 */
public class CacheUtil {
	private static final Logger log = LoggerFactory.getLogger(CacheUtil.class);
	
	/**
	 * 调用缓存方法
	 * @Title:CacheUtil
	 * @return
	 * @author xgq
	 * 2016年6月23日
	 */
	public static Cache getCache(){
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("platformCache");
		return cache;
	}
	
	
	/*----------------- 加载数据到缓存中 start -----------------*/
	/**
	 * 将数据字典值存入缓存中
	* @Title: putDictionaryToCache 
	* @param dictionaryService
	* @param dictionarylists
	 */
	public static void putDictionaryToCache(DictionaryService dictionaryService,List<BDictionary> dictionarylists){
		//从EHCache中获取缓存对象
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("platformCache");
		//判断是否有‘dictionaryList’缓存数据，如果没有新建
		Element element = cache.get("dictionaryList");
		if (element == null) {
			element = new Element("dictionaryList", new ConcurrentHashMap<Integer, List<BDictionary>>());
		}
		@SuppressWarnings("unchecked")
		Map<String, List<BDictionary>> map = (Map<String, List<BDictionary>>) element.getObjectValue();
		map.clear();
		if(dictionarylists!=null && dictionarylists.size()>0){
			for(int i=0;i<dictionarylists.size();i++){
				BDictionary domain = dictionarylists.get(i);
				List<BDictionary> codelists = null;
				if (map.containsKey(domain.getDicCode())) {
					codelists = map.get(domain.getDicCode());
				} else {
					codelists = new ArrayList<BDictionary>();
				}
				codelists = dictionaryService.findByParentCode(domain.getDicCode());
				map.put(domain.getDicCode(), codelists);
			}
		}
		//放入缓存
		cache.put(element);	
	}
	
	/*----------------- 加载数据到缓存中 end -----------------*/
	
	/**
	 * 根据父类code值，获取子类集合
	* @Title: getCodelist 
	* @param kindCode
	* @return
	 */
	public static List<BDictionary> getCodelist(String kindCode) {
		//从EHCache中获取缓存对象
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("platformCache");
		net.sf.ehcache.Element element = cache.get("dictionaryList");
		if (element == null) {
			log.error("从ehcache中获取dictionaryList失败");
			return null;
		}
		@SuppressWarnings("unchecked")
		Map<String, List<BDictionary>> map = (Map<String, List<BDictionary>>) element.getObjectValue();
		if (map == null) {
			log.error("获取dictionaryList缓存失败");
			return null;
		}
		List<BDictionary> dictionarylists =map.get(kindCode);
		return dictionarylists;
	}
	
	/**
	 * 根据父类代码和子类代码取代码名称
	 * @param kindCode 父类code
	 * @param dicCode 子类code
	 * @return
	 */
	public static String getValueByCode(String kindCode, String dicCode) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("platformCache");
		net.sf.ehcache.Element element = cache.get("dictionaryList");
		if (element == null) {
			log.error("从ehcache中获取dictionaryList失败");
			return null;
		}
		@SuppressWarnings("unchecked")
		Map<String, List<BDictionary>> map = (Map<String, List<BDictionary>>) element.getObjectValue();
		if (map == null) {
			log.error("获取codelist缓存失败");
			return null;
		}
		String dicValue = null;
		List<BDictionary> dictionarylists = map.get(kindCode);	
		if (dictionarylists != null &&dictionarylists.size() > 0) {
			for(int i=0;i<dictionarylists.size();i++){
				BDictionary domain = dictionarylists.get(i);
				if(dicCode.equals(domain.getDicCode())){
					dicValue =domain.getDicValue();
					break;
				}
			}
		}
		return dicValue;
	}
	
}
