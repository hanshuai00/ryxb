<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>青岛如愿寻宝网络科技有限公司</title>
<style type="text/css">
.bg-f1 {
    background-color: #ffffff;
}
.header{
	height:10%;
}
.logo {
	padding:5px;
    height:100%;
    float: right;
    cursor: pointer;
}
.logo-name {
    color:#f24837;
    float: left;
    font-size: 20px;
    font-weight:600;
    font-family:'Lato', Arial, sans-serif;
    line-height:60px;
    cursor: pointer;
}

.menu li{
	height:100%;
 	font-size: 12px;
	line-height:60px;
	list-style-type:none;
	text-align:center;
	margin-left:10px;
	margin-right:10px;
	border-bottom: 5px solid #ffffff;
	cursor: pointer;
}
li.selected{
 	border-bottom: 5px solid #f24837;
}
.menu a{
	color: #000000;
	text-decoration: none;
}
.main{
	height:80%;
 	margin-top:-10px; 
}
#login-input{
	display:none;
    position: absolute;
    left:50%;
    margin-left:-400px;
    padding:20px;
    background-color: #ffffff;
    border: 0px solid #f24837;
    top:50px;
    width:800px;
    height:400px;
    filter:alpha(opacity=80);  
   	-moz-opacity:0.8;  
   	-khtml-opacity: 0.8;  
   	opacity: 0.8;   
}

#main-bg img{
    width:100%;
    height:450px;
    margin-top:10px;
    
}
.footer{
	background-color: #f24837;
 	margin-top:10px; 
	height:10%;
}
.footer p{
	padding:20 0 0 0px;
	text-align:center;
	font-size: 10px;
    font-weight:600;
    font-family:黑体;
	color: #ffffff;
}
</style>
</head>

<body class="bg-f1">
<div class="col-md-12 not-padding header">	
	<div class="col-md-2 not-padding" >
	</div>
	<div class="col-md-8 not-padding" >
		<div class="col-md-1 not-padding" ></div>
		<div class="col-md-1 not-padding" >
	        <img src="<p:basePath/>assets/images/logo1.png" class="logo">
	    </div>	
		<div class="col-md-3 not-padding logo-name" >如愿寻宝</div>
		<div class="col-md-6 not-padding menu" >
			
			<div class="col-md-3 not-padding" >
				<li class="selected" id="home"><a >首页</a></li>
			</div>
			<div class="col-md-3 not-padding" >
				<li id="aboutUs"><a >关于我们</a></li>
			</div>
			<div class="col-md-3 not-padding" >
				<li id="introduction"><a >产品介绍</a></li>
			</div>
			<div class="col-md-3 not-padding" >
				<li id="contactUs"><a >联系我们</a></li>
			</div>
		</div>
		
	
	</div>
	<div class="col-md-2 not-padding" >

	</div>
	
	
	
	
</div>
<div class="col-md-12 not-padding main">
	<div id="login-input">			
	</div>
	<div id="main-bg" >
	        <img src="<p:basePath/>assets/images/index.jpg" alt="">
	 </div>

</div>
<div class="col-md-12 not-padding footer">
	<p>
		青岛如愿寻宝网络科技有限公司<br/>
		鲁ICP备17020948号  Copyright ©2017
	</p>
</div>
</body>
<script type="text/javascript">
	$('.menu li').click(function(){
		$('.menu li').removeClass("selected")
		$(this).addClass("selected");

	})
	
	$('.logo').click(function(){
		window.location.href="login.jsp";
	})
	$('.logo-name').click(function(){
		window.location.href="login.jsp";
	})
	
	$('#home').click(function(){
		$("#login-input").css('display','none');
		
	})
	$('#aboutUs').click(function(){
		$("#login-input").html(
				"如愿寻宝是青岛如愿寻宝网络科技有限公司旗下一元夺宝、趣味限购为特色的专业B2C购物平台，力求打造一个高品质无忧保障的网上购物商城。<br/>"
				+ "是一个“我为人人、人人为我”的新型互助购物平台。平台将用户共同需求的商品分成若干等份，只需1元即可支持1等份，当1件商品的所有等份都获得支持后，根据平台互助购物协议，此商品将归其中一位支持者所有。"
		);
		$("#login-input").css('display','block');
		
	})
	$('#introduction').click(function(){
		$("#login-input").html(
				"规则：" + "<br/>" 
				+"1、依据市场价将每个商品分成若干等份，每份1元。" + "<br/>" 
				+"2、每个用户可对1件商品支持1份或多份，每支持1份获得1个云购码。" + "<br/>" 
				+"3、当1件商品的所有份额都获得支持后，计算出1个“幸运云购码”，持有“幸运云购码”的支持者即拥有该商品。" + "<br/>" 
				+"计算方式：" + "<br/>" 
				+"1、以该商品最后支持时间为截至点，取全网最后100条支持时间记录。" + "<br/>" 
				+"2、每条时间记录按时、分、秒、毫秒依次排列成一个数值。" + "<br/>" 
				+"3、将100个数值之和除以商品总需支持份数后取余，加上10000001为计算结果。" + "<br/>" 

		);
		$("#login-input").css('display','block');
		
	})
	$('#contactUs').click(function(){
		$("#login-input").html(
				"青岛如愿寻宝网络科技有限公司" + "<br/>" 
				+"热 线：0532-85631558"+ "<br/>" 
				+"邮 箱：RYXB_APP@126.com" + "<br/>" 
				+"地 址：青岛城阳区正阳路165号中联广场3楼"+ "<br/>" 
				+"邮 编：518030"
		);
		$("#login-input").css('display','block');
		
	})
	
</script>
</html>
