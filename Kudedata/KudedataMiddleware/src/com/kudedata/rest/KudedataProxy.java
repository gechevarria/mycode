package com.kudedata.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.kudedata.conf.Config;

@Path("/webservice")
public class KudedataProxy {

	@POST
	@Path("/receiveEDI/{idEmpresa}")
	@Consumes("application/octet-stream")
	@Produces("text/plain")
	public String receiveEDIFile(@Context HttpServletRequest a_request,
			@PathParam("idEmpresa") String idEmpresa, InputStream a_fileInputStream)
			throws Throwable {		
	String strResponse ="ok";
	if (Config.VALIDATION_PENDING_RECEIVED_EDI_FOLDER==null)
		Config.init();
		
	InputStream EDIfileInputStream = a_fileInputStream;
	
	copyEDIReceivedFileToPendingTransactionsFolder(idEmpresa,EDIfileInputStream);			
	
	return (strResponse);

	}
	
	/**
	 * @return
	 * servicio que comprueba si hay mensajes EDI pendientes para enviar a una determinada empresa y en caso de que los haya los envía (todos)
	 */  
	@GET
	@Path("/checkAndSendEDIFile/{idEmpresa}")
	@Produces("text/plain")
	public Response getFile(@PathParam("idEmpresa") String idEmpresa) {
		if (Config.PENDING_TO_BE_SENT_EDI_FILES==null)
			Config.init();
	    File file = new File(Config.PENDING_TO_BE_SENT_EDI_FILES+File.separator+idEmpresa+File.separator+"ediTransaction.edi");
	    ResponseBuilder response = Response.ok((Object) file);
	    response.header("Content-Disposition",
	        "attachment; filename=ediTransaction.edi");
	    return response.build();

	}

	/*@GET
	@Path("/checkAndSendEDIFile/{idEmpresa}")
	@Produces("text/plain")
	public File getFile2() {
	    File file = new File(Config.VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER+File.separator+"ediTransaction_EMP1_1416489741667.edi");
	    ResponseBuilder response = Response.ok((Object) file);
	    response.header("Content-Disposition",
	        "attachment; filename=newfile.zip");
	    return file;

	}
*/
	
	/**
	 * @param idEmpresa
	 * @param EDIfileInputStream
	 * copia el fichero EDI recibido a la carpeta de la empresa correspondiente, además de esto crea un xml con los datos del fichero EDI
	 * y a partir de este y mediante el uso de xsl genera un html leible por el usuario con los datos más relevantes de la transacción con el 
	 * objetivo de que se conozca el contenido de la transacción especialmente para aquellos casos en los que es necesaria una aprobación manual
	 */
	private void copyEDIReceivedFileToPendingTransactionsFolder(
			String idEmpresa, InputStream EDIfileInputStream) {
		OutputStream outputStream = null;
		
		String fileName = "ediTransaction_"+idEmpresa+"_"+System.currentTimeMillis()+".edi";
		
		// write the inputStream to a FileOutputStream
		try {
			outputStream = new FileOutputStream(new File(
					Config.VALIDATION_PENDING_RECEIVED_EDI_FOLDER+File.separator+idEmpresa+File.separator+fileName));
		

		int read = 0;
		byte[] bytes = new byte[1024];
		
		while ((read = EDIfileInputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		
		outputStream.close();
		generateXMLANDHtmlFromEDIFIle (Config.VALIDATION_PENDING_RECEIVED_EDI_FOLDER+File.separator+idEmpresa+File.separator+fileName, idEmpresa);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/** Método que crea un xml (paso intermedio) y un html a partir del fichero edi y los almacena en la misma carpeta 
	 * @param completeEDIFileName
	 * @param idEmpresa
	 */
	private void generateXMLANDHtmlFromEDIFIle(String completeEDIFileName, String idEmpresa) {
		String completeXMLFilename = completeEDIFileName.replace(".edi", ".xml");
		String completeHTMLFilename = completeEDIFileName.replace(".edi", ".html");;
		EDItoXML eDItoXML = new EDItoXML(completeEDIFileName, completeXMLFilename);
		eDItoXML.run();
		
	};

}
