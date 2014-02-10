package org.core.p3.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.core.p3.filter.P3Filter;


/**
 * URL解析规则对象
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class UrlRole {

	private static Logger logger = Logger.getLogger(UrlRole.class);

	private StringBuffer className;
	private String methodName;

	private UrlRole() {
	}

	public String getClassName() {
		return className == null ? "" : className.toString();
	}

	public String getMethodName() {
		return methodName;
	}

	public String toString() {
		return className + "<" + methodName + ">";
	}


	/**
	 * 解析url{@link StringBuffer}返回一个新的实例
	 * @param url
	 * @return UrlRole
	 */
	public static UrlRole parse(StringBuffer url) {

		UrlRole def = new UrlRole();

		int point = url.lastIndexOf(".");
		if (point != -1)
			url.delete(point, url.length());

		int a = url.indexOf("!");

		if (a == url.length() - 1) {
			def.className = url.deleteCharAt(url.length() - 1);
			def.methodName = "execute";
		} else if (a != -1) {
			def.methodName = url.substring(a + 1);
			def.className = url.delete(a, url.length());
		} else {
			def.className = url;
			def.methodName = "execute";
		}

		int b = def.className.lastIndexOf("/");

		char c = def.className.charAt(b + 1);

		if (c >= 'a' && c <= 'z')
			def.className.setCharAt(b + 1, (char) (c - 32));
		def.className.replace(b, b + 1, ".action.").append("Action");

		while ((b = def.className.indexOf("/")) != -1) {
			def.className.setCharAt(b, '.');
		}

		def.className.insert(0, P3Filter.ACTION_PACKAGE);
		if (logger.isDebugEnabled())
			logger.debug("action define : " + def);
		return def;
	}

	/**
	 * 解析{@link javax.servlet.http.HttpServletRequest}返回一个新的实例
	 * @param request
	 * @return UrlRole
	 */
	public static UrlRole parse(HttpServletRequest request) {
		StringBuffer url = new StringBuffer(request.getRequestURI());
		url.delete(0, request.getContextPath().length());
		return parse(url);
	}
}
