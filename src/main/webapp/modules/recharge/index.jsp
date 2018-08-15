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
	<title>充值记录查询</title>
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
			<form id="searchForm" method="post" >
				<div class="col-md-4 col-sm-8 col-xs-8" >
					会员账号：<input type="text" id="s_userName" class="searchInput" placeholder="请输入用户名" />
				</div>
				<div class="col-md-3 col-sm-6 col-xs-6" >
					充值方式：
						<select id="s_payType" style="width:50%">
							<option value="">- - - 请选择 - - -</option>
							<p:dicCodeList kindCode="PAY_TYPE" varDictionarylist="code" >
							   <option value="${code.dicCode}" >${code.dicValue}</option>
							</p:dicCodeList>
						</select>
				</div>
				
				<div class="col-md-2 " >
					<button type="button" class="btn btn-warning btn-sm" onclick="query();"><i class="glyphicon"></i>查询</button>
					<button type="button" class="btn btn-warning btn-sm" onclick="reset();"><i class="glyphicon"></i>清空</button>
				</div>
			</form>
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
	    url: "<p:basePath/>pc/getRechargeListPC.act",
	    data:{"pageNo":pageNo,
	    	"payType":$("#s_payType").val(),
			"userName":$("#s_userName").val()
        },
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
function query(){
	loadData(1);
}
function reset(){
	$("#searchForm")[0].reset();;
}

</script>
</html>
