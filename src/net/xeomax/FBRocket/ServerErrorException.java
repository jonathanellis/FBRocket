package net.xeomax.FBRocket;

/**
 * Thrown on errors reported by the Facebook servers.
 * @author Jonathan Ellis
 *
 */
public class ServerErrorException extends Exception {
	private static final long serialVersionUID = -2868130651500796509L;
	private String errCode;
	private String errMessage;
	
	public ServerErrorException(String errCode, String errMessage) {
		super(errMessage);
		this.errCode = errCode;
		this.errMessage = errMessage;
	}
	
	/**
	 * @return The actual error code from the Facebook servers.
	 */
	public String getErrCode() {
		return errCode;
	}
	
	/**
	 * @return The actual error message from the Facebook servers.
	 */
	public String getErrMessage() {
		return errMessage;
	}
	
	/**
	 * Determines whether the cause of the exception was not being logged-in (i.e. the session has expired). A good way of using the code would
	 * be like this: <br />
	 * <code>
	 * try {<br />
	 * facebook.setStatus("hello");<br />
	 * } catch (ServerErrorException e) {<br />
	 * if (e.notLoggedIn()) {<br />
	 * fbRocket.login();<br />
	 * } else {<br />
	 * do_something_else();<br />
	 * }<br />
	 * }<br />
	 * </code>
	 * @return True if the exception is caused by you not being logged in.
	 */
	public boolean notLoggedIn() {
		if (errCode.equals("102")) return true;
		return false;
	}
	
	public String toString() {
		return "Facebook Server Error + " + errCode + " - " +  errMessage;
	}
}
