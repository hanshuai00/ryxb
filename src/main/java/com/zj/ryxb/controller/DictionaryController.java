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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.model.BDictionary;
import com.zj.ryxb.service.DictionaryService;

@Controller
@RequestMapping("/")
public class DictionaryController  extends BaseController{
	@Autowired
	private DictionaryService dictionaryService;
	/**
     * 转向数据字典列表页面
     */
    @RequestMapping(value = "pc/zdgl.act")
    public String Index(HttpServletRequest request, ModelMap model) {
        return "/modules/dictionary/index";
    }
    /**
     *进引导页图片管理页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "pc/bootImage.act")
    public String bootImageIndex(HttpServletRequest request, ModelMap model) {
        return "/modules/bootImage/index";
    }
	
    /**
	 * 服务端获取数据字典
	 */
	@RequestMapping(value = "pc/getDictionaryPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getgetDictionaryPC(HttpServletRequest request, HttpServletResponse response, ModelMap model){
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
		
		List<BDictionary> list= dictionaryService.queryListByPage(map);
		// 总数据条数
		Integer totalCount = dictionaryService.findTotalCount(map);
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, pageSize);
		
		model.put("resultList", list);
		model.put("pageInfo", pageInfo);
		
		return "/modules/dictionary/data";
	}
	/**
	 * 删除数据字典
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pc/deleteDictonary.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@Transactional
	public String deleteDictonary(HttpServletRequest request,Map<String, Object> model){
		String dicId = request.getParameter("dicId");
		//删除父类数据
		dictionaryService.deleteByPrimaryKey(Long.valueOf(dicId));
		//删除父类id关联得所有子类数据
		dictionaryService.deleteByParentId(Long.valueOf(dicId));
		
		return "/modules/dictionary/index";
	}
	
	/**
	 * 跳转新增页面
	 */
	@RequestMapping(value ="pc/addDictonary.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String addDictonary(Map<String, Object> model) {
		model.put("newflag", "true");
		
		return "/modules/dictionary/edit";
	}

	/**
	 * 跳转新增、编辑页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="pc/editDictonary.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String editDictonary(String dicCode, Map<String, Object> model) {
		model.put("parent", dictionaryService.findParent(dicCode));
		model.put("codelist", dictionaryService.findByParentCode(dicCode));
		model.put("newflag", "false");
		
		return "/modules/dictionary/edit";
	}
	
	/**
	 * 修改数据字典——父类名称及子类信息
	 * @param model
	 * @return
	 * @throws ClassNotFoundException
	 */
	@RequestMapping("pc/saveUpdateDictonary.act")
	@Transactional
	public String saveUpdateDictonary(HttpServletRequest request,
			Map<String, Object> model) throws ClassNotFoundException {
		String parentID = request.getParameter("parentID");
		String parentDicCode = request.getParameter("parentDicCode");
		String parentDicValue = request.getParameter("parentDicValue");
		
		String[] dicValues = request.getParameterValues("dicValue");
		String[] dicCodes = request.getParameterValues("dicCode");
		String[] sorts = request.getParameterValues("orderIndex");
		
		//判断是新增 or 修改
		if(StringUtils.isNotBlank(parentID)){ //修改
			BDictionary  record = new BDictionary();
			record.setId(Long.valueOf(parentID));
			record.setDicValue(parentDicValue);
			dictionaryService.saveOrUpdate(record);
			
			//删除现有的数据字典
			dictionaryService.deleteByParentId(Long.valueOf(parentID));
			
		}else{//新增
			BDictionary dict = dictionaryService.findParent(parentDicCode);
			if(dict !=null){
				model.put("closeflg", "true"); //父类编号已存在
				return "/modules/dictionary/edit";
			}
			BDictionary  record = new BDictionary();
			record.setDicValue(parentDicValue);
			record.setDicCode(parentDicCode);
			record.setOrderIndex(1);
			dictionaryService.saveOrUpdate(record);
		}
		//保存新的——子类数据字典
		if(dicValues !=null && dicValues.length>0){
			for (int i = 0; i < dicValues.length; i++) {
				BDictionary dict = new BDictionary();
				dict.setPid(Long.valueOf(parentID));
				dict.setDicCode(dicCodes[i]);
				dict.setDicValue(dicValues[i]);
				dict.setOrderIndex(Integer.valueOf(sorts[i]));
				dict.setEnabled(1);
				dictionaryService.saveOrUpdate(dict);
			}
		}
		
		return "/modules/dictionary/index";
	}
	
	 /**
	 * 服务端获取引导图
	 */
	@RequestMapping(value = "pc/getBootImageDicList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getBootImageDicList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		// 有参数时获取参数值，否则默认值
		String page = request.getParameter("pageNo");//当前页码
		if(page != null && page != "0"){
			pageNo =Integer.valueOf(page) ;
		}
		
		List<BDictionary> list = dictionaryService.findByParentCode("BOOT_IMAGE");
		// 总数据条数
		pageInfo = PaginationUtil.getPageInfo(list.size(), pageNo, pageSize);
		
		model.put("bootPath", BOOT_IMAGE_PATH);
		model.put("resultList", list);
		model.put("pageInfo", pageInfo);
		
		return "/modules/bootImage/data";
	}
	/**
	 * 服务端保存引导图
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pc/saveBootImageDic.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String  saveBootImageDic(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String dicId = request.getParameter("dicId");//数据字典id
		String bootImage = request.getParameter("bootImage");//引导图名称
		
		BDictionary record= new BDictionary();
		record.setId(Long.valueOf(dicId));
		record.setDicValue(bootImage);
		int result = dictionaryService.saveOrUpdate(record);
		
		if(result>0){
			returnMap.put("state", true);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_SUCCESS);
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_FAILED);
		}
		return gson.toJson(returnMap);
	}
	
}
