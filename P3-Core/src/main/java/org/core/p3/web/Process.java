package org.core.p3.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>请求处理链接口
 * <p>用于集成Spring声明式事务，但是为了兼容mFramework-Core 4.0，暂时没有启动
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 */
public interface Process {

	public boolean process(HttpServletRequest request,
			HttpServletResponse response) throws IOException;

	public void setUrlPattern(String urlPattern);

	public boolean execUrlPattern(StringBuffer url);
}
