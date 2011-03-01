package net.xeomax.FBRocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * You need to create an FBRocket object before doing anything else. From the FBRocket you can then initiate a call
 * to login() which uses the Facebook Connect system to authenticate the user.
 * 
 * You will require an API key from Facebook, please consult http://developers.facebook.com/get_started.php for information
 * on how to do this.
 * 
 * @author Jonathan Ellis
 *
 */
public class FBRocket {
	protected final static String TAG = "FBRocket";
	private String apiKey;
	private String appName;
	private Activity activity;

	/**
	 * Initialises a new FBRocket ready to handle login/load operations.
	 * @param activity A reference to your application's Activity class.
	 * @param appName The name of your application.
	 * @param apiKey Your API key as supplied to you by Facebook. (Not your secret API key).
	 */
	public FBRocket(Activity activity, String appName, String apiKey) {
		this.activity = activity;
		this.appName = appName;
		this.apiKey = apiKey;
	}
    
	/**
	 * Commences the login process by creating a browser control which handles the login via Facebook Connect.
	 * Note that even when calling login(), the window may simply appear and disappear again without user input - this
	 * just means that there was a cookie stored from a previously successful login.<br />
	 * Many developers will be understandably frustrated by the use of this method, as it requires users to leave the
	 * environment of their program and load a web-page, etc. in order to authenticate their Facebook account. Unfortunately
	 * at this point there exists no other method of authenticating with the Facebook servers. In order to minimise the
	 * number of times login() needs to be called, use the save() method on an existing Facebook object and then check
	 * whether it exists the next time the program runs and load it if the session is still valid.
	 * @param callback The callback class for handling login success/fail; must implement LoginListener.
	 * @param returnWindow The resource ID of the window you want to return to once the (e.g. R.layout.main).
	 */
    public void login(LoginListener callback, int returnWindow) {
    	new LoginWindow(this, callback, returnWindow);
    }
    
    /**
     * Commences the login process by creating a browser control which handles the login via Facebook Connect. Assumes
     * that the Activity reference passed in the FBRocket implements LoginListener.
     * @param returnWindow The resource ID of the window you want to return to once the (e.g. R.layout.main).
     */
    public void login(int returnWindow) {
    	new LoginWindow(this, (LoginListener) activity, returnWindow);
    }
    
    
    protected Activity getActivity() {
    	return activity;
    	
    }
    
    public String getAPIKey() {
    	return apiKey;
    }
    
    public String getAppName() {
    	return appName;
    }
    
    public void displayDialog(String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
        dlgAlert.setMessage(message);
        dlgAlert.create().show();
    }
    
    public void displayToast(String message) {
        Toast t = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        t.show();
    }
    
    /**
     * Loads a previously-saved Facebook from persistent storage (experimental feature) and checks whether the
     * session is still valid. If it is, it does a callback of onLoginSuccess, and if not, onLoginFail (the
     * same behaviour as the login() method).
     * @param callback The callback class for handling login success/fail; must implement LoginListener.
     */
    public void loadFacebook(LoginListener callback) {
    	SharedPreferences preferences = activity.getSharedPreferences(appName, 0);
    	String sessionKey = preferences.getString("sessionKey", "");
    	String secretKey = preferences.getString("secretKey", "");
    	String uid = preferences.getString("uid", "");
    	Facebook facebook = new Facebook(this, sessionKey, secretKey, uid);
    	try {
			if (facebook.sessionIsValid()) {
				callback.onLoginSuccess(facebook);
			} else {
				callback.onLoginFail();
			}
		} catch (ServerErrorException e) {
			e.printStackTrace();
			callback.onLoginFail();
		}
    }
    
    /**
     * Loads a previously-saved Facebook from persistent storage and assumes that the Activity passed in the FBRocket
     * constructor implements LoginListener.
     */
    public void loadFacebook() {
    	loadFacebook((LoginListener) activity);
    }
    
    /**
     * Determines whether there exists a previously-saved Facebook object (which is saved by calling save() on any
     * Facebook object).
     * @return True if there exists a previously-saved Facebook object, otherwise false.
     */
    public boolean existsSavedFacebook() {
    	SharedPreferences preferences = activity.getSharedPreferences(appName, 0);
    	return !preferences.getString("sessionKey", "").equals("");
    }
    
    public static void main(String[] args) {
    	System.out.println("FBRocket is a library designed for the Android platform and is not usable as a stand-alone product.\nSee http://www.xeomax.net/fbrocket for further information. \nThe program will now terminate.");
    }
    
}