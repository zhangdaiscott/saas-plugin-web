package org.core.p3.support.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;

/**
 * <p>{@link javax.servlet.ServletContext}代理类
 * <p>当Web应用程序没有配置contextConfigLocation时
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 * @see org.rzzl.P3Dispatcher
 */
public class SpringContextLocationsServletContextProxy implements
		InvocationHandler {

	private ServletContext target;
	private String configLocation;

	public SpringContextLocationsServletContextProxy(ServletContext target,
			String configLocation) {
		this.target = target;
		this.configLocation = configLocation;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (method.getName().equals("equals")) {
			return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
		} else if (method.getName().equals("hashCode")) {
			return new Integer(System.identityHashCode(proxy));
		} else if (method.getName().equals("toString")) {
			StringBuffer buf = new StringBuffer(
					"ServletContext proxy for target Web Server ServletContext ");
			if (target != null) {
				buf.append("[").append(target.toString()).append("]");
			} else {
				buf.append("[null]");
			}
			return buf.toString();
		} else if (method.getName().equals("getInitParameter")) {
			if (ContextLoader.CONFIG_LOCATION_PARAM.equals(args[0])) {
				return configLocation;
			}
		}

		try {
			Object retVal = method.invoke(target, args);

			if (method.getName().equals("getInitParameterNames")) {
				Enumeration<?> en = (Enumeration<?>) retVal;
				Set<Object> s = new HashSet<Object>();
				while (en.hasMoreElements()) {
					s.add(en.nextElement());
				}
				s.add(ContextLoader.CONFIG_LOCATION_PARAM);
				return Collections.enumeration(s);
			}

			return retVal;
		} catch (InvocationTargetException ex) {
			throw ex.getTargetException();
		}
	}
}
