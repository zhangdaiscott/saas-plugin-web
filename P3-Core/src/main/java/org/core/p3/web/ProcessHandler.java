package org.core.p3.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.core.p3.helper.TxtReadUtil;
import org.core.p3.web.process.ErrorProcessHandler;
import org.core.p3.web.response.GZIPResponseWrapper;

import com.google.gson.Gson;

/**
 * <p>抽象的请求处理器
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public abstract class ProcessHandler implements Process {

	private final static Logger logger = Logger.getLogger(ProcessHandler.class);
	//ContentType
	private static String responsejson;
	private Pattern[] urlPatterns;

	public abstract boolean process(HttpServletRequest request,
			HttpServletResponse response) throws IOException;

	/**
	 * 初始化请求处理器启用的URL规则
	 */
	public void setUrlPattern(String urlPattern) {
		String[] urls = urlPattern.split(",");
		urlPatterns = new Pattern[urls.length];
		for (int i = 0; i < urls.length; i++) {
			urlPatterns[i] = Pattern.compile("^" + urls[i].replace("*", "\\S*")
					+ "$");
		}
		if (logger.isInfoEnabled())
			logger.info("set url pattern : " + urlPattern);
	}

	/**
	 * zip压缩后输出到response
	 * @param request
	 * @param response
	 * @param is
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected void zip(HttpServletRequest request, HttpServletResponse response,
			InputStream is) throws IOException {
		String acceptEncoding = request.getHeader("accept-encoding");

		//----------------------------------------------------------------------------------------
		//设置网页ContentType
		if(StringUtils.isEmpty(responsejson)){
			responsejson = TxtReadUtil.parseTemplate("org/core/p3/utils/content-type.json", null);
		}
		Map<String,String> lm = new Gson().fromJson(responsejson, Map.class);
		String encoding = response.getCharacterEncoding();
		for (String key : lm.keySet()) {
			if(request.getRequestURI().endsWith(key)){
				response.setContentType(lm.get(key));
				break;
			}
        } 
		//----------------------------------------------------------------------------------------
		if (acceptEncoding != null && acceptEncoding.indexOf("gzip") != -1) {
			if(request.getRequestURI().endsWith(".ico")){
				response.setContentType("image/x-icon");
			}else{
				String sContentType = response.getContentType();
				response = new GZIPResponseWrapper((HttpServletResponse) response);
				response.setCharacterEncoding(encoding);
				response.setContentType(sContentType);
			}
		}

		OutputStream os = response.getOutputStream();
		byte[] buff = new byte[2048];
		int bytesRead;

		while (-1 != (bytesRead = is.read(buff, 0, buff.length))) {
			os.write(buff, 0, bytesRead);
		}
		os.flush();
		os.close();
		is.close();
	}

	/**
	 * <p>输出到response
	 * <p>如果非ajax请求，
	 * 则使用{@link #zip(HttpServletRequest, HttpServletResponse, InputStream)}压缩后再输出
	 * <p>如果输出发生错误，则使用{@link ErrorProcessHandler}进行错误处理
	 * @param request
	 * @param response
	 * @param content
	 * @return boolean
	 */
	protected boolean outputToResponse(HttpServletRequest request,
			HttpServletResponse response, String content){
		try {
			outputToResponseNoCatchException(request,response,content);
			return true;
		} catch (Throwable t) {
			ErrorProcessHandler error = new ErrorProcessHandler(t);
			return error.process(request, response);
		}
	}
	
	/**
	 * <p>输出到response
	 * <p>如果非ajax请求，则使用{@link #zip(HttpServletRequest, HttpServletResponse, InputStream)}压缩后再输出
	 * @param request
	 * @param response
	 * @param content
	 * @throws IOException
	 */
	protected void outputToResponseNoCatchException(HttpServletRequest request,
			HttpServletResponse response, String content) throws IOException {
		String xRequestedWith = request.getHeader("X-Requested-With");
		if (xRequestedWith != null
				&& xRequestedWith.equals("XMLHttpRequest")) {
			response.getWriter().write(content);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			InputStream is = new ByteArrayInputStream(
					content.getBytes(response.getCharacterEncoding()));
			zip(request, response, is);
		}
	}

	/**
	 * 返回是否对当前的URL进行处理
	 */
	public boolean execUrlPattern(StringBuffer url) {
		for (Pattern p : urlPatterns) {
			if (p.matcher(url).matches()) {
				return true;
			}
		}
		return false;
	}
}
