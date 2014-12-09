package com.kudedata.connector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author 106800
 * clase java conector a desplegar en cada empresa para comunciarse con el middleware KUDEDATA
 * Realiza dos funciones principalmente recursivamente (daemons):
 * 1. Consulta si hay mensajes EDI a enviar mediante el acceso a una carpeta en la que se irán dejando los mensajes pendientes. En cuyo caso
 * los envía.
 * 2. Consulta si hay mensajes en el middleware para la empresa aprobados, en cuyo caso los recoge y los deja en una carpeta local para su procesamiento.   
 */
public class KudedataConnector {
	protected static WebResource webResource;
	private static Client client;
	private static ClientConfig config;
	protected static long CHECKTOSENDTIMEOUTINMILLISECONDS;
	protected static long CHECKTORECEIVETIMEOUTINMILLISECONDS;
	protected static String EDITOSENDFOLDER;
	protected static String EDITORECEIVEFOLDER;
	protected static String EDIALREADYSENTFOLDER;
	protected static String ENTERPRISEID;
	
	
	private final static Logger LOGGER = Logger.getLogger(KudedataConnector.class .getName());
	static {
		LOGGER.setLevel(Level.INFO);
	}

	public static void init(String configFilePath) {
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
		//se lee el fichero de propiedades 
		input = new FileInputStream(configFilePath);	 			
		prop.load(input);
		CHECKTOSENDTIMEOUTINMILLISECONDS = new Long(prop.getProperty("CHECKTOSENDTIMEOUTINMILLISECONDS")).longValue();
		CHECKTORECEIVETIMEOUTINMILLISECONDS = new Long(prop.getProperty("CHECKTORECEIVETIMEOUTINMILLISECONDS")).longValue();
		EDITOSENDFOLDER = prop.getProperty("EDITOSENDFOLDER");
		EDITORECEIVEFOLDER = prop.getProperty("EDITORECEIVEFOLDER");
		EDIALREADYSENTFOLDER = prop.getProperty("EDIALREADYSENTFOLDER");
		ENTERPRISEID = prop.getProperty("ENTERPRISEID");
	 
		} catch (IOException ex) {
			LOGGER.info("No se puede leer el fichero de configuración."+ex.getLocalizedMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Create Client (consumes WebService using Restful
		config = new DefaultClientConfig();
		config.getProperties()
				.put(com.sun.jersey.client.urlconnection.HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
						new com.sun.jersey.client.urlconnection.HTTPSProperties(
								getHostnameVerifier(), getSSLContext()));
		client = Client.create(config);
		webResource = client.resource(getBaseURI());

	}
	
	

	public static void main(String[] args) {
		
		String configPath= "D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/KudedataConnector/src/com/kudedata/connector/config.properties";
		init(configPath);
		
			/*Thread checkAndSendEDIThread = new Thread(new CheckAndSendEDIThread(), "checkAndSendEDIThread");      
		checkAndSendEDIThread.start();*/
		
		Thread checkAndReceiveEDIThread = new Thread(new CheckAndReceiveEDIThread(), "checkAndReceiveEDIThread");      
		checkAndReceiveEDIThread.start();
		
		/*String fileName = "D:/Projects/KUDETADA/EDI/messages/orders1.edi";
		InputStream fileInStream = null;
		try {
			fileInStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sContentDisposition = "attachment; filename=\"" + fileName
				+ "\"";

		ClientResponse response = webResource.path("webservice").path("test")
				.type(MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", sContentDisposition)
				.post(ClientResponse.class, fileInStream);

		try {

			readResponse(response);

		} catch (Exception e) {
		}*/
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://localhost:8443/kudedata/message/")
				.build();
	}
	
	
	private static HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {

			@Override
			public boolean verify(String hostname,
					javax.net.ssl.SSLSession sslSession) {
				return true;
			}
		};
	}

	private static SSLContext getSSLContext() {
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.info("Hay un problema con SSL"+e1.getLocalizedMessage());
		}
		
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}

		} };
		try {
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			LOGGER.info("Hay un problema con SSL"+e.getLocalizedMessage());	
		}
		return sc;
		
	}

	public static void destroyClient() {
		client.destroy();				
	}


	public static void createClient() {
		client = Client.create(config);
		webResource = client.resource(getBaseURI());		
	}

}


