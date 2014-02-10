package com.buss.main.action;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.core.p3.helper.FreemarkerViewHelper;
import org.core.p3.helper.VelocityHelper;
import org.core.p3.web.process.AbstractAction;

public class CoreAction extends AbstractAction {
	public Object execute() throws ResourceNotFoundException,
			ParseErrorException, MethodInvocationException, Exception {
		return VelocityHelper.merge("cmd/index.vm", null);
	}

	public Object toFreemarker() throws ResourceNotFoundException,
			ParseErrorException, MethodInvocationException, Exception {
		return FreemarkerViewHelper.merge("cmd/dd/demo.ftl", null);
	}
	
	public Object toBootstrap() throws ResourceNotFoundException,
			ParseErrorException, MethodInvocationException, Exception {
		return FreemarkerViewHelper.merge("cmd/bootstrap.vm", null);
	}
}
