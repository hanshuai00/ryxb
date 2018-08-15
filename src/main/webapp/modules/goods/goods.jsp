<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<input type="hidden" id="goodId" name="goodId" value="${goods.id}" />
<div class=" col-md-12 not-padding"style="color:red">
	<div class="col-md-2 not-padding text-right">
		&nbsp;
	</div>
	<div class="col-md-7 not-padding"  id="message" >
	</div>
</div>
<div class="space col-md-12 not-padding">
	<div class="col-md-2 not-padding text-right">
		分类：
	</div>
	<div class="col-md-4 not-padding">
		<select name="catalogueId" id="catalogueId" style="width: 150px;height: 25px;">
			<option value="" >请选择</option>
			<c:forEach items="${catalogue}" var="cata" >
				<option value="${cata.id }" <c:if test="${goods.catalogueId == cata.id}">selected</c:if> >${cata.catalogue }</option>
			</c:forEach>
	   </select>
	</div>
	<div class="col-md-2 not-padding text-right">
		商品名称：
	</div>
	<div class="col-md-4 not-padding">
		<input type="text" value="${goods.goodsName}" name="goodsName" id="goodsName"  />
	</div>
</div>
<div class="space col-md-12 not-padding">
	
	<div class="col-md-2 not-padding text-right">
		标题：
	</div>
	<div class="col-md-10 not-padding">
		<input type="text" style="width:87.5%" value="${goods.title}" name="title" id="title"  />
	</div>
</div>
<div class=" col-md-12 not-padding" style="font-size:9px;color:red">
	<div class="col-md-2 not-padding text-right">
	</div>
	<div class="col-md-10 not-padding" >
		*图片的第一张为主图，拖动图片可调整顺序。将要删除的图片拖入回收站。
	</div>
</div>
<div class="space col-md-12 not-padding" id="multi">
	
	<div class="col-md-2 not-padding text-right">
		图片：
	</div>
	<div class="col-md-6 not-padding tile__list" id="imageContainer" style="height:50px;">
		<c:if test="${not empty goods.goodsImage}">
			<c:set var="goodsImage" value="${goods.goodsImage}"/>
			<c:set var="images" value="${fn:split(goodsImage, ',')}" />
			<c:forEach items="${images}" var="image">
				<img src="http://www.ryxb.info${goodPath}${image}"  iname="${image}" width="50" height="50" />
			</c:forEach>
		</c:if>
	</div>
	<div class="col-md-1 not-padding text-right">
		回收站：
	</div>
	<div class="col-md-3 not-padding tile__list" id="deleteContainer" style="height:50px;">

	</div>

	
</div>
<div class="space col-md-12 not-padding">
	 <iframe name="uploadIFrame" id="uploadIFrame"  style="display:none"></iframe>
	 <form id="uploadForm" action="<p:basePath/>uploader/uploadFile.act"  method="post" target="uploadIFrame" enctype="multipart/form-data">  			 
		<div class="col-md-2 not-padding text-right" >
			
		</div>
		<div class="col-md-3 not-padding text-right"">
			<input type="file" id="uploadFile" name="file" value="" onchange="file_onchange()"/> 
			<input type="hidden" id="fileName" name="fileName" value="" /> 				
			<input type="hidden" id="imagePath" name="imagePath" value="goods">	
		</div>
		<div class="col-md-5 not-padding">
			<input type="submit" value="上传"  />
		</div>
	</form>
</div>

<div class="space col-md-12 not-padding">
	<div class="col-md-2 not-padding text-right">
		价格：
	</div>
	<div class="col-md-4 not-padding ">
		<input type="text" value="${goods.price}" name="price" id="price"  />元
	</div>
	<div class="col-md-2 not-padding text-right">
		最小购买单元：
	</div>
	<div class="col-md-4 not-padding">
		<input type="text" value="${goods.minLimit}" name="minLimit" id="minLimit"  />元
	</div>
</div>
<div class="space col-md-12 not-padding">
	<div class="col-md-2 not-padding text-right">
		单位：
	</div>
	<div class="col-md-4 not-padding ">
		<input type="text" value="${goods.unit}<c:if test="${empty goods.unit}">台</c:if>" name="unit" id="unit"  />
	</div>
	<div class="col-md-2 not-padding text-right">
		默认开奖时间：
	</div>
	<div class="col-md-4 not-padding">
		<input type="text" value="${goods.turnaround}<c:if test="${empty goods.turnaround}">300</c:if>" name="turnaround" id="turnaround"  />秒
	</div>
</div>
<div class="space col-md-12 not-padding">
	<div class="col-md-2 not-padding text-right">
		总期数：
	</div>
	<div class="col-md-4 not-padding ">
		<input type="text" value="${goods.totalIssue}<c:if test="${empty goods.totalIssue}">30</c:if>" name="totalIssue" id="totalIssue"  />
	</div>
	<div class="col-md-2 not-padding text-right">
		状态：
	</div>
	<div class="col-md-4 not-padding ">
		<label style="font-weight:normal;"><input type="radio" name="status" value="1" 
			<c:if test="${goods.status == '1'}">checked</c:if><c:if test="${empty goods.status}">checked</c:if>/> 上架</label>
  		<label style="font-weight:normal;"><input type="radio" name="status" value="0" 
  			<c:if test="${goods.status == '0'}">checked</c:if>/> 下架</label>
	</div>

</div>

<div class="col-md-12" style="margin-top:10px;text-align:center;">
	<button type="button" class="btn btn-success btn-sm" onclick="save();"><i class=""></i>保存</button>
	<button type="button" class="btn btn-warning btn-sm" onclick="hide();"><i class=""></i>关闭</button>
</div>
<script type="text/javascript">
$(function () {
	var iframe = document.getElementById("uploadIFrame"); 
	iframe.onload = function(){
		var responseData = this.contentDocument.body.textContent || this.contentWindow.document.body.textContent;
		var data = JSON.parse(responseData);
		src = '<img src="http://www.ryxb.info' + data.imagePath + data.imageName + '" iname="' + data.imageName + '" width="50" height="50" />';
		$("#imageContainer").append(src);
	}
	
	Sortable.create(document.getElementById('imageContainer'), {
		group: 'photo',
		animation: 150
	});

	Sortable.create(document.getElementById('deleteContainer'), {
		group: 'photo',
		animation: 150
	});

});

function file_onchange(){
	var file = $('#uploadFile').val();
	var fileName=getFileName(file);
	$('#fileName').val(fileName);
}
function getFileName(o){
    var pos=o.lastIndexOf("\\");
    return o.substring(pos+1);  
}
</script>