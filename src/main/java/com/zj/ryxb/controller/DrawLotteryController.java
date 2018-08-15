package com.zj.ryxb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.model.GGoods;
import com.zj.ryxb.service.GoodsService;

/**
 * 
 * @Description:商品控制类
 * @ClassName: LoginController 
 */
@Controller
@RequestMapping("/")
public class DrawLotteryController extends BaseController{
	
	@Autowired
	private GoodsService goodsService;
	
    /**
     * 开奖管理（PC端）
     */
    @RequestMapping(value = "kjgl.act")
    public ModelAndView kjgl(HttpServletRequest request, ModelMap model) {
    	ModelAndView mv = new ModelAndView("/modules/goods/drawList");
        return mv;
    }
    
    
    /**
	 * 待开奖商品列表（PC端）
	 */
    @RequestMapping(value = "drawLotteryList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView drawLotteryList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String searchFlag = request.getParameter("searchFlag");
		String page = request.getParameter("pageNo");//当前页码
		String pSize = request.getParameter("pageSize");//当前
		
		Integer size=10;
		if(StringUtils.isBlank(pSize)){
			size = pageSize;
		}else{
			size = Integer.valueOf(pSize);
		}
		
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);// 有参数时获取参数值，否则默认值
		}

		List<GGoods> goodList = goodsService.queryDrawLotteryList();
		
		List<GGoods> resultList = goodList.stream()
				.filter(t -> t.getSearchFlag().contains(searchFlag))
				.collect(Collectors.toList());
		
		List<GGoods> resultList1 = resultList.stream()
				.skip(size * (pageNo -1)).limit(size)
				.collect(Collectors.toList());
		
		pageInfo = PaginationUtil.getPageInfo(resultList.size(), pageNo, size);
		
		model.put("resultList", resultList1);
		model.put("pageInfo", pageInfo);
		model.put("goodPath", GOODS_IMAGE_PATH);
		
		ModelAndView mv = new ModelAndView("/modules/goods/draw");
        return mv;
	}
	
}
