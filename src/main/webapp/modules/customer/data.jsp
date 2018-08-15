<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

<div class="col-md-12 not-padding" style="height: 500px;overflow:auto; ">
	  <table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>用户名</td>
	     <td>昵称</td>
	     <td>姓名</td>
	     <td>性别</td>
	     <td>生日</td>
	     <td>夺宝金额(元)</td>
	     <td>等级</td>
	     <td>状态</td>
	     <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.userName }</td>
				<td>${item.nickname }</td>
				<td>${item.trueName }</td>
				<td><p:dicCode4Value kindCode="SEX" dicCode="${item.userGender }"></p:dicCode4Value></td>
				<td>${item.birthday }</td>
				<td>${item.cusAmount }</td>
				<td><p:dicCode4Value kindCode="USER_LEVEL" dicCode="${item.userLevel }"></p:dicCode4Value></td>
				<td><p:dicCode4Value kindCode="ENABLE" dicCode="${item.enabled }"></p:dicCode4Value></td>
				<td>
				    <button type="button" class="btn btn-primary btn-sm" onclick="demo_recharge(${item.id});"><i class="glyphicon glyphicon-pencil"></i>充值</button>
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