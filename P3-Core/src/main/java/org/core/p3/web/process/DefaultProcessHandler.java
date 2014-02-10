package org.core.p3.web.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.core.p3.web.ProcessHandler;

/**
 * 默认的处理器 无实现
 * @author admin
 *
 */
public class DefaultProcessHandler extends ProcessHandler {

	public DefaultProcessHandler() {}

	@Override
	public boolean process(HttpServletRequest request,
			HttpServletResponse response) {
		return false;
	}
}
