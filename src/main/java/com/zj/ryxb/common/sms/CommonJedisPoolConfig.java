package com.zj.ryxb.common.redis;

/**
 * 
 * @author wanghuaiqiang
 *
 */
public class CommonJedisPoolConfig {
	/**
	 * 用来计算具体存放在那台redis的最大hash值，大于该值进行模运算
	 */
	public static final int REDIS_KEY_HASH_CODE_MAX_VALUE = 10000;
	
	private String server = "";
	private int port = 6379;
	private int maxActive = 100;
	private int maxIdle = 10;
	private long maxWait = 1000l;
	
	/**
	 * 用来计算该机器存储数据的范围
	 */
	private int cacheRangePoint = 0;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public int getCacheRangePoint() {
		return cacheRangePoint;
	}

	public void setCacheRangePoint(int cacheRangePoint) {
		this.cacheRangePoint = cacheRangePoint;
	}

}
