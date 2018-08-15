package com.zj.ryxb.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.zj.ryxb.common.util.CodeUtil;
import com.zj.ryxb.common.util.PropertyUtils;

/**
 * ios,android附件上传下载,传来的参数所有不是json，但是返回结果集还是json
 * @author zxf
 *
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {
	
	/**
	 * @Title: uploadFile
	 * @param  fileName 附件名称(全名，包含后缀类型)
	 * @param  typePath   附件类型路径
	 * @param  request
	 * @param  file
	 * @param  设定文件
	 * @return ResultIos 返回类型
	 */
	@RequestMapping(value = "uploadFile",produces = "text/plain;charset=UTF-8",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public  String uploadFile(String fileName,String typePath,
			HttpServletRequest request, MultipartFile  file) {
		Map<String, Object> returnMap = new HashMap<String , Object>();
		Gson gson = new Gson();
		//服务器的路径地址
		//usr/tomcat/webapps  +  /usr/tomcat/webapps/uploader
		//String filePath = request.getSession().getServletContext().getRealPath("upload");//上传保存服务器目录文件路径
		String filePath = PropertyUtils.getProperty("local.file.path");//服务器
		String path  = "/uploader/"+typePath+"/";
		System.out.println("fileName="+fileName);


        if(fileName != null &&  fileName !=""){
        	//服务器保存的文件名(重命名后)
    		String localFileName = CodeUtil.findNewCode("G")+"."+fileName.substring(fileName.lastIndexOf(".")+1);
        	//保存到我们服务器里
   	     	File targetFile = new File(filePath+path, localFileName);
	        //判断上传文件是否已存在
	        if(!targetFile.exists()){ 
	        	//不存在则创建一个目录文件，它的路径名由当前 File 对象指定，包括任一必须的父路径。  
	        	//先创建空壳XXX.jpg ，如果存在XXX就不再创建 
	            targetFile.mkdirs();
	        }
	        //保存到本地
	        try {  
	        	//复制文件（把里面内容，保存到上面空壳去）
	        	file.transferTo(targetFile);
	        	
	        	
	        	returnMap.put("imagePath", path);
	        	returnMap.put("imageName", localFileName);
	        	returnMap.put("state", true);
				returnMap.put("msg", "上传成功");
	            
	        } catch (Exception e) {  
	            e.printStackTrace();
	            returnMap.put("state", false);
				returnMap.put("msg", "上传本地出错，请重新上传");
				return gson.toJson(returnMap);
	        }  
        }else{
        	returnMap.put("state", false);
			returnMap.put("msg", "没有上传文件，请重新检查上传");
        }
    
        return gson.toJson(returnMap);
	 }  
	
	/**
	* @Description: 文件下载
	* @Title: downloadFile 
	* @param fileName(原附件名称 )
	* @param response
	* @return  当有错误时候 移动端接受return对象出错，下载不返回
	* @throws Exception
	 */
	@RequestMapping(value = "downloadFile",method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public  void downloadFile(String fileName,HttpServletRequest request, HttpServletResponse response) throws Exception {
		fileName = CodeUtil.enCodeStr(fileName);
        //当前文件路径
        String filePath = request.getSession().getServletContext().getRealPath("/") + "upload\\";
        //String filePath = PropertyUtils.getProperty("ftp_upload_src");//81.5 服务器
        
        File  file = new File(filePath + File.separator + fileName);
         
        // 清空response 
        response.reset();   
        // 设置response的Header 
        response.addHeader("Content-Disposition", "AttachmentDomain;filename=\""+new String(fileName.getBytes("UTF-8"),"iso-8859-1")+ "\"");  //转码之后下载的文件不会出现中文乱码
        response.addHeader("Content-Length", "" + file.length()); 
        response.setContentType("application/octet-stream;charset=UTF-8"); //"multipart/form-data;charset=UTF-8"
        try {
            //以流的形式下载文件
            InputStream inputStream =new BufferedInputStream(new FileInputStream(file));
            
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] b = new byte[inputStream.available()];
            int length;
            while ((length = inputStream.read(b)) > 0) {
            	outputStream.write(b, 0, length);
            }; 
            // 这里主要关闭。
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

	public static void main(String[] args) {
		try {
			String a = URLDecoder.decode("드라이브모드 버튼", "UTF-8");
			System.out.println(a);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
