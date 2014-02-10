package org.core.p3.helper;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.core.p3.exception.SystemStartupException;
import org.core.p3.utils.SimpleFormat;
import org.core.p3.web.SystemInfo;
import org.springframework.beans.factory.InitializingBean;

/**
 * Velocity工具类
 * 
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 * 
 */
public class VelocityHelper implements InitializingBean {

	final static Logger logger = Logger.getLogger(VelocityHelper.class);
	private String propertiesFile;
	private Properties prop;

	private VelocityHelper() {
	}

	/**
	 * 设置配置文件
	 * 
	 * @param propertiesFile
	 */
	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	/**
	 * <p>
	 * 设置其他配置属性
	 * <p>
	 * 这里设置的内容会覆盖{@link #setPropertiesFile(String propertiesFile)}的内容
	 * 
	 * @param prop
	 */
	public void setProperties(Properties prop) {
		this.prop = prop;
	}

	/**
	 * <p>
	 * 实现的{@link org.springframework.beans.factory.InitializingBean}方法
	 * <p>
	 * 初始化Velocity
	 * 
	 * @see org.apache.velocity.app.Velocity#init(Properties prop)
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Properties p = new Properties();
			if (propertiesFile != null)
				p.load(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(propertiesFile));
			if (prop != null)
				p.putAll(prop);
			Velocity.init(p);
			if (logger.isInfoEnabled()) {
				logger.info("Loading VelocityEngine....");
				Enumeration<?> en = p.propertyNames();
				while (en.hasMoreElements()) {
					String key = en.nextElement().toString();
					logger.info("\t" + key + "=" + p.getProperty(key));
				}
			}
		} catch (Exception e) {
			throw new SystemStartupException("启动Velocity失败", e);
		}
	}

	/**
	 * 编译模板
	 * 
	 * @param template
	 * @return String
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws MethodInvocationException
	 * @throws Exception
	 * @see #merge(String template, VelocityContext velocityContext)
	 */
	public static String merge(String template)
			throws ResourceNotFoundException, ParseErrorException,
			MethodInvocationException, Exception {
		return merge(template, null);
	}

	/**
	 * <p>
	 * 编译模板
	 * <p>
	 * 强制从classpath:/content/路径下加载模板文件
	 * <p>
	 * 同时强制在{@link org.apache.velocity.VelocityContext}中注入<br>
	 * request={@link javax.servlet.http.HttpServletRequest},<br>
	 * response={@link javax.servlet.http.HttpServletResponse},<br>
	 * Format={@link org.core.p3.utils.SimpleFormat},<br>
	 * mSGA={@link org.core.mframework.system.VelocityViewHelper},<br>
	 * Security={@link org.rzzl.t3.auth.SecuAPI}
	 * 
	 * @param template
	 * @param velocityContext
	 *            Velocity上下文参数
	 * @return String
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws MethodInvocationException
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String merge(String template, VelocityContext velocityContext)
			throws ResourceNotFoundException, ParseErrorException,
			MethodInvocationException, Exception {
		if (logger.isDebugEnabled())
			logger.debug("Velocity loading：" + template);

		HttpServletRequest request = SynchronizationHelper.getCurrentRequest();
		HttpServletResponse response = SynchronizationHelper
				.getCurrentResponse();

		if (velocityContext == null)
			velocityContext = new VelocityContext();

		velocityContext.put("request", request);
		velocityContext.put("response", response);
		velocityContext.put("Format", new SimpleFormat());
		velocityContext.put("mSGA",
				org.core.mframework.system.VelocityViewHelper.class.newInstance());

		StringBuffer serverInfo = new StringBuffer();
		velocityContext.put("T3_SERVER_INFO", serverInfo.toString());
		velocityContext.put("T3_SYSTEM_INFO", SystemInfo.class.newInstance());

		// 读取资源文件
		StringWriter writer = new StringWriter();
		Velocity.mergeTemplate("content/" + template, "UTF-8", velocityContext,
				writer);
		
		return writer.toString();
	}
}
