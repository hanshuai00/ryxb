<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>
<html > 
<head>
	<title>轮播图设置</title>
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
	left: 5%; 
	width: 800px; 
	padding: 10 0px; 
	border: 5px solid #5cb85c; 
	background-color: white;
	overflow: auto; 
	z-index:1002; 	 
} 

.green_content { 
	display:none;
	position: absolute; 
	top: 10%; 
	left: 20%; 
	width: 675px; 
	padding: 10 0px; 
	border: 5px solid #5cb85c; 
	background-color: white;
	overflow: auto; 
	z-index:1002; 	 
} 

.space {
  margin-top: 4px;
  margin-bottom: 4px;
}
.searchInput{
	box-shadow:inset 0 0 4px #eee; width:150px; margin:0; padding:5px 12px; border-radius:4px; border:1px solid silver; font-size:0.8em;
}
.searchLogo{
	cursor:pointer;
	float:left;
	widtd:20px;
	height:20px;
	transform: translate(-100%, 20%);
}
.searchLogo:hover{
	filter:alpha(opacity=50);  
   	-moz-opacity:0.5;  
   	-khtml-opacity: 0.5;  
   	opacity: 0.5;  
}
.refreshLogo{
	cursor:pointer;
	float:right;
	height:20px;
	transform: translate(-60%, 20%);
}
.refreshLogo:hover{
	filter:alpha(opacity=50);  
   	-moz-opacity:0.5;  
   	-khtml-opacity: 0.5;  
   	opacity: 0.5;  
}
</style>
<body class="bg-f1">

<div class="red_content"  id="goodsContainer">
	<%-- <%@include file="goods.jsp" %> --%>
	<%@include file="addCarouse.jsp" %>
</div>

<div class="container" id="content" style="padding:5 0px;">
	<div class="row page" id="demo" style="display:block;">

			<div class="row" id="Tbody" style="height:80%;">
				<!-- 数据值 -->
				<%@ include file="carousel.jsp"%> 
				
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
	var searchFlag = $("#searchFlag").val();
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getGoodsListLB.act",
	    data :{
	    	"searchFlag":searchFlag,
			"pageNo":pageNo,
			"pageSize":10,
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

function checkRow(value){
	
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

//添加修改商品轮播图
function modify(id){
	$.ajax({
		type:"post",
		url:"<p:basePath/>addGoodsCarouse.act",
	 	dataType: "html",
	 	data:{
			"id":id
        },
	 	async: true,
	 	beforeSend : function() {

		},
		success:function(data){
			$("#goodsContainer").html(data);
		},
		complete : function() {
			$(".red_content").css("display","block");
		}
	});
}


function save(){
	var id = $("#goodId").val();
	if($("#isTop").val()==null ||$("#isTop").val()=="" ){
		$("#message").html("设置轮播图类型不能为空!");
		return false;
	}
	var isTop = $("#isTop").val();

	var homeImage = "";
	var imgNum = $("#imageContainer").find("img").length;
 	if(imgNum == 0 ){
		$("#message").html("未上传图片");
		return false;
	}else{
		var imgArray = [];
		$("#imageContainer").find("img:visible").each(function(index, ele){
			var myEle = $(ele);
			var iname = myEle.attr("iname");
			imgArray.push(iname);
		});
		homeImage = imgArray.toString()
	} 
	
 	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>saveGoodsCarouse.act",
	    data:{
	    	"id":id,
            "isTop" : isTop,
            "homeImage":homeImage
        },
	    dataType: "json",
		success:function(data){
			console.log(data)
			$(".red_content").css("display","none");
			loadData(1);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("网络链接错误，请检查网络");
	   	}
	});
 
}


function hide(){
	$(".red_content").css("display","none");
}

$("#searchFlag").keydown(function (event) {
    if (event.which == "13") {
       query();
    }
});
function query(){
	loadData(1);
}

function refresh(){
	loadData(1);
}
</script>
</html>
