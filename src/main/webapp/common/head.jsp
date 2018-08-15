﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<div id="header">
		<ul>
		<li>
			<div class="logo-left" >
	            <img src="${pageContext.request.contextPath}/images/logo.png" style="height:38px;" alt="" pagespeed_url_hash="971388425">
	        </div>
        </li>
        <li>
	        <div class="logo-line">
	            <img src="${pageContext.request.contextPath}/images/line.png"  style="height:40px;mag" alt="" pagespeed_url_hash="1153638848">
	        </div>
    	</li>
        <li>
			<div class="logo-right" style="color:#000;margin:10px 0px 0px 0px;font-size: 25px;font-family:黑体" >如愿寻宝后台管理系统</div>
		</li>
        <li>
        	
        </li>
		</ul>
		
		<div style="height:40;line-height:40px;float:right;margin:0 40px;">

       		<a href="${pageContext.request.contextPath}/j_spring_security_logout">退出</a>
       	</div>

       	<div style="height:40;line-height:40px;float:right;margin:5px;">

			<a href="javascript:history.go(1);"><span><img style="height:30px;" src="${pageContext.request.contextPath}/images/forward.png"onmouseout="this.src='${pageContext.request.contextPath}/images/forward.png'" onmousemove="this.src='${pageContext.request.contextPath}/images/forward_.png'"/></span></a>
			
       	</div>
       	<div style="height:40;line-height:40px;float:right;margin:5px;">

			<a href="javascript:history.go(-1);"><span><img style="height:30px;" src="${pageContext.request.contextPath}/images/back.png" onmouseout="this.src='${pageContext.request.contextPath}/images/back.png'" onmousemove="this.src='${pageContext.request.contextPath}/images/back_.png'"/></span></a>

       	</div>
	</div><!-- end #header-wrap -->