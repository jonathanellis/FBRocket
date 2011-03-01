package net.xeomax.FBRocket;

/**
 * Represents a friend on Facebook.
 * @author Jonathan Ellis
 *
 */
public class Friend {
	public String uid;
	public String name;
	public String pic;
	public String profile_update_time;
	public String timezone;
	public String birthday_date;
	public Status status;
	public String online_presence;
	public String locale;
	public String profile_url;
	public String website;
	public String is_blocked;
	
	public Friend(String uid, String name, String pic, String profile_update_time, String timezone, String birthday_date, Status status, String online_presence, String locale, String profile_url, String website, String is_blocked) {
		this.uid = uid;
		this.name = name;
		this.pic = pic;
		this.profile_update_time = profile_update_time;
		this.timezone = timezone;
		this.birthday_date = birthday_date;
		this.status = status;
		this.online_presence = online_presence;
		this.locale = locale;
		this.profile_url = profile_url;
		this.website = website;
		this.is_blocked = is_blocked;
	}
}
