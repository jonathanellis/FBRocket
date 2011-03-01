package net.xeomax.FBRocket;

import org.json.JSONException;
import org.json.JSONObject;

class ServerObject extends JSONObject {
	
	public ServerObject(String string) throws JSONException {
		super(string);
	}
	
	public Status getStatus(String key) throws JSONException {
		if (QueryProcessor.getJSONType(get(key).toString()) == QueryProcessor.JSONType.OBJECT) {
			JSONObject statusObject = getJSONObject(key);
			String message = statusObject.get("message").toString();
			String time = statusObject.get("time").toString();
			String status_id = statusObject.get("status_id").toString();
			return new Status(message, time, status_id);
		}
		return new Status("","","");
	}
}