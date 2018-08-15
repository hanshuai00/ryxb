<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>

<div class="col-md-12 not-padding">
	  <table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>分类名称</td>
	     <td>描述</td>
	     <td>状态</td>
	     <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<td>${item.catalogue }</td>
				<td>${item.description }</td>
				<td><p:dicCode4Value kindCode="ENABLE" dicCode="${item.enabled }"></p:dicCode4Value></td>
				<td>
					<a class="colorbox" title="修改" href="#">修改</a>
					<a class="colorbox" title="删除" href="#">删除</a>
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