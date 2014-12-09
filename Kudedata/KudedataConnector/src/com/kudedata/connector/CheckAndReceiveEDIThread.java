package com.kudedata.connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jersey.api.client.ClientResponse;

/**
 * @author 106800
 * llama al servicio ofrecido por el middleware kudedata para comprobar si hay mensajes EDI aprobados pendientes de recibir y si es así los rebibe y almacena
 * en una carpeta para su posterior procesamiento
 */
public class CheckAndReceiveEDIThread implements Runnable{
	
	private final static Logger LOGGER = Logger.getLogger(CheckAndSendEDIThread.class .getName());
	static {
		LOGGER.setLevel(Level.INFO);
	}
	
	@Override
    public void run() {
        while(true){
        	checkAndReceiveEDIMessages();
        }
    }	
	
	/**
	 * 
	 */
	// funciona desde el navegador con https://localhost:8443/kudedata/message/webservice/checkAndSendEDIFile/EMP1
	public void checkAndReceiveEDIMessages() {
		KudedataConnector.createClient();
		
	    ClientResponse response = KudedataConnector.webResource.path("webservice").path("checkAndSendEDIFile").path(KudedataConnector.ENTERPRISEID).type("application/text")
	        .get(ClientResponse.class);
	    
	    byte[] buffer;
		try {
			InputStream initialStream= response.getEntityInputStream();
		    OutputStream outStream = new FileOutputStream(KudedataConnector.EDITORECEIVEFOLDER+File.separator+System.currentTimeMillis()+".edi");
			buffer = new byte[1024];						  
		    
		    int bytesRead;
            //read from is to buffer
            while((bytesRead = initialStream.read(buffer)) !=-1){
                outStream.write(buffer, 0, bytesRead);
            }
            initialStream.close();
            //flush OutputStream to write any buffered data to file
            outStream.flush();
            outStream.close();
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			response.close();
		}
		KudedataConnector.destroyClient();
		try {
			Thread.sleep(KudedataConnector.CHECKTORECEIVETIMEOUTINMILLISECONDS);
		} catch (InterruptedException e) {
			LOGGER.info("Se ha producido un error al recibir la respuesta del servidor."+e.getLocalizedMessage());
		}
	}

}
