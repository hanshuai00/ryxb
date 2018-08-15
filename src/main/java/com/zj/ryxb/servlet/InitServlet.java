package com.zj.ryxb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.ryxb.common.util.CacheUtil;
import com.zj.ryxb.model.BDictionary;
import com.zj.ryxb.service.DictionaryService;

/**
 * 
 * @Description:
 * @ClassName: CacheServlet 
 * @Author： xf   
 * @Date： 2015-8-25 上午10:37:25
 * 用这个注入web，xml里的mybean对应的代理类
 */
@Component("myBean") 
public class InitServlet extends HttpServlet{
	private static final long serialVersionUID = -7687273306834889158L;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	 /** 
     * @see HttpServlet#HttpServlet() 
     */  
    public InitServlet() {  
        super();  
    } 
    /**
     * 系统初始化
     */
	@Override
	public void init(ServletConfig config) throws ServletException {
		List<BDictionary> dictionarylists = dictionaryService.findParentList();
		//将数据字典存入缓存
		CacheUtil.putDictionaryToCache(dictionaryService, dictionarylists);	
	}
	
	
	/** 
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
          
    }  
    /** 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        // TODO Auto-generated method stub  
    }  
 
}
