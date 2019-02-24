package net.floodlightcontroller.ipblacklist;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import org.restlet.resource.Post;
import org.restlet.resource.Get;

import org.restlet.data.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPBlacklistAddResource extends IPBlacklistResourceBase {
	
	public void method(){
		System.out.println("Entered Add Resource method!!!!!");
	}

	private static final Logger log = LoggerFactory.getLogger(IPBlacklistAddResource.class);

	@Get("json")
	public Object handleRequest() {
		System.out.println("entered add resource get method!!");
		IPBlacklistService ip = getIPBlacklistService();
		return "{\"ip-address\":\"" + ip.getIPAddress() + "\"}";
	}


	@Post
	public String handlePost(String fmJson) {
		System.out.println("entered add resource post method!!");
		IPBlacklistService ip = getIPBlacklistService();

		String newIP;
		System.out.println("I am here!!");
		try {
			newIP = jsonExtractIPAddress(fmJson);
			System.out.println("I am here here..");
		} catch (IOException e) {
			log.error("Error parsing new ip address: " + fmJson, e);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return "{\"status\" : \"Error! Could not parse new ip address, see log for details.\"}";
		}

		ip.setIPAddress(newIP);

		setStatus(Status.SUCCESS_OK);
		
		return ("{\"status\": \"ip address added\"}");
	}

	public static String jsonExtractIPAddress(String fmJson) throws IOException {
		String ip_address = "";
		MappingJsonFactory f = new MappingJsonFactory();
		JsonParser jp;

		try {
			jp = f.createParser(fmJson);
		} catch (JsonParseException e) {
			throw new IOException(e);
		}

		jp.nextToken();
		if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
			throw new IOException("Expected START_OBJECT");
		}

		while (jp.nextToken() != JsonToken.END_OBJECT) {
			if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
				throw new IOException("Expected FIELD_NAME");
			}

			String n = jp.getCurrentName();
			jp.nextToken();
			if (jp.getText().equals(""))
				continue;

			if (n == "ipaddress") {
				ip_address = jp.getText();
			
				break;
			}
		}

		return ip_address;
	}
}
