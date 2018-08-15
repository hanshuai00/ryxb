package com.zj.ryxb.common.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
/**
 * @Name:
 * @Author: lizhao（作者）
 * @Version: V1.00 （版本号）
 * @Create Date: 2015-11-26（创建日期）
 * @Description:
 */
public class KeyUtil {
	
    @SuppressWarnings("resource")
	public static Key getKey(ServletContext context) {
        String path=(context.getRealPath("/key"));
        File file=new File(path,"key.txt");
        try {
        if(!file.exists()){
           Key key =MacProvider.generateKey(SignatureAlgorithm.HS512);
            ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file));
            oo.writeObject(key);
            oo.close();
            return key;
        }
        ObjectInputStream ois = null;

            ois = new ObjectInputStream(new FileInputStream(file));

        Key key= (Key) ois.readObject();
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    /**
     * 获取当前XXX分钟后的日期
     * author：Lizhao
     * Date:15/12/12
     * version:1.0
     * @param minutes 分钟
     *
     * @return
     */
    public static Date getExpiryDate(int minutes) {
        // 根据当前日期，来得到到期日期
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }
    

    /**
	* 获取访问者IP
	* 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	* 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	* 如果还不存在则调用Request .getRemoteAddr()。
	* @param request
	* @return
	*/
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
			return ip.substring(0, index);
			} else {
			return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
}
