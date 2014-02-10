package org.core.p3.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.core.p3.helper.SynchronizationHelper;
import org.core.p3.web.process.ErrorProcessHandler;

/**
 * <p>用户请求处理工具
 * <p>每当用户发起一个请求{@link HttpServletRequest}，将由此类进行处理
 * <p>获取配置在Spring上下文中的{@link Process}集合，按顺序逐个处理，直到{@link Process}认为请求已经执行完毕时，提交请求并给用户反馈结果，
 * 如果所有的{@link Process}均执行完毕后认为需要继续处理，则将请求处理权限还给{@link org.rzzl.P3Dispatcher}
 * <p>同时使用{@link org.core.p3.helper.SynchronizationHelper}同步请求信息，
 * <p>最后进行线程数据的清理
 * @author admin
 * @see org.rzzl.P3Dispatcher
 * @see org.core.p3.helper.SynchronizationHelper#setCurrentRequest(HttpServletRequest, HttpServletResponse)
 * @see org.rzzl.t3.helper.TransactionHelper#beginTransaction()
 * @see org.rzzl.t3.helper.TransactionHelper#commitTransaction()
 * @see org.rzzl.t3.helper.TransactionHelper#rollbackTransaction(Throwable)
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class RequestProcess {

	final static Logger logger = Logger.getLogger(RequestProcess.class);

	private List<Process> processHandlers;
	private String encoding = "UTF-8";

	public RequestProcess() {
	}

	/**
	 * 处理请求
	 * @param request
	 * @param response
	 * @return 如果认为请求处理完毕返回true，否则返回false
	 * @throws IOException
	 */
	public boolean process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("request " + request.getRequestURL());

		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);

		SynchronizationHelper.setCurrentRequest(request, response);
		
		StringBuffer url = new StringBuffer(request.getRequestURI()).delete(0,request.getContextPath().length());
		try {
			if (logger.isDebugEnabled())
				logger.debug("processChain begin");
			if (processHandlers != null)
				for (Process handler : processHandlers) {
					if (handler.execUrlPattern(url)) {
						if (logger.isDebugEnabled())
							logger.debug("\t" + handler.getClass().getName()+ " process");
						if (handler.process(request, response)) {
							if (logger.isDebugEnabled())
								logger.debug("processChain finish");
							return true;
						}
					}
				}
			if (logger.isDebugEnabled())
				logger.debug("processChain finish");
			return false;
		} catch (Throwable t) {
			ProcessHandler error = new ErrorProcessHandler(t);
			return error.process(request, response);
		} finally {
			SynchronizationHelper.clear();
		}
	}

	public void setProcessHandlers(List<Process> processHandlers) {
		this.processHandlers = processHandlers;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
