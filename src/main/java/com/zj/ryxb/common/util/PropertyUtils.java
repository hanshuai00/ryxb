package com.zj.ryxb.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @ClassName: PropertyUtils
* @Description:(调用接口访问路径)
* @author jxj
* @date 2015-8-7 - 上午9:40:09
* @version : 1.0
 */
public class PropertyUtils {

	private static final Logger log = LoggerFactory.getLogger(PropertyUtils.class);

	private static Properties prop = new Properties();
	static {
		InputStream in = PropertyUtils.class.getResourceAsStream("/config/config.properties");
		try {
			prop.load(in);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得配置文件 /config/config.properties 对应的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getProperty(String key, String defaultValue) {
		String value = prop.getProperty(key, defaultValue);
		log.info("property key is :{},value is :{}", key, value);
		return value;

	}

	public static String getProperty(String key) {
		String value = prop.getProperty(key);
		log.info("property key is :{},value is :{}", key, value);
		return value;
	}

}
