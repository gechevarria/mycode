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

import com.kudedata.rest.KudedataProxy;

public class AlfrescoConnector {
	static Log log = LogFactory.getLog(KudedataProxy.class);
	
	public static void main(String[] args) {
		try {
			File fileToUpload = new File ("/home/alfresco/KUDEDATA/DESARROLLO_NEW/MIDDLEWARE/TRANSFORMATIONS/HTML/order.html");
			String idDestinatario = "UKABI";
			//uploadFile(fileToUpload,idDestinatario);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/** sube un fichero html a alfresco (resultado de la transformacion del edi)
	 * @param fileToUpload
	 * @param idEmpresaOrigen
	 * @param idEmpresaDestinataria
	 * @param transactionId
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void uploadFile(File fileToUpload, String idEmpresaOrigen, String idEmpresaDestinataria, String transactionId) throws HttpException, IOException {	
		String accessingTicket = "";
		accessingTicket = getTicket(idEmpresaDestinataria);
		
		String url = "http://localhost:8080/alfresco/service/api/upload?alf_ticket="
				+ accessingTicket;

		HttpClient client = new HttpClient();
				
		String filetype = "text/html";
		
		String filename = fileToUpload.getName().replace(".html", "_"+idEmpresaOrigen+"-"+idEmpresaDestinataria+"_"+transactionId+".html");
						
		PostMethod mPost = new PostMethod(url);
		
		Part[] parts = {
				new FilePart("filedata", filename, fileToUpload, filetype, null),
				new StringPart("filename", filename),
				new StringPart("description", "description"),
				
				new StringPart("destination",
						"nodeRef=workspace://SpacesStore/cff5102c-a7e0-4621-a183-8122767bebb0/"),
				
				new StringPart("siteid", "KUDEDATA"),
				new StringPart("containerid", "documentLibrary"),
				new StringPart("uploaddirectory", idEmpresaDestinataria)

		};
		mPost.setRequestEntity(new MultipartRequestEntity(parts, mPost
				.getParams()));		
	}		

	/** obtiene el ticket que hace falta para acceder al api de alfresco
	 * @param idDestinatario
	 * @return
	 */
	private static String getTicket(String idDestinatario) {		
		String password = "KUDEDATA";		
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
	 * parsea el ticket desde el xml
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

	/**
	 * devuelvel el nombre de los documentos pendientes de ser procesados
	 * @param idEmpresa
	 * @return
	 */
	public static String checkIfPendingTransactions(String idEmpresa) {
		String fileName = "";
		fileName = CMISClient.getDocumentNameInFolder(idEmpresa);
		return fileName;
	}
}
