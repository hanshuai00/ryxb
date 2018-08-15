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
		商品名称：
	</div>
	<div class="col-md-4 not-padding">
		<input type="text" value="${goods.goodsName}" name="goodsName" id="goodsName" readonly style="border: 0px" />
	</div>
	<div class="col-md-2 not-padding text-right">
		是否轮播图：
	</div>
	<div class="col-md-4 not-padding">
		<select name="isTop" id="isTop" style="width: 150px;height: 25px;">
			<p:dicCodeList kindCode="IS_NOT" varDictionarylist="code" >
			   <option value="${code.dicCode}" <c:if test="${goods.isTop == code.dicCode}">selected</c:if>>${code.dicValue}</option>
			</p:dicCodeList>
	   </select>
	</div>
	
</div>
<div class=" col-md-12 not-padding" style="font-size:9px;color:red">
	<div class="col-md-2 not-padding text-right">
	</div>
	<div class="col-md-10 not-padding" >
		*备注：轮播图每次只能修改上传一张。
	</div>
</div>
<div class="space col-md-12 not-padding" id="multi">
	<div class="col-md-2 not-padding text-right">
		轮播图片：
	</div>
	<div class="col-md-6 not-padding tile__list" id="imageContainer" style="height:100px;">
		<c:if test="${not empty goods.homeImage}">
			<c:set var="homeImage" value="${goods.homeImage}"/>
			<c:set var="images" value="${fn:split(homeImage, ',')}" />
			<c:forEach items="${images}" var="image">
				<img src="http://www.ryxb.info${homePath}${image}"  iname="${image}" width="400" height="100" />
			</c:forEach>
		</c:if>
	</div>

	
</div>
<div class="space col-md-12 not-padding">
	 <iframe name="uploadIFrame" id="uploadIFrame"  style="display:none"></iframe>
	 <form id="uploadForm" action="<p:basePath/>uploader/uploadFile.act"  method="post" target="uploadIFrame" enctype="multipart/form-data">  			 
		<div class="col-md-1 not-padding text-right" >
			
		</div>
		<div class="col-md-4 not-padding text-right"">
			<input type="file" id="uploadFile" name="file" value="" onchange="file_onchange()"/> 
			<input type="hidden" id="fileName" name="fileName" value="" /> 				
			<input type="hidden" id="imagePath" name="imagePath" value="home">	
		</div>
		<div class="col-md-2 not-padding">
			<input type="submit" value="上传"  />
		</div>
	</form>
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
		src = '<img src="http://www.ryxb.info' + data.imagePath + data.imageName + '" iname="' + data.imageName + '" width="100" height="100" />';
		$("#imageContainer").html(src);
	}
	
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