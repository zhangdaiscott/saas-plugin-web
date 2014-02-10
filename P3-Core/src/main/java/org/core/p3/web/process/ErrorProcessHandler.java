package org.core.p3.web.process;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.core.p3.filter.P3Filter;
import org.core.p3.helper.VelocityHelper;
import org.core.p3.web.ProcessHandler;
import org.core.p3.web.UrlRole;


/**
 * <p>错误处理器
 * <p>将系统及运行时错误完整展现给用户
 * @author admin
 *
 */
public class ErrorProcessHandler extends ProcessHandler {

	private Throwable t;
	private UrlRole urlRole;

	public ErrorProcessHandler(Throwable t) {
		this.t = t;
	}

	public ErrorProcessHandler(Throwable t,UrlRole urlRole) {
		this.t = t;
		this.urlRole = urlRole;
	}
	
	public void setUrlRole(UrlRole urlRole){
		this.urlRole = urlRole;
	}

	private String getExceptionStackTrace(Throwable t) {
		StringBuffer sb = new StringBuffer(
				"<strong><span style='color:black'>Caused by : <a href='http://docs.java.com'>"
						+ t.getClass().getName() + "</a> : " + t.getMessage()
						+ "</span></strong><br />");
		for (StackTraceElement s : t.getStackTrace()) {
			if (true) {
				if (s.getClassName().contains("com.mframework")
						|| s.getClassName().contains(
								P3Filter.ACTION_PACKAGE)) {
					sb.append(String
							.format("&nbsp &nbsp &nbsp &nbsp <strong><span style='color:#f00'> at %s.%s (<span style='color:#00d'>%s : %s</span>)</span></strong><br />",
									s.getClassName(), s.getMethodName(),
									s.getFileName(), s.getLineNumber()));
				} else {
					sb.append(String
							.format("&nbsp &nbsp &nbsp &nbsp <span style='color:#d00;'> at %s.%s (<span style='color:#00d'>%s : %s</span>)</span><br />",
									s.getClassName(), s.getMethodName(),
									s.getFileName(), s.getLineNumber()));
				}
			}
		}
		Throwable c = t.getCause();
		if (c != null) {
			sb.append(getExceptionStackTrace(c));
		}
		return sb.toString();
	}

	@Override
	public boolean process(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			VelocityContext context = new VelocityContext();
			context.put("throwable", t);
			context.put("urlRole", urlRole);
			context.put("message", t.getMessage());
			context.put("ExceptionStack", getExceptionStackTrace(t));
			if (t instanceof InstantiationException) {
				context.put("message", "页面没有找到");
			} else if (t instanceof IllegalAccessException) {
				context.put("message", "页面没有找到");
			} else if (t instanceof ClassNotFoundException) {
				context.put("message", "页面没有找到");
			} else if (t instanceof NoSuchMethodException) {
				context.put("message", "页面没有找到");
			} else if (t instanceof IllegalArgumentException) {
				context.put("message", "操作失败");
			} else if (t instanceof InvocationTargetException) {
				context.put("message", "系统错误");
			} else if (t instanceof IOException) {
				context.put("message", "系统错误");
			} else if (t instanceof RuntimeException) {
				context.put("message", "操作失败");
			}  else {
				context.put("message", t.getMessage());
			}
			t.printStackTrace();
			outputToResponseNoCatchException(request, response,
					VelocityHelper.merge("cmd/error.vm", context));
			return true;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
