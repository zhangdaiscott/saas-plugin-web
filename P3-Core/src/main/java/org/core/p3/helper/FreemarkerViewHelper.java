package org.core.p3.helper;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.core.mframework.system.VelocityViewHelper;
import org.core.p3.utils.SimpleFormat;
import org.core.p3.web.SystemInfo;
import org.springframework.beans.factory.InitializingBean;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Freemarker工具类
 * 
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 * 
 */
public class FreemarkerViewHelper implements InitializingBean {

	final static Logger logger = Logger.getLogger(FreemarkerViewHelper.class);
	private static Configuration _tplConfig = new Configuration();
	static{
		_tplConfig.setClassForTemplateLoading(FreemarkerViewHelper.class, "/content");
	}

	/**
	 * 解析ftl
	 * @param tplName 模板名
	 * @param encoding 编码
	 * @param paras 参数
	 * @return
	 */
	public static String parseTemplate(String tplName, String encoding,
			Map<String, Object> paras) {
		try {
			StringWriter swriter = new StringWriter();
			Template mytpl = null;
			mytpl = _tplConfig.getTemplate(tplName, encoding);
			mytpl.process(paras, swriter);
			return swriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}
	public static String parseTemplate(String tplName, Map<String, Object> paras) {
		return parseTemplate(tplName, "utf-8", paras);
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
	public static String merge(String template,Map<String, Object> paras)
			throws ResourceNotFoundException, ParseErrorException,
			MethodInvocationException, Exception {
		if (logger.isDebugEnabled())
			logger.debug("Freemarker loading：" + template);

		HttpServletRequest request = SynchronizationHelper.getCurrentRequest();
		HttpServletResponse response = SynchronizationHelper
				.getCurrentResponse();

		if (paras == null)
			paras = new HashMap<String, Object>();

		paras.put("request", request);
		paras.put("response", response);
		paras.put("Format", new SimpleFormat());
		StringBuffer serverInfo = new StringBuffer();
		paras.put("T3_SERVER_INFO", serverInfo.toString());
		paras.put("T3_SYSTEM_INFO", SystemInfo.class.newInstance());

		// 读取资源文件
		String content = parseTemplate(template, "UTF-8", paras);
		return content;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
