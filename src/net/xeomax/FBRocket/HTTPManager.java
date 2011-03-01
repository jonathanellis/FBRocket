package net.xeomax.FBRocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

class HTTPManager {
	
	public static String getResponse(Query query) {
		return getResponse("http://api.facebook.com/restserver.php", query);
	}
	
	public static String getResponse(String urlString, Query query) {
		try {
			URL url = new URL(urlString);
	        URLConnection conn = url.openConnection();
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());
	        outputStream.write(query.toString());
	        outputStream.flush();
	        outputStream.close();
	        
			InputStream inputStream = conn.getInputStream();
			StringBuffer sb = new StringBuffer();
			int b = inputStream.read();
			while (b != -1) {
				char c = (char) b;
				sb.append(c);
				b = inputStream.read();
			}
			
			return sb.toString();
			
	        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
