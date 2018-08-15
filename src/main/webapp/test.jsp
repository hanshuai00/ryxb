<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>
<html>
  <head>
    
    <title>接口测试</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">


  </head>
  
  <body>
            参数1:<input type="text" id="param1" placeholder="请输入参数" "></br>
            参数2:<input type="text" id="param2" placeholder="请输入参数" "></br>
            参数3:<input type="text" id="param3" placeholder="请输入参数" "></br>
            参数4:<input type="text" id="param4" placeholder="请输入参数" "></br></br>
    <input type="button" onclick="getSmsCode()" value="获取验证码"/> 
    <input type="button" onclick="customerRegister()" value="注册"/>
    <input type="button" onclick="customerLoginIn()" value="登录"/>
    <input type="button" onclick="xiufuLoginIn()" value="xiufu登录"/>
    <input type="button" onclick="getBonusList()" value="红包列表"/>
    <input type="button" onclick="getGoodsList()" value="商品列表"/>
    <input type="button" onclick="getCatalogue()" value="商品分类"/>
    <input type="button" onclick="goodsDetail()" value="商品详情"/>
    
    
    <br/>
    <input type="button" onclick="addCart()" value="加入购物车"/>
    <input type="button" onclick="getCartList()" value="查询购物车"/>
    <input type="button" onclick="removeCart()" value="删除购物车"/>
    <input type="button" onclick="getDistrictByParentId()" value="地区列表"/>
    <input type="button" onclick="saveOrder()" value="提交订单"/>
    <input type="button" onclick="yuePay()" value="余额支付"/>
    
    <br/>
    <input type="button" onclick="checkAppVersion()" value="检查版本"/>
    <input type="button" onclick="getOrderDetailList()" value="寻宝记录"/>
    
  </body>
<script type="text/javascript">

function test(){
	customerLoginIn()
}

function getSmsCode(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>getSmsCode.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "userName" : $("#param1").val(),
		  "smsType" : "1"
		  
		},
		success: function(resp){
			console.log(resp)


		}
	});
}


function customerRegister(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>customerRegister.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "userName" : $("#param1").val(),
		  "password" : $("#param2").val(),
		  "smsCode" : $("#param3").val(),
		  "nickname" : "楚风",
		  "trueName" : "韩帅",
		  "userGender" : "1"
		  
		},
		success: function(resp){
			console.log(resp)


		}
	});
}

function customerLoginIn(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>customerLoginIn.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "userName" : "18562698975",
		  "password" : "hanshuai"
		},
		success: function(resp){
			console.log(resp)
			
		    setCookie("token", resp.token);
		}
	});
}

function xiufuLoginIn(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>customerLoginIn.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "userName" : "18266625783",
		  "password" : "111111"
		},
		success: function(resp){
			console.log(resp)
			
		    setCookie("token", resp.token);
		}
	});
}
function getBonusList(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>findCusBonusList.act",
	    dataType : "json",
        data : {
  		  "token" : getCookie("token")
  		},
	    success: function(req){
	    	console.log(req)	
	    }
	});

}

function getGoodsList(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>goodsList.act",
	    dataType : "json",
	    data : {
  		  	"token" : getCookie("token"),
  		    "keyWord" : $("#param1").val(),
  			"pageNo" : 1,
  			"pageSize" :100
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});

}

function getCatalogue(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getCatalogue.act",
	    dataType : "json",
	    data : {
  		  "token" : getCookie("token")
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});

}

function goodsDetail(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>goodsDetail.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  			"goodsId" : $("#param1").val(),
  			"issue" : $("#param2").val()	
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function addCart(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>addCart.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  			"goodsId" : $("#param1").val(),
  			"issue" : $("#param3").val(),
  			"quantity" : $("#param4").val()		
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function getCartList(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getCartList.act",
	    dataType : "json",
	    data : {
  		  "token" : getCookie("token")
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}


function removeCart(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>removeCart.act",
	    dataType : "json",
	    data : {
	    	"token" : getCookie("token"),
  			"goodsId" :  $("#param1").val()
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function getDistrictByParentId(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getDistrictByParentId.act",
	    dataType : "json",
	    data : {
  			"parentId" :  $("#param1").val()
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function saveOrder(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>saveOrder.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  			"goodsIdStr" : $("#param1").val(),
  			"goodsNameStr" : $("#param2").val(),
  			"issueStr" : $("#param3").val(),
  			"quantityStr" : $("#param4").val()
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function yuePay(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>yuePay.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  			"orderNum" : $("#param1").val()
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}
	
function getOrderDetailList(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getOrderDetailList.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token")
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function checkAppVersion(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>checkAppVersion.act",
	    dataType : "json",
	    success: function(req){
	    	console.log(req)

	    }
	});
}
	
</script>


</html>
