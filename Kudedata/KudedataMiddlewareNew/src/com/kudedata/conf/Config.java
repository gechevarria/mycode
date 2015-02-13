package com.kudedata.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 106800
 * clase que contiene una serie de propiedades est�ticas recuperadas de un fichero de configuraci�n.
 */
public class Config {	
	public static String VALIDATION_PENDING_RECEIVED_EDI_FOLDER = null;
	public static String PENDING_TO_BE_SENT_EDI_FILES = null;
	public static String ALREADY_SENT_EDI_FILES = null;
	public static final String ORDER_EDI_ID = "EAN008";
	public static final String INVOIC_EDI_ID = "EAN011";
	public static final String RECADV_EDI_ID = "EAN005";
	public static final String DESADV_EDI_ID = "EAN007";
	public static String HTML_FILES_FOLDER = null;
		
	
	private final static Logger LOGGER = Logger.getLogger(Config.class .getName());
	static {
		LOGGER.setLevel(Level.INFO);
	}

	public static void init() {
		Properties prop = new Properties();
		InputStream input = null;
		String OS = System.getProperty("os.name");
		
		String configPath= "";
		if (OS.contains("Wind"))
			configPath = "D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/KudedataMiddleware/src/com/kudedata/conf/config.properties";
		else
			configPath = "/home/alfresco/workspace/KudedataMiddlewareNew/src/com/kudedata/conf/config-linux.properties";
		try {
		
		//String configFilePath= "D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/KudedataMiddleware/src/com/kudedata/conf/config.properties";
		//se lee el fichero de propiedades 
		input = new FileInputStream(configPath);	 			
		prop.load(input);		
		VALIDATION_PENDING_RECEIVED_EDI_FOLDER = prop.getProperty("VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER");		
		PENDING_TO_BE_SENT_EDI_FILES = prop.getProperty("PENDING_TO_BE_SENT_EDI_FILES");
		ALREADY_SENT_EDI_FILES = prop.getProperty("ALREADY_SENT_EDI_FILES");
		HTML_FILES_FOLDER = prop.getProperty("HTML_FILES_FOLDER");
		
		} catch (IOException ex) {
			LOGGER.info("No se puede leer el fichero de configuraci�n."+ex.getLocalizedMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}	

}


