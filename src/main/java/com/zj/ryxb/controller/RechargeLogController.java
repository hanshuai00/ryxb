package com.zj.ryxb.controller;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.google.gson.Gson;
import com.zj.ryxb.common.alipay.AlipayConfig;
import com.zj.ryxb.common.alipay.AlipayCore;
import com.zj.ryxb.common.alipay.AlipayUtil;
import com.zj.ryxb.common.alipay.DatetimeUtil;
import com.zj.ryxb.common.alipay.PayUtil;
import com.zj.ryxb.common.sms.CheckBuilder;
import com.zj.ryxb.common.sms.SmsCodeUtil;
import com.zj.ryxb.common.sms.SmsCodeVO;
import com.zj.ryxb.common.util.CalendarUtil;
import com.zj.ryxb.common.util.GlobalConstants;
import com.zj.ryxb.common.util.KeyUtil;
import com.zj.ryxb.common.util.PaginationUtil;
import com.zj.ryxb.common.util.TokenUtil;
import com.zj.ryxb.dao.CAmountMapper;
import com.zj.ryxb.dao.CBonusMapper;
import com.zj.ryxb.model.BRechargeLog;
import com.zj.ryxb.model.CAmount;
import com.zj.ryxb.model.CBonus;
import com.zj.ryxb.model.MOrder;
import com.zj.ryxb.service.CustomerService;
import com.zj.ryxb.service.DictionaryService;
import com.zj.ryxb.service.OrderService;
import com.zj.ryxb.service.RechargeLogService;

/**
 * 会员充值记录控制类
 * @author zxf
 *
 */
@Controller
@RequestMapping("/")
public class RechargeLogController extends BaseController {
	//private static final Logger LOG = Logger.getLogger(RechargeLogController.class);
	
	@Autowired
	private RechargeLogService rechargeLogService;
	
	@Autowired
	private CAmountMapper amountMapper;
	/**
	 * 客户红包
	 */
	@Autowired
	CBonusMapper cBonusMapper;

	@Autowired
	CustomerService customerService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	OrderService  orderService;
	/**
	 * 会员客户充值（手机端）
	 */
	@RequestMapping(value = "customerRecharge.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String customerRecharge(HttpServletRequest request, 
			HttpServletResponse response, ModelMap model){
		String str = "";	
		Gson gson = new Gson();
		Map<String, Object> rMap = new HashMap<String , Object>();
		//获取token
		String strToken = request.getParameter("token");
		Key key= KeyUtil.getKey(GlobalConstants.context);
		
		String payType = request.getParameter("payType");//1微信2支付宝
		String amount = request.getParameter("amount");//充值金额
		
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
		if(StringUtils.isBlank(payType)){
			rMap.put("state",false);
			rMap.put("msg", "支付类型不能为空！");
			str = gson.toJson(rMap);
			return str;
		}
		if(StringUtils.isBlank(amount) || Double.valueOf(amount)<=0.00 ){
			rMap.put("state",false);
			rMap.put("msg", "充值金额不能为空或不能小于零！");
			str = gson.toJson(rMap);
			return str;
		}
		
		//充值流水号,R+YYMMDDhhmissSSS+用户ID后6位
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String rechargeNum = "R"+ formatter.format(now) + StringUtils.right("000000" + customerId, 6);
				
		BRechargeLog  record = new BRechargeLog();
		record.setRechargeNum(rechargeNum);
		record.setCustomerId(Long.valueOf(customerId));
		record.setAmount(Double.valueOf(amount));
		record.setPayType(Integer.valueOf(payType));
		record.setStatus(0);//默认支付失败
		record.setEnabled(1);
		int m = rechargeLogService.saveOrUpdate(record);
		
		
		//签名
		Map<String, String> param = new HashMap<>();
		// 公共请求参数
		param.put("app_id", AlipayConfig.app_id);// 商户订单号
		param.put("method", "alipay.trade.app.pay");// 交易金额
		param.put("format", "json");
		param.put("charset", AlipayConfig.input_charset);
		param.put("timestamp", DatetimeUtil.formatDateTime(new Date()));
		param.put("version", "1.0");
		param.put("notify_url", "http://www.ryxb.info/receiveAlipaynotify.act"); // 支付宝服务器主动通知商户服务
		param.put("sign_type", AlipayConfig.sign_type);
		Map<String, Object> pcont = new HashMap<>();
		// 支付业务请求参数
		pcont.put("out_trade_no", rechargeNum); // 商户订单号
		pcont.put("total_amount", String.valueOf(amount));// 交易金额
		pcont.put("timeout_express", "30m"); // 订单标题
		pcont.put("subject", "ryxb支付宝支付"); // 订单标题
		pcont.put("body", "ryxb夺宝币充值交易");// 对交易或商品的描述
		pcont.put("product_code", "QUICK_MSECURITY_PAY");// 销售产品码
		
		param.put("biz_content", JSON.toJSONString(pcont)); // 业务请求参数  不需要对json字符串转义 
		String sign = PayUtil.getSign(param, AlipayConfig.private_key);// 业务请求参数
		
		if(m>0){
			//TODO测试
			//rechargeLogService.rechargeSuccee(record);
			
			rMap.put("state",true);
			rMap.put("rechargeNum",rechargeNum);//充值流水号
			rMap.put("sign",sign);//支付宝签名
			rMap.put("msg", "生成充值订单成功！");
		}else{
			rMap.put("state",false);
			rMap.put("msg", "生成充值订单失败！");
		}
		str = gson.toJson(rMap);
		
		return str;
	}
	
	/**
	 * 支付宝APP支付 服务器异步返回结果的验签
	 * http://www.ryxb.info/receiveAlipaynotify.act
	 * @throws AlipayApiException 
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "receiveAlipaynotify.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String receiveAlipaynotify(HttpServletRequest request, 
							HttpServletResponse response, ModelMap model) throws Exception{
		
		//获取支付宝POST过来反馈信息
		Map requestParams = request.getParameterMap();
		//Enumeration<?> pNames = request.getParameterNames();
		//System.out.println("支付宝支付结果通知"+requestParams.toString());
		
		
		Map<String,String> params = new HashMap<String,String>();		
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";
		  }
		  //乱码解决，这段代码在出现乱码时使用。
		  //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		  params.put(name, valueStr);
		}
		/*while (pNames.hasMoreElements()) {
			String pName = (String) pNames.nextElement();
			params.put(pName, request.getParameter(pName));
		}*/
		//记录日志文件
		//AlipayCore.logResult(this.getRootPath(), JSON.toJSONString(params), "notify_url");
		
		//商户订单号	
		// String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号	
		//String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		//String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		String trade_status = request.getParameter("trade_status");//交易状态
		//异步通知ID
		//String notify_id=request.getParameter("notify_id");
		String trade_no =params.get("trade_no"); //支付宝交易号	
		String out_trade_no =  params.get("out_trade_no");//商户订单号
		String total_amount =  params.get("total_amount");//订单金额
		String seller_id =  params.get("seller_id");//卖家支付宝用户号
		String app_id =  params.get("app_id");//支付宝分配给开发者的应用Id
		
		MOrder order=null;
		// 获取到返回的所有参数 先判断是否交易成功trade_status 再做签名校验
		if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) { //交易支付成功
			System.out.println("---------付款交易成功！---------");
			//切记alipay_public_key是支付宝的公钥，请去open.alipay.com对应应用下查看。
			//boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.input_charset, AlipayConfig.sign_type);
			//if(flag){ //验签成功后
			    //按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
				//1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
				//2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）
				//3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）		
				//4、验证app_id是否为该商户本身
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("orderNum", out_trade_no);//商品订单号
				//map.put("rechargeNum", out_trade_no);
				map.put("enabled", 1);
				//订单付款业务逻辑
				List<MOrder> orderList = orderService.queryListByPage(map);
				if(orderList !=null && orderList.size()>0){ //1
					order = orderList.get(0);
				}
				if(order !=null && out_trade_no.equals(order.getOrderNum())){ //1
					if(Double.valueOf(total_amount).equals(order.getTotalAmount())){ //2
						if(AlipayConfig.partner.equals(seller_id) &&
								AlipayConfig.app_id.equals(app_id)){ //3、4、
							//都验证通过进行业务逻辑处理
							//判断订单是否已取消
							if(order.getStatus()>=0){//未取消：修改订单状态
								order.setAlipayAmount(Double.valueOf(total_amount));//支付宝实际支付金额，等于总金额
								orderService.receiveAlipayHandel(order);
							}else{ //已取消：调用支付宝退款接口进行退款
								this.orderPayRefund(request, response, trade_no, out_trade_no,total_amount);
							}
							
							//return ("success");// 更新状态成功
						}else{
							if(order.getBonusId()!=null){
								CBonus  record = new CBonus();
								record.setId(order.getBonusId());
								record.setStatus(0);//红包使用状态，0未使用1已使用2已失效
								cBonusMapper.updateByPrimaryKeySelective(record);//更新红包状态
							}
							//return ("failure");// 更新状态失败  
						}
					}else{
						if(order.getBonusId()!=null){
							CBonus  record = new CBonus();
							record.setId(order.getBonusId());
							record.setStatus(0);//红包使用状态，0未使用1已使用2已失效
							cBonusMapper.updateByPrimaryKeySelective(record);//更新红包状态
						}
						//return ("failure");// 更新状态失败  
					}
				}
				
				return ("success");// 更新状态成功
				//金额充值业务逻辑
				/*List<BRechargeLog> list = rechargeLogService.queryListByPage(map);
				if(list !=null && list.size()>0){ //1、
					BRechargeLog  recharge = list.get(0);
					if(Double.valueOf(total_amount).equals(recharge.getAmount())){ //2
						if(AlipayConfig.partner.equals(seller_id) &&
								AlipayConfig.app_id.equals(app_id)){ //3、4、
							//都验证通过进行业务逻辑处理
							int m = rechargeLogService.rechargeSuccee(recharge);
							
							return ("success");//
						}else{
							
							return ("failure");// 更新状态失败  
						}
					}else{
						recharge.setStatus(0); 
						rechargeLogService.saveOrUpdate(recharge);
						return ("failure");// 更新状态失败  
					}
				}else{
					 return ("failure");// 更新状态失败  
				}*/
				
			/*}else{
			     //验签失败则记录异常日志，并在response中返回failure.
				 System.out.println("验证失败,不去更新状态");  
				 AlipayCore.logResult(this.getRootPath(), "验证失败,不去更新状态", "verify_error");
		         return ("failure");  
			}*/
		}else{
			 System.out.println("支付宝交易超时关闭或退款,不去更新状态");  
			 AlipayCore.logResult(this.getRootPath(), "支付宝交易超时关闭或退款,不去更新状态", "trade_error");
			 return ("failure");// 交易失败  
	    }
	}
	
   /* 参数		参数名称	类型		必填	描述										范例
	notify_time	通知时间	Date	是	通知的发送时间。格式为yyyy-MM-dd HH:mm:ss	2015-14-27 15:45:58
	notify_type	通知类型	String(64)	是	通知的类型	trade_status_sync
	notify_id	通知校验ID	String(128)	是	通知校验ID	ac05099524730693a8b330c5ecf72da9786
	app_id	支付宝分配给开发者的应用Id	String(32)	是	支付宝分配给开发者的应用Id	2014072300007148
	charset	编码格式	String(10)	是	编码格式，如utf-8、gbk、gb2312等	utf-8
	version	接口版本	String(3)	是	调用的接口版本，固定为：1.0	1.0
	sign_type	签名类型	String(10)	是	商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2	RSA2
	sign	签名	String(256)	是	请参考异步返回结果的验签	601510b7970e52cc63db0f44997cf70e
	trade_no	支付宝交易号	String(64)	是	支付宝交易凭证号	2013112011001004330000121536
	out_trade_no	商户订单号	String(64)	是	原支付请求的商户订单号	6823789339978248
	out_biz_no	商户业务号	String(64)	否	商户业务ID，主要是退款通知中返回退款申请的流水号	HZRF001
	buyer_id	买家支付宝用户号	String(16)	否	买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字	2088102122524333
	buyer_logon_id	买家支付宝账号	String(100)	否	买家支付宝账号	15901825620
	seller_id	卖家支付宝用户号	String(30)	否	卖家支付宝用户号	2088101106499364
	seller_email	卖家支付宝账号	String(100)	否	卖家支付宝账号	zhuzhanghu@alitest.com
	trade_status	交易状态	String(32)	否	交易目前所处的状态，见交易状态说明	TRADE_CLOSED
	total_amount	订单金额	Number(9,2)	否	本次交易支付的订单金额，单位为人民币（元）	20
	receipt_amount	实收金额	Number(9,2)	否	商家在交易中实际收到的款项，单位为元	15
	invoice_amount	开票金额	Number(9,2)	否	用户在交易中支付的可开发票的金额	10.00
	buyer_pay_amount	付款金额	Number(9,2)	否	用户在交易中支付的金额	13.88
	point_amount	集分宝金额	Number(9,2)	否	使用集分宝支付的金额	12.00
	refund_fee	总退款金额	Number(9,2)	否	退款通知中，返回总退款金额，单位为元，支持两位小数	2.58
	subject	订单标题	String(256)	否	商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来	当面付交易
	body	商品描述	String(400)	否	该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来	当面付交易内容
	gmt_create	交易创建时间	Date	否	该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss	2015-04-27 15:45:57
	gmt_payment	交易付款时间	Date	否	该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss	2015-04-27 15:45:57
	gmt_refund	交易退款时间	Date	否	该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.S	2015-04-28 15:45:57.320
	gmt_close	交易结束时间	Date	否	该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss	2015-04-29 15:45:57
	fund_bill_list	支付金额信息	String(512)	否	支付成功的各个渠道金额信息，详见资金明细信息说明	[{“amount”:“15.00”,“fundChannel”:“ALIPAYACCOUNT”}]
	passback_params	回传参数	String(512)	否	公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝	merchantBizType%3d3C%26merchantBizNo%3d2016010101111
	voucher_detail_list	优惠券信息	String	否	本交易支付时所使用的所有优惠券信息，详见优惠券信息说明	[{“amount”:“0.20”,“merchantContribute”:“0.00”,“name”:“一键创建券模板的券名称”,“otherContribute”:“0.20”,“type”:“ALIPAY_DISCOUNT_VOUCHER”,“memo”:“学生卡8折优惠”]
	*/
			
  /*https://api.xx.com/receive_notify.htm?total_amount=2.00&buyer_id=2088102116773037
		&body=大乐透2.1&trade_no=2016071921001003030200089909&refund_fee=0.00
		&notify_time=2016-07-19 14:10:49&subject=大乐透2.1&sign_type=RSA2
		&charset=utf-8&notify_type=trade_status_sync&out_trade_no=0719141034-6418
		&gmt_close=2016-07-19 14:10:46&gmt_payment=2016-07-19 14:10:47
		&trade_status=TRADE_SUCCESS&version=1.0
		&sign=kPbQIjX+xQc8F0/A6/AocELIjhhZnGbcBN6G4MM/HmfWL4ZiHM6fWl5NQhzXJusaklZ
		1LFuMo+lHQUELAYeugH8LYFvxnNajOvZhuxNFbN2LhF0l/KL8ANtj8oyPM4NN7Qft2kWJTDJUpQO
		zCzNnV9hDxh5AaT9FPqRS6ZKxnzM=&gmt_create=2016-07-19 14:10:44
		&app_id=2015102700040153&seller_id=2088102119685838
		&notify_id=4a91b7a78a503640467525113fb7d8bg8e
	*/
	
	/**
	 * 订单退款
	 * @param request
	 * @param response
	 * @param tradeno 支付宝交易订单号
	 * @param orderno 商户订单号 
	 * @param refundAmount 退款金额
	 */
	public void orderPayRefund(HttpServletRequest request, HttpServletResponse response, 
			String tradeno, String orderno,String refundAmount) {
		
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest(); // 统一收单交易退款接口
		// 只需要传入业务参数
		Map<String, Object> param = new HashMap<>();
		param.put("out_trade_no", orderno); // 商户订单号
		param.put("trade_no", tradeno);// 支付宝交易订单号
		param.put("refund_amount", refundAmount);// 退款金额
		param.put("refund_reason", "超时支付退款");// 退款原因
		param.put("out_request_no", PayUtil.getRefundNo()); //退款单号
		alipayRequest.setBizContent(JSON.toJSONString(param)); // 不需要对json字符串转义 

		Map<String, Object> restmap = new HashMap<>();// 返回支付宝退款信息
		boolean flag = false; // 退款状态
		try {
			AlipayTradeRefundResponse alipayResponse = AlipayUtil.getAlipayClient().execute(alipayRequest);
			if (alipayResponse.isSuccess()) {
				// 调用成功，则处理业务逻辑
				if ("10000".equals(alipayResponse.getCode())) {
					// 订单退款成功
					flag = true;
					restmap.put("out_trade_no", alipayResponse.getOutTradeNo());//商户订单号
					restmap.put("trade_no", alipayResponse.getTradeNo());//支付宝交易号
					restmap.put("buyer_logon_id", alipayResponse.getBuyerLogonId());// 用户的登录id
					restmap.put("refund_fee", alipayResponse.getRefundFee());//退款总金额
					restmap.put("gmt_refund_pay", alipayResponse.getGmtRefundPay()); // 退款支付时间
					restmap.put("buyer_user_id", alipayResponse.getBuyerUserId());// 买家在支付宝的用户id
					//LOG.info("订单退款结果：退款成功");
					AlipayCore.logResult(this.getRootPath(), "订单退款结果：退款成功---"+restmap.toString(), "_PayRefund_success");
				} else {
					//LOG.info("订单退款失败：" + alipayResponse.getMsg() + ":" + alipayResponse.getSubMsg());
					AlipayCore.logResult(this.getRootPath(), "订单退款失败---"+alipayResponse.getMsg() + ":" + alipayResponse.getSubMsg(), "_PayRefund_failure");
				}
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		/*if (flag) {
			// 订单退款成功
			WebUtil.response(response,WebUtil.packJsonp(callback,
							JSON.toJSONString(new JsonResult(1, "订单退款成功", new ResponseData(null, restmap)),
									SerializerFeatureUtil.FEATURES)));
		} else { // 订单退款失败
			WebUtil.response(response, WebUtil.packJsonp(callback, 
							JSON.toJSONString(new JsonResult(-1, "订单退款失败", new ResponseData()), 
									SerializerFeatureUtil.FEATURES)));
		}*/
	}
	
	/**
	 * 服务端——跳转进入会员充值页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pc/addRechargePC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String addRechargePC(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String customerId = request.getParameter("objectId");
		if(StringUtils.isNotBlank(customerId)){
			CAmount amount = amountMapper.getAmountByCustomerId(Long.valueOf(customerId));
			model.put("amount", amount);
			model.put("customerId", customerId);
			
		}
		return "/modules/customer/addRecharge";
	}
	
	/**
	 * 获取服务端充值时的——短信验证码
	 * @param domain
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "pc/getRechargeSmsCode.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getRechargeSmsCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		//获取充值验证手机
		String phone = dictionaryService.getDicValueByCode("RECHARGE_MOBILE", "reMobile");//request.getParameter("phone");		
		if(StringUtils.isNotBlank(phone)){
			String result = SmsCodeUtil.sendCode(phone,CheckBuilder.RECHARGE_TEMPLATE);
			SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
			if(codeVO !=null &&"200".equals(codeVO.code)){
				returnMap.put("state", true);
				returnMap.put("msg", "验证码发送成功,请查看手机获取！");
			}else{
				returnMap.put("state", false);
				returnMap.put("msg", "验证码发送失败！");
			}
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "获取验证手机失败！");
		}
		
		return gson.toJson(returnMap);
	}
	
	/**
	 * 保存会员充值记录和会员金额记录表
	 * @param domain
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "pc/saveRechargePC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String saveRechargePC(CAmount domain ,HttpServletRequest request, HttpServletResponse response){
		Gson gson = new Gson();
		Map<String, Object> returnMap = new HashMap<String , Object>();
		String smsCode = request.getParameter("smsCode");
		if(StringUtils.isBlank(smsCode)){
			returnMap.put("state", false);
			returnMap.put("msg", "充值短信验证码不能为空！");
            return gson.toJson(returnMap);
		}
		if (domain == null) {
			returnMap.put("state", false);
			returnMap.put("msg", GlobalConstants.MSG_SAVE_FAILED);
            return gson.toJson(returnMap);
        }
		
		//校验短信验证码是否正确
		String result="";
		String phone = dictionaryService.getDicValueByCode("RECHARGE_MOBILE", "reMobile");
		if(StringUtils.isBlank(phone)){
			returnMap.put("state", false);
			returnMap.put("msg", "获取充值验证手机错误！");
			return gson.toJson(returnMap);
		}
		try {
			result = SmsCodeUtil.verifyCode(phone, smsCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SmsCodeVO codeVO = gson.fromJson(result,SmsCodeVO.class);
		if(codeVO !=null && "200".equals(codeVO.code)){
			//保存充值记录
			SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
			String rechargeNum = "R"+ formatter.format(new Date()) + "_system"; //表示后台手动充值
			BRechargeLog  record = new BRechargeLog();
			record.setRechargeNum(rechargeNum);
			record.setCustomerId(Long.valueOf(domain.getCustomerId()));
			record.setAmount(Double.valueOf(domain.getActualAmount()));
			record.setPayType(3);//支付方式：系统
			record.setStatus(1);//修改支付状态：成功
			record.setEnabled(1);
			int m = rechargeLogService.saveOrUpdate(record);
			//保存客户金额记录
			//查询当前会员的最后一条记录
			domain.setCurAmount(domain.getRemainAmout());
			domain.setRemainAmout(domain.getRemainAmout() +  domain.getActualAmount());			
			domain.setRechargeType(1);//1:充值2:消费
			domain.setEnabled(1);
			domain.setCreateTime(CalendarUtil.getLongFormatDate());	
			int i  = amountMapper.insert(domain);
			if (i > 0) {
				//修改会员等级
				Map<String,Object> map = new HashMap<String,Object>();	
				map.put("customerId", domain.getCustomerId());
				map.put("rechargeType", 1);//1:充值2:消费
				map.put("enabled", 1);
				Double totalAmount = amountMapper.getTotalAmount(map);
				customerService.updateUserLevel(domain.getCustomerId(), totalAmount);
				
				returnMap.put("state", true);
				returnMap.put("msg", "充值成功");
	        } else {
	        	returnMap.put("state", false);
				returnMap.put("msg", "充值失败");
	        }
		}else{
			returnMap.put("state", false);
			returnMap.put("msg", "充值短信验证码错误！");
		}
		
		return gson.toJson(returnMap);
	}
	
	

	/**
	 * 服务端——跳转进入充值记录查询页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="pc/czjlcx.act",method ={RequestMethod.POST,RequestMethod.GET})
	public String indexRechargePC(HttpServletRequest request, ModelMap model){
		
		return "/modules/recharge/index";
	}
	/**
	 * 服务端获取充值记录信息
	 * 分页查询
	 */
	@RequestMapping(value = "pc/getRechargeListPC.act",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	public String getRechargeListPC(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		// 有参数时获取参数值，否则默认值
		String page = request.getParameter("pageNo");//当前页码
		String payType = request.getParameter("payType");
		String userName = request.getParameter("userName");
		String nickName = request.getParameter("nickName");
		
		pageNo=1;
		pageSize=10;
		if(page != null && page != "0"){
			pageNo =Integer.valueOf(page) ;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageIndex", (pageNo-1)*pageSize);//从第几条开始取
		map.put("pageSize", pageSize);//查询条数
		map.put("enabled", "1");//1：有效  0：无效
		//map.put("status", 1);
		if(StringUtils.isNotBlank(payType)){
			map.put("payType", payType);
		}
		if(StringUtils.isNotBlank(userName)){
			map.put("userName", userName);
		}
		if(StringUtils.isNotBlank(nickName)){
			map.put("nickName", "%"+nickName+"%");
		}
		
		List<BRechargeLog> list= rechargeLogService.queryListByPage(map);
		// 总数据条数
		Integer totalCount = rechargeLogService.findTotalCount(map);
		pageInfo = PaginationUtil.getPageInfo(totalCount, pageNo, pageSize);
		
		model.put("resultList", list);
		model.put("pageInfo", pageInfo);
		
		return "/modules/recharge/data";
	}
}
