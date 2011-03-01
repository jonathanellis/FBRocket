package net.xeomax.FBRocket;

/**
 * Represents a Facebook status update.
 * @author Jonathan Ellis
 *
 */
public class Status {
	public String message;
	public String time;
	public String status_id;
	
	public Status(String message, String time, String status_id) {
		this.message = message;
		this.time = time;
		this.status_id = status_id;
	}
	
	public String toString() {
		return message;
	}
}