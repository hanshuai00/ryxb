package com.zj.ryxb.common.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSON;
import com.zj.ryxb.common.util.PropertyUtils;

/**
 * 
 * @author wanghuaiqiang
 * 
 */
public class CommonJedisFactory {
	private static Logger logger = LoggerFactory.getLogger(CommonJedisFactory.class);
	
	private static final TreeMap<Integer, JedisPool> VM_JEDIS_POOL_MAP = new TreeMap<Integer, JedisPool>();

	private static List<CommonJedisPoolConfig> commonJedisPoolConfigList = new ArrayList<CommonJedisPoolConfig>(0);
	
	private static int dbIndex = 0;
	
	/**
	 * 实例化redis缓存数据
	 */
	public void initJedisPoolCluster() {
		String configStr = PropertyUtils.getProperty("redis.cluster.config");
		if (configStr == null) {
			configStr = "";
		}
		//读取并实例化redi缓存池
		commonJedisPoolConfigList = JSON.parseArray(configStr, CommonJedisPoolConfig.class);
		
		chkRefreshJedisPoolCluster();
		
		dbIndex =Integer.valueOf(PropertyUtils.getProperty("redis.db.index")) ;
	}

	public static JedisPool getJedisPool(String key) {
		if (key == null) {
			return null;
		}
		if (VM_JEDIS_POOL_MAP.isEmpty()) {
			return null;
		}

		int keyHashCode = getRealRedisHashCode(key.hashCode());

		Set<Integer> keySet = VM_JEDIS_POOL_MAP.keySet();

		JedisPool firstJedisPool = null;
		int mapIndex = 0;

		Iterator<Integer> iter = keySet.iterator();
		while (iter.hasNext()) {
			Integer mapKey = iter.next();
			if (mapIndex == 0) {
				firstJedisPool = VM_JEDIS_POOL_MAP.get(mapKey);
			}
			mapIndex++;

			if (mapKey > keyHashCode) {
				return VM_JEDIS_POOL_MAP.get(mapKey);
			}
		}
		
		return firstJedisPool;
	}
	
	public static int getDbIndex() {
		return dbIndex;
	}
	
	
	/**
	 * 检查刷新redis连接池
	 */
	public static void chkRefreshJedisPoolCluster() {
		// 读取配置并更新信息
		try {
			refreshJedisPoolCluster(commonJedisPoolConfigList);
		} catch (Exception e) {
			logger.error("解析json字符串出错:", e);
		}
		
	}

	// 需要改成private
	private static void refreshJedisPoolCluster(List<CommonJedisPoolConfig> commonJedisPoolConfigList) {
		if ((commonJedisPoolConfigList == null) || (commonJedisPoolConfigList.isEmpty())) {
			return;
		}

		// 停止现有的JedisPool
		Set<Integer> keySet = VM_JEDIS_POOL_MAP.keySet();
		Iterator<Integer> iter = keySet.iterator();
		while (iter.hasNext()) {
			Integer key = iter.next();
			JedisPool jedisPool = VM_JEDIS_POOL_MAP.get(key);
			if (jedisPool != null) {
				jedisPool.destroy();
			}
			VM_JEDIS_POOL_MAP.remove(key);
		}

		// 建立新的JedisPool
		for (Iterator<CommonJedisPoolConfig> iterator = commonJedisPoolConfigList.iterator(); iterator.hasNext();) {
			CommonJedisPoolConfig commonJedisPoolConfig = (CommonJedisPoolConfig) iterator.next();
			if (commonJedisPoolConfig == null) {
				continue;
			}

			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(commonJedisPoolConfig.getMaxActive());
			config.setMaxIdle(commonJedisPoolConfig.getMaxIdle());
			config.setMaxWait(commonJedisPoolConfig.getMaxWait());
			JedisPool pool = new JedisPool(config, commonJedisPoolConfig.getServer(), commonJedisPoolConfig.getPort());
			if (pool != null) {
				VM_JEDIS_POOL_MAP.put(getRealRedisHashCode(commonJedisPoolConfig.getCacheRangePoint()), pool);
			}
		}
	}

	private static int getRealRedisHashCode(int hashCode) {
		return hashCode % CommonJedisPoolConfig.REDIS_KEY_HASH_CODE_MAX_VALUE;
	}
}
