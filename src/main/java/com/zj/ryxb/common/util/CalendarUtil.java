/**
 * @Company： 青鸟软通   
 * @Title: CalendarUtil.java 
 * @Description：描述 
 * @Author： wxinpeng   
 * @Date： 2013-11-14 下午2:42:30 
 * @Version： V1.0  
 * @Modify：          
 */
package com.zj.ryxb.common.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.alibaba.fastjson.JSON;
import com.zj.ryxb.common.alipay.AlipayConfig;
import com.zj.ryxb.common.alipay.DatetimeUtil;
import com.zj.ryxb.common.alipay.PayUtil;

/** 
 * @ClassName: CalendarUtil 
 * @Description: 
 */
public class CalendarUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final FastDateFormat FORMATER1 = FastDateFormat.getInstance("yyyy-MM-dd");
	public static final FastDateFormat FORMATER2 = FastDateFormat.getInstance("yyyyMMdd");
	public static final FastDateFormat FORMATER3 = FastDateFormat.getInstance("yyyy年MM月dd日 HH:mm");
	public static final FastDateFormat FORMATER4 = FastDateFormat.getInstance("yyyyMMddHHmmss");
	public static final FastDateFormat FORMATER5 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	/**
	 * 获取毫秒的时间规范格式
	 */
	public static final FastDateFormat FORMATER6 = FastDateFormat.getInstance("yyyyMMddHHmmssSS");
	
	public static final FastDateFormat FORMATER7 = FastDateFormat.getInstance("yyMMddHHmmss");

	/**
	 * @Title: getLongDate 
	 * @Description: (这里用一句话描述这个类的作用)
	 * @param date
	 * @return
	 */
	public static String getSortDate(Date date) {
		if (date != null) {
			return FORMATER1.format(date);
		}
		return null;
	}

	/**
	 * @Title: getZhDate 
	 * @Description: (得到中文的时间)
	 * @return
	 */
	public static String getZhDate() {
		Calendar calendar = Calendar.getInstance();
		return FORMATER3.format(calendar.getTime());
	}

	/**
	 * @Title: getLongDate 
	 * @Description: (得到时间戳的字符串)
	 * @return
	 */
	public static String getLongDate() {
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.getTimeInMillis());
	}

	/**
	 * @Title: getSortDate 
	 * @Description: (得到时间的字符串 yyyyMMdd)
	 * @return
	 */
	public static String getSortDate() {
		Calendar calendar = Calendar.getInstance();
		return FORMATER2.format(calendar.getTime());
	}

	/**
	 * 得到时间的字符串 yyyy-MM-dd HH:mm:ss
	* @Title: getLongFormatDate 
	* @return
	 */
	public static String getLongFormatDate() {
		Calendar calendar = Calendar.getInstance();
		return FORMATER5.format(calendar.getTime());
	}
	/**
	 * 将Date()转换为 yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param date
	 * @return
	 */
	public static String transLongFormatDate(Date date) {
		if (date != null) {
			return FORMATER5.format(date);
		}
		return null;
	}

	/**
	 * @Title: getYear 
	 * @Description: (这里用一句话描述这个类的作用)
	 * @return
	 */
	public static String getYear() {
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.get(Calendar.YEAR));
	}

	/**
	 * 判断是否是闰年
	* @Title: isLeapYear 
	* @Description: (这里用一句话描述这个类的作用)
	* @param year
	* @return  
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断某个月有多少天
	* @Title: getDayNumber 
	* @Description: 判断某个月有多少天
	* @param year
	* @param month
	* @return
	 */
	public static Integer getDaysNumber(Integer year, Integer month) {
		Calendar time = Calendar.getInstance();
		time.clear();
		time.set(Calendar.YEAR, year);
		time.set(Calendar.MONTH, month + 1); //Calendar对象默认一月为0
		int maxday = time.getActualMaximum(Calendar.DAY_OF_MONTH);

		return maxday;
	}

	/**
	 * 得到毫秒级别时间的字符串 yyyyMMddHHmmssSS
	* @Title: getHaomiaoFormatDate 
	* @return  
	 */
	public static String getHaomiaoFormatDate() {
		Calendar calendar = Calendar.getInstance();
		return FORMATER6.format(calendar.getTime());
	}
	/**
	 * 得到秒级别时间的字符串 yyMMddHHmmss(140813160109)
	 * @Title: getMiaoFormatDate 
	 * @return  
	 */
	public static String getMiaoFormatDate() {
		Calendar calendar = Calendar.getInstance();
		return FORMATER7.format(calendar.getTime());
	}
	/**
	* @Title: getFirstDayOfYeay 
	* @Description: 一年的第一天
	* @return  date
	 */
	public static Date getFirstDayOfYeay(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	  * 将长整型数字转换为日期格式的字符串
	  * @param time 长整型日期
	  * @param format 转换后个日期格式类型，如：yyyy-MM-dd HH:mm:ss
	  * @return
	  */
	 public static String convertLong2String(long time, String format) {
		 if (time > 0l) {
			if (!StringUtils.isBlank(format)){
			   Date date = new Date(time);
			   return FastDateFormat.getInstance(format).format(date);
			}
		 }
		return "";
	 }
	 
	 /**
	 * 根据字符串获取时间
	 * @param strDate 字符串时间
	 * @param format 字符串格式,如：yyyy-MM-dd
	 * @return
	 */
	public static Date getDateFromString(String strDate,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
		}
		return date;
	}
	/**
	 * 取得输入时间小时点的最后一秒钟  xx点59分59秒结束    yyyy-MM-dd HH:mm:ss
	 */ 
	@SuppressWarnings("deprecation")
	public static Date GetDayHoursEndTime(Date date){
		Date newDate = new Date(date.getYear(),date.getMonth(),date.getDate(),date.getHours(),59,59);
		//newDate.setDate(d.getDate()-days);
		return newDate;
	}
	/**
	 * 取得输入时间小时点的开始一秒钟  xx点00分00秒开始    yyyy-MM-dd HH:mm:ss
	 */ 
	@SuppressWarnings("deprecation")
	public static Date GetDayHoursStartTime(Date date){
		Date newDate = new Date(date.getYear(),date.getMonth(),date.getDate(),date.getHours(),00,00); 
		return newDate;
	}
	
	/**
	 * 
	* @Description: 获取当前‘yyyy-MM-dd’类型日期的下一天
	* @Title: getNextDate 
	* @param strDate
	* @return
	 */
	public static String getNextDate(String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day+1);
		return sdf.format(calendar.getTime());
	}
	/**
	 * 
	* @Description: 获取当前日期的下一天
	* @Title: getNextDate 
	* @param date
	* @return
	 */
	public static Date getNextDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day+1);
		return calendar.getTime();
	}
	/**
	 * 根据时间间隔获得指定日期加上实际间隔后的日期
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getNextDate(Date date,Integer days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day+days);
		return calendar.getTime();
	}
	
	 /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return String 返回值为：xx天xx小时xx分xx秒 
     */  
    public static String getDistanceTime(String str1, String str2) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            day = diff / (24 * 60 * 60 * 1000);  
            hour = (diff / (60 * 60 * 1000) - day * 24);  
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";  
    }
    
    /**
     * 两个时间相差距离多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return 
     */
    public static long getDistanceTimeForSec(String str1, String str2) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;   
        long sec = 0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1 >= time2) { 
                diff = time1 - time2;  
            } else {  
            	//diff = time2 - time1; 
            	diff = -1; 
            }
            sec = (diff/1000);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return sec;  
    }  
    public static void main(String[] str){
    	long s= getDistanceTimeForSec("2017-06-05 11:00:20","2017-06-05 11:41:20");
    	System.out.println(s);
    	/*String s="1";
    	Double d=1.00;
    	if(Double.valueOf(d).equals(d)){
    		System.out.println(11);
    	}else{
    		System.out.println(22);
    	}*/
    	//签名
		/*Map<String, String> param = new HashMap<>();
		// 公共请求参数
		param.put("app_id", AlipayConfig.app_id);// 商户订单号
		param.put("method", "alipay.trade.app.pay");// 交易金额
		param.put("format", "json");
		param.put("charset", AlipayConfig.input_charset);
		param.put("timestamp", DatetimeUtil.formatDateTime(new Date()));
		param.put("version", "1.0");
		param.put("notify_url", "http://www.ryxb.info/receiveAlipaynotify.act"); // 支付宝服务器主动通知商户服务
		param.put("sign_type", AlipayConfig.sign_type);
		Map<String, Object> pcont = new HashMap<>();
		// 支付业务请求参数
		pcont.put("out_trade_no", "R10001"); // 商户订单号
		pcont.put("total_amount", String.valueOf(10));// 交易金额
		pcont.put("timeout_express", "30m"); // 订单标题
		pcont.put("subject", "ryxb支付宝支付"); // 订单标题
		pcont.put("body", "ryxb夺宝币充值交易");// 对交易或商品的描述
		pcont.put("product_code", "QUICK_MSECURITY_PAY");// 销售产品码
		
		param.put("biz_content", JSON.toJSONString(pcont)); // 业务请求参数  不需要对json字符串转义 
		String sign = PayUtil.getSign(param, AlipayConfig.private_key);// 业务请求参数
		System.out.println(sign);*/
    	
    }
}
