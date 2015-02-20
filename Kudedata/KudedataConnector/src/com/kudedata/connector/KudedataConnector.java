package com.kudedata.connector;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
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
 * @author 106800 clase java conector a desplegar en cada empresa para
 *         comunciarse con el middleware KUDEDATA Realiza dos funciones
 *         principalmente recursivamente (daemons): 1. Consulta si hay mensajes
 *         EDI a enviar mediante el acceso a una carpeta en la que se ir�n
 *         dejando los mensajes pendientes. En cuyo caso los env�a. 2. Consulta
 *         si hay mensajes en el middleware para la empresa aprobados, en cuyo
 *         caso los recoje y los deja en una carpeta local para su
 *         procesamiento.
 */
public class KudedataConnector {
	protected static WebResource webResource;
	private static Client client;
	private static ClientConfig config;

	private final static Logger LOGGER = Logger
			.getLogger(KudedataConnector.class.getName());
	static {
		LOGGER.setLevel(Level.INFO);
	}

	/**
	 * Crea el cliente que consume webservices restful
	 */
	public static void init() {		
		config = new DefaultClientConfig();
		config.getProperties()
				.put(com.sun.jersey.client.urlconnection.HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
						new com.sun.jersey.client.urlconnection.HTTPSProperties(
								getHostnameVerifier(), getSSLContext()));
		client = Client.create(config);
		webResource = client.resource(getBaseURI());

	}

	public static void main(String[] args) {

		KudedataConnector.init();
		if (Config.CONFIG_INITIALIZED == false)
			Config.init();
		//arranque del hilo que comprueba si hay mensajes edi y los envía
		Thread checkAndSendEDIThread = new Thread(new CheckAndSendEDIThread(),
				"checkAndSendEDIThread");
		checkAndSendEDIThread.start();
		//arranque del hilo que comprueba si hay mensajes edi en el middleware para esta empresa y los recibe
		Thread checkAndReceiveEDIThread = new Thread(new
		CheckAndReceiveEDIThread(), "checkAndReceiveEDIThread");
		checkAndReceiveEDIThread.start();

	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://localhost:8444/kudedata/message/")
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
			LOGGER.info("Hay un problema con SSL" + e1.getLocalizedMessage());
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
			LOGGER.info("Hay un problema con SSL" + e.getLocalizedMessage());
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
