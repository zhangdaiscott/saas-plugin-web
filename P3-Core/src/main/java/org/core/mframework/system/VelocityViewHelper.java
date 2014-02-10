package org.core.mframework.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.core.p3.helper.SpringHelper;
import org.core.p3.helper.SynchronizationHelper;
import org.core.p3.helper.VelocityHelper;

/**
 * 
 * @see org.rzzl.P3Dispatcher
 * @author adminopen.org
 * @see org.rzzl.t3.helper.CostomConfigHelper
 * 
 */
public class VelocityViewHelper {


	/**
	 * 
	 * @param template
	 * @param velocityContext
	 * @return String
	 */
	public static String mergeVelocity(String template) throws ResourceNotFoundException,
			ParseErrorException, MethodInvocationException, Exception {
		return VelocityHelper.merge(template, new VelocityContext());
	}
	
	/**
	 * 
	 * @param template
	 * @param velocityContext
	 * @return String
	 */
	public static String mergeVelocity(String template,
			VelocityContext velocityContext) throws ResourceNotFoundException,
			ParseErrorException, MethodInvocationException, Exception {
		return VelocityHelper.merge(template, velocityContext);
	}


	/**
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getCurrentRequest() {
		return SynchronizationHelper.getCurrentRequest();
	}


	/**
	 * 
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getCurrentResponse() {
		return SynchronizationHelper.getCurrentResponse();
	}


	/**
	 * 
	 * @param name
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T)SpringHelper.getBean(name);
	}

}
