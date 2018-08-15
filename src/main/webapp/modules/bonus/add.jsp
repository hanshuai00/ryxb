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
		<input type="hidden" value="${bonus.id }" name="id" id="id"  />
		<div class="col-md-2 not-padding text-right">
			红包名称：
		</div>
		<div class="col-md-4 not-padding">
			<input type="text" value="${bonus.bonusName }" name="bonusName" id="bonusName"  />
		</div>
		<div class="col-md-2 not-padding text-right">
			红包金额：
		</div>
		<div class="col-md-4 not-padding">
			<input type="text" value="${bonus.amount }" name="amount" id="amount" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>(元)
		</div>
	</div>
	<div class="space col-md-12 not-padding">	
		<div class="col-md-2 not-padding text-right">
			红包描述：
		</div>
		<div class="col-md-10 not-padding">
			<input type="text" style="width:87.5%" value="${bonus.description }" name="description" id="description"  />
		</div>
	</div>
	<div class="space col-md-12 not-padding">	
		<div class="col-md-2 not-padding text-right">
			使用条件：
		</div>
		<div class="col-md-10 not-padding">
			<input type="text" style="width:87.5%" value="${bonus.useCondition }" name="useCondition" id="useCondition"  />
		</div>
	</div>
	
	<div class="space col-md-12 not-padding">
		<div class="col-md-2 not-padding text-right">
			有效期：
		</div>
		<div class="col-md-4 not-padding ">
			<input type="text" value="${bonus.indate }" name="indate" id="indate" onkeyup="value=value.replace(/[^\-?\d.]/g,'')" />(天)
		</div>
		<div class="col-md-2 not-padding text-right">
			红包类型：
		</div>
		<div class="col-md-4 not-padding">
		     <select id="bonusType" name="bonusType" style="width:60%">
				<option value="">- - - 请选择 - - -</option>
				<p:dicCodeList kindCode="BONUS_TYPE" varDictionarylist="code" >
				 	<%-- <option value="${code.dicCode}" >${code.dicValue}</option> --%>
				   <option value="${code.dicCode}" <c:if test="${code.dicCode == bonus.bonusType}">selected="selected"</c:if>>${code.dicValue}</option>
				</p:dicCodeList>
			</select>
			<%-- <p:dicCodeList kindCode="BONUS_TYPE" varDictionarylist="code">
			    <input type="radio" name="pproperty" value="${code.dicValue}"  
			    <c:if test="${code.dicCode == requestScope.jbProject.pproperty}">checked="checked"</c:if> />${code.dicValue}
			</p:dicCodeList> --%>
		</div>
	</div>
	
	
	<div class="col-md-12" style="margin-top:10px;text-align:center;">
		<button type="button" class="btn btn-success btn-sm" onclick="batch_save();"><i class=""></i>确定</button>
		<button type="button" class="btn btn-success btn-sm" onclick="batch_cancel();"><i class=""></i>取消</button>
	</div>
	</form>