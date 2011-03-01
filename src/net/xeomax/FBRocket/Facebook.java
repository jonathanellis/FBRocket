package net.xeomax.FBRocket;

import java.util.List;
import android.content.SharedPreferences;

/**
 * The Facebook class provides access to many of the API features provided by Facebook, wrapped up
 * for easy integration with existing Android projects.
 * 
 * Before any of the methods in this class can be used, the class must be initialised with sessionKey
 * and secretKey values. These values can either be retrieved by calling login() on FBRocket which
 * launches a login window which creates a Facebook object, or by retrieving a previously-persisted
 * Facebook object.
 * 
 * @author Jonathan Ellis
 *
 */
public class Facebook {
	private FBRocket fbRocket;
	private String sessionKey;
	private String secretKey;
	private String uid;
	
    protected Facebook(FBRocket fbRocket, String sessionKey, String secretKey, String uid) {
    	this.fbRocket = fbRocket;
    	this.sessionKey = sessionKey;
    	this.secretKey = secretKey;
    	this.uid = uid;
    }
    
    public String getAPIKey() {
    	return fbRocket.getAPIKey();
    }
    
    public String getSessionKey() {
    	return sessionKey;
    }
    
    public String getSecretKey() {
    	return secretKey;
    }
    
    public String getUid() {
    	return uid;
    }
    
    /**
     * Saves this Facebook object to persistent storage for retrieval later by the load() method in the FBRocket.
     * Note that when we re-load your Facbook later, sessionIsValid() will be called on it to determine whether the session
     * is still valid or whether the session has expired (i.e. you've been logged out automatically by the Facebook servers.)
     */
    public void save() {
    	 SharedPreferences preferences = fbRocket.getActivity().getSharedPreferences(fbRocket.getAppName(), 0);
         SharedPreferences.Editor editor = preferences.edit();
         editor.putString("sessionKey", sessionKey);
         editor.putString("secretKey", secretKey);
         editor.putString("uid", uid);
         editor.commit();
    }
    
    /**
     * Makes a basic status.get request to the server to check whether the session is still valid or not. If the session
     * is valid, then we would expect to get a good response from the server, in which case the method returns true. If
     * the server reports that the user is not logged-in then clearly the session has expired and the method returns false.
     * If the server responds with some other error (i.e. other than error 102 - invalid session) then the method throws
     * a ServerErrorException.
     * @return True if the current session is valid, otherwise false.
     * @throws ServerErrorException
     */
    public boolean sessionIsValid() throws ServerErrorException {
    	try {
			getStatus();
			return true;
		} catch (ServerErrorException e) {
			if (e.notLoggedIn()) return false;
			else throw e;
		}
    }
    
    //  *****************************************************************************************************************************
	
    /**
     * Create a new note for the user.
     * @param title A title for the new note.
     * @param content Content of the new note.
     * @return The id of the newly-created note.
     * @throws ServerErrorException 
     */
    public String createNote(String title, String content) throws ServerErrorException {
		Query query = new Query(this, "notes.create");
		query.put("title", title);
		query.put("content", content);
		query.sign();
		return QueryProcessor.getRawString(query);    	
    }
    
    public String createToken() throws ServerErrorException {
		Query query = new Query(this, "auth.createToken");
		query.sign();
		return QueryProcessor.getRawString(query);
    }
    
    /**
     * Edit an existing note on the server. You need the id of the note you want to edit.
     * @param id The id of the note you want to edit
     * @param title The new title of the note
     * @param content The new content of the note
     * @return True if the operation was successful, otherwise false.
     * @throws ServerErrorException 
     */
    public boolean editNote(String id, String title, String content) throws ServerErrorException {
    	Query query = new Query(this, "notes.edit");
		query.put("note_id", id);
		query.put("title", title);
		query.put("content", content);
		query.sign();
		return QueryProcessor.getBoolean(query);
    }
    
    /**
     * Ends the current session, requiring the user to login again before being able to
     * perform any further API calls.
     * @return True if the operation succeeded, otherwise false.
     * @throws ServerErrorException 
     */
	public boolean expireSession() throws ServerErrorException {
		Query query = new Query(this, "auth.expireSession");
		query.sign();
		return QueryProcessor.getBoolean(query);
	}
	
	/**
	 * Alias for expireSession().
	 * @return True if the operation succeeded, otherwise false.
	 * @throws ServerErrorException
	 */
	public boolean logout() throws ServerErrorException {
		return expireSession();
	}
	
	/**
	 * Send an FQL (Facebook Query Language) query to the server.
	 * @param fqlQuery The query string
	 * @return The raw server response
	 * @throws ServerErrorException 
	 */
	public String fqlQuery(String fqlQuery) throws ServerErrorException {
		Query query = new Query(this, "fql.query");
        query.put("query", fqlQuery);
        query.sign();
		return QueryProcessor.getRawString(query);
	}
	
	/**
	 * Retrieves a list of the logged-in user's friends' uids. You can then use getFriend() to retrieve more detailed info.
	 * @return A list of friend uids.
	 * @throws ServerErrorException 
	 */
	public List<String> getFriendUIDs() throws ServerErrorException {
		Query query = new Query(this, "friends.get");
		query.sign();
		return QueryProcessor.getStringList(query);
	}
	
	/**
	 * Retrieves detailed information about a given uid. The user must be friends with the target uid in order for this to work. See the
	 * Javadoc for the Friend class for more detailed information as to what profile information is accessible.
	 * @param uid
	 * @return A Friend object which 
	 * @throws ServerErrorException
	 */
	public Friend getFriend(String uid) throws ServerErrorException {
		Query query = new Query(this, "fql.query");
		String fqlQuery = "SELECT uid, name, pic, profile_update_time, timezone, birthday_date, status, online_presence, locale, profile_url, website, is_blocked FROM user WHERE uid=" + uid;
        query.put("query", fqlQuery);
        query.sign();
		return QueryProcessor.getFriend(query);
	}
	
	/**
	 * Retrieves friend information for a number of uids.
	 * @param uids String array of uids.
	 * @return A list of Friends who match the given uids.
	 * @throws ServerErrorException
	 */
	public List<Friend> getFriends(String[] uids) throws ServerErrorException {
		String whereClause = "";
		for (int i=0; i<uids.length-1; i++) {
			whereClause += "uid = " + uids[i] + " OR ";
		}
		whereClause += "uid = " + uids[uids.length-1];
		
		Query query = new Query(this, "fql.query");
		String fqlQuery = "SELECT uid, name, pic, profile_update_time, timezone, birthday_date, status, online_presence, locale, profile_url, website, is_blocked FROM user WHERE " + whereClause;
        query.put("query", fqlQuery);
        query.sign();
		return QueryProcessor.getFriendList(query);
	}
	
	public String getSession() throws ServerErrorException {
		Query query = new Query(this, "auth.getSession");
		query.put("auth_token", createToken());
		// query.put("generate_session_secret", "true");
		query.sign();
		return QueryProcessor.getRawString(query);
	}
	
	/**
	 * Retrieves a given number of past status updates of the logged-in user. To retrieve only the current status,
	 * call getStatus() with no arguments. To retrieve status updates of other users, use fqlQuery(...).
	 * @param limit The number of status updates you want to retrieve.
	 * @return A List of Status objects
	 * @throws ServerErrorException
	 */
	public List<Status> getStatus(int limit) throws ServerErrorException {
		Query query = new Query(this, "status.get");
		query.put("limit", limit + "");
        query.sign();
        return QueryProcessor.getStatusList(query);
	}

	/**
	 * Retrieves the logged-in user's current status (see getStatus(...) for more info).
	 * @return The logged-in user's current status
	 * @throws ServerErrorException
	 */
	public Status getStatus() throws ServerErrorException {
		return getStatus(1).get(0);
	}
	
	/**
	 * Retrieves ALL the logged-in user's past status updates. This may be a lot - you should probably use
	 * getStatus(...) with an appropriate limit.
	 * @return A List of all past status updates.
	 * @throws ServerErrorException
	 */
	public List<Status> getAllStatus() throws ServerErrorException {
		Query query = new Query(this, "status.get");
        query.sign();
        return QueryProcessor.getStatusList(query);		
	}
	
	/**
	 * Sets the currently logged-in user's status. Remember that the user's name is always automatically prefixed.
	 * @param newStatus The new status for the user.
	 * @return true if the operation was successful, otherwise, false.
	 * @throws ServerErrorException 
	 */
	public boolean setStatus(String newStatus) throws ServerErrorException {
		Query query = new Query(this, "status.set");
        query.put("status", newStatus);
        query.sign();
        return QueryProcessor.getBoolean(query);
	}
	
	/**
	 * Publishes an attachment to the user's stream.
	 * @param message The message the user enters for the post at the time of publication.
	 * @param attachment An Attachment object to be published.
	 * @return The ID of the stream item.
	 * @throws ServerErrorException
	 */
	public String publishToStream(String message, Attachment attachment) throws ServerErrorException {
		Query query = new Query(this, "stream.publish");
		query.put("message", message);
		query.put("attachment", attachment + "");
		query.sign();
        return QueryProcessor.getRawString(query);
	}
	
}
