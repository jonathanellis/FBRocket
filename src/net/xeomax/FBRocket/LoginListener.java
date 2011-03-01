package net.xeomax.FBRocket;

/**
 * Handles callbacks from the login handler. You must have some class which implements the LoginListener interface,
 * then pass a reference to this class when calling login() or load(). If you don't pass this as an argument,
 * then we assume that your Activity class passed in the FBRocket constructor implements LoginListener. If this is
 * not the case, you'll end up with a CastException.
 * @author Jonathan Ellis
 *
 */
public interface LoginListener {
	/**
	 * If the login succeeds, then the relevant login handler calls onLoginSuccess().
	 * @param facebook A reference to a (new) Facebook object on which you can execute Facebook queries.
	 */
	public void onLoginSuccess(Facebook facebook);
	
	/**
	 * If the login fails, then the relevant login handler calls onLoginFail().
	 */
	public void onLoginFail();
}