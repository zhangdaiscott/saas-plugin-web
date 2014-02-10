package org.core.p3.web.process;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.core.p3.web.ProcessHandler;
import org.core.p3.web.UrlRole;

/**
 * {@link AbstractAction}对象处理器
 * <p>在处理的开始阶段，根据事务规则，开启一个新的事务{@link org.rzzl.t3.helper.TransactionHelper},
 * <p>根据处理的结果，执行事务的提交和回滚
 * @author admin
 *
 */
public class ActionProcessHandler extends ProcessHandler {

	private static Logger logger = Logger.getLogger(ActionProcessHandler.class);

	public ActionProcessHandler() {
	}

	@Override
	public boolean process(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return invoke(request, response, UrlRole.parse(request));
	}

	/**
	 * 执行{@link AbstractAction}
	 * @param request
	 * @param response
	 * @param urlRole
	 * @return
	 * @throws IOException
	 */
	private boolean invoke(HttpServletRequest request,
			HttpServletResponse response, UrlRole urlRole) throws IOException {

		try {
			Class<?> actionClass = Class.forName(urlRole.getClassName());
			AbstractAction action = (AbstractAction) actionClass.newInstance();
			Method actionMethod = actionClass.getMethod(urlRole.getMethodName());

			action.request = request;
			action.response = response;

			Object rtn = actionMethod.invoke(action);
			boolean result = true;
			if (rtn != null) {
				result = outputToResponse(request, response, rtn.toString());
			}
			return result;
		} catch (Throwable t) {
			ErrorProcessHandler error = new ErrorProcessHandler(t);
			error.setUrlRole(urlRole);
			return error.process(request, response);
		}
	}
}
