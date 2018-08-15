<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

<div class="col-md-12 not-padding" style="height: 500px;overflow:auto; ">
	  <table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>用户名</td>
	     <td>姓名</td>
	     <td>性别</td>
	     <td>状态</td>
	     <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.userName }</td>
				<td>${item.trueName }</td>
				<td><p:dicCode4Value kindCode="SEX" dicCode="${item.sex }"></p:dicCode4Value></td>
				<td><p:dicCode4Value kindCode="ENABLE" dicCode="${item.enabled }"></p:dicCode4Value></td>
				<td>
				    <button type="button" class="btn btn-primary btn-sm" onclick="demo_modifyPassword(${item.id});"><i class="glyphicon glyphicon-pencil"></i>修改密码</button>
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