<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<style>
#contentDiv{
	width:100%;
	height:446px;
	overflow-x: auto;
	overflow-y: auto;
}
#data tr td{
	padding: 5px;
	display:table-cell; 
	vertical-align:middle
}
</style>
<div class="col-md-12 not-padding" id="contentDiv">
	<table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>商品名称</td>
	     <td>期数</td>
	     <td>金额(元)</td>
	     <td>最小购买单元</td>
	     <td>开奖时间</td>
		 <td>开奖规则</td>
		 <td>中奖人id</td>
		 <td>中奖人昵称</td>
		 <td>中奖金额限制</td>
	 	 <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.goodsName }</td>
				<td>${item.issue }</td>
				<td>${item.price }</td>
				<td>${item.minLimit }</td>
				<td>${item.saleTime }</td>
				<td><p:dicCode4Value kindCode="RULE_TYPE" dicCode="${item.rule }"></p:dicCode4Value></td> 
				<td><c:if test="${item.rule == '3'}">${item.winnerId }</c:if></td>
				<td><c:if test="${item.rule == '3'}">${item.description }</c:if></td>
				<td><c:if test="${item.rule == '2'}">${item.amountLimit }</c:if></td>
				<td style="width:50px;">
					<button type="button" class="btn btn-warning btn-sm" onclick="modify('${item.id}','${item.issue}');"><i class="glyphicon glyphicon-pencil"></i>设置</button>
				 </td>
			</tr>
         </c:forEach>
	   </tbody>
	  </table>
	  

</div>

 <!-- 分页 -->
	 <div class="row_wj" style="text-align: right;">
		<div class="quotes" id="pagination">
			${pageInfo}
		</div>
	 </div>
     <!-- 分页 -->
