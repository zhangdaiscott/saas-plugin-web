package com.buss.demo.action;


import org.apache.log4j.Logger;
import org.core.mframework.system.AbstractAction;
import org.core.mframework.system.VelocityViewHelper;


public class DemoAction extends AbstractAction {

	private static Logger log = Logger.getLogger(DemoAction.class);

	public DemoAction() {
		
	}
	public Object execute() throws Exception {
		return VelocityViewHelper.mergeVelocity("demo/demo.vm");
	}
	
	public Object demo() throws Exception {
		return VelocityViewHelper.mergeVelocity("demo/demo.vm");
	}
}
