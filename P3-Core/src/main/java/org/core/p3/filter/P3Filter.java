package org.core.p3.filter;

import java.io.IOException;
import java.lang.reflect.Proxy;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.core.p3.helper.SpringHelper;
import org.core.p3.support.spring.SpringContextLocationsServletContextProxy;
import org.springframework.web.context.ContextLoader;

/**
 * <p>框架入口(初始化Spring)，业务web程序请求的切入点(实现{@link javax.servlet.Filter})
 * <p>如果没有配置spring，使用默认值启动系统{@link SpringContextLocationsServletContextProxy}
 * <p>请求处理使用{@link org.core.p3.web.RequestProcess}，如果{@link org.core.p3.web.RequestProcess}认为请求已经处理完毕，则不再对请求操作，
 * 否则，继续容器过滤器链
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 * @see org.core.p3.web.RequestProcess
 *
 */
public class P3Filter implements Filter {

	public static final String ACTION_PACKAGE = "com.buss";
	public static final String DEFAULT_CONFIG_LOCATION_PARAM = "classpath:config/applicationContext*.xml";
	private final Logger logger = Logger.getLogger(P3Filter.class);

	protected ServletContext servletContext;
	protected ContextLoader contextLoader = new ContextLoader();

	@Override
	public void init(FilterConfig config) throws ServletException {
		servletContext = config.getServletContext();
		// 如果用户没有配置Spring配置文件路径，使用默认的路径，引用代理ServletContext
		if (servletContext
				.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM) == null) {
			
			logger.warn("\n" +
						"！！！没有找到初始化参数["+ ContextLoader.CONFIG_LOCATION_PARAM + "]在web.xml中，\n" +
						"！！！将使用默认配置\""+DEFAULT_CONFIG_LOCATION_PARAM+"\"作为Spring配置启动，\n" +
						"！！！或者您现在可以停止运行程序，在web.xml中加入如下配置，并尝试重新运行。\n" +
						"<context-param>\n" +
						"\t<param-name>contextConfigLocation</param-name>\n" +
						"\t<param-value>"+P3Filter.DEFAULT_CONFIG_LOCATION_PARAM+"</param-value>\n" +
						"</context-param>");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			servletContext = (ServletContext) Proxy.newProxyInstance(
					ServletContext.class.getClassLoader(),
					new Class[] { ServletContext.class },
					new SpringContextLocationsServletContextProxy(
							servletContext, DEFAULT_CONFIG_LOCATION_PARAM));
		}

		logWelcome();
		try {
			contextLoader.initWebApplicationContext(servletContext);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logGoodluck();
	}

	@Override
	public void destroy() {
		contextLoader.closeWebApplicationContext(servletContext);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) {
		try {
			/*
			 * 调用处理请求处理链
			 */
			if (!SpringHelper.getRequestProcess().process(
					(HttpServletRequest) request,
					(HttpServletResponse) response)) {
				chain.doFilter(request, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	protected void logWelcome() {
		if (logger.isInfoEnabled())
			logger.info(("\n\t=================================================================")
					+ ("\n\t==============     Welcome to Plugin-Web-Framework ================")
					+ ("\n\t==================================================================="));
	}

	protected void logGoodluck() {
		if (logger.isInfoEnabled()) {
			logger.info(("\n\t===================================================================")
					+ ("\n\t==============        Plugin-Web-Framework         ================")
					+ ("\n\t==============        Version  1.0.0               ================")
					+ ("\n\t==============        Good luck!!                  ================")
					+ ("\n\t==============                                     ================")
					+ ("\n\t==============                                     ================")
					+ ("\n\t==============                                     ================")
					+ ("\n\t==============                                     ================")
					+ ("\n\t==============                                     ================")
					+ ("\n\t==============                                     ================")
					+ ("\n\t==============          scott@jeecg.org            ================")
					+ ("\n\t==============          http://www.jeecg.org        ===============")
					+ ("\n\t==================================================================="));
		}
	}
}
