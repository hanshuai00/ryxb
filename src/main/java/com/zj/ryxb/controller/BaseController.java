package com.zj.ryxb.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);
	
	/**
	 * token有效期
	 */
	public  int  EXPIRY_DAY= 365;
	/**
	 * 分页信息
	 */
	public String pageInfo ="";
	/**
	 * 总页数
	 */
	public  Integer pgeSum =0;
	/**
	 * 当前页，默认从1开始
	 */
	public  Integer pageNo = 1;
	/**
	 * 每页条数，
	 */
	public  Integer pageSize =10;
	
	/**
	 * 首页轮播图相对地址
	 */
	public final static String HOME_PAGE_IMAGE_PATH ="/uploader/home/";
	/**
	 * 用户头像相对地址
	 */
	public final static String USER_IMAGE_PATH ="/uploader/user/";
	/**
	 * 商品图片相对地址
	 */
	public final static String GOODS_IMAGE_PATH ="/uploader/goods/";
	/**
	 * 评论图片相对地址
	 */
	public final static String COMMENT_IMAGE_PATH ="/uploader/comment/";
	/**
	 * 闪屏(引导页)图片相对地址
	 */
	public final static String BOOT_IMAGE_PATH ="/uploader/boot/";
	
   /* 首页轮播图、      用户头像、              商品图、       评论图片
    * homePath userPath goodPath commentPath */
	
	@Autowired  
	protected  HttpServletRequest request;
	
	/**
	 * 获取项目跟路径
	 * @return
	 */
	public  String getRootPath(){  
        //取得根目录路径  
		String rootPath= request.getSession().getServletContext().getRealPath("/");
           
        return rootPath;
    }
		
	/**
	 * 返回前台json
	 * @param response
	 * @param msg
	 */
	public void responseJson(HttpServletResponse response,String msg){
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
	    PrintWriter out = null;
	    try {
	      out = response.getWriter();
	      out.print(msg);
	      out.flush();
	      out.close();
	    } catch (IOException e) {
	     log.error("IO异常", e);
	    }
	    finally
	    {
	      out.flush();
	      out.close();
	    }
	}
	
	/**
	 * 返回前台html
	 * @param response
	 * @param msg
	 */
	public void responseHtml(HttpServletResponse response,String msg){
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
	    PrintWriter out = null;
	    try {
	      out = response.getWriter();
	      out.print(msg);
	      out.flush();
	      out.close();
	    } catch (IOException e) {
	    	log.error("IO异常", e);
	    }
	    finally
	    {
	      out.flush();
	      out.close();
	    }
	}
	
	// AJAX输出，返回null
    public String ajax(String content, String type, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setContentType(type + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            out = response.getWriter();
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException e) {
        	log.error("ajax", e);
        }finally
	    {
  	      out.flush();
  	      out.close();
  	    }
        return null;
    }
	
	/**AJAX输出HTML，返回null**/
    public String ajaxHtml(String html, HttpServletResponse response) {
        return ajax(html, "text/html", response);
    }
    
    /**AJAX输出json，返回null**/
    public String ajaxJson(String json, HttpServletResponse response) {
        return ajax(json, "application/json", response);
    }
   
    public void logException(String msg, Exception ex) {
    	log.error(msg, ex);
	}
	
}
