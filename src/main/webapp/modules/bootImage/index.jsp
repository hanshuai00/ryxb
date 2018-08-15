<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head> 
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>系统引导页管理</title>
	<script type="text/javascript" src="<p:basePath/>assets/js/Sortable.js"></script> 
</head>
<style type="text/css">
.bg-f1 {
    background-color: #f1f1f1;
}
.not-padding {
    padding: 0;
}
.table {
    width: 100%;
    max-width: 100%;
    margin-bottom: 20px;
}
.table.table-hdbg {
    background-color: #f6f6f6;
    color: #666666 !important;
    text-align: center;
    font-size: 12px;
}
.table.table-hdbg thead td, .table.table-hdbg thead th {
    color: #FFFFFF;
    font-size: 12px;
    background-color: #65b5d3;
}
.table.table-hdbg tbody tr:nth-child(odd) td:nth-child(even), 
.table.table-hdbg tbody tr:nth-child(odd) th:nth-child(even) {
    background-color: #ebebeb;
}
.table.table-hdbg tbody tr:nth-child(odd) td:nth-child(odd), 
.table.table-hdbg tbody tr:nth-child(odd) th:nth-child(odd) {
    background-color: #e3e3e3;
}
.table.table-hdbg tbody tr:nth-child(even) td:nth-child(even), 
.table.table-hdbg tbody tr:nth-child(even) th:nth-child(even) {
    background-color: #FFFFFF;
}
.td-first-left tbody td:first-child {
    text-align: center;
}

.flist {
    height: 34px;
    line-height: 34px;
}

em{
	margin:10px;
	padding: 5 10px;
	font-style: normal;

	height: 34px;
    line-height: 34px;
	
	background-color: #e3e3e3;
    border-radius: 6px;
    color: #FFFFFF;
    word-break:keep-all;           /* 不换行 */
	white-space:nowrap;          /* 不换行 */
}

em.selected{
    background-color: #5bc0de;
    border-radius: 6px;
    color: #FFFFFF;
}


/* ↓[窗口-window] */
.red_content { 
	display:none;
	position: absolute; 
	top: 15%; 
	left: 20%; 
	width: 675px; 
	padding: 10 0px; 
	border: 5px solid #5cb85c; 
	background-color: white;
	overflow: auto; 
	z-index:1002; 	 
} 

.green_content { 
	display:none;
	position: absolute; 
	top: 15%; 
	left: 20%; 
	width: 675px; 
	padding: 10 0px; 
	border: 5px solid #5cb85c; 
	background-color: white;
	overflow: auto; 
	z-index:1002; 	 
} 
</style>
<body class="bg-f1">

<div class="red_content" id="addBody">
	<div class="space col-md-12 not-padding" style="color:red">
		<div class="col-md-1 not-padding text-right">
		</div>
		<div class="col-md-6 not-padding text-left">
			备注：引导图片每次只能修改上传一张
		</div>
		<input type="hidden" value="" name="id" id="dicId" />
	</div>
		
	<div class="space col-md-12 not-padding">
		<div class="col-md-2 not-padding text-right">
			图片：
		</div>
		<div class="col-md-10 not-padding" id="imageContainer">
			<!-- <img src="http://www.ryxb.info/uploader/goods/G20170425062137822.jpg" iname="G20170425062137822.jpg" width="50" height="50" draggable="false" > -->
		</div>
	</div>
	<div class="space col-md-12 not-padding">
		<div class="col-md-12 not-padding text-right">
			 <iframe name="uploadIFrame" id="uploadIFrame"  style="display:none"></iframe>
			 <form id="uploadForm" action="<p:basePath/>uploader/uploadFile.act"  method="post" target="uploadIFrame" enctype="multipart/form-data">  			 
				<div class="col-md-2 not-padding text-right" >
					
				</div>
				<div class="col-md-3 not-padding text-right"">
					<input type="file" id="uploadFile" name="file" value="" onchange="file_onchange()"/> 
					<input type="hidden" id="fileName" name="fileName" value="" /> 				
					<input type="hidden" id="imagePath" name="imagePath" value="boot">	
				</div>
				<div class="col-md-5 not-padding">
					<input type="submit" value="上传"  />
				</div>
			</form>
		</div>
	</div>
	
	<div class="col-md-12" style="margin-top:10px;text-align:center;">
		<button type="button" class="btn btn-success btn-sm" onclick="batch_save();"><i class=""></i>确定</button>
		<button type="button" class="btn btn-success btn-sm" onclick="batch_cancel();"><i class=""></i>取消</button>
	</div>
</div>

<div class="green_content"  >
	<div class="col-md-12 col-sm-4 col-xs-4">
		<textarea rows="10" cols="84" id="dirList" name="dirList" onchange="dirInput()" 
			placeholder="请粘贴目录，支持从excel拷贝，目录之间用换号符或逗号分割，会自动过滤空行"></textarea>
	</div>
	<div class="col-md-12 col-sm-4 col-xs-4" style="margin-top:10px;text-align:center;">
		<button type="button" class="btn btn-success btn-sm" onclick="batch_mkdir();"><i class=""></i>确定</button>
	</div>

</div>

<div class="container" id="content" style="padding:10 0px;">
	<div class="row page" id="demo" style="display:block;">
			<div class="row"  id="Tbody" >
				<!--列表项-->
				<!-- 数据值 -->
				<%@ include file="data.jsp"%> 
			    <!--列表项-->	
		</div>

	</div>
</div>

</body>
<script type="text/javascript">
$(function () {
	var iframe = document.getElementById("uploadIFrame"); 
	iframe.onload = function(){
		var responseData = this.contentDocument.body.textContent || this.contentWindow.document.body.textContent;
		var data = JSON.parse(responseData);
		src = '<img src="http://www.ryxb.info' + data.imagePath + data.imageName + '" iname="' + data.imageName + '" width="100" height="100" />';
		$("#imageContainer").append(src);
	}
	
	Sortable.create(document.getElementById('imageContainer'), {
		group: 'photo',
		animation: 150
	});

	
	//默认开始查询第一页
	loadData(1);
});

//load查询数据
function loadData(pageNo){	
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>pc/getBootImageDicList.act",
	    data : "pageNo="+pageNo,
	    dataType: "html",
		success:function(data){
		    $("#Tbody").html("");
			$("#Tbody").html(data);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("网络链接错误，请检查网络");
	   	}
	});
}

//分页
function _serPagination(page){
	loadData(page);
}

//上一页
function _serPrevious(){
	var np,page  ;
	 np=$("#pagination").find(".current").html();
	 page=parseInt(np)-1;
	 loadData(page); 
}
//下一页
function _serNext(){
	 var np,page  ;
	 np=$("#pagination").find(".current").html();
	 page=parseInt(np)+1;
	 loadData(page); 
}
//跳到第一页
function _serFirst(){
	loadData(1);
}
//跳到最后一页
function _serLast(){
	var lp=$("#pagination").find(".totalpage").html();
	loadData(lp);
}

//修改页面
function demo_edit(objectId) {
	$(".red_content").css("display","block");
	$("#dicId").val(objectId);
}


//新增页面——保存
function batch_save(){
	var dicId =$("#dicId").val();
	var bootImage = "";
	var imgNum = $("#imageContainer").find("img").length;
 	if(imgNum == 0 ){
		$("#message").html("未上传图片");
		return false;
	}else{
		var imgArray = [];
		$("#imageContainer").find("img").each(function(index, ele){
			var myEle = $(ele);
			var iname = myEle.attr("iname");
			imgArray.push(iname);
		});
		bootImage = imgArray.toString()
	}
	$.ajax({
        type: "POST",
        url:"<p:basePath/>pc/saveBootImageDic.act",
        data:{"dicId":dicId,"bootImage":bootImage},
        async: false,
        cache: false,
        success : function(resp) {
			resp = eval('(' + resp + ')');
			if (resp.state) {
				alert(resp.msg);
				loadData(1);
				$(".red_content").css("display","none");
			} else {
				alert(resp.msg);
			}
		},
        error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("网络链接错误，请检查网络");
	   	}
    });
	
}
//新增页面——取消
function batch_cancel(){
	$(".red_content").css("display","none");
}

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
</html>
