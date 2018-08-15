package com.zj.ryxb.common.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 
 * Redis数据处理
 * @author wanghuaiqiang
 * 
 */
public class CommonJedisUtil {
	private static Logger logger = LoggerFactory.getLogger(CommonJedisFactory.class);
	
	/**
	 * 设置值
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, String value) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			logger.error("jedisPool is null");
			return false;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			jedis.set(key, value);
//			if(key!=null && key.equals("login.url")){
//				logger.error("jedisPool set key...........key="+key +"      value="+value);
//			}
			
			return true;
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 获取值信息
	 * @param key
	 * @return
	 */
	public static String get(String key) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return null;
		}

		Jedis jedis = null;
		boolean failed = false;
		String ret = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			ret = jedis.get(key);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
		return ret;
	}
	
	/***
	 * 获取参数
	 * @param key
	 * @return
	 */
	/*public static String get(String key, HttpServletRequest request){
		//先从redis中查，如果没有调用http接口访问
		if(request != null) {
			IpAndAddress ipAndAddredd = new IpAndAddress();
			String ip = ipAndAddredd.getinOrOutIp(request);
			if(ip.equals("out")){
				//外网
				key = key + ".out";
			} else if(ip.equals("remark")){
				//本地
				key = key + ".rd";
			}
    	}
		return get(key);
	}*/
	
	/**
	 * 设置有效期
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static boolean expire(String key, int seconds) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return false;
		}

		Jedis jedis = null;
		boolean failed = false;
		Long expireReturn = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			expireReturn = jedis.expire(key, seconds);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
		
		if ( (expireReturn != null) && (expireReturn == 1) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除
	 * @param key
	 */
	public static void del(String key) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return ;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			jedis.del(key);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 返回给定key的剩余生存时间(time to live)(以秒为单位) 
	 * @param key
	 * @return
	 */
	public static int ttl(String key) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return 0;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			Long seconds = jedis.ttl(key);
			if (seconds != null) {
				return seconds.intValue();
			}
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
		
		return 0;
	}
	
	/**
	 * 移除给定key的生存时间 
	 * @param key
	 * @return
	 */
	public static void persist(String key) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return ;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			jedis.persist(key);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 检查给定key是否存在
	 * @param key
	 * @return
	 */
	public static boolean exists(String key) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return false;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			return jedis.exists(key);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 将值value插入到列表key的表头
	 * @param key
	 * @param value
	 */
	public static void lpush(String key, String value) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return ;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			jedis.lpush(key, value);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
	 * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> lrange(String key, long start, long end) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return new ArrayList<String>(0);
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			return jedis.lrange(key, start, end);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 获取list长度
	 * @param key
	 * @return
	 */
	public static long llen(String key) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(key);
		if (jedisPool == null) {
			return 0;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			Long ret = jedis.llen(key);
			if (ret == null) {
				return 0;
			} else {
				return ret.longValue();
			}
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 将一个键值对存入hash表中
	 * @param hashKey
	 * @param fieldKey
	 * @param fieldValue
	 * @return
	 * @throws JedisConnectionException
	 */
	public static boolean hset(String hashKey, String fieldKey, String fieldValue) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(hashKey);
		if (jedisPool == null) {
			logger.error("jedisPool is null");
			return false;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			jedis.hset(hashKey, fieldKey, fieldValue);
			return true;
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 获取一个hash表中所有的键值对
	 * @param hashKey
	 * @return
	 * @throws JedisConnectionException
	 */
	public static Map<String,String> hgetAll(String hashKey) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(hashKey);
		if (jedisPool == null) {
			logger.error("jedisPool is null");
			return null;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			return jedis.hgetAll(hashKey);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 获取一个hash表中的某个键值对
	 * @param hashKey
	 * @param fieldKey
	 * @return
	 * @throws JedisConnectionException
	 */
	public static String hget(String hashKey, String fieldKey) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(hashKey);
		if (jedisPool == null) {
			logger.error("jedisPool is null");
			return null;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			return jedis.hget(hashKey, fieldKey);
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
	
	/**
	 * 删除一个hash表中的某个键值对
	 * @param hashKey
	 * @param fieldKey
	 * @return
	 * @throws JedisConnectionException
	 */
	public static boolean hdel(String hashKey, String fieldKey) throws JedisConnectionException {
		JedisPool jedisPool = CommonJedisFactory.getJedisPool(hashKey);
		if (jedisPool == null) {
			logger.error("jedisPool is null");
			return false;
		}

		Jedis jedis = null;
		boolean failed = false;
		try {
			jedis = jedisPool.getResource();
			jedis.select(CommonJedisFactory.getDbIndex());
			jedis.hdel(hashKey, fieldKey);
			return true;
		} catch (JedisConnectionException e) {
            failed = true;
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if ( (jedis != null) && (!failed) ) {
            	jedisPool.returnResource(jedis);
            }
        }
	}
}
