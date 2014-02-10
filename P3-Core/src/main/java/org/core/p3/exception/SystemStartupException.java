package org.core.p3.exception;

/**
 * 系统启动时异常
 * 
 * @author admin
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class SystemStartupException extends RuntimeException {
	
	private static final long serialVersionUID = -3293734664148113053L;

	public SystemStartupException() {
	}

	public SystemStartupException(String message) {
		super(message);
	}

	public SystemStartupException(Throwable cause) {
		super(cause);
	}

	public SystemStartupException(String message, Throwable cause) {
		super(message, cause);
	}

}
