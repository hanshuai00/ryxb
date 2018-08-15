package com.zj.ryxb.common.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

public class CodeUtil {

	/**
	 * 生成编号code
	 * @param prefix 前缀，表示类型
	 * @return 编号code
	 */
	public static String findNewCode(String prefix) {

		Random random = new Random();
		int n = random.nextInt(900);
		String code = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String date = sdf.format(new Date());
		code = prefix + date + String.valueOf(100 + n);
		return code;
	}
	
	/**
	 * 编号ID唯一标识
	 * @return String
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * @Title: splitString
	 * @Description: (清楚表头表尾的root操作)
	 * @return String 返回类型
	 */
	public static String splitString(String xml){
		String result = new String();
		
		if(xml.contains("<root>")){
			String table[] = xml.replaceAll(" +","").split("<root>");
			String body[] = table[1].split("</root>");
			result =  body[0];
		}else{
			result = xml;
		}
		/*String table[] = xml.replaceAll(" +","").split("<root>");
		String body[] = table[1].split("</root>");
		return body[0];*/
		return result;
	}
	
	/**
	* @Description: 字符编码转换，解决中午乱码问题
	* @Title: enCodeStr 
	* @param str
	* @return
	 */
   public static String enCodeStr(String str) {  
        try {  
          return new String(str.getBytes("iso-8859-1"), "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
	 
	public static void main(String[] str){
		/*int a =1000;
		int b =1024;
		int c = a%b==0?a/b:a/b+1;*/
		
		System.out.println(Integer.valueOf("0.1"));
		
		
		/*double x = 1024;
        double y = 1000;
        //向上取整
        double m = Math.ceil(y/x);
        //向下取整
        double n = Math.floor(y/x);
        System.out.println("向上取整"+m+"\n向下取整"+n);
        //注意int/int结果是int，所以要把997变成997.0 变成double类型才能取值
        System.out.println(Math.ceil(997.0/10));*/
		/*List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
		names=names.stream().sorted((f1,f2) -> f1.compareTo(f2)).collect(Collectors.toList());
		
		Converter<String, Integer> converter = Integer::valueOf;
		Integer converted = converter.convert("123");
		System.out.println(converted);   // 123
		
		final int num =1;
		Converter<Integer, String> stringCon = (from) ->String.valueOf(from + num);
		System.out.println(stringCon.convert(4));;
		*/
	/**	String[] atp = {"Rafael Nadal", "Novak Djokovic",  
			       "Stanislas Wawrinka",  
			       "David Ferrer","Roger Federer",  
			       "Andy Murray","Tomas Berdych",  
			       "Juan Martin Del Potro"};  
		List<String> players =  Arrays.asList(atp);
		//遍历 显示
		players.forEach((t) -> System.out.println(t+";"));
		//按首字母排序 遍历 显示
		Comparator<String> tt = (String t1,String t2)->(t1.compareTo(t2));
		Arrays.sort(atp,tt);
		Arrays.asList(atp).forEach((t) -> System.out.println(t+";"));
		//new Thread(()->System.out.println("ddd")).start();
		//按长度排序 显示
		Arrays.sort(atp,(String s1,String s2)->s1.length()-s2.length());
		Arrays.asList(atp).forEach((t) -> System.out.println(t+";"));
		
		//计算 count, min, max, sum, and average for numbers  
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);  
		IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();  
		  
		System.out.println("List中最大的数字 : " + stats.getMax());  
		System.out.println("List中最小的数字 : " + stats.getMin());  
		System.out.println("所有数字的总和   : " + stats.getSum());  
		System.out.println("所有数字的平均值 : " + stats.getAverage()); */
			  
	}
	
}
