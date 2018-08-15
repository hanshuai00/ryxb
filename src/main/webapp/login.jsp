<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<script type="text/javascript" src="<p:basePath/>assets/js/jquery-1.11.1.min.js"></script> 

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=8,IE=9,IE=10" /><!-- IE11下兼容模式 -->
<title>如愿寻宝后台管理系统</title>

<style>
.login-bg{
    background-color:#f1f1f1;
}

.logo-left,.logo-right{
    float: left;
    height:60px;

}

#main-bg img{
    width:100%;
    height:450px;
    margin-top:10px;
}

#login-input{
    position: absolute;
    right:10%;
    top:150px;
    width:281px;
}
.login-input-bg{
    background: url("<p:basePath/>assets/images/login_20.png") no-repeat;
    height:45px;
    margin-bottom:15px;
    position:relative;
}
.icon-user,.icon-lock{
    width:20px;
    height:20px;
    display:inline-block;
    margin:10px 17px;
}
.icon-user{
    background:url("<p:basePath/>assets/images/login_13.png") no-repeat;
}
.icon-lock{
    background:url("<p:basePath/>assets/images/login_15.png") no-repeat;
}
.login-input-input{
    position: absolute;
    top:5px;
    left:55px;
    font-family: "Microsoft YaHei";
    width:217px;
    height:30px;
    line-height:30px;
    background:transparent;
    border:none;
    outline: none;
    display:inline-block;
    color:#fff;
    font-size:16px;
}
.footer{
	margin:20px;
}
</style>
</head>
<body class="login-bg">
<script type="text/javascript">
 
function keyDownLogin(e) {
	 
    // 兼容FF和IE和Opera
    var theEvent = e || window.event;    
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;    
    if (code == 13) {
    	loginIn();
           
    }    
      
}
		//跳转后台，验证是不是管理员，如果是的话，并且帐号和密码都没问题跳转到权限页面
		function loginIn(){
			 
			if($("#txt_username").val()=="" ){
				$("#txt_username").focus();
				$("#msg").html("请输入用户名");
				$("#msg").show();
				return;
			}
			if($("txt_password").val()==""){
				$("#txt_password").focus();
				$("#msg").html("请输入密码");
				$("#msg").show();
				return;
			}
			$.ajax({
			    type: "POST",
			    url: "<p:basePath/>loginIn.act",
			    dataType : "json",
				cache : false,
				async : true,
			    data : {
				  "username" : $("#txt_username").val(),
				  "password" : $("#txt_password").val()
			    },
			    success: function(resp){
				    console.log(resp)
					if (resp.state) {
						//登录成功，跳转到主页面
						window.location.href = "<p:basePath/>" + resp.msg;
					} else {
						$("#msg").html(resp.msg);
						$("#msg").show();

					}
			    }
			});
	}
	</script>


<div class="head">	
	<div class="logo-left" >
        <img src="<p:basePath/>assets/images/logo1.png" style="height:60px;" alt="" pagespeed_url_hash="971388425">
    </div>	
	<div class="" style="color:#000;float: left;margin:5 20px;padding:0;font-size: 40px;font-family:黑体" >如愿寻宝后台管理系统</div>
</div>
<div class="main">	
    <div class="">
        <div id="login-input">
          <form action="#" method="post">
	
	          <div id="msg" style="display:none;padding-right:62px;text-align:right;color:red;"></div>
	          <div class="login-input-bg">
	              <i class="icon-user"></i>
	              <input type="text" class="login-input-input" name="username" id="txt_username" placeholder="请输入用户名">
	          </div>
	          <div class="login-input-bg">
	              <i class="icon-lock"></i>
	             <!--  <input type="password" class="login-input-input" name="password" id="txt_password" placeholder="请输入密码"> -->
	           <input type="password" id="txt_password" name="password" class="login-input-input"  placeholder="请输入密码" onkeydown="keyDownLogin(event)">
	         
	          </div>
	          <a href="#"><img src="<p:basePath/>assets/images/login_23.png" alt="" style="margin-top:10px;" onclick="loginIn()"></a>  
          </form>
        </div>
        <div id="main-bg" style="width:100%;margin-left:auto;margin-right:auto;">
	        <img src="<p:basePath/>assets/images/login_10.jpg" alt="">
	    </div>
    </div>
    
    
</div>
<div class="footer">		
	<center>鲁ICP备17020948号  Copyright ©2017</center>
</div>


</body>
</html>