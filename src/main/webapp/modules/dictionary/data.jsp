<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

<div class="col-md-12 not-padding">
	  <table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>类型编号</td>
	     <td>类型名称</td>
	     <td>排序</td>
	     <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.dicCode }</td>
				<td>${item.dicValue }</td>
				<td>${item.orderIndex }</td>
				<td>
					<button type="button" class="btn btn-warning btn-sm" onclick="demo_edit('${item.dicCode}');"><i class="glyphicon glyphicon-pencil"></i>修改</button>
					<button type="button" class="btn btn-danger btn-sm" onclick="demo_delete'${item.id}');"><i class="glyphicon glyphicon-remove"></i>删除</button>
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