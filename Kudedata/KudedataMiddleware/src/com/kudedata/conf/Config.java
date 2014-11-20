package com.kudedata.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 106800
 * clase que contiene una serie de propiedades estáticas recuperadas de un fichero de configuración.
 */
public class Config {	
	public static String VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER = null;
		
	
	private final static Logger LOGGER = Logger.getLogger(Config.class .getName());
	static {
		LOGGER.setLevel(Level.INFO);
	}

	public static void init() {
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
		String configFilePath= "D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/Kudedata/src/com/kudedata/conf/config.properties";
		//se lee el fichero de propiedades 
		input = new FileInputStream(configFilePath);	 			
		prop.load(input);		
		VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER = prop.getProperty("VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER");		
	 
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
	}	

}


