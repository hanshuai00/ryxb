<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

 <form id="addOrUpdateForm" method="post" >
	 <div class=" col-md-12 not-padding"style="color:red">
		<div class="col-md-2 not-padding text-right">
			&nbsp;
		</div>
		<div class="col-md-7 not-padding"  id="message" >
		</div>
	</div>
	<div class="space col-md-12 not-padding">
		<input type="hidden" value="${record.customerId }" name="customerId" id="customerId"  />
		<input type="hidden" value="${record.id }" name="winId" id="winId"  />
		<div class="col-md-2 not-padding text-right">
			收货人姓名：
		</div>
		<div class="col-md-4 not-padding">
			${address.receiverName }
		</div>
		<div class="col-md-2 not-padding text-right">
			手机号码：
		</div>
		<div class="col-md-4 not-padding">
			<input type="text" value="${address.cellPhone }" name="cellPhone" id="cellPhone" required="required" />
		</div>
	</div>
	<div class="space col-md-12 not-padding">	
		<div class="col-md-2 not-padding text-right">
			详细地址：
		</div>
		<div class="col-md-10 not-padding">
			${address.streetInfo }
		</div>
	</div>
	<div class="space col-md-12 not-padding">	
		<div class="col-md-2 not-padding text-right">
			中奖商品：
		</div>
		<div class="col-md-4 not-padding">
			<input type="text"  value="${record.goodsName }" name="goodsName" id="goodsName" required="required"  />
		</div>
		<div class="col-md-2 not-padding text-right">
			中奖人昵称：
		</div>
		<div class="col-md-4 not-padding">
			<input type="text"  value="${record.nickname }" name="nickname" id="nickname" required="required" />
		</div>
	</div>
	
	<div class="space col-md-12 not-padding">
		<div class="col-md-2 not-padding text-right">
			快递公司：
		</div>
		<div class="col-md-4 not-padding ">
			<input type="text" value="" name="param1" id="param1" required="required" />
		</div>
		<div class="col-md-2 not-padding text-right">
			快递单号：
		</div>
		<div class="col-md-4 not-padding">
		     <input type="text" value="" name="param2" id="param2" required="required" />
		</div>
	</div>
	
	
	<div class="col-md-12" style="margin-top:10px;text-align:center;">
		<button type="button" class="btn btn-success btn-sm" onclick="batch_save();"><i class=""></i>确定</button>
		<button type="button" class="btn btn-success btn-sm" onclick="batch_cancel();"><i class=""></i>取消</button>
	</div>
	</form>