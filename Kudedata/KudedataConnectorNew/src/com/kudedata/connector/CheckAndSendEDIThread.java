package com.kudedata.connector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
 * comprueba si hay mensajes EDI pendientes de env�o consultando en la carpeta correspondiente y si es as� llama al servicio ofrecido por el middleware kudedata para enviarlos
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
	 * lee el directorio para ver si hay ficheros EDI pendientes de env�o
	 */
	private void checkIfThereAreEDIMessages() {
		String sDirectorio = Config.EDI_TOSEND_FOLDER;
		File f = new File(sDirectorio);
		if (f.exists()){ 
			File[] ficheros = f.listFiles();
			for (int x=0;x<ficheros.length;x++){
			  //los ficheros tendr�n el formato "nombreFichero.edi"
			  if (ficheros[x].getName().contains(".edi")){
				sendEDIToKudedataMiddleware(ficheros[x]);  
			  }
			}
		}
		else { 
			LOGGER.info("El directorio "+sDirectorio+ " no existe !!");
		}
		try {
			Thread.sleep(Config.CHECK_TOSEND_TIMEOUT_IN_MILLISECONDS);
		} catch (InterruptedException e) {
			LOGGER.info("Se ha producido un error al recibir la respuesta del servidor."+e.getLocalizedMessage());
		}
		
	}

	/**
	 * @param file
	 * env�a ficheros pendientes al middleware (encriptados usando ssl) para su procesamiento
	 */
	private void sendEDIToKudedataMiddleware(File ediFile) {
		KudedataConnector.createClient();
		String fileName = ediFile.getName();
		String EDIFileType = "";
		String idEmpresaDestinataria = "";
		
		
		EDIFileType = geteditYPE(ediFile);
		idEmpresaDestinataria = fileName.substring(fileName.indexOf("-")+1, fileName.indexOf("."));
		
		InputStream fileInStream = null;
		try {
			fileInStream = new FileInputStream(ediFile);
		} catch (FileNotFoundException e1) {
			LOGGER.info("El fichero EDI "+fileName+ " no existe !!");
		}
		String sContentDisposition = "attachment; filename=\"" + fileName
				+ "\"";
		//invocaci�n al servicio rest que recibe el fichero edi pas�ndole adem�s como par�metro el identificativo de la empresa y el tipo de mensaje EDI
		ClientResponse response = KudedataConnector.webResource.path("webservice").path("receiveEDI").path(Config.ENTERPRISE_ID).path(idEmpresaDestinataria).path(EDIFileType)
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
			moveFileToAlreadySentFolder (ediFile);
			
		}
		response.close();
		KudedataConnector.destroyClient();
	}

	private void moveFileToAlreadySentFolder(File sourceFile) {		
		Path FROM = Paths.get(sourceFile.toURI());		
        Path TO = Paths.get(Config.EDI_ALREADY_SENT_FOLDER+File.separator+sourceFile.getName());
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

	/**
	 * Obtiene el tipo de mensaje EDI que se procesa
	 * 
	 * @param ediFile
	 * @return
	 */
	private static String geteditYPE(File ediFile) {
		File xsltFile = null;
		String ediType = "";
		FileReader namereader = null;
		try {
			namereader = new FileReader(ediFile.getAbsolutePath());
			BufferedReader in = new BufferedReader(namereader);
			String readLine = in.readLine();
			ediType = readLine.substring(readLine.length() - 7,
					readLine.length() - 1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return ediType;
	}

}
