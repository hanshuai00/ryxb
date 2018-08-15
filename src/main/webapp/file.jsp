<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>文件上传</title>
	<script type="text/javascript" src="assets/js/jquery-1.11.1.min.js"></script>
</head>
<body>
 
 	<div style="height: 100%; width: 100%;">
		 <div style="width:100%; height: 20%;margin-top:30px;border-top:#4FC1E9 8px solid;">
		 	 <h3>文件上传</h3>
			 <!-- enctype=”multipart/form-data” 这个是上传文件必须的 -->
			 <form  id="" action="<p:basePath/>file/uploadFile.act" method="post" enctype="multipart/form-data">  
				 <input type="file" id="uploadFile_id" name="file" value="" /> 
				 <input type="text" id="fileName" name="fileName" value="" /> 
				 <input type="text" id="typePath" name="typePath" value="user">
				 <input type="submit" value="上传" />
			 </form>
		  </div>  
  
		<div id="result" style="float: left; padding: 10px"></div>
	</div>
</body>
</html>