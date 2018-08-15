package com.zj.ryxb.common.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;

import org.apache.commons.lang.StringUtils;

import com.zj.ryxb.common.redis.CommonJedisUtil;
/** 
* @ClassName: TokenUtil 
* @Description:(token管理工具类) 
* @author lizhijun
* @date 2016年8月11日 下午12:57:44 
*  
*/


@PermitAll
public class TokenUtil {
	//全局用户map
	public static Map<String,String> onlineMap = new HashMap<String,String>();
	
	/**
	 * 生成保存token
	 * @param tel
	 * @param expires token有效期
	 * @param key 安全信息
	 * @param userId 用户id
	 * @param claims token信息
	 * @return
	 */
    public static String getJWTString(String tel,Date expires,Key key,
    						String userId,Map<String,Object> claims){
        if (tel == null) {
            throw new NullPointerException("null username is illegal");
        }
        if (expires == null) {
            throw new NullPointerException("null expires is illegal");
        }
        if (key == null) {
            throw new NullPointerException("null key is illegal");
        }
        SignatureAlgorithm signatureAlgorithm =SignatureAlgorithm.HS256;
        //生成JWT数据签名
        String jwtString = Jwts.builder()
                .setIssuer("Jersey-Security-Basic")
                .setId(userId)
                .setSubject(tel)
                .setAudience("user")
                .setExpiration(expires)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm,key)
                .compact();
        //客户登录后，保存用户名与token的map
        //onlineMap.put(GlobalConstants.REDIS_CUS+userId, jwtString); 
        //放到redis中
        CommonJedisUtil.set(GlobalConstants.REDIS_CUS+userId,jwtString);
        return jwtString;
    }
    
    /**
     * 根据key值删除token
     * @param key
     */
    public static void delToken(String key){
    	//删除redis中的token
    	CommonJedisUtil.del(key);
    	//onlineMap.remove(key);
    }
    /**
     * 验证token是否可以解码
     * @param token
     * @param key
     * @return
     */
    public static boolean isValid(String token, Key key) {
    	//判断token是否在map中
    	/*if(!onlineMap.values().contains(token)){
    		return false;
    	}*/
        try {
        	//解码
            Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 验证token是否在redis中,是否有效
     * @param key   key值
     * @param token value值
     * @return
     */
    public static boolean redisTokenValid(String key,String token) {
    	if(StringUtils.isNotBlank(key)){
    		if(CommonJedisUtil.exists(key)){
        		if(token.equals(CommonJedisUtil.get(key))){
        			return true;
        		}else{
        			return false;
        		}
        	}else{
        		return false;
        	}
    	}else{
    		return false;
    	}
    	
    	/*if(StringUtils.isNotBlank(key)){
    		if(token.equals(onlineMap.get(key)) ){
        		return true;
        	}else{
        		return false;
        	}
    	}else{
    		return false;
    	}*/
    }
    /**
     * 解析token 获取用户名
     * @param jwsToken
     * @param key
     * @return
     */
    public static String getName(String jwsToken, Key key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            String name = String.valueOf(claimsJws.getBody().get("name"));
            return name;
        }
        return null;
    }
    /** 
     * 解析token 获取用户ID 
     * @param @param jwsToken
     * @param @param key
     * @return String 
     */
     public static String getUserId(String jwsToken, Key key) {
         if (isValid(jwsToken, key)) {
             Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
             String companyid = String.valueOf(claimsJws.getBody().get("id"));
           return companyid;
         }
         return null;
     }
    public static String[] getRoles(String jwsToken, Key key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getAudience().split(",");
        }
        return new String[]{};
    }
    public static int getVersion(String jwsToken, Key key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return Integer.parseInt(claimsJws.getBody().getId());
        }
        return -1;
    }
    
    /** 
    * @Title: setAcccessToken 
    * @Description: (存放鉴权中心token) 
    * @param @param accessToken
    * @param @param key
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws 
    */
    public static void setAcccessToken(String authToken, Key key,String accessToken) {
        if (isValid(authToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            claimsJws.getBody().put("acessToken", accessToken);
        }
    }
    /** 
    * @Title: getCompanyId 
    * @Description: (获取鉴权中心Token) 
    * @param @param jwsToken
    * @param @param key
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws 
    */
    public static String getAccessToken(String jwsToken, Key key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getSubject();
        }
        return null;
    }
}