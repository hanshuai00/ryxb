<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

<div class="col-md-12 not-padding" style="height: 500px;overflow:auto; ">
	  <table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>充值订单号</td>
	     <td>会员账号</td>
	     <td>会员昵称</td>
	     <td>充值方式</td>
	     <td>充值金额(元)</td>
	     <td>充值状态</td>
	     <td>充值时间</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.rechargeNum }</td>
				<td>${item.userName }</td>
				<td>${item.nickname }</td>
				<td><p:dicCode4Value kindCode="PAY_TYPE" dicCode="${item.payType }"></p:dicCode4Value></td>
				<td>${item.amount }</td>
				<td><p:dicCode4Value kindCode="PAY_STATUS" dicCode="${item.status }"></p:dicCode4Value></td>
				<td>${item.createTime }</td>
				<%-- <td>
					<button type="button" class="btn btn-warning btn-sm" onclick="demo_edit(${item.id});"><i class="glyphicon glyphicon-pencil"></i>修改</button>
					<button type="button" class="btn btn-danger btn-sm" onclick="demo_delete(${item.id});"><i class="glyphicon glyphicon-remove"></i>删除</button>
				 </td> --%>
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