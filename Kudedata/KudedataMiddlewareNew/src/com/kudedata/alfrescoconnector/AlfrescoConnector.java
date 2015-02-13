package com.kudedata.alfrescoconnector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kudedata.conf.Config;
import com.kudedata.rest.KudedataProxy;

public class AlfrescoConnector {
	static Log log = LogFactory.getLog(KudedataProxy.class);
	
	public static void uploadFile(File fileToUpload, String idDestinatario) throws HttpException, IOException {
		
		String accessingTicket = "";
		accessingTicket = getTicket(idDestinatario);
		log.info("KUDEDATA-TRAZA.uploadFile1 accessingTicket="+accessingTicket);
		String url = "http://localhost:8080/alfresco/service/api/upload?alf_ticket="
				+ accessingTicket;

		HttpClient client = new HttpClient();
		

		String filetype = "text/html";

		String filename = fileToUpload.getName();
		log.info("KUDEDATA-TRAZA.uploadFile2 filename="+filename);
		PostMethod mPost = new PostMethod(url);
		
		Part[] parts = {
				new FilePart("filedata", filename, fileToUpload, filetype, null),
				new StringPart("filename", filename),
				new StringPart("description", "description"),
				
				new StringPart("destination",
						"nodeRef=workspace://SpacesStore/cff5102c-a7e0-4621-a183-8122767bebb0/"),
				
				new StringPart("siteid", "KUDEDATA"),
				new StringPart("containerid", "documentLibrary"),
				new StringPart("uploaddirectory", idDestinatario)

		};
		mPost.setRequestEntity(new MultipartRequestEntity(parts, mPost
				.getParams()));
		int statusCode1 = client.executeMethod(mPost);

	}

	private static String getTicket(String idDestinatario) {
		
		String password = "KUDEDATA";
		
		/*String name = "admin";
		String password = "admin";*/

		// First we need to pre-auth and get an authentication ticket
		//String authURL = "http://localhost:8080/alfresco/service/api/login?u=admin&pw=admin";
		String authURL = "http://localhost:8080/alfresco/service/api/login?u="+idDestinatario+"&pw="+password;

		ByteArrayOutputStream bais = null;
		InputStream is = null;
		byte[] xmlTicketByteArray = null;
		String xmlTicketString = null;
		String ticket = null;

		try {
			bais = new ByteArrayOutputStream();
			URL url = new URL(authURL);
			URLConnection urlConnection = url.openConnection();

			is = urlConnection.getInputStream();
			byte[] byteChunk = new byte[512];
			int n;

			while ((n = is.read(byteChunk)) > 0) {
				bais.write(byteChunk, 0, n);
			}

			xmlTicketByteArray = bais.toByteArray();
			xmlTicketString = new String(xmlTicketByteArray, "UTF-8");
		} catch (Throwable t) {

		}

		ticket = getAuthenticationTicket(xmlTicketString);
		return ticket;
	}

	/**
	 * Parses out the authentication ticket from the XML.
	 * 
	 * @param anXMLAuthTicket
	 * @return
	 */
	private static String getAuthenticationTicket(String anXMLAuthTicket) {

		String extractPattern = "(?s)^.*?<ticket>(.*?)</ticket>";
		String authString = null;

		Matcher m = Pattern.compile(extractPattern).matcher(anXMLAuthTicket);

		if (m.find()) {
			authString = m.group(1);
		}
		return authString;
	}
}
