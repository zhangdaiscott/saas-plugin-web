package org.core.p3.web.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.core.p3.filter.P3Filter;
import org.core.p3.helper.SynchronizationHelper;
import org.core.p3.helper.VelocityHelper;


/**
 * <p>抽象的Action对象
 * <p>所有的Action对象必须实现该抽象类
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public abstract class AbstractAction {


	/**
	 * 
	 */
	protected HttpServletRequest request;
	
	/**
	 * 
	 */
	protected HttpServletResponse response;

	/**
	 * 初始化上下文属性
	 */
	protected AbstractAction() {
		request = SynchronizationHelper.getCurrentRequest();
		response = SynchronizationHelper.getCurrentResponse();

	}

	/**
	 * 默认的操作，返回对应的vm模板，如org.rzzl.main.action.DesktopAction对应/content/main/Desktop.vm文件
	 * @return Object
	 * @throws Exception
	 */
	public Object execute() throws Exception {

		StringBuffer name = new StringBuffer(getClass().getName());
		name.delete(0, name.lastIndexOf(".") + 1);
		name.delete(name.length() - 6, name.length());

		StringBuffer path = new StringBuffer(getClass().getPackage().getName());
		path.delete(0, P3Filter.ACTION_PACKAGE.length() + 1);
		path.delete(path.length() - 6, path.length());

		int b;
		while ((b = path.indexOf(".")) != -1) {
			path.setCharAt(b, '/');
		}

		return VelocityHelper.merge(path.append(name).append(".vm").toString(),
				null);
	}
}
