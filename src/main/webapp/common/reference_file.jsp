<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 图标 后期放到taglib.jsp里面 -->
<link rel="stylesheet" type="text/css" href="<p:basePath/>assets/js/easyui/css/default.css" />
<link rel="stylesheet" type="text/css" href="<p:basePath/>assets/js/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<p:basePath/>assets/js/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<p:basePath/>assets/css/admin/default.css" />
<link rel="stylesheet" type="text/css" href="<p:basePath/>assets/css/admin/template.css" />
<link rel="stylesheet" type="text/css" href="<p:basePath/>assets/css/admin/attachment.css" />


<!-- js文件 -->
<script type="text/javascript" src="<p:basePath/>assets/js/jquery-1.11.1.min.js"></script> 
<script type="text/javascript" src="<p:basePath/>assets/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<p:basePath/>assets/js/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	//得到项目的路径
	$(function() {
		window.alert = function(title, content, type, fn) {
			title  = title || "提示";
			var t  = type || "info";
			var f  = fn || function() {
			};
			$.messager.alert(title, content, t, f);
		}
		window.wait = function(content) {
		}
		window.error = function(content) {
		}
		window.confirm = function(content, okfn, nofn) {
			var ct = content || "确定要删除吗?";
			okfn = okfn  || function() {
			};
			var nofn = nofn || function() {
			};
			$.messager.confirm("提示",ct, function(f) {
				if(f){
					okfn();
				}else{
					nofn();		
				}
			});
		}
		window.tooltip = function(dom, content) {
		}
		
	});
</script>

