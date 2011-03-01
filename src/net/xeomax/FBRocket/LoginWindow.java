package net.xeomax.FBRocket;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class LoginWindow {
	private FBRocket fbRocket;
	private LoginListener callback;
	private int returnWindow;
	private WebView wv;

	public LoginWindow(FBRocket fbRocket, LoginListener callback, int returnWindow) {
		this.fbRocket = fbRocket;
		this.callback = callback;
		this.returnWindow = returnWindow;
		
       	wv = new WebView(fbRocket.getActivity());
       	fbRocket.getActivity().setContentView(wv);
       	wv.getSettings().setAllowFileAccess(true);
       	wv.getSettings().setJavaScriptEnabled(true);
       	wv.setWebViewClient(new CustomWebViewClient());
 
       	wv.loadUrl("http://m.facebook.com/login.php?api_key=" + fbRocket.getAPIKey() + "&connect_display=popup&v=1.0&next=http://www.facebook.com/connect/login_success.html&cancel_url=http://www.facebook.com/connect/login_failure.html&fbconnect=true&return_session=true&req_perms=read_stream,publish_stream,offline_access"); 
	}
	
	public static HashMap<String, String> parseQueryString(String url) {
		HashMap<String,String> values = new HashMap<String, String>();
		if (url.contains("?")) {
			String queryString = url.split("\\?")[1];
			String[] kvPairs = queryString.split("&");
			for (String kvPair : kvPairs) {
				String[] kvSplit = kvPair.split("=");
				values.put(kvSplit[0], kvSplit[1]);
			}
		}
		return values;
	}
	
	public void deregisterWV() {
		wv.setWebViewClient(new WebViewClient());
	}
	
	public static String getURLOnly(String url) {
		return url.split("\\?")[0];
	}

    private class CustomWebViewClient extends WebViewClient {

       /* public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        } */
        
        public void onPageFinished(WebView view, String url) {
	        System.out.println("Nav to: " + url);
        	if (getURLOnly(url).equals("http://www.facebook.com/connect/login_failure.html")) {
        		callback.onLoginFail();
	           	fbRocket.getActivity().setContentView(returnWindow);
        	}
        	else if (getURLOnly(url).equals("http://www.facebook.com/connect/login_success.html")) {
        		deregisterWV();
  	           	HashMap<String,String> kvPairs = parseQueryString(url);
  	           	// for old authentication:
  	           	if (kvPairs.containsKey("session")) {
		        	String sessionJSONString = Uri.decode(kvPairs.get("session"));
		        	try {
						JSONObject sessionJSON = new JSONObject(sessionJSONString);
			        	String sessionKey = sessionJSON.get("session_key").toString();
			        	String secretKey = sessionJSON.get("secret").toString();
			        	String uid = sessionJSON.get("uid").toString();
			        	Facebook facebook = new Facebook(fbRocket, sessionKey, secretKey, uid);
			        	callback.onLoginSuccess(facebook);
			           	fbRocket.getActivity().setContentView(returnWindow);
					} catch (JSONException e) {
						e.printStackTrace();
					}
  	           	}
  	           	else if (kvPairs.containsKey("auth_token")) {
  	           		
  	           	}
        	}
        }
    }
}