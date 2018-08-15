<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<style type="text/css">
.hide{
	display:none;
}
.show{
	displsy:block;
}
.border0{
	width:100px;
}

.searchInput{
	box-shadow:inset 0 0 4px #eee; width:150px; margin:0; padding:5px 12px; border-radius:4px; border:1px solid silver; font-size:0.8em;
}
.searchLogo{
	cursor:pointer;
	float:left;
	widtd:20px;
	height:20px;
	transform: translate(-20%, 20%);
	color:#c9021b;

}
.searchLogo:hover{
	filter:alpha(opacity=50);  
   	-moz-opacity:0.5;  
   	-khtml-opacity: 0.5;  
   	opacity: 0.5;  
}
</style>
<input type="hidden" id="goodId" name="goodId" value="${goods.id}" />
<div class=" col-md-12 not-padding"style="color:red">
	<div class="col-md-2 not-padding text-right">
	</div>
	<div class="col-md-7 not-padding"  id="message" >
	</div>
	<div class="col-md-2 col-sm-4 col-xs-4" >
		<input type="text" id="searchFlag" class="searchInput" onchange="query()" placeholder="请输入手机号或昵称" />
		
	</div>
	<div class="col-md-1 " >
		<img src="<p:basePath/>assets/images/search.svg" title="搜索"  onclick="query()" class="searchLogo">
	</div>
</div>
<div class="space col-md-12 not-padding">
	<div class="col-md-1 not-padding text-right">
		规则：
	</div>
	<div class="col-md-5 not-padding ">
		<label style="font-weight:normal;"><input type="radio" name="status" value="1" 
			<c:if test="${goods.rule == '1'}">checked</c:if> /> 随机中奖</label>
  		<label style="font-weight:normal;"><input type="radio" name="status" value="2" 
  			<c:if test="${goods.rule == '2'}">checked</c:if>/> 设定最低人次</label>
  		<label style="font-weight:normal;"><input type="radio" name="status" value="3" 
  			<c:if test="${goods.rule == '3'}">checked</c:if>/> 指定中奖人</label>
	</div>
	<div class="col-md-2 not-padding text-right zjr <c:if test="${goods.rule == '3'}">show</c:if><c:if test="${goods.rule != '3'}">hide</c:if>">
		中奖人：
	</div>
	<div class="col-md-2 not-padding text-left zjr <c:if test="${goods.rule == '3'}">show</c:if><c:if test="${goods.rule != '3'}">hide</c:if>" >
		<input type="hidden" class="border0" value="${goods.winnerId}" name="winnerId" id="winnerId"  readonly/>
		<input type="text" class="border0" value="${nickMap[goods.winnerId]}" name="nickName" id="nickName"  readonly/>
	</div>
	<div class="col-md-2 not-padding text-right zdrc <c:if test="${goods.rule == '2'}">show</c:if><c:if test="${goods.rule != '2'}">hide</c:if>">
		最低中奖人次：
	</div>
	<div class="col-md-2 not-padding text-left zdrc <c:if test="${goods.rule == '2'}">show</c:if><c:if test="${goods.rule != '2'}">hide</c:if>" >
		<input type="text" class="border0" value="${goods.amountLimit}" name="amountLimit" id="amountLimit"  />
	</div>

</div>
<div class="col-md-12 not-padding" style="height:300px;overflow-y: auto;">
	<table id="data" class="table table-hdbg td-first-left">
	   <thead>
	    <tr>
	     <td>序号</td>
	     <td>商品名称</td>
	     <td>期数</td>
	     <td>订单号</td>
	     <td>客户ID</td>
	     <td>手机号</td>
	     <td>客户昵称</td>
	     <td>购买人次</td>
	 	 <td>操作</td>
	    </tr>
	   </thead>
	   <tbody id="listTbody">
	     <c:forEach var="item" items="${detailList}" varStatus="status">
           	<tr class="trRow" name="${nameMap[item.customerId]}${nickMap[item.customerId]}">
				<td>${status.index + 1}</td>
				<td>${item.goodsName }</td>
				<td>${item.issue }</td>
				<td>${item.orderNum }</td>
				<td>${item.customerId }</td>
				<td>${nameMap[item.customerId]}</td>
				<td>${nickMap[item.customerId]}</td>
				<td>${item.quantity }</td>
				<td style="width:180px;">
					<button type="button" class="btn btn-warning btn-sm" onclick="setZjr(${item.customerId},'${nickMap[item.customerId]}');"><i class="glyphicon glyphicon-pencil"></i>中奖人</button>
					<button type="button" class="btn btn-warning btn-sm" onclick="setZdrc(${item.quantity});"><i class="glyphicon glyphicon-pencil"></i>最低人次</button>
				 </td>
			</tr>
         </c:forEach>
	   </tbody>
	  </table>
</div>
<div class="col-md-12" style="margin-top:10px;text-align:center;">
	<button type="button" class="btn btn-success btn-sm" onclick="save();"><i class=""></i>保存</button>
	<button type="button" class="btn btn-warning btn-sm" onclick="hide();"><i class=""></i>关闭</button>
</div>

<script type="text/javascript">

$(function(){
	$(":radio").click(function(){
		if($(this).val() ==1){
			$(".zjr").removeClass("show").addClass("hide");
			$(".zdrc").removeClass("show").addClass("hide");
		}
		if($(this).val() ==2){
			$(".zjr").removeClass("show").addClass("hide");
			$(".zdrc").removeClass("hide").addClass("show");
		}
		if($(this).val() ==3){			
			$(".zjr").removeClass("hide").addClass("show");
			$(".zdrc").removeClass("show").addClass("hide");
		}
	});
});

function setZjr(id,name){
	$(".zjr").addClass("show").removeClass("hide");
	$(".zdrc").addClass("hide").removeClass("show");
	$("#winnerId").val(id);
	$("#nickName").val(name);
	$("input[name='status']:eq(2)").click(); 
}
function setZdrc(quantity){
	$(".zdrc").addClass("show").removeClass("hide");
	$(".zjr").addClass("hide").removeClass("show");
	$("#amountLimit").val(quantity);
	$("input[name='status']:eq(1)").click()(); 
}

$("#searchFlag").keyup(function (event) {
    //if (event.which == "13") {
       query();
    //}
});
function query(){
	var searchFlag = $("#searchFlag").val();
	if(searchFlag ==""){
		$(".trRow").css("display","table-row");
	}else{
		$(".trRow").css("display","none");
		$("tr[name*='" + searchFlag + "']").css("display","table-row");
	}
	
}
</script>