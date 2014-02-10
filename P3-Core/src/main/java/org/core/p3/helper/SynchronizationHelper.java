package org.core.p3.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 同步请求执行过程中的
 * {@link javax.servlet.http.HttpServletRequest},
 * {@link javax.servlet.http.HttpServletResponse}对象
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class SynchronizationHelper {

	private static Logger logger = Logger.getLogger(SynchronizationHelper.class);
	private final static ThreadLocal<HttpServletRequest> localServletRequest = new ThreadLocal<HttpServletRequest>();
	private final static ThreadLocal<HttpServletResponse> localServletResponse = new ThreadLocal<HttpServletResponse>();
	
	private SynchronizationHelper() {}

	/**
	 * 清除线程中的同步数据
	 */
	public static void clear() {
		localServletRequest.remove();
		localServletResponse.remove();
		if(logger.isDebugEnabled())
			logger.debug("清理当前线程");
	}
	
	/**
	 * 设置线程中的同步数据
	 * @param request
	 * @param response
	 */
	public static void setCurrentRequest(HttpServletRequest request,
			HttpServletResponse response) {
		localServletRequest.set(request);
		localServletResponse.set(response);
		if(logger.isDebugEnabled())
			logger.debug("把当前请求绑定到当前进程");
	}
	
	/**
	 * 获取线程中的{@link javax.servlet.http.HttpServletRequest}对象
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getCurrentRequest() {
		return localServletRequest.get();
	}

	/**
	 * 获取线程中的{@link javax.servlet.http.HttpServletResponse}对象
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getCurrentResponse() {
		return localServletResponse.get();
	}
}
