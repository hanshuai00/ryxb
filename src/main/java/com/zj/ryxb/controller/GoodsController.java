package com.zj.ryxb.controller;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.GCatalogue;
import com.zj.ryxb.model.GGoods;
import com.zj.ryxb.model.MOrderDetail;
import com.zj.ryxb.model.MOrderParticipant;
import com.zj.ryxb.model.MOrderWin;
import com.zj.ryxb.model.MWinner;
import com.zj.ryxb.service.CatalogueService;
import com.zj.ryxb.service.CustomerService;
import com.zj.ryxb.service.GoodsService;
import com.zj.ryxb.service.OrderDetailService;
import com.zj.ryxb.service.OrderWinService;

/**
 * 
 * @Description:商品控制类
 * @ClassName: LoginController 
 */
@Controller
@RequestMapping("/")
public class GoodsController extends BaseController{
	
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CatalogueService catalogueService;
	@Autowired
	OrderWinService orderWinService;
	@Autowired
	OrderDetailService orderDetailService;
	@Autowired
	CustomerService customerService;
	
	/**
     * 转向商品管理
     */
    @RequestMapping(value = "goodsManagement.act")
    public ModelAndView goodsManagement(HttpServletRequest request, ModelMap model) {
    	ModelAndView mv = new ModelAndView("/modules/goods/goodsList");
    	List<GCatalogue> catalogue = catalogueService.getRootCatalogue();
    	model.addAttribute("catalogue", catalogue); 
        return mv;
    }
    
    /**
	 * 获取首页轮播图（手机端）
	 * homePath: "/uploader/home/"
	 */
    @RequestMapping(value = "getHomePageImage.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getHomePageImage(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		//去掉token验证   2017-05-15
		/*String strToken = request.getParameter("token");
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		if (!TokenUtil.isValid(strToken,key)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(returnMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			returnMap.put("state",false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(returnMap);
		}*/
		//获取置顶类型的 商品 （首页轮播图商品）,非下架、有效状态
		List<GGoods> goodList = goodsService.queryGoodsIsTop();
		
		if(goodList!=null){
			returnMap.put("state", true);
			returnMap.put("total", goodList.size());
			returnMap.put("items", goodList);
			returnMap.put("homePath", HOME_PAGE_IMAGE_PATH);
			returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_ERROR);
		}

		str = gson.toJson(returnMap);
		return str;
	}
		
	/**
	 * 获取商品列表（手机端）
	 */
    @RequestMapping(value = "goodsList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getGoodsList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		//去掉token验证   2017-05-15
		/*String strToken = request.getParameter("token");
		
		Key key= KeyUtil.getKey(GlobalConstants.context);
		if (!TokenUtil.isValid(strToken,key)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(returnMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			returnMap.put("state",false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(returnMap);
		}*/
		
		String page = request.getParameter("pageNo");//页码数
		String size = request.getParameter("pageSize");//每页显示条数
		
		String searchFlag = request.getParameter("keyWord");
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		pageNo=1;
		pageSize=10;
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);
		}
		if(StringUtils.isNotBlank(size)){
			pageSize =Integer.valueOf(size);
		}
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");
		map.put("status", 1);
		
		if(StringUtils.isNotBlank(searchFlag)){
			map.put("searchFlag",searchFlag);
		}

		List<GGoods> goodList = goodsService.queryGoodsFromApp(map);
		int total = goodsService.findTotalCount(map);
		
		if(goodList!=null){
			returnMap.put("state", true);
			returnMap.put("total", total);
			returnMap.put("pageNo", pageNo);
			returnMap.put("pageSize", pageSize);
			returnMap.put("items", goodList);
			returnMap.put("goodPath", GOODS_IMAGE_PATH);
			returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_ERROR);
		}

		str = gson.toJson(returnMap);
		return str;
	}
	
	/**
	 * 获取商品列表（PC端）
	 */
	@RequestMapping(value = "getGoodsListPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getGoodsListAll(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String goodsNum = request.getParameter("goodsNum");
		String goodsName = request.getParameter("goodsName");
		String searchFlag = request.getParameter("searchFlag");
		String status = request.getParameter("status");
		String enabled = request.getParameter("enabled");
		String page = request.getParameter("pageNo");//当前页码
		String pSize = request.getParameter("pageSize");//当前
		
		Integer size=10;
		if(StringUtils.isBlank(pSize)){
			size = pageSize;
		}else{
			size = Integer.valueOf(pSize);
		}
		

		Map<String,Object> map = new HashMap<String,Object>();
		
		if(StringUtils.isNotBlank(goodsNum)){
			map.put("goodsNum","%" + goodsNum + "%");
		}
		if(StringUtils.isNotBlank(goodsName)){
			map.put("goodsName","%" + goodsName + "%");
		}
		if(StringUtils.isNotBlank(searchFlag)){
			map.put("searchFlag","%" + searchFlag + "%");
		}
		if(StringUtils.isNotBlank(status)){
			map.put("status",status);
		}
		if(StringUtils.isNotBlank(enabled)){
			map.put("enabled",enabled);
		}else{
			map.put("enabled", "1");//1：有效  0：无效
		}
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);// 有参数时获取参数值，否则默认值
		}
		map.put("pageIndex", (pageNo-1)*size);//从第几条开始取
		map.put("pageSize", size);//查询条数
		
		List<GGoods> goodList = goodsService.queryGoodsFromApp(map);
		int totalCount = goodsService.findTotalCount(map);
		
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, size);
		
		model.put("resultList", goodList);
		model.put("pageInfo", pageInfo);
		model.put("goodPath", GOODS_IMAGE_PATH);
		return "/modules/goods/data";
	}
	
	/**
	 * 获取商品分类
	 */
	@RequestMapping(value = "getCatalogue.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getCatalogue(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		//去掉token验证   2017-05-15
		/*String strToken = request.getParameter("token");
		
		Key key= KeyUtil.getKey(GlobalConstants.context);
		String customerId = TokenUtil.getUserId(strToken,key);
		if (!TokenUtil.isValid(strToken,key)){
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(returnMap);
		}
		
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			returnMap.put("state",false);
			returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(returnMap);
		}*/

		List<GCatalogue> catalogue = catalogueService.getRootCatalogue();
		
		if(catalogue!=null){
			returnMap.put("state", true);
			returnMap.put("total", catalogue.size());
			returnMap.put("items", catalogue);
			returnMap.put("goodPath", GOODS_IMAGE_PATH);
			returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_ERROR);
		}

		str = gson.toJson(returnMap);
		return str;
	}
	
	/**
	 * 获取商品详情（手机端）
	 */
	@RequestMapping(value = "goodsDetail.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getGoodsDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String customerId ="0";
		
		String strToken = request.getParameter("token");
		if(StringUtils.isNotBlank(strToken)){ //判断token是否null或者""
			Key key= KeyUtil.getKey(GlobalConstants.context);
			if (!TokenUtil.isValid(strToken,key)){
				returnMap.put("state", false);
				returnMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
				return gson.toJson(returnMap);
			}
			customerId = TokenUtil.getUserId(strToken,key);
			if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
				returnMap.put("state",false);
				returnMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
				return gson.toJson(returnMap);
			}
		}
		
		String goodsId = request.getParameter("goodsId");
		String issue = request.getParameter("issue");
		//获取商品详情包含有当前开奖期数：开奖进度、剩余数量(不含未支付订单)
		if(StringUtils.isBlank(goodsId)){
			returnMap.put("state",false);
			returnMap.put("msg", "商品数据ID为空！");
			return gson.toJson(returnMap);
		}
		GGoods goods_m = goodsService.selectByPrimaryKey(Long.valueOf(goodsId));
		//屏蔽不传给前台的字段
		goods_m.setAmountLimit(null);
		goods_m.setWinnerId(null);
		goods_m.setRule(null);
		goods_m.setCreateTime(null);
		goods_m.setSearchFlag(null);
		goods_m.setSaleTime(null);
		goods_m.setTurnaround(null);
		goods_m.setSort(null);
		goods_m.setEnabled(null);
		goods_m.setIsTop(null);
		
		Map<String,Object> map = new HashMap<String , Object>();
		map.put("goodsId", goodsId);
		map.put("issue", issue);
		map.put("customerId", customerId);
		// 查询 商品详情 ——订单详情数据参与人列表 
		List<MOrderParticipant> participantList = orderDetailService.selectByGoodidIssue(map);
		
		MWinner tuhao = new MWinner();
		MWinner shafa = new MWinner();
		if(participantList.size()>0){
			//按customerId汇总
			Map<Long, Integer> cGroup = participantList.stream().collect(Collectors.groupingBy(MOrderParticipant::getCustomerId,Collectors.summingInt(MOrderParticipant::getQuantity)));
			Map<Long,Integer> cGroupSort = (Map<Long, Integer>) cGroup.entrySet().stream().sorted(Map.Entry.<Long,Integer>comparingByValue().reversed())
					.limit(1).collect(
							Collectors.toMap(
								       e -> e.getKey(),    // Map.Entry::getKey
								       e -> e.getValue(),  // Map.Entry::getValue
								       (v1, v2) -> { throw new IllegalStateException(); },
								       () -> new TreeMap<>()  // TreeMap::new
							)
					);
			Long tuHaoId =cGroupSort.keySet().iterator().next();
			Integer tuHaoCount = cGroupSort.get(tuHaoId);
			CCustomer tuHaoInfo = customerService.findById(tuHaoId);
			tuhao.setUserName(tuHaoInfo.getUserName());
			tuhao.setNickname(tuHaoInfo.getNickname());
			tuhao.setUserImage(tuHaoInfo.getUserImage());
			tuhao.setPersonCount(tuHaoCount.intValue());

			MOrderParticipant first = participantList.get(0);
			CCustomer diyiInfo = customerService.findById(first.getCustomerId());
			shafa.setUserName(diyiInfo.getUserName());
			shafa.setNickname(diyiInfo.getNickname());
			shafa.setUserImage(diyiInfo.getUserImage());
		}

		MWinner winner = new MWinner();
		MWinner baowei = new MWinner();
		if(Integer.valueOf(issue) < goods_m.getIssue()){
			MOrderWin orderWin = orderWinService.selectByGoodidIssue(map);
			CCustomer customer = customerService.findById(orderWin.getCustomerId());
			MOrderParticipant articipant = orderDetailService.selectLastIp(orderWin.getCustomerId());
			winner.setUserName(customer.getUserName());
			winner.setNickname(customer.getNickname());
			winner.setUserImage(customer.getUserImage());
			winner.setOrderNum(orderWin.getOrderNum());
			winner.setDrawTime(orderWin.getDrawTime());
			winner.setLastip(articipant.getLastip()==null?"":articipant.getLastip());
			winner.setFirstRegion(articipant.getFirstRegion()==null?"":articipant.getFirstRegion());
			winner.setSeconeRegion(articipant.getSeconeRegion()==null?"":articipant.getSeconeRegion());
			goods_m.setStatus(3);//商品状态：1进行中2待开奖3已开奖（供前端展示用，不保存数据库）
			goods_m.setRestQuantity(0);
			goods_m.setProgress(100.0);
			
			MOrderParticipant last = participantList.get(participantList.size()-1);
			
			CCustomer baoweiInfo = customerService.findById(last.getCustomerId());
			baowei.setUserName(baoweiInfo.getUserName());
			baowei.setNickname(baoweiInfo.getNickname());
			baowei.setUserImage(baoweiInfo.getUserImage());
		}

		returnMap.put("state", true);
		returnMap.put("goodPath", GOODS_IMAGE_PATH);
		returnMap.put("userPath", USER_IMAGE_PATH);
		if(!"0".equals(customerId)){
			int count = orderDetailService.findTotalCount(map);			
			if(count>0){
				returnMap.put("isbought", 1);//1参与 0未参与
			}else{
				returnMap.put("isbought", 0);
			}
		}else{
			returnMap.put("isbought","未购买");
		}
		
		returnMap.put("goods", goods_m);
		returnMap.put("participantList", participantList);
		returnMap.put("winner", winner);
		returnMap.put("tuhao", tuhao);
		returnMap.put("shafa", shafa);
		returnMap.put("baowei", baowei);
		returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
		str = gson.toJson(returnMap);
		return str;
	}
    
    
    /**
	 * 新增、修改 保存商品（PC端）
	 */
    @RequestMapping(value = "saveGoods.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveGoods(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		
		String id = request.getParameter("id");
		String goodsName = request.getParameter("goodsName");
		String goodsImage = request.getParameter("goodsImage");
		String catalogueId = request.getParameter("catalogueId");
		String catalogue = request.getParameter("catalogue");
		String title = request.getParameter("title");
		String price = request.getParameter("price");
		String minLimit = request.getParameter("minLimit");
		String unit = request.getParameter("unit");
		String turnaround = request.getParameter("turnaround");
		String totalIssue = request.getParameter("totalIssue");
		String status = request.getParameter("status");

		
		GGoods goods = new GGoods();
		goods.setGoodsName(goodsName);
		goods.setTitle(title);
		goods.setGoodsImage(goodsImage);
		goods.setCatalogueId(Long.valueOf(catalogueId));
		goods.setCatalogue(catalogue);
		goods.setPrice(Double.valueOf(price));
		goods.setMinLimit(Double.valueOf(minLimit));
		goods.setUnit(unit);
		goods.setTurnaround(Integer.valueOf(turnaround));
		goods.setTotalIssue(Integer.valueOf(totalIssue));
		goods.setStatus(Integer.valueOf(status));
		
		//搜索关键字，分类+商品名称+标题
		String searchFlag = catalogue + " " + goodsName + " " + title;
		goods.setSearchFlag(searchFlag);
		
		int result =0;
		//如果id为空则新增，否则修改
		if(StringUtils.isBlank(id)){
			//商品编码,"G" + catalogueId(两位) + YYMMDDhhmissSSS
			Date now = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
			String goodsNum = "G" +  StringUtils.right("00" + catalogueId, 2)  + formatter.format(now) ;
			//获取有效商品当前最大排序号
			Integer sort = goodsService.findCurrentMaxSort();
			
			goods.setGoodsNum(goodsNum);
			goods.setIssue(1);
			goods.setEnabled(1);
			goods.setRule(1);
			goods.setIsTop(0);
			if(sort!=null && sort>=0){
				goods.setSort(sort+1);
			}else{
				goods.setSort(1);
			}
			goods.setCreateTime(CalendarUtil.getLongFormatDate());
			result = goodsService.insert(goods);
		}else{
			goods.setId(Long.valueOf(id));
			result = goodsService.updateByPrimaryKeySelective(goods);
		}

		
		if(result==1){
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_FAILED);
		}
				

		str = gson.toJson(returnMap);
		return str;
	}

    /**
	 * 删除商品（PC端）
	 */
    @RequestMapping(value = "deleteGoods.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
	public String deleteGoods(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		
		String id = request.getParameter("id");
		GGoods goods = goodsService.selectByPrimaryKey(Long.valueOf(id));
		
		int result =0;
		if(goods != null){
			GGoods good = new GGoods();
			good.setId(goods.getId());
			good.setEnabled(0);
			good.setSort(0);
			good.setIsTop(0);
			good.setHomeImage(null);
			result = goodsService.updateByPrimaryKeySelective(good);
		}
		
		if(result==1){
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_DEL_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_DEL_FAILED);
		}
		str = gson.toJson(returnMap);
		return str;
	}
    /**
   	 * 修改商品顺序——置顶操作（PC端）
   	 */
    @RequestMapping(value = "updateGoodsSort.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
	public String updateGoodsSort(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		String id = request.getParameter("id");
		GGoods goods = goodsService.selectByPrimaryKey(Long.valueOf(id));
		
		//按sort排序查询有效商品数据
		Map<String,Object> map1 = new HashMap<>();
		map1.put("enabled", 1);
		List<GGoods> list = goodsService.queryListByPage(map1);
		
		int result =0;
		if(goods != null){
			//先更新其他数据的sort顺序
			if(list!=null && list.size()>0){
				int m=2;
				//将其他商品的排序字段从2开始递增重新排序
				for(GGoods record :list){
					if(record.getId()!=goods.getId()){
						GGoods good1 = new GGoods();
						good1.setId(record.getId());
						good1.setSort(m);
						goodsService.updateByPrimaryKeySelective(good1);
						m++;
					}
				}
			}
			
			//修改需要置顶的数据
			GGoods good = new GGoods();
			good.setId(goods.getId());
			good.setSort(1);//将置顶的商品设置位1
			result = goodsService.updateByPrimaryKeySelective(good);
		}
		
		if(result==1){
			returnMap.put("state", true);
			returnMap.put("msg", "置顶成功");
		}else{
			returnMap.put("state", false);
			returnMap.put("msg","置顶失败");
		}
		str = gson.toJson(returnMap);
		return str;
	}
    
    /**
	 * 进修改商品页面（PC端）
	 */
    @RequestMapping(value = "modifyGoods.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String modifyGoods(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id = request.getParameter("id");
		GGoods goods = goodsService.selectByPrimaryKey(Long.valueOf(id));
		
		if(goods != null){
			List<GCatalogue> catalogue = catalogueService.getRootCatalogue();
	    	model.addAttribute("catalogue", catalogue); 
	    	
			model.addAttribute("goodPath",  GOODS_IMAGE_PATH); 
			model.addAttribute("goods",  goods); 
		}
		return "modules/goods/goods";
	}
    
    /**
	 * 新增商品初始页面（PC端）
	 */
    @RequestMapping(value = "addGoods.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String addIndex(HttpServletRequest request, HttpServletResponse response, ModelMap model){

		List<GCatalogue> catalogue = catalogueService.getRootCatalogue();
    	model.addAttribute("catalogue", catalogue); 
    	
		model.addAttribute("goodPath",  GOODS_IMAGE_PATH); 
		model.addAttribute("goods",  new GGoods()); 

		return "modules/goods/goods";
	}
    
    
    /**
	 * 修改商品开奖规则（PC端）
	 */
    @RequestMapping(value = "modifyGoodsRule.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String modifyGoodsRule(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id = request.getParameter("id");
		String issue = request.getParameter("issue");
		GGoods goods = goodsService.selectByPrimaryKey(Long.valueOf(id));
		
		Map<String,Object> map = new HashMap<String , Object>();
		map.put("goodsId", id);
		map.put("issue", issue);
		List<MOrderDetail> detailList = orderDetailService.getBuyList(map);
		List<MOrderDetail> detailListOrder = detailList.stream()
				.sorted((f1, f2) -> Integer.compare(f2.getQuantity() , f1.getQuantity()))
				.collect(Collectors.toList());
		
		List<CCustomer> cuslist = customerService.selectByOderDetail(map);
		Map<Long,String> nickMap = cuslist.stream().collect(Collectors.toMap(t -> t.getId() ,t -> t.getNickname()));
		Map<Long,String> nameMap = cuslist.stream().collect(Collectors.toMap(t -> t.getId() ,t -> t.getUserName()));

		
		model.addAttribute("goods",  goods); 
		model.addAttribute("detailList",  detailListOrder); 
		model.addAttribute("nickMap",  nickMap); 
		model.addAttribute("nameMap",  nameMap); 
		return "modules/goods/rule";
	}
    
    
    /**
	 * 保存商品开奖规则（PC端）
	 */
    @RequestMapping(value = "saveGoodsRule.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveGoodsRule(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String str = "";	
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		
		String id = request.getParameter("id");
		String rule = request.getParameter("rule");
		String winnerId = request.getParameter("winnerId");
		String amountLimit = request.getParameter("amountLimit");

		GGoods goods = new GGoods();
		goods.setId(Long.valueOf(id));
		goods.setRule(Integer.valueOf(rule));
		
		if(rule.equals("3")){
			goods.setWinnerId(Long.valueOf(winnerId));
		}
		if(rule.equals("2")){
			goods.setAmountLimit(Double.valueOf(amountLimit));
		}

		int result = goodsService.updateByPrimaryKeySelective(goods);

		
		if(result==1){
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_FAILED);
		}

		str = gson.toJson(returnMap);
		return str;
	}
    
    /**
     * 转向轮播图设置
     */
    @RequestMapping(value = "lbtsz.act")
    public ModelAndView lbtsz(HttpServletRequest request, ModelMap model) {
    	ModelAndView mv = new ModelAndView("/modules/goods/carouselList");
        return mv;
    }
    
    /**
	 * 获取轮播图列表（PC端）,查询有效状态得
	 */
	@RequestMapping(value = "getGoodsListLB.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getGoodsListLB(HttpServletRequest request, HttpServletResponse response, ModelMap model){

		String page = request.getParameter("pageNo");//当前页码
		String pSize = request.getParameter("pageSize");//当前
		
		Integer size=10;
		if(StringUtils.isBlank(pSize)){
			size = pageSize;
		}else{
			size = Integer.valueOf(pSize);
		}

		Map<String,Object> map = new HashMap<String,Object>();
		
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);// 有参数时获取参数值，否则默认值
		}
		map.put("pageIndex", (pageNo-1)*size);//从第几条开始取
		map.put("pageSize", size);//查询条数
		map.put("enabled", "1");
		
		List<GGoods> goodList = goodsService.queryGoodsLB(map);
		int totalCount = goodsService.findTotalCount(map);
		
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, size);
		
		model.put("resultList", goodList);
		model.put("pageInfo", pageInfo);
		model.put("homePath", HOME_PAGE_IMAGE_PATH);
		return "/modules/goods/carousel";
	}
	
	 /**
	 * 添加、修改商品轮播图（PC端）
	 */
    @RequestMapping(value = "addGoodsCarouse.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String addGoodsCarouse(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id = request.getParameter("id");
		GGoods goods = goodsService.selectByPrimaryKey(Long.valueOf(id));
		
		if(goods != null){
			List<GCatalogue> catalogue = catalogueService.getRootCatalogue();
	    	model.addAttribute("catalogue", catalogue); 
	    	
			model.addAttribute("homePath",  HOME_PAGE_IMAGE_PATH); 
			model.addAttribute("goods",  goods); 
		}
		return "modules/goods/addCarouse";
	}

    /**
     * 保存商品轮播图图片
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "saveGoodsCarouse.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
   	@ResponseBody
   	public String saveGoodsCarouse(HttpServletRequest request, HttpServletResponse response, ModelMap model){
   		String str = "";	
   		Map<String, Object> returnMap = new HashMap<String , Object>();
   		Gson gson = new Gson();
   		
   		String id = request.getParameter("id");
   		String isTop = request.getParameter("isTop");
   		String homeImage = request.getParameter("homeImage");

   		GGoods goods = new GGoods();
   		goods.setId(Long.valueOf(id));
   		goods.setIsTop(Integer.valueOf(isTop));
   		goods.setHomeImage(homeImage);

   		int result = goodsService.updateByPrimaryKeySelective(goods);	
   		goodsService.updateLB();

   		if(result==1){
   			returnMap.put("state", true);
   			returnMap.put("msg", GlobalConstants.MSG_SAVE_SUCCESS);
   		}else{
   			returnMap.put("state", false);
   			returnMap.put("msg", GlobalConstants.MSG_SAVE_FAILED);
   		}

   		str = gson.toJson(returnMap);
   		return str;
   	}
}
