<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

<div class="col-md-12 not-padding" style="height: 500px;overflow:auto; ">
	  <table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>红包名称</td>
	     <td>红包描述</td>
	     <td>红包金额(元)</td>
	     <td>使用条件</td>
	     <td>有效期限(天)</td>
	     <td>红包类型</td>
	     <td>状态</td>
	     <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.bonusName }</td>
				<td>${item.description }</td>
				<td>${item.amount }</td>
				<td>${item.useCondition }</td>
				<td>${item.indate }</td>
				<td><p:dicCode4Value kindCode="BONUS_TYPE" dicCode="${item.bonusType }"></p:dicCode4Value></td>
				<td><p:dicCode4Value kindCode="ENABLE" dicCode="${item.enabled }"></p:dicCode4Value></td>
				<td>
					<button type="button" class="btn btn-warning btn-sm" onclick="demo_edit(${item.id});"><i class="glyphicon glyphicon-pencil"></i>修改</button>
					<button type="button" class="btn btn-danger btn-sm" onclick="demo_delete(${item.id});"><i class="glyphicon glyphicon-remove"></i>删除</button>
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