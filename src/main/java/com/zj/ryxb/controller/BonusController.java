/**
 * 
 */
package com.zj.ryxb.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.model.BBonus;
import com.zj.ryxb.model.BDictionary;
import com.zj.ryxb.model.CBonus;
import com.zj.ryxb.service.BonusService;

/**
 * 红包种类、客户红包控制处理类
 * @ClassName: BBonusController 
 * @Author： zxf   
 * @Date： 2017年3月23日 下午9:48:32
 */
@Controller
@RequestMapping("/")
public class BonusController  extends BaseController {
	/**
	 * 红包服务类
	 */
	@Autowired
	private BonusService bonusService;
	/**
	 * 根据客户id获取客户的所有红包集合
	* @Title: findCusBonusList 
	* @param request
	* @return
	 */
	@RequestMapping(value = "findCusBonusList.act", method ={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @Transactional
	public Map<String, Object> findCusBonus(HttpServletRequest request, ModelMap model){ 	
		Map<String,  Object> rMap = new HashMap<String,  Object>();
		
		String strToken = request.getParameter("token");
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		if (!TokenUtil.isValid(strToken,key)){
			rMap.put("state", false);
			rMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return rMap;
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return rMap;
		}
		
		Map<String,Object> map = new HashMap<String,  Object>();
		map.put("customerId", Long.valueOf(customerId));
		map.put("status",0);
		
		List<CBonus> cBonusList = bonusService.queryCBonusList(map);
		if(cBonusList!=null){
			rMap.put("state", true);
			rMap.put("total", cBonusList.size());
			rMap.put("items", cBonusList);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			rMap.put("state", false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		return rMap;
		
	}
	
	
	/**
     * 转向红包类型管理列表页面
     */
    @RequestMapping(value = "pc/hbgl.act")
    public String Index(HttpServletRequest request, ModelMap model) {
        return "/modules/bonus/index";
    }
    
	
    /**
	 * 服务端获取红包类型
	 */
	@RequestMapping(value = "pc/getBBonusListPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getBBonusListPC(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		// 有参数时获取参数值，否则默认值
		String page = request.getParameter("pageNo");//当前页码
		if(page != null && page != "0"){
			pageNo =Integer.valueOf(page) ;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");//1：有效  0：无效
		map.put("isParent", "true");//查询父类
		
		List<BBonus> list= bonusService.queryBBonusList(map);
		// 总数据条数
		Integer totalCount = bonusService.findBBonusTotalCount(map);
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, pageSize);
		
		model.put("resultList", list);
		model.put("pageInfo", pageInfo);
		
		return "/modules/bonus/data";
	}
	
	/**
	 * 跳转进入新增、修改页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pc/addOrEditBBonus.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String addOrEditBBonus(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id = request.getParameter("objectId");
		if(StringUtils.isNotBlank(id)){
			BBonus bonus = bonusService.selectBBonusById(Long.valueOf(id));
			model.put("bonus", bonus);
		}
		return "/modules/bonus/add";
	}
	/*@RequestMapping(value = "pc/getBBonusById.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getBBonusById(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String id = request.getParameter("objectId");
		BBonus bonus = bonusService.selectBBonusById(Long.valueOf(id));
		if(bonus !=null){
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_SUCCESS);
			returnMap.put("bonus", bonus);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		return gson.toJson(returnMap);
	}*/
	/**
	 * 新增、修改保存
	 * @param domain
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "pc/saveAddOrUpdateBBonus.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveAddOrUpdateBBonus(BBonus domain ,HttpServletRequest request, HttpServletResponse response){
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		if (domain == null) {
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_FAILED);
            return gson.toJson(returnMap);
        }
		int i  = bonusService.saveOrUpdageBBonus(domain);
		if (i > 0) {
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_SUCCESS);
        } else {
        	returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_FAILED);
        }
		return gson.toJson(returnMap);
	}
	/**
	 * 删除红包种类
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pc/deleteBBonus.act",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String deleteBBonus(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String objectId = request.getParameter("objectId");//红包id
		if (StringUtils.isBlank(objectId)) {
			returnMap.put("state", false);
			returnMap.put("msg", "请选择至少一条记录！");
            return gson.toJson(returnMap);
		}
		
		int m = this.bonusService.deleteBBonusById(objectId);
		if (m > 0) {
			returnMap.put("state", true);
			returnMap.put("msg", "删除成功！");
        } else {
        	returnMap.put("state", false);
        	returnMap.put("msg", "删除失败！");
        }
        return gson.toJson(returnMap);
	}
	
	/**
	@RequestMapping(value = "findCusBonusList.act", method ={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @Transactional
	public Map<String, Object> findCusBonus(HttpServletRequest request){ 	
		Map<String,  Object> rMap = new HashMap<String,  Object>();
		
		
		return rMap;	
	}
	*/

}
