<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>文件下载</title>
	<script type="text/javascript" src="assets/js/jquery-1.11.1.min.js"></script>
</head>
<body>
  <div class="test"><a href="http://mp.weixin.qq.com/mp/redirect?url=http://mobile.xinlianwang.com/android/distributor/DistributorApp.apk#weixin.qq.com#wechat_redirect ">有效跳转</a></div>
</body>
<script type="text/javascript"> 
function is_weixin(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i)=="micromessenger") {
        return true;
    } else {
        return false;
    }
}
/* 
 * 智能机浏览器版本信息: 
*/ 
var browser={ 
	versions:function(){
		var u = navigator.userAgent, app = navigator.appVersion; 
		return {//移动终端浏览器版本信息 
		trident: u.indexOf('Trident') > -1, //IE内核 
		presto: u.indexOf('Presto') > -1, //opera内核 
		webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核 
		gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核 
		weixin : u.indexOf('MicroMessenger') > -1, 
		mobile: !!u.match(/AppleWebKit.*Mobile.*/)||!!u.match(/AppleWebKit/), //是否为移动终端 
		ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端 
		android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器 
		iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQ HD浏览器 
		iPad: u.indexOf('iPad') > -1, //是否iPad 
		webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部 
		}; 
	}(), 
language:(navigator.browserLanguage || navigator.language).toLowerCase() 
} 

/**document.writeln("语言版本: "+browser.language);
document.writeln(" android终端: "+browser.versions.android); 
document.writeln(" 是否为移动终端: "+browser.versions.mobile); 
document.writeln(" ios终端: "+browser.versions.ios); 
document.writeln(" 是否为iPhone: "+browser.versions.iPhone); 
document.writeln(" 是否iPad: "+browser.versions.iPad);
document.writeln(navigator.userAgent); */ 

$(function (){
	var apkUrl='${apkUrl}';
	var iosUrl='${iosUrl}';
	if(browser.versions.ios ||browser.versions.iPhone || browser.versions.iPad){
		//window.location="http://www.ryxb.info"+iosUrl;
		//window.location="https://itunes.apple.com/cn/app/如愿寻宝/id1235361259?mt=8";
		window.location=iosUrl;
	}
 	if(browser.versions.android){
 		window.location="http://www.ryxb.info"+apkUrl;
	} 
});


</script>
</html>

