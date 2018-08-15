<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

<div class="col-md-12 not-padding" style="height: 500px;overflow:auto; ">
	  <table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>商品名称</td>
	     <td>商品价格(元)</td>
	     <td>开奖期数</td>
	     <td>中奖号码</td>
	     <td>中奖会员</td>
	     <td>中奖会员昵称</td>
	     <td>本期夺宝人数</td>
	     <td>开奖时间</td>
	     <td>开奖状态</td>
	     <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.goodsName }</td>
				<td>${item.goodPrice }</td>
				<td>${item.issue }</td>
				<td>${item.orderNum }</td>
				<td>${item.userName }</td>
				<td>${item.nickname }</td>
				<td>${item.personCount }</td>
				<td>${item.drawTime }</td>
				<td><p:dicCode4Value kindCode="WIN_STATUS" dicCode="${item.winStatus }"></p:dicCode4Value></td>
				<td>
					<c:if test="${item.winStatus==2 }">
						<button type="button" class="btn btn-warning btn-sm" onclick="demo_sendSms(${item.id},${item.customerId});"><i class="glyphicon glyphicon-pencil"></i>发货</button>
					</c:if>
					<%-- <c:if test="${item.winStatus==3 }">
						<button type="button" class="btn btn-warning btn-sm" onclick="demo_edit(${item.customerId});"><i class="glyphicon glyphicon-pencil"></i>收货</button>
					</c:if> --%>
				 </td>
			</tr>
         </c:forEach>
	   </tbody>
	  </table>
	  
	 <!-- 分页 -->
	 <div class="row_wj" style="text-align: right;">
		<div class="quotes" id="pagination">
			${pageInfo}
		</div>
	 </div>
     <!-- 分页 -->
</div>