/**
* @Title: AutowiredHttpServlet.java 
* @Description：描述 
* @Author： xf   
* @Date： 2015-8-18 下午1:07:58 
* @Version： V1.0  
* @Modify：           
*/ 
package com.zj.ryxb.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/** 
 * 用于系统初始化时代理启动Servlet
 * @Description:用于代理启动Servlet的
 * @ClassName: AutowiredHttpServlet  
 */
public class AutowiredHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;    
    private String targetServletBean;    
    private Servlet proxy;    
        
    @Override    
    public void init() throws ServletException {    
        this.targetServletBean = this.getInitParameter("targetServletBean");    
        this.getServletBean();    
        this.proxy.init(this.getServletConfig());    
    }    
    
    @Override    
    protected void service(HttpServletRequest request, HttpServletResponse response)    
            throws ServletException, IOException {    
        proxy.service(request,response);     
    }    
    
    private void getServletBean(){    
        ServletContext servletContext = this.getServletContext();    
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);    
        this.proxy = (Servlet) wac.getBean(targetServletBean);    
    }  
   
}
