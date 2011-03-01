package net.xeomax.TestRocket;

import net.xeomax.FBRocket.FBRocket;
import net.xeomax.FBRocket.Facebook;
import net.xeomax.FBRocket.LoginListener;
import net.xeomax.FBRocket.ServerErrorException;
import android.app.Activity;
import android.os.Bundle;

public class TestRocket extends Activity implements LoginListener {
	
	private FBRocket fbRocket;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // You need to put in your Facebook API key here:
        fbRocket = new FBRocket(this, "RocketTest", "YOUR_API_KEY_GOES_HERE");
        
        // Determine whether there exists a previously-saved Facebook:
        if (fbRocket.existsSavedFacebook()) {
 			fbRocket.loadFacebook();
         } else {
 			fbRocket.login(R.layout.main);
 		}
        
    }

	@Override
	public void onLoginFail() {
		fbRocket.displayToast("Login failed!");
		fbRocket.login(R.layout.main);
	}

	@Override
	public void onLoginSuccess(Facebook facebook) {
		fbRocket.displayToast("Login success!");
		
		// Set the logged-in user's status:
		try {
			
			facebook.setStatus("I am using FBRocket -- it's great!");
			
			String uid = facebook.getFriendUIDs().get(0); // Just get the uid of the first friend returned...
			fbRocket.displayDialog("Friend's name: " + facebook.getFriend(uid).name); // ... and retrieve this friend's name.
			
		} catch (ServerErrorException e) {
			// Check if the exception was caused by not being logged-in:
			if (e.notLoggedIn()) {
				// ...if it was, then login again:
				fbRocket.login(R.layout.main);
			} else {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
}