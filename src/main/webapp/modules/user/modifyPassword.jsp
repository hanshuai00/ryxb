<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

 <form id="modifyPasswordForm" method="post" >
 	<div class=" col-md-12 not-padding"style="color:red">
		<div class="col-md-2 not-padding text-right">
			&nbsp;
		</div>
		<div class="col-md-7 not-padding"  id="message" >
		</div>
	</div>
	<div class="space col-md-12 not-padding">
		<div class="col-md-2 not-padding text-right">
			短信验证码：
		</div>
		<div class="col-md-7 not-padding">
			<input type="text" value="" name="smsCode" id="smsCode" onkeyup="value=value.replace(/[^\-?\d.]/g,'')" title="只能输入数字"/>
			<button type="button" class="btn btn-success btn-sm" onclick="getPasswordSmsCode();"><i class=""></i>获取验证码</button>
		</div>
	</div>
	<div class="space col-md-12 not-padding">
		<input type="hidden" value="${userId}" name="userId" id="userId"  />
		<div class="col-md-2 not-padding text-right">
			新密码：
		</div>
		<div class="col-md-3 not-padding">
			<input type="text" value="" name="newPassword" id="newPassword" />
		</div>
	</div>
	
	<div class="col-md-12" style="margin-top:10px;text-align:center;">
		<button type="button" class="btn btn-success btn-sm" onclick="batch_save();"><i class=""></i>确定</button>
		<button type="button" class="btn btn-success btn-sm" onclick="batch_cancel();"><i class=""></i>取消</button>
	</div>
</form>