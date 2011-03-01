package net.xeomax.FBRocket;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ServerArray extends JSONArray {
	
	public ServerArray(String string) throws JSONException {
		super(string);
	}
	
	public Status getStatus(int index) throws JSONException {
		if (QueryProcessor.getJSONType(get(index).toString()) == QueryProcessor.JSONType.OBJECT) {
			JSONObject statusObject = getJSONObject(index);
			String message = statusObject.get("message").toString();
			String time = statusObject.get("time").toString();
			String status_id = statusObject.get("status_id").toString();
			return new Status(message, time, status_id);
		}
		return new Status("","","");
	}
	
	public Friend getFriend(int index) throws JSONException {
		ServerObject friendObject = getServerObject(index);
		String uid = friendObject.getString("uid");
		String name = friendObject.getString("name");
		String pic = friendObject.getString("pic").replaceAll("\\\\", "");
		String profile_update_time = friendObject.getString("profile_update_time");
		String timezone = friendObject.getString("timezone");
		String birthday_date = friendObject.getString("birthday_date");
		Status status = friendObject.getStatus("status");
		String online_presence = friendObject.getString("online_presence");
		String locale = friendObject.getString("locale");
		String profile_url = friendObject.getString("profile_url");
		String website = friendObject.getString("website");
		String is_blocked = friendObject.getString("is_blocked");
		return new Friend(uid, name, pic, profile_update_time, timezone, birthday_date, status, online_presence, locale, profile_url, website, is_blocked);
	}
	
	public ServerObject getServerObject(int index) throws JSONException {
		String string = getString(index);
		return new ServerObject(string);
	}
	
	public List<String> toList() {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<length(); i++) {
			try {
				list.add(get(i).toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}