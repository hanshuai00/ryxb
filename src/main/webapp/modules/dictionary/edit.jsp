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
	<title>添加数据字典</title>
	<link href="<p:basePath/>assets/css/reset.css" rel="stylesheet" type="text/css"/>
	<link href="<p:basePath/>assets/css/list.css" rel="stylesheet" type="text/css"/>
<script>
	var path = "<p:basePath/>";
	$(document).ready(function() {
		var closeflg = '${closeflg}';
		if(closeflg != null && closeflg !='' && closeflg == 'true'){
			alert("父类型编号已存在，保存失败!");//父类编号已存在
		}
		
	});
	
	function save() {
		//正则表达式 ：数字验证
		var reg = /^\d+$/;
		if (document.getElementById("parentDicCode").value == "") {
			alert("父类型编号不能为空");
			return false;
		}
		if (document.getElementById("parentDicValue").value == "") {
			alert("父类型名称不能为空");
			return false;
		}
		/* if (document.getElementById("dicCode").value == "") {
			alert("名称编号不能为空");
			return false;
		}
		if (document.getElementById("dicValue").value == "") {
			alert("名称不能为空");
			return false;
		} 
		if (!document.getElementById("orderIndex").value.match(reg)) {
			alert("排序中请输入数字");
			return false;
		}*/

		var codevalues = document.getElementsByName("dicCode");
		for ( var i = 0; i < codevalues.length; i++) {
			for ( var j = i + 1; j < codevalues.length; j++) {
				if (codevalues[i].value == codevalues[j].value) {
					alert("名称编号重复");
					return false;
				}
			}
		}

		document.getElementById("Form1").submit();
	}

	//插入行
	function insertRows() {
		var tempRow = 0;
		var tbl = document.getElementById("dictTbl");
		tempRow = tbl.rows.length;
		var Rows = tbl.rows;//类似数组的Rows 
		var newRow = tbl.insertRow(tbl.rows.length);//插入新的一行 
		var Cells = newRow.cells;//类似数组的Cells 
		for ( var i = 0; i < 4; i++) {//每行4列数据 
			var newCell = Rows[newRow.rowIndex].insertCell(Cells.length);
			newCell.align = "center";
			switch (i) {
			case 0:
				newCell.innerHTML = "<input name=\"dicValue\" type=\"text\" id=\""+tempRow+"\" size=\"30\" maxlength=25 class=\"input_fix\">";
				break;
			case 1:
				newCell.innerHTML = "<input name=\"dicCode\" type=\"text\" id=\""+tempRow+"\" size=\"10\" maxlength=25 class=\"input_fix\">";
				break;
			case 2:
				newCell.innerHTML = "<input name=\"orderIndex\" type=\"text\" id=\""+tempRow+"\" size=\"10\" maxlength=25 class=\"input_fix\">";
				break;
			case 3:
				newCell.innerHTML = "<a  onclick='delTableRow(this)' title=\"删除\"><i class=\"glyphicon glyphicon-remove\"></i></a>";
				break;
			}
		}
	}

	//删除行
	function delTableRow(obj) {
		obj = $(obj).parent().parent().detach();
	}
	//取消操作
	function cancel() {
		window.location.href = "<p:basePath/>pc/zdgl.act";
	}
</script>
</head>
<body>
	<!-- <div class="panel_tit_blue">
		<h2>
			<i class="glyphicon glyphicon-list"></i>数据字典
		</h2>
	</div> -->
	<div class="panel_content_blue">
		<form name="Form1" id="Form1" method="post" action="<p:basePath/>pc/saveUpdateDictonary.act">
			<table style="width: 100%; border: 0" class="table_input1">
				<tbody>
					<tr>
						<th width="25%" align="center">父类型编号：</th>
						<td width="75%" align="left">
						<input type="hidden" id="parentID" name="parentID"value="${parent.id}" />
						<c:if test="${newflag =='true'}">
							<input type="text" id="parentDicCode" name="parentDicCode"
								value="" size="10" class="input_fix"
								maxlength="20" />
						</c:if>
						<c:if test="${newflag =='false'}">
							<input type="text" readonly="readonly" id="parentDicCode" name="parentDicCode"
								value="${parent.dicCode}" size="10" class="input_fix"
								maxlength="20" />
						</c:if>
						</td>
					</tr>
					<tr>
						<th>父类型名称：</th>
						<td><input type="text" id="parentDicValue" name="parentDicValue"
							size="10" value="${parent.dicValue}" class="input_fix"
							maxlength="20" />
						</td>
					</tr>
					<tr>
						<td colspan=2 align="right">
							<button type="button" name="saveitem" onclick="insertRows()" class="toolbar-border16c">
								<i class="icon-ok add16c"></i>添加选项
							</button>
						</td>
					</tr>
				</tbody>
			</table>
			<div class=" panel_content_blue">
				<table style="width: 100%; border: 0" class="list_table" id="dictTbl">
					<thead>
						<tr>
							<td width="26%">类型名称</td>
							<td width="15%">类型编号值</td>
							<td width="16%">排序</td>
							<td width="7%">删除</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="code" items="${codelist}">
						<tr>
							<td>
								<input id="dicValue" name="dicValue" value="${code.dicValue}" type="text" size="30" maxlength="25" class="input_fix">
							</td>
							<td>
								<input id="dicCode" name="dicCode" value="${code.dicCode}" type="text" size="10" maxlength="25" class="input_fix">
							</td>
							<td>
								<input id="orderIndex" name="orderIndex" value="${code.orderIndex}" type="text" size="10" maxlength="25" class="input_fix">
							</td>
							<td>
								<a  onclick="delTableRow('${code.id}')" title="删除"><i class="glyphicon glyphicon-remove"></i></a>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<table style="width: 100%; border: 0">
				<tfoot>
					<tr>
						<td colspan="3" class="text-center">
							<button type="button" class="btn_1 color-green1" onclick="save()">
								<i class="glyphicon glyphicon-save"></i>保存
							</button>
							<button type="button" class="btn_1" onclick="cancel()">
								<i class="glyphicon glyphicon-remove"></i>取消
							</button>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
	</div>
</body>
</html>


