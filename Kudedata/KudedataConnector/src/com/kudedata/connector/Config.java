package com.kudedata.connector;

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
	protected static long CHECK_TOSEND_TIMEOUT_IN_MILLISECONDS;
	protected static long CHECK_TORECEIVE_TIMEOUT_IN_MILLISECONDS;	
	protected static String EDI_TOSEND_FOLDER = null;
	protected static String EDI_TORECEIVE_FOLDER = null;
	protected static String EDI_ALREADY_SENT_FOLDER = null;		
	public static final String ORDER_EDI_ID = "EAN008";
	public static final String INVOIC_EDI_ID = "EAN011";
	public static final String RECADV_EDI_ID = "EAN005";
	public static final String DESADV_EDI_ID = "EAN007";
	protected static String ENTERPRISE_ID = null;
	protected static boolean CONFIG_INITIALIZED = false;	
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
			configPath = "D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/KudedataConnectorNew/src/com/kudedata/connector/config.properties";
		else
			configPath = "/home/alfresco/workspace/KudedataConnectorNew/src/com/kudedata/connector/config-linux.properties";
		try {
		
			CONFIG_INITIALIZED=true;
		//se lee el fichero de propiedades 
		input = new FileInputStream(configPath);	 			
		prop.load(input);		
		CHECK_TOSEND_TIMEOUT_IN_MILLISECONDS = new Long(prop.getProperty("CHECKTOSENDTIMEOUTINMILLISECONDS")).longValue();
		CHECK_TORECEIVE_TIMEOUT_IN_MILLISECONDS = new Long(prop.getProperty("CHECKTORECEIVETIMEOUTINMILLISECONDS")).longValue();
		EDI_TOSEND_FOLDER = prop.getProperty("EDITOSENDFOLDER");
		EDI_ALREADY_SENT_FOLDER = prop.getProperty("EDIALREADYSENTFOLDER");
		EDI_TORECEIVE_FOLDER = prop.getProperty("EDITORECEIVEFOLDER");		
		ENTERPRISE_ID = prop.getProperty("ENTERPRISEID");
		
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


