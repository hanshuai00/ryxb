package support.common;

import javax.servlet.http.HttpServletRequest;

public class BasicInfo {
	private static final ThreadLocal<HttpServletRequest> httpServletRequest = new ThreadLocal<HttpServletRequest>();
	
	/**
	 * 将当前request绑定到线程
	 * @param request
	 */
	public static void setRequest(HttpServletRequest request) {
		httpServletRequest.set(request);
	}
	
	/**
	 * 获取request
	 * @param request
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = httpServletRequest.get();
		return request;
	}
	
	/**
	 * 获取项目基础路径
	 * @return
	 */
	public static String getBasePath() {
		HttpServletRequest request = httpServletRequest.get();
		StringBuffer basePath = new StringBuffer();
		basePath.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath()).append("/");
		return basePath.toString();
	}
	
	/**
	 * 获取项目磁盘根目录
	 * @return
	 */
	public static String getBaseDiskPath() {
		HttpServletRequest request = httpServletRequest.get();
		return request.getSession().getServletContext().getRealPath("/");
	}

}
