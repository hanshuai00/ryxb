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
	     <td>分类</td>
	     <td>主图</td>
	     <td>商品名称</td>
	      <td>金额(元)</td>
	     <td>总期数</td>
	     <td>当前期数</td>
	     <td>剩余人次</td>
	     <td>进度(%)</td>
	     <td>最小购买单元</td>
	     <td>开奖周期(秒)</td>
	     <td>状态</td>
	 	 <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${resultList}" varStatus="status">
           	<tr>
				<td>${status.index + 1}</td>
				<%-- <input name="rowChk" type="checkbox" value="${item.id}" row="${status.index}"> --%>
				<td>${item.catalogue }</td>
				<td style="width:180px;">
					<c:if test="${not empty item.goodsImage}">
						<c:set var="goodsImage" value="${item.goodsImage}"/>
						<c:set var="images" value="${fn:split(goodsImage, ',')}" />
						<c:forEach items="${images}" var="image">
							<img src="http://www.ryxb.info${goodPath}${image}" width="30" height="30" />
						</c:forEach>
					</c:if>
				</td>
				<td>${item.goodsName }</td>
				<td>${item.price }</td>
				<td>${item.totalIssue }</td>
				<td>${item.issue }</td>
				<td>${item.restQuantity }</td>
				<td>${item.progress }</td>
				<td>${item.minLimit }</td>
				<td>${item.turnaround }</td>
				<td><p:dicCode4Value kindCode="GOODS_STATUS" dicCode="${item.status }"></p:dicCode4Value></td>
				<td style="width:200px;">
					<button type="button" class="btn btn-warning btn-sm" onclick="modify(${item.id});"><i class="glyphicon glyphicon-pencil"></i>修改</button>
					<button type="button" class="btn btn-danger btn-sm" onclick="remove(${item.id});"><i class="glyphicon glyphicon-remove"></i>删除</button>
					<button type="button" class="btn btn-warning btn-sm" onclick="updateSort(${item.id});"><i class="glyphicon glyphicon-pencil"></i>置顶</button>
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