package org.core.p3.web.process;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.core.p3.web.ProcessHandler;

/**
 * <p>静态资源加载处理器
 * <p>加载例如js,css,jpg等文件给客户端
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class ResourceProcessHandler extends ProcessHandler {

	public ResourceProcessHandler() {
	}

	@Override
	public boolean process(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String url = request.getRequestURI();
			url = url.replaceFirst(request.getContextPath(), "");
			InputStream is = this.getClass().getResourceAsStream("/content" + url);

			if (is != null) {
				zip(request,response,is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
