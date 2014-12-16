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

public class AlfrescoConnector {

	public static void main(String[] args) {
		try {
			//addContent();
			uploadFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

private static void uploadFile() throws HttpException, IOException {
	//URL: http://localhost:8080/alfresco/service/api/upload/?alf_ticket = {User's ticket}
	String accessingTicket = "";
	accessingTicket = getTicket();
	
	String url = "http://localhost:8080/alfresco/service/api/upload?alf_ticket="+getTicket();
		 
	
	HttpClient client = new HttpClient();
	
	
	//NEW
	 File file = new File("/home/cloudlab/order.html");

		String filetype = "text/html";

		String filename="order.html";

	PostMethod mPost = new PostMethod(url);
	// File f1 =fileobj;
	Part[] parts = {
	new FilePart("filedata", filename, file, filetype, null),
	new StringPart("filename", filename),
	new StringPart("description", "description"),
	//new StringPart("destination", "workspace:SpacesStore/cff5102c-a7e0-4621-a183-8122767bebb0/"),
	new StringPart("destination", "nodeRef=workspace://SpacesStore/cff5102c-a7e0-4621-a183-8122767bebb0/"),
	//workspace://SpacesStore/6eabdd9c-0489-4d71-8c43-c8ed4c3c6c85
	//new StringPart("description", description),

	//modify this according to where yougme wanna put your content
	new StringPart("siteid", "KUDEDATA"),
	new StringPart("containerid", "documentLibrary"),
	new StringPart("uploaddirectory", "UKABI")
	// esto no funciona new StringPart("uploaddirectory", "/UKABI/")
	// esto no funciona new StringPart("uploaddirectory", "/UKABI")
	// esto no funciona new StringPart("uploaddirectory", "UKABI/")
	};
	mPost.setRequestEntity(new MultipartRequestEntity(parts, mPost
	.getParams()));
	int statusCode1 = client.executeMethod(mPost);
	
	//OLD
	
	

	  /* File file = new File("/home/cloudlab/order.html");

		String filetype = "text/html";

		String filename="order.html";

		 

		HttpClient client = new HttpClient();

		
		PostMethod post = new PostMethod(url);

		Part[] parts = {

			(Part) new FilePart("filedata", filename, file, filetype, null),

			(Part) new StringPart("filename", filename),

			(Part) new StringPart("description", "description"),

			(Part) new StringPart("siteid", "KUDEDATA")

			//(Part) new StringPart("containerid", "UKABI"),

			// your can add more paramter here

			//new StringPart("uploaddirectory", "documentLibrary"),

			//new StringPart("updatenoderef", "updatenoderef"),

			//new StringPart("contenttype", "contenttype"),

			//new StringPart("aspects", "aspects")
			
			};

		post.setRequestEntity(new MultipartRequestEntity((org.apache.commons.httpclient.methods.multipart.Part[]) parts, post.getParams()));

		int status = client.executeMethod(post);

		System.out.println(post.getResponseBodyAsString());*/

		//post.releaseConnection();
		
	}



private static String getTicket() {
	String name = "admin";
    String password = "admin";

    //First we need to pre-auth and get an authentication ticket
    String authURL = "http://localhost:8080/alfresco/service/api/login?u=admin&pw=admin";

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
  * @param anXMLAuthTicket
  * @return
  */
 private static String getAuthenticationTicket(String anXMLAuthTicket){

    String extractPattern = "(?s)^.*?<ticket>(.*?)</ticket>";
    String authString = null;

    Matcher m = Pattern.compile(extractPattern).matcher(anXMLAuthTicket);

    if (m.find()) {
       authString = m.group(1);
    }
    return authString;
 }
}
 