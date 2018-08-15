<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<html > 
<head>
	<title>jsTree</title>
	<link rel="stylesheet" href="<p:basePath/>assets/css/bootstrap/bootstrap.min.css" />
	<link rel="stylesheet" href="<p:basePath/>assets/js/jstree/dist/themes/default/style.min.css" />
	<script type="text/javascript" src="<p:basePath/>assets/js/jquery-1.11.1.min.js"></script> 
	<script src="<p:basePath/>assets/js/jstree/dist/jstree.min.js"></script>
</head>
<style type="text/css">
.bg-f1 {
    background-color: #f1f1f1;
}
.not-padding {
    padding: 0;
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
    text-align: left;
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

<div class="red_content"  >

	<div class="col-md-12 col-sm-4 col-xs-4">
		<textarea rows="10" cols="84" id="nodeList" name="nodeList" onchange="nodeInput()" 
			placeholder="请粘贴节点，支持从excel拷贝，节点之间用换号符或逗号分割，会自动过滤空行"></textarea>
	</div>
	<div class="col-md-12 col-sm-4 col-xs-4" style="margin-top:10px;text-align:center;">
		<button type="button" class="btn btn-success btn-sm" onclick="batch_mknode();"><i class=""></i>确定</button>
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
			<div class="col-md-12">
<!-- 				<h3>结构树编辑</h3> -->
				<div class="row">
					<div class="col-md-9 col-sm-8 col-xs-8">
						<button type="button" class="btn btn-success btn-sm" onclick="demo_mkdir();"><i class="glyphicon glyphicon-asterisk"></i>新建目录</button>
						<button type="button" class="btn btn-success btn-sm" onclick="demo_create();"><i class="glyphicon glyphicon-asterisk"></i>新建节点</button>
						<button type="button" class="btn btn-success btn-sm" onclick="show_green();"><i class="glyphicon glyphicon-asterisk"></i>批量增加目录</button>
						<button type="button" class="btn btn-success btn-sm" onclick="show_red();"><i class="glyphicon glyphicon-asterisk"></i>批量增加节点</button>
						<button type="button" class="btn btn-warning btn-sm" onclick="demo_rename();"><i class="glyphicon glyphicon-pencil"></i>重命名</button>
						<button type="button" class="btn btn-warning btn-sm" onclick="demo_todir();"><i class="glyphicon glyphicon-pencil"></i>节点变目录</button>
						<button type="button" class="btn btn-warning btn-sm" onclick="demo_tonode();"><i class="glyphicon glyphicon-pencil"></i>目录变节点</button>
						<button type="button" class="btn btn-danger btn-sm" onclick="demo_delete();"><i class="glyphicon glyphicon-remove"></i>删除</button>
					</div>
					<div class="col-md-2 col-sm-4 col-xs-4" style="text-align:right;">
						<input type="text" value="" style="box-shadow:inset 0 0 4px #eee; width:120px; margin:0; padding:6px 12px; border-radius:4px; border:1px solid silver; font-size:1.1em;" id="demo_q" placeholder="Search" />
					</div>
					
				</div>
				<div class="row" >
					<div class="col-md-3 ">
						<div id="jstree_demo" class="demo" style="margin-top:1em; min-height:200px;"></div>
						
					</div>
					<div class="col-md-9 ">
						<div id="attribute" class="" style="margin-top:1em; min-height:200px;">
						
						  <table id="data" class="table table-hdbg td-first-left">
						   <thead>
						    <tr>
						     <td style="width:100px;">车型</td>
						     <td>1车<input name="theadChk" type="checkbox" id="D01" col="1" onclick="checkCol()" /></td>
						     <td>2车<input name="theadChk" type="checkbox" id="D02" col="2" onclick="checkCol()" /></td>
						     <td>3车<input name="theadChk" type="checkbox" id="D03" col="3" onclick="checkCol()" /></td>
						     <td>4车<input name="theadChk" type="checkbox" id="D04" col="4" onclick="checkCol()" /></td>
						     <td>5车<input name="theadChk" type="checkbox" id="D05" col="5" onclick="checkCol()" /></td>
						     <td>6车<input name="theadChk" type="checkbox" id="D06" col="6" onclick="checkCol()" /></td>
						     <td>7车<input name="theadChk" type="checkbox" id="D07" col="7" onclick="checkCol()" /></td>
						     <td>8车<input name="theadChk" type="checkbox" id="D08" col="8" onclick="checkCol()" /></td>
						     <td>9车<input name="theadChk" type="checkbox" id="D09" col="9" onclick="checkCol()" /></td>
						     <td>10车<input name="theadChk" type="checkbox" id="D10" col="10" onclick="checkCol()" /></td>
						     <td>11车<input name="theadChk" type="checkbox" id="D11" col="11" onclick="checkCol()" /></td>
						     <td>12车<input name="theadChk" type="checkbox" id="D12" col="12" onclick="checkCol()" /></td>
						     <td>13车<input name="theadChk" type="checkbox" id="D13" col="13" onclick="checkCol()" /></td>
						     <td>14车<input name="theadChk" type="checkbox" id="D14" col="14" onclick="checkCol()" /></td>
						     <td>15车<input name="theadChk" type="checkbox" id="D15" col="15" onclick="checkCol()" /></td>
						     <td>16车<input name="theadChk" type="checkbox" id="D16" col="16" onclick="checkCol()" /></td>
						    </tr>
						   </thead>
						   <tbody>
						    <tr>
						     <td>380A<input name="rowChk" type="checkbox" row="1" onclick="checkRow()" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D01" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D02" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D03" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D04" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D05" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D06" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D07" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D08" paramtypecode="A10,A12,A14,A16" /></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
						    </tr>
						    <tr>
						     <td>CRH2A<input name="rowChk" type="checkbox" row="2" onclick="checkRow()" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D01" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D02" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D03" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D04" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D05" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D06" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D07" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D08" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D09" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D10" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D11" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D12" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D13" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D14" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D15" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D16" paramtypecode="A11,A13,A15,A17" /></td>
						    </tr>
						     <tr>
						     <td>380A统<input name="rowChk" type="checkbox" row="3" onclick="checkRow()" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D01" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D02" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D03" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D04" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D05" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D06" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D07" paramtypecode="A10,A12,A14,A16" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="1位侧轴端" coachno="D08" paramtypecode="A10,A12,A14,A16" /></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></td>
						    </tr>
						    <tr>
						     <td>CRH2A统<input name="rowChk" type="checkbox" row="4" onclick="checkRow()" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D01" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D02" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D03" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D04" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D05" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D06" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D07" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D08" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D09" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D10" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D11" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D12" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D13" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D14" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D15" paramtypecode="A11,A13,A15,A17" /></td>
						     <td><input name="dataItemChk" type="checkbox" charttitle="2位侧轴端" coachno="D16" paramtypecode="A11,A13,A15,A17" /></td>
						    </tr>
						    
						    
						   
						   </tbody>
						  </table>
						  
						  <div class="flist" >
						  
						  	<div class="col-md-1 not-padding" style="text-align:right">系统：</div>
							<div class="col-md-11" >
						
								<dd >	
									<em class="selected">车体</em>
									<em>车端连接</em>
									<em>转向架及其辅助</em>
									<em>主供电</em>
									<em>牵引</em>
									<em>辅助电气</em>
									<em>供风制动</em>
									<em>网络及辅助监控</em>
									<em>旅客信息</em>
									<em>空调</em>
									<em>给排水卫生</em>
									<em>外门及车内设施</em>
									<em>驾驶设施</em>
									<em>电务车载</em>
									<em>其它</em>
								</dd>
		
							</div>
						 </div>	
						 <div class="flist" >
							<div class="col-md-1 not-padding" style="margin:0px;text-align:right">子系统：</div>
							<div class="col-md-11">
						
								<dd >	
									<em class="selected">车体骨架</em>
									<em>设备舱</em>
									<em>车体其它功能结构</em>
									<em>车窗</em>
								</dd>
		
							</div>
						 </div>	
						 <div class="flist" >	
							<div class="col-md-1 not-padding" style="margin:0px;text-align:right">功能组：</div>
							<div class="col-md-11" >
								<dd >
									<em class="selected">司机室框架及前端吸能装置</em>
									<em>车顶组成</em>
									<em>侧墙组成</em>
									<em>端墙组成</em>
									<em>车体隔断结构及其它</em>
									<em>底架组成</em>
								</dd>
							</div>
						 </div>	
						 <div class="flist" >	
							<div class="col-md-1 not-padding" style="margin:0px;text-align:right">组件分解：</div>
							<div class="col-md-11" >
						
								<dd >
									<em class="selected">司机室骨架</em>
									<em>司机室外壳</em>
									<em>前鼻外壳</em>
									<em>车体前端吸能结构</em>
									<em>防爬组件</em>
									<em>司机室框架组件</em>
								</dd>	
							</div>
						 </div>	

						 <div class="flist" >	
							
							<div class="col-md-1 not-padding" style="margin:0px;text-align:right">运维类型：</div>
							<div class="col-md-11" >
						
								<dd>	
									<em class="selected">高价互换件</em>
									<em>易损易耗件</em>
									<em>随车备品</em>
									<em>维修辅料</em>
									<em>普通件</em>
								</dd>		
							</div>
						 </div>	
						 <div class="flist" >	
							
							<div class="col-md-1 not-padding" style="margin:0px;text-align:right">保障级别：</div>
							<div class="col-md-11" >
						
								<dd>	
									<em class="selected">行车安全</em>
									<em>行车秩序</em>
									<em>旅客舒适</em>
									<em>运用检修</em>
									<em>其他</em>
								</dd>		
							</div>
						 </div>	
						 <div class="flist" >	
							<div class="col-md-1 not-padding" style="margin:0px;text-align:right">材质类型：</div>
							<div class="col-md-11" >
						
								<dd>	
									<em class="selected">电气类</em>
									<em>橡胶类</em>
									<em>易碎类</em>
									<em>化工类</em>
									<em>其他</em>
								</dd>		
							</div>
						 </div>	

						</div>
					</div>
					
				</div>
				
			
			</div>

		</div>
	</div>

</body>
<script>
var currentId = undefined;
var currentNode;

$('em').click(function(){
	
	$(this).addClass("selected").siblings().removeClass("selected");


})

$(function () {
	var to = false;
	$('#demo_q').keyup(function () {
		if(to) { clearTimeout(to); }
		to = setTimeout(function () {
			var v = $('#demo_q').val();
			$('#jstree_demo').jstree(true).search(v);
		}, 250);
	});
	
	$.ajax({
	    type: "GET",
	    url: "<p:basePath/>getTree.act",
	    dataType : "json",
	    success: function(resp){
		    $('#jstree_demo').jstree({
				"core" : {
					"animation" : 0,
					"check_callback" : true,
					'force_text' : true,
					"themes" : { "stripes" : true },
					'data' : resp
				},
				"types" : {
					"#" : { "max_children" : 1, "max_depth" : 20 },
					"root" : { "icon" : "<p:basePath/>assets/js/jstree/images/tree_icon.png", "valid_children" : ["default","file"] },
					"default" : { "valid_children" : ["root","default","file"] },
					"file" : { "icon" : "glyphicon glyphicon-file", "valid_children" : [] }
				},
				"plugins" : [  "contextmenu",  "dnd", "search", "state", "types", "wholerow" ],
				
			});
			
	    }
	});

});
function demo_todir(){
	if(currentId==undefined){
		return false;
	}
	if(currentNode.type!="file"){
		return false;
	}
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>modifyNode.act",
	    dataType : "json",
		cache : false,
		async : true,
	    data : {
		  "id" : currentId,
		  "type" : "default"
	    },
	    success: function(resp){
	    	location.reload();
			if(resp == 0){
				alert("转变失败！");
			}
	    }
    });    	
}
function demo_tonode(){
	if(currentId==undefined){
		return false;
	}
	if(currentNode.type!="default"){
		return false;
	}
	if(currentNode.children_d.length>0){
		return false;
	}
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>modifyNode.act",
	    dataType : "json",
		cache : false,
		async : true,
	    data : {
		  "id" : currentId,
		  "type" : "file"
	    },
	    success: function(resp){
	    	location.reload();
			if(resp == 0){
				alert("转变失败！");
			}
	    }
    });    	
}
function demo_mkdir() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	sel = ref.create_node(sel, {"type":"default"});
	if(sel) {
		ref.edit(sel);
	}
};
function demo_create() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	sel = ref.create_node(sel, {"type":"file"});
	if(sel) {
		ref.edit(sel);
	}
};
function demo_rename() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	ref.edit(sel);
};
function demo_delete() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	ref.delete_node(sel);
};


$('#jstree_demo').on('changed.jstree', function (e, data) {  
	currentId = data.selected[0];
	currentNode = data.node;
});  

$('#jstree_demo').on('create_node.jstree', function(e, data) {	
     $.ajax({
	    type: "POST",
	    url: "<p:basePath/>createNode.act",
	    dataType : "json",
		cache : false,
		async : true,
	    data : {
		  "id" : data.node.id,
		  "parent" : data.node.parent,
		  "text" : data.node.text,
		  "type" : data.node.type,
		  "position" : data.position
	    },
	    success: function(resp){
	    	//location.reload();
			if(resp == 0){
				alert("创建失败！");
			}
	    }
    });    
});

$('#jstree_demo').on('rename_node.jstree', function(e, data) {
    $.ajax({
	    type: "POST",
	    url: "<p:basePath/>modifyNode.act",
	    dataType : "json",
		cache : false,
		async : true,
	    data : {
		  "id" : data.node.id,
		  "text" : data.node.text
	    },
	    success: function(resp){
	    	//location.reload();
			if(resp == 0){
				alert("重命名失败！");
			}
	    }
    });    
});


$('#jstree_demo').on('move_node.jstree',function(e,data){ 
    $.ajax({
	    type: "POST",
	    url: "<p:basePath/>modifyNode.act",
	    dataType : "json",
		cache : false,
		async : true,
	    data : {
	    	"id" : data.node.id,
			"parent" : data.node.parent,
			"moveType" : data.node.type,
			"position" : data.position
	    },
	    success: function(resp){
	    	location.reload();
			if(resp == 0){
				alert("重命名失败！");
			}
	    }
    });     

}); 


$('#jstree_demo').on('delete_node.jstree', function(e, data) { 
    $.ajax({
	    type: "POST",
	    url: "<p:basePath/>deleteNode.act",
	    dataType : "json",
		cache : false,
		async : true,
	    data : {
		  "id" : data.node.id
	    },
	    success: function(resp){	    	
			if(resp == 0){
				alert("删除失败！");
			}
	    }
    });     
    for(var i=0;i<data.node.children_d.length;i++){   	
    	var node = data.node.children_d[i];
    	$.ajax({
    	    type: "POST",
    	    url: "<p:basePath/>deleteNode.act",
    	    dataType : "json",
    		cache : false,
    		async : true,
    	    data : {
    		  "id" : node
    	    },
    	    success: function(resp){	    	
    			if(resp == 0){
    				alert("删除失败！");
    			}
    	    }
        });   
    } 
    location.reload();
});

$('#jstree_demo').on('copy_node.jstree', function(e, data) { 
 	 $.ajax({
	    type: "POST",
	    url: "<p:basePath/>createNode.act",
	    dataType : "json",
		cache : false,
		async : true,
	    data : {
		  "id" : data.node.id,
		  "parent" : data.node.parent,
		  "text" : data.node.text,
		  "type" : data.node.type,
		  "position" : data.position
	    },
	    success: function(resp){
			if(resp == 0){
				alert("创建失败！");
			}
	    }
    });     
	
	for(var i=0;i<data.node.children_d.length;i++){
		var node = data.node.children_d[i];
		var children = data.instance._model.data[node];		
		$.ajax({
		    type: "POST",
		    url: "<p:basePath/>createNode.act",
		    dataType : "json",
			cache : false,
			async : true,
		    data : {
			  "id" : children.id,
			  "parent" : children.parent,
			  "text" : children.text,
			  "type" : children.type,
			  "position" : i
		    },
		    success: function(resp){
				if(resp == 0){
					alert("创建失败！");
				}
		    }
	    });    
	}
});


//规范录入
function nodeInput(){
	var cardstr=$("#nodeList").val();
	var str1= cardstr.replace(/\n/g,",");
	var str2= str1.replace(/\s+/g,",");
	var list = str2.split(",");
	var rest = "";
	for (var i = 0; i < list.length; i++) {
		if(list[i] != ""){
			rest+="," + list[i];
		}
	}
	rest=rest.substring(1);
	$("#nodeList").val(rest);	
}
function dirInput(){
	var cardstr=$("#dirList").val();
	var str1= cardstr.replace(/\n/g,",");
	var str2= str1.replace(/\s+/g,",");
	var list = str2.split(",");
	var rest = "";
	for (var i = 0; i < list.length; i++) {
		if(list[i] != ""){
			rest+="," + list[i];
		}
	}
	rest=rest.substring(1);
	$("#dirList").val(rest);	
}

  
function show_red(){
	$(".red_content").css("display", "block");
}
function show_green(){
	$(".green_content").css("display", "block");
}
function batch_mknode(){
	$(".red_content").css("display", "none");
	var ref = $('#jstree_demo').jstree(true),
	sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	var cardstr=$("#nodeList").val();
	var list = cardstr.split(",");
	for (var i = 0; i < list.length; i++) {
		ref.create_node(sel, {"text":list[i],"type":"file"});
	}	
	$("#nodeList").val("");
	$('#jstree_demo').jstree("toggle_node",sel);
}
function batch_mkdir(){
	$(".green_content").css("display", "none");
	var ref = $('#jstree_demo').jstree(true),
	sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	var cardstr=$("#dirList").val();
	var list = cardstr.split(",");
	for (var i = 0; i < list.length; i++) {
		ref.create_node(sel, {"text":list[i],"type":"default"});
	}	
	$("#dirList").val("");
	$('#jstree_demo').jstree("toggle_node",sel);
}

//-------------------------------------------------------
function checkCol(){
	$("input[name='dataItemChk']:checkbox:checked").prop("checked",false);
	$("input[name='rowChk']:checkbox:checked").prop("checked",false);
	$("input[name='theadChk']:checkbox:checked").each(function(index, ele){
		var myEle = $(ele);
		var col = myEle.attr("col");
		$("#data").find("tr").each(function() {
			$(this).children("td:eq(" + col + ")").find("input").prop("checked",true);
		})
	});
}

function checkRow(){
	$("input[name='dataItemChk']:checkbox:checked").prop("checked",false);
	$("input[name='theadChk']:checkbox:checked").prop("checked",false);
	$("input[name='rowChk']:checkbox:checked").each(function(index, ele){
		var myEle = $(ele);
		var row = myEle.attr("row");
		$("#data").find("tr:eq(" + row + ")").find("input").prop("checked",true);
	});
}
</script>
</html>
