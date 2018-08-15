<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>如愿寻宝后台管理系统</title>
<style type="text/css">
.bg-f1 {
    background-color: #f1f1f1;
}

#menu dt{
    padding: 10px 20px;
    text-align: left;
    line-height: 24px;
    font-size: 18px;
    font-weight: 500;
    color: #000000;
    cursor: pointer;
    
}
#menu dd{
 	margin: 5px 20px;
    line-height: 24px;
    color: #000000;
    cursor: pointer;
    text-align: center;
    font-size: 14px;
}

#menu dd a{
    display: block;
    color: #000000;
    text-decoration: none;
}
.selected {
    background-color: #9f9f9f;
}
</style>


</head>
<body class="bg-f1">
<div id="div-mask" class="loading-mask" style="display: none;">
	<div class="loading-bar"></div>
</div>
<div class="col-md-12 not-padding">	
	<div class="col-md-1 not-padding" >
        <img src="<p:basePath/>assets/images/logo1.png" style="height:60px;" alt="" pagespeed_url_hash="971388425">
    </div>	
	<div class="col-md-10 not-padding" style="color:#000;float: left;padding:0;font-size: 40px;font-family:黑体" >如愿寻宝后台管理系统</div>
	<div class="col-md-1 not-padding" style="height:60px;line-height: 60px;text-align: center;">
		<a  style="font-size: 20px;color: #000000; text-decoration: none;" href="<p:basePath/>loginOut.act" class="exit">退出</a>
	</div>
	
</div>
<div class="col-md-12 not-padding relative">
	<div class="col-md-2 main-left" role="navbar-left" data-auto="0">
		<div class="js-nav-wid" id="menu" >  
		</div>

	</div>
	<div class="col-md-10 main" role="main" >
		<div class="" id="rediv">
            <iframe src="goodsManagement.act" style="width:100%;height:85%;min-height: 525px" frameBorder=0 scrolling=no name="reframe" id="reframe" > </iframe>
        </div>
	
	</div>
</div>

</body>
<script type="text/javascript">
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getMenu.act",
	    dataType : "json",
	    success: function(req){
	    	var menustr = '<dl>';
	    	$.each(req.menulist,function(key,value){
			    menustr += '<dt>' + key + '</dt>'
			    $.each(value,function(index,item){
			    	 menustr +='<dd >'
			    	 		 + '<a href="<p:basePath/>'  + item.url + '" target="reframe" >' +item.name+ '</a>'
			    	 		 + '</dd>';
				})
			})
			menustr += '</dl>';
			$("#menu").html(menustr);
			$('dd').eq(0).addClass("selected");
			
			$('dd').click(function(){		
				$(this).addClass("selected").siblings().removeClass("selected");
			})

	    }
	});

	
	
</script>
</html>

