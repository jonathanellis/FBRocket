package net.xeomax.FBRocket;

import org.json.JSONException;
import org.json.JSONObject;

public class Attachment extends JSONObject {
	
	/**
	 * Creates a new Attachment that can be posted to a user's wall with the Facebook publishToStream() method. This support is quite basic at the moment and will be improved in future releases. There is also no support for media objects (e.g. mp3/flash) yet.
	 * @param name The title of the post. The post should fit on one line in a user's stream; make sure you account for the width of any thumbnail.
	 * @param href The URL to the source of the post referenced in the name. The URL should not be longer than 1024 characters.
	 * @param caption A subtitle for the post that should describe why the user posted the item or the action the user took. This field can contain plain text only, as well as the {*actor*} token, which gets replaced by a link to the profile of the session user. The caption should fit on one line in a user's stream; make sure you account for the width of any thumbnail.
	 * @param description Descriptive text about the story. This field can contain plain text only and should be no longer than is necessary for a reader to understand the story.
	 */
	public Attachment(String name, String href, String caption, String description) {
		try {
			put("name", name);
			put("href", href);
			put("caption", caption);
			put("description", description);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
