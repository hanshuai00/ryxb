package com.zj.ryxb.controller;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.model.CCustomer;
import com.zj.ryxb.model.MOrderComment;
import com.zj.ryxb.model.MOrderWin;
import com.zj.ryxb.service.CustomerService;
import com.zj.ryxb.service.OrderCommentService;
import com.zj.ryxb.service.OrderWinService;

/**
 * 评论 晒单
 * @author zxf
 *
 */
@Controller
@RequestMapping("/")
public class OrderCommentController extends BaseController {
	@Autowired
	private OrderCommentService commentService;
	@Autowired
	private CustomerService  customerService;
	@Autowired
	private OrderWinService orderWinService;
	
	/**
	 * 保存宝物中奖晒单，（手机端）
	 */
	@RequestMapping(value = "saveOrderComment.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveOrderComment(HttpServletRequest request, 
			HttpServletResponse response, ModelMap model){
		String str = "";	
		Gson gson = new Gson();
		Map<String, Object> rMap = new HashMap<String , Object>();
		//获取token
		String strToken = request.getParameter("token");
		String goodsId = request.getParameter("goodsId");//商品ID
		String issue = request.getParameter("issue");//商品当前期数
		String orderNum = request.getParameter("orderNum");//订单流水号
		String comment = request.getParameter("comment");//评论内容
		String commentImage = request.getParameter("commentImage");//评论图片
		
		
		Key key= KeyUtil.getKey(GlobalConstants.context);
		if (!TokenUtil.isValid(strToken,key)){
			rMap.put("state", false);
			rMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
			return gson.toJson(rMap);
		}
		String customerId = TokenUtil.getUserId(strToken,key);
		if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
			return gson.toJson(rMap);
		}
		
		if(StringUtils.isBlank(goodsId)){
			rMap.put("state",false);
			rMap.put("msg", "商品ID不能为空！");
			str = gson.toJson(rMap);
			return str;
		}
		if(StringUtils.isBlank(issue)){
			rMap.put("state",false);
			rMap.put("msg", "商品开奖期数不能为空！");
			str = gson.toJson(rMap);
			return str;
		}
		if(StringUtils.isBlank(orderNum)){
			rMap.put("state",false);
			rMap.put("msg", "订单流水号不能为空！");
			str = gson.toJson(rMap);
			return str;
		}
		
		CCustomer customer = customerService.findById(Long.valueOf(customerId));
		
		MOrderComment  record = new MOrderComment();
		record.setGoodsId(Long.valueOf(goodsId));
		record.setIssue(Integer.valueOf(issue));
		record.setOrderNum(orderNum);
		record.setCustomerId(Long.valueOf(customerId));
		record.setNickname(customer.getNickname());
		record.setComment(comment);
		record.setCommentImage(commentImage);
		int m = commentService.saveOrUpdate(record);
		
		//更新中奖记录表的晒单状态
		Map<String,Object> map = new HashMap<String , Object>();
		map.put("goodsId", goodsId);
		map.put("issue", issue);
		MOrderWin win = orderWinService.selectByGoodidIssue(map);
		if(win !=null && win.getId()!=null){
			MOrderWin newRecord = new MOrderWin();
			newRecord.setId(win.getId());
			newRecord.setReadyComment(1);
			orderWinService.updateByPrimaryKeySelective(newRecord);
		}
		
		if(m>0){
			rMap.put("state",true);
			rMap.put("msg", "晒单成功！");
		}else{
			rMap.put("state",false);
			rMap.put("msg", "晒单失败！");
		}
		str = gson.toJson(rMap);
		
		return str;
	}
	
	/**
	 * 往期晒单记录、我的晒单记录（手机端）
	 */
	@RequestMapping(value = "getOrderCommentList.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getOrderCommentList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> rMap = new HashMap<String , Object>();
		Gson gson = new Gson();

		String strToken = request.getParameter("token");
		String searchType = request.getParameter("searchType");//查询类型，1商品的往期晒单记录 2会员客户的晒单记录
		String page = request.getParameter("pageNo");//当前页码数
		String size = request.getParameter("pageSize");//每页显示条数
		String goodId = request.getParameter("goodId"); //商品id(searchType=1时，必填)
		
		String customerId ="0";
		if(StringUtils.isNotBlank(strToken)){ //判断token是否null或者""
			Key key= KeyUtil.getKey(GlobalConstants.context);
			if (!TokenUtil.isValid(strToken,key)){
				rMap.put("state", false);
				rMap.put("msg", GlobalConstants.MSG_TOKEN_ERROR);
				return gson.toJson(rMap);
			}
			customerId = TokenUtil.getUserId(strToken,key);
			if(!TokenUtil.redisTokenValid(GlobalConstants.REDIS_CUS+customerId,strToken)){
				rMap.put("state",false);
				rMap.put("msg", GlobalConstants.MSG_TOKEN_INVALID);
				return gson.toJson(rMap);
			}
		}
		pageNo=1;
		pageSize=10;
		if(StringUtils.isNotBlank(page)){
			pageNo =Integer.valueOf(page);
		}
		if(StringUtils.isNotBlank(size)){
			pageSize =Integer.valueOf(size);
		}
		if(StringUtils.isBlank(searchType)){
			rMap.put("state",false);
			rMap.put("msg", "查询类型不能为空！");
			return gson.toJson(rMap);
		}
		
		Map<String, Object> map = new HashMap<String , Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");
		
		if("1".equals(searchType)){
			if(StringUtils.isBlank(goodId)){
				rMap.put("state",false);
				rMap.put("msg", "商品ID不能为空！");
				return gson.toJson(rMap);
			}
			map.put("goodsId",  Long.valueOf(goodId));

		}else if("2".equals(searchType)){
			map.put("customerId", Long.valueOf(customerId));
		}
		
		int total = commentService.findListMapTotalCount(map);
		List<Map<Object,Object>> commentList =commentService.queryListMapByPage(map);
		
		List<Map<Object,Object>> listMap = new ArrayList<Map<Object,Object>>();
		if(commentList!=null){
			if(commentList.size()>0){
				for(Map<Object,Object> object : commentList){
					Map<Object,Object> m = new HashMap<Object,Object>();
					m.put("id",object.get("id"));//晒单记录id
					m.put("orderNum", object.get("order_num")==null?"":object.get("order_num"));//晒单订单号
					m.put("goodsId",object.get("goods_id")==null?"":object.get("goods_id"));//商品id
					m.put("issue",object.get("issue")==null?"":object.get("issue"));//当前开奖期数
					m.put("comment",object.get("comment")==null?"":object.get("comment"));//晒单评论内容
					m.put("commentImage",object.get("comment_image")==null?"":object.get("comment_image"));//晒单图片
					m.put("createTime",object.get("create_time")==null?"":object.get("create_time"));//晒单时间
					
					m.put("customerId",object.get("customer_id")==null?"":object.get("customer_id"));//晒单会员id
					m.put("nickname",object.get("c_nikename")==null?"":object.get("c_nikename"));//晒单会员昵称
					m.put("userName",object.get("user_name")==null?"":object.get("user_name"));//晒单会员用户名
					m.put("userImage",object.get("userImage")==null?"":object.get("userImage"));//晒单会员头像图片名
					
					m.put("goodsName",object.get("goods_name")==null?"":object.get("goods_name"));//商品名称
					//m.put("goodsImage",object.get("goods_image")==null?"":object.get("goods_image"));//商品图片
					m.put("personCount",object.get("person_count")==null?"":object.get("person_count"));//本期夺宝人数
					m.put("drawTime",object.get("draw_time")==null?"":object.get("draw_time"));//开奖时间
					m.put("winOrderNum", object.get("win_orderNum")==null?"":object.get("win_orderNum"));//订单号(中奖号码)
					
					listMap.add(m);
				}
			}
			rMap.put("total", total);
			rMap.put("items", listMap);
			rMap.put("pageNo", pageNo);
			rMap.put("pageSize", pageSize);
			rMap.put("commentPath", COMMENT_IMAGE_PATH);
			rMap.put("userPath", USER_IMAGE_PATH);
			rMap.put("state",true);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);
		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}
		
		
		/**int total = commentService.findTotalCount(map);
		List<MOrderComment> commentList = commentService.queryListByPage(map);
		
		List<MOrderComment> newCommentList =new ArrayList<MOrderComment>();
		if(commentList!=null){
			if(commentList.size()>0){
				for(MOrderComment comment :commentList){//遍历数据，添加上中奖会员头像和商品价格信息
					comment.setOrderNum(comment.getOrderNum()==null?"":comment.getOrderNum());
					
					CCustomer customer = customerService.findById(comment.getCustomerId());
					comment.userImage=customer.getUserImage()==null?"":customer.getUserImage();
					comment.setNickname(customer.getNickname()==null?"":customer.getNickname());
					
					newCommentList.add(comment);
				}
			}
			rMap.put("total", total);
			rMap.put("items", newCommentList);
			rMap.put("pageNo", pageNo);
			rMap.put("pageSize", pageSize);
			rMap.put("commentPath", COMMENT_IMAGE_PATH);
			rMap.put("userPath", USER_IMAGE_PATH);
			rMap.put("state",true);
			rMap.put("msg", GlobalConstants.MSG_SUCCESS);
		
		}else{
			rMap.put("state",false);
			rMap.put("msg", GlobalConstants.MSG_ERROR);
		}*/
		
		return gson.toJson(rMap);
	}
	
	
	
}
