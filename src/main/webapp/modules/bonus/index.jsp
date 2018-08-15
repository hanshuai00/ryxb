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
	<title>客户管理</title>
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
    <!-- 新增、修改页面 -->
	<%@ include file="add.jsp"%> 
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
			<div class="row">
				<div class="col-md-9 col-sm-8 col-xs-8">
					<button type="button" class="btn btn-success btn-sm" onclick="demo_add();"><i class="glyphicon glyphicon-asterisk"></i>新增</button>
				</div>
				<!-- <div class="col-md-2 col-sm-4 col-xs-4" style="text-align:right;">
					<input type="text" value="" style="box-shadow:inset 0 0 4px #eee; width:120px; margin:0; padding:6px 12px; 
							border-radius:4px; border:1px solid silver; font-size:1.1em;" id="demo_q" placeholder="Search" />
				</div> -->
			</div>
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
	//默认开始查询第一页
	loadData(1);
});

//load查询数据
function loadData(pageNo){	
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>pc/getBBonusListPC.act",
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


//新增页面
function demo_add() {
	 $.ajax({
	    type: "POST",
	    url: "<p:basePath/>pc/addOrEditBBonus.act",
	    dataType: "html",
		success:function(data){
		    $("#addBody").html("");
			$("#addBody").html(data);
			$(".red_content").css("display","block");
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("网络链接错误，请检查网络");
	   	}
	});
	 
}
//修改页面
function demo_edit(objectId) {
	 $.ajax({
	    type: "POST",
	    url: "<p:basePath/>pc/addOrEditBBonus.act",
	    data : {"objectId" :objectId},
	    dataType: "html",
		success:function(data){
		    $("#addBody").html("");
			$("#addBody").html(data);
			$(".red_content").css("display","block");
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("网络链接错误，请检查网络");
	   	}
	});
	 
	 /* $.ajax({
			type: "POST",
			url: "<p:basePath/>pc/getBBonusById.act",
			dataType : "json",
			cache : false,
			async : true,
			data : {"objectId" :objectId},
			success: function(resp){
				//resp = eval('(' + resp + ')');
				if (resp.state) {
					var bonus = resp.bonus;
					
					$(".red_content").css("display","block");
				} else {
					alert(resp.msg);
				}
			}
		}); */
}
//删除
function demo_delete(objectId) {
	if(confirm("确定要删除吗?")){
   		$.ajax({
			type: "POST",
			url: "<p:basePath/>pc/deleteBBonus.act",
			dataType : "json",
			async: false,
	        cache: false,
			data : {"objectId" :objectId},
			success : function(resp) {
				//resp = eval('(' + resp + ')');
				if (resp.state) {
					alert(resp.msg);
					loadData(1);
				} else {
					alert(resp.msg);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("网络链接错误，请检查网络");
		   	}
		});
   	}
}

//新增页面——保存
function batch_save(){
	if($("#bonusName").val()==null ||$("#bonusName").val()=="" ){
		$("#message").html("红包名称不能为空!");
		return false;
	}
	if($("#bonusName").val().length>100){
    	$("#message").html("红包名称长度不能大于100!");
    	return false;
    }
	if($("#amount").val()==null ||$("#amount").val()=="" ){
		$("#message").html("红包金额不能为空!");
		return false;
	}
    if($("#amount").val()<0){
    	$("#message").html("红包金额不能小于0!");
    	return false;
    }
    if($("#amount").val().length>16){
    	$("#message").html("红包金额长度不能大于16!");
    	return false;
    }
    if($("#indate").val()==null ||$("#indate").val()=="" ){
		$("#message").html("红包有效期不能为空!");
		return false;
	}
    if($("#indate").val()<0){
    	$("#message").html("红包有效期不能小于0!");
    	return false;
    }
    if($("#indate").val().length>10){
    	$("#message").html("红包有效期长度不能大于10!");
    	return false;
    }
    if($("#bonusType").val()==null ||$("#bonusType").val()=="" ){
		$("#message").html("红包类型不能为空!");
		return false;
	}
    
	$.ajax({
        type: "POST",
        url:"<p:basePath/>pc/saveAddOrUpdateBBonus.act",
        data:$('#addOrUpdateForm').serialize(),// 你的formid
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

</script>
</html>
