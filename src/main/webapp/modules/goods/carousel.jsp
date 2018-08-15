<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<style>
#contentDiv{
	height:446px;
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
	     <td>主图</td>
	     <td>状态</td>
	     <td>是否轮播图</td>
	     <td>修改日期</td>
	 	 <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}<input name="rowChk" type="checkbox" value="${item.id}" row="${status.index}"></td>
				<td>${item.goodsName }</td>
				<td style="width:180px;">
					<c:if test="${not empty item.homeImage}">
						<img src="http://www.ryxb.info${homePath}${item.homeImage}" width="65" height="18" />
					</c:if>
				</td>
				<td><p:dicCode4Value kindCode="GOODS_STATUS" dicCode="${item.status }"></p:dicCode4Value></td>
				<td><p:dicCode4Value kindCode="IS_NOT" dicCode="${item.isTop }"></p:dicCode4Value></td>
				<td>${item.createTime }</td>
				<td style="width:150px;">
					<button type="button" class="btn btn-warning btn-sm" onclick="modify(${item.id});"><i class="glyphicon glyphicon-pencil"></i>设置</button>
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