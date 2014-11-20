package com.kudedata.connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;

/**
 * @author 106800
 * comprueba si hay mensajes EDI pendientes de envío consultando en la carpeta correspondiente y si es así llama al servicio ofrecido por el middleware kudedata para enviarlos
 */
public class CheckAndSendEDIThread implements Runnable{
	
	private final static Logger LOGGER = Logger.getLogger(CheckAndSendEDIThread.class .getName());
	static {
		LOGGER.setLevel(Level.INFO);
	}
	
	@Override
    public void run() {
        while(true){
        	checkIfThereAreEDIMessages();
        }
    }

	/**
	 * lee el directorio para ver si hay ficheros EDI pendientes de envío
	 */
	private void checkIfThereAreEDIMessages() {
		String sDirectorio = KudedataConnector.EDITOSENDFOLDER;
		File f = new File(sDirectorio);
		if (f.exists()){ 
			File[] ficheros = f.listFiles();
			for (int x=0;x<ficheros.length;x++){
			  //los ficheros tendrán el formato "nombreFichero.edi"
			  if (ficheros[x].getName().contains(".edi")){
				sendEDIToKudedataMiddleware(ficheros[x]);  
			  }
			}
		}
		else { 
			LOGGER.info("El directorio "+sDirectorio+ " no existe !!");
		}
		try {
			Thread.sleep(KudedataConnector.CHECKTOSENDTIMEOUTINMILLISECONDS);
		} catch (InterruptedException e) {
			LOGGER.info("Se ha producido un error al recibir la respuesta del servidor."+e.getLocalizedMessage());
		}
		
	}

	/**
	 * @param file
	 * envía ficheros pendientes al middleware (encriptados usando ssl) para su procesamiento
	 */
	private void sendEDIToKudedataMiddleware(File fileName) {
		InputStream fileInStream = null;
		try {
			fileInStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e1) {
			LOGGER.info("El fichero EDI"+fileName+ " no existe !!");
		}
		String sContentDisposition = "attachment; filename=\"" + fileName.getName()
				+ "\"";
		//invocación al servicio rest que recibe el fichero edi pasándole además como parámetro el identificativo de la empresa
		ClientResponse response = KudedataConnector.webResource.path("webservice").path("receiveEDI").path(KudedataConnector.ENTERPRISEID)
				.type(MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", sContentDisposition)
				.post(ClientResponse.class, fileInStream);
		try {
			fileInStream.close();
		} catch (IOException e) {
			LOGGER.info("Se ha producido un error al cerrar el stream. "+e.getLocalizedMessage());
		}
		//Si la respuesta es "ok", el mensaje ha llegado al servidor y se ha almacenado, por lo que se pasa el fichero a la carpeta de enviados
		LOGGER.info(response.toString());
		if (response.toString().contains("OK")){
			moveFileToAlreadySentFolder (fileName);
			
		}
	}

	private void moveFileToAlreadySentFolder(File sourceFile) {		
		Path FROM = Paths.get(sourceFile.toURI());		
        Path TO = Paths.get(KudedataConnector.EDIALREADYSENTFOLDER+File.separator+sourceFile.getName());
        //sobreescribir el fichero de destino, si existe, y copiar
        // los atributos, incluyendo los permisos rwx
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES          
        }; 
        try {
			Files.copy(FROM, TO, options);
			sourceFile.delete();
			
		} catch (IOException e) {
			LOGGER.info("Se ha producido un error copiar el fichero EDI a la carpeta de enviados. "+e.getLocalizedMessage());
		}		
	}

	

}
