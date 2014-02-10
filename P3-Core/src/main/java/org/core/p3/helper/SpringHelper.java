package org.core.p3.helper;

import org.apache.log4j.Logger;
import org.core.p3.exception.SystemStartupException;
import org.core.p3.web.RequestProcess;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>Spring上下文工具
 * <p>获取Spring上下文中管理的Bean实例
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class SpringHelper implements ApplicationContextAware {

	final static Logger logger = Logger.getLogger(SpringHelper.class);

	private static ApplicationContext _context;
	private static RequestProcess requestProcess;

	private SpringHelper() {}

	/**
	 * 返回上下文中管理的对象实例
	 * @param name
	 * @return T
	 * @see org.springframework.context.ApplicationContext#getBean(String name)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) (_context.containsBean(name) ? _context.getBean(name) : null);
	}

	/**
	 * 返回系统请求处理链
	 * @return T
	 * @see org.core.p3.web.RequestProcess
	 */
	public static RequestProcess getRequestProcess() {
		return requestProcess;
	}

	/**
	 * 实现的{@link org.springframework.context.ApplicationContextAware}接口
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		_context = applicationContext;

		requestProcess = (RequestProcess) _context.getBean("requestProcess");
		if (requestProcess == null) {
			throw new SystemStartupException(
					"在Spring中没有找到可用的请求处理链requestProcess");
		}
		if (logger.isInfoEnabled())
			logger.info("定义requestProcess为:"
					+ requestProcess.getClass().getName());
	}
}
