package com.zj.ryxb.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.model.GCatalogue;
import com.zj.ryxb.service.CatalogueService;

/**
 * 商品分类控制类
 * @author zxf
 *
 */
@Controller
@RequestMapping("/")
public class CatalogueController extends BaseController {

	@Autowired
	private CatalogueService catalogueService;
	
	/**
     * 转向商品分类列表页面
     */
    @RequestMapping(value = "spfl.act")
    public String spflIndex(HttpServletRequest request, ModelMap model) {
        return "/modules/catalogue/index";
    }
    
	
    /**
	 * 服务端获取商品分类
	 */
	@RequestMapping(value = "getCatalogueListPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getCatalogue(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		// 有参数时获取参数值，否则默认值
		String page = request.getParameter("pageNo");//当前页码
		if(page != null && page != "0"){
			pageNo =Integer.valueOf(page) ;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");//1：有效  0：无效
		
		List<GCatalogue> catalogueList = catalogueService.queryListByPage(map);
		// 总数据条数
		Integer totalCount = catalogueService.findTotalCount(map);
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, pageSize);
		
		model.put("resultList", catalogueList);
		model.put("pageInfo", pageInfo);
		
		return "/modules/catalogue/data";
	}
}
