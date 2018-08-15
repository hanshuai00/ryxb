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
    <input type="button" onclick="getSmsCode()" value="短信验证码"/>
    <input type="button" onclick="customerRegister()" value="注册"/>
    <input type="button" onclick="customerLoginIn()" value="登录"/>
    <input type="button" onclick="findCustomerById()" value="会员信息"/>
    <input type="button" onclick="resetCusPassword()" value="重置密码"/>
    <input type="button" onclick="getBonusList()" value="红包列表"/>
    <input type="button" onclick="getGoodsList()" value="商品列表"/>
     <input type="button" onclick="getHomePageImage()" value="首页轮播图"/>
    <input type="button" onclick="getCatalogue()" value="商品分类"/>
    <input type="button" onclick="customerRecharge()" value="会员充值"/>
    
    
    <br/>
    <input type="button" onclick="modifyPassword()" value="修改密码"/>
    <input type="button" onclick="addCart()" value="加入购物车"/>
    <input type="button" onclick="getCartList()" value="查询购物车"/>
    <input type="button" onclick="removeCart()" value="删除购物车"/>
    <input type="button" onclick="getDistrictByParentId()" value="地区列表"/>
    <input type="button" onclick="pay()" value="结算"/>
    <input type="button" onclick="saveCAddress()" value="新增地址"/>
    <input type="button" onclick="findCAddressByCusId()" value="查询地址"/>
    <input type="button" onclick="getOrderWinInfo()" value="中奖信息"/>
     <br/>
    <input type="button" onclick="getOrderDetailList()" value="寻宝记录"/>
    <input type="button" onclick="getOrderWinList1()" value="宝物榜"/>
    <input type="button" onclick="getOrderWinList()" value="中奖记录"/>
    <input type="button" onclick="getOrderCommentList()" value="晒单记录"/>
    <input type="button" onclick="getContactMode()" value="联系客服"/>
    <input type="button" onclick="checkAppVersion()" value="检查版本"/>
    <input type="button" onclick="getMessageList()" value="系统公告"/>
    <input type="button" onclick="getWinNotice()" value="夺宝动态"/>
    <input type="button" onclick="getBootImage()" value="引导页图"/>
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
		  "userName" : "18661696673",
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
		  "userName" : "18661696673",
		  "smsCode" : "9570",
		  "password" : "123456",
		  "nickname" : "辛亥",
		  "trueName" : "秀福",
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
		  "userName" : "18661696673",
		  "password" : "123456"
		},
		/* data : {
		    "userName" : "18562698975",
			"password" : "hanshuai"
		}, */
		success: function(resp){
			console.log(resp)
			
		    setCookie("token", resp.token);
		}
	});
}

function findCustomerById(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>findCustomerById.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "token" : getCookie("token")
		},
		success: function(resp){
			console.log(resp)
		}
	});
}


function resetCusPassword(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>resetCusPassword.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "userName" : "18661696673",
		  "smsCode" : "3625",
		  "password" : "654321"
		},
		success: function(resp){
			console.log(resp)
		}
	});
}

function modifyPassword(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>modifyCusPassword.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "token" : getCookie("token"),
		  "oldPassword" : "123456",
		  "newPassword" : "123456",
		  "newPassword2" : "123456"
		},
		success: function(resp){
			console.log(resp)
		}
	});
}


function customerRecharge(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>customerRecharge.act",
	    dataType : "json",
	    data : {
  		  	"token" : getCookie("token"),
  			"payType" : $("#param1").val(),
  			"amount" : $("#param2").val()
  		},
	    success: function(req){
	    	console.log(req)

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
  			"pageNo" : $("#param1").val(),
  			"pageSize" : $("#param2").val()
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});

}
function getHomePageImage(){
	$.ajax({
		type: "POST",
		url: "<p:basePath/>getHomePageImage.act",
		dataType : "json",
		cache : false,
		async : true,
		data : {
		  "token" : getCookie("token")
		},
		success: function(resp){
			console.log(resp)
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

function addCart(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>addCart.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  			"goodsId" : $("#param1").val(),
  			"quantity" : $("#param2").val(),
  			"issue" : $("#param3").val()
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

function pay(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>pay.act",
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

function saveCAddress(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>saveCAddress.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  			"addressID" : "",
  			"receiverName" : $("#param1").val(),
  			"cellPhone" : $("#param2").val(),
  			"postalCode" : $("#param3").val(),
  			"streetInfo" : $("#param4").val()
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function findCAddressByCusId(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>findCAddressByCusId.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token")
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
function getOrderWinList1(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getOrderWinList.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  		 	"searchType":3
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}
function getOrderWinList(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getOrderWinList.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  		 	"searchType":2
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}


function getOrderWinInfo(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getOrderWinInfo.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  		 	"issue":6,
  		 	"goodId":4
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

function getOrderCommentList(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getOrderCommentList.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token"),
  		 	"searchType":1,
  		 	"goodId":4
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}


function getContactMode(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getContactMode.act",
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
function getBootImage(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getBootImage.act",
	    dataType : "json",
	    success: function(req){
	    	console.log(req)

	    }
	});
}
function getMessageList(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getMessageList.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token")
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}
function getWinNotice(){
	$.ajax({
	    type: "POST",
	    url: "<p:basePath/>getWinNotice.act",
	    dataType : "json",
	    data : {
  		 	"token" : getCookie("token")
  		},
	    success: function(req){
	    	console.log(req)

	    }
	});
}

</script>


</html>
