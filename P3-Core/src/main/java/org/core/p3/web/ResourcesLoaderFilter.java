package org.core.p3.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.core.p3.web.process.ResourceProcessHandler;

/**
 * 转发系统资源
 * @author admin
 *
 */
public class ResourcesLoaderFilter implements Filter {

	private ProcessHandler processHandler;

	@Override
	public void init(FilterConfig config) throws ServletException {
		processHandler = new ResourceProcessHandler();
		processHandler.setUrlPattern(config.getInitParameter("urlPattern"));
	}

	@Override
	public void destroy() {
		processHandler = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) {
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			StringBuffer url = new StringBuffer(httpRequest.getRequestURI())
					.delete(0, httpRequest.getContextPath().length());

			if (!(processHandler.execUrlPattern(url) && processHandler.process(
					httpRequest, httpResponse))) {
				chain.doFilter(request, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
}
