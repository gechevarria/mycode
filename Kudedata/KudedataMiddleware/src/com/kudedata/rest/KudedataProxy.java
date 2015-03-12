package com.kudedata.rest;

import java.io.File;
import java.io.FileInputStream;
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
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kudedata.alfrescoconnector.AlfrescoConnector;
import com.kudedata.conf.Config;
import com.kudedata.fact.FilterFile;

@Path("/webservice")
public class KudedataProxy {
	static Log log = LogFactory.getLog(KudedataProxy.class);

	/** recibe un fichero edi encriptado y lo sube a alfresco
	 * @param a_request
	 * @param idEmpresaOrigen
	 * @param idEmpresaDestinataria
	 * @param ediType
	 * @param a_fileInputStream
	 * @return
	 * @throws Throwable
	 */
	@POST
	@Path("/receiveEDI/{idEmpresaOrigen}/{idEmpresaDestinataria}/{ediType}")
	@Consumes("application/octet-stream")
	@Produces("text/plain")
	public String receiveEDIFile(@Context HttpServletRequest a_request,
			@PathParam("idEmpresaOrigen") String idEmpresaOrigen,@PathParam("idEmpresaDestinataria") String idEmpresaDestinataria, @PathParam("ediType") String ediType, InputStream a_fileInputStream)
			throws Throwable {	
	String strResponse ="ok";	
	if (Config.EDI_MESSAGES_FOLDER==null)
		Config.init();
	
	InputStream EDIfileInputStream = a_fileInputStream;
	
	if (a_fileInputStream==null)
		log.info("KUDEDATA-TRAZA.receiveEDIFile1 a_fileInputStream es null");
	
	String transactionId = new Long(System.currentTimeMillis()).toString();
	
	File htmlFileToUpload = copyEDIReceivedFileToPendingTransactionsFolder(idEmpresaOrigen,idEmpresaDestinataria,ediType, EDIfileInputStream,transactionId);
	
	AlfrescoConnector.uploadFile(htmlFileToUpload,idEmpresaOrigen,idEmpresaDestinataria,transactionId);	
	
	return (strResponse);

	}
	
	public static void main (String[] args){
		Config.init();
		String idEmpresa="CAMEPACK";
		String fileName="desadv_UKABI-CAMEPACK_1424345501064.html";
		//getEDIFileFromPendigToBeProcesedFolder(fileName);
	}

	/**
	 * @return
	 * servicio que comprueba si hay mensajes EDI pendientes para enviar a una determinada empresa y en caso de que los haya los env�a (todos)
	 */  
	@GET
	@Path("/checkAndSendEDIFile/{idEmpresa}")
	@Produces("text/plain")
	public Response getFile(@PathParam("idEmpresa") String idEmpresa) {
		if (Config.EDI_MESSAGES_FOLDER==null)
			Config.init();
		ResponseBuilder response = Response.noContent();
		String fileName = AlfrescoConnector.checkIfPendingTransactions(idEmpresa);		
		if (fileName==""){			
			return response.build();
		}
		File ediFileFromPendigToBeProcesedFolder = getEDIFileFromPendigToBeProcesedFolder (fileName);		
		if (ediFileFromPendigToBeProcesedFolder==null){			
			return response.build();
		}	   	
	    response = Response.ok((Object) ediFileFromPendigToBeProcesedFolder);
	    
	    response.header("Content-Disposition",
	        "attachment; filename="+ediFileFromPendigToBeProcesedFolder.getName());
	    return response.build();

	}
		
	/**
	 * Moves edi file to the alreadyprocessed Folder
	 * @param ediFileFromPendigToBeProcesedFolder
	 */
	private void moveEdiFileToAlreadyProcessedFolder(
			File ediFileFromPendigToBeProcesedFolder) {
		InputStream inStream = null;
		OutputStream outStream = null;
	 
	    	try{
	 	    	    
	    	    File bfile =new File(Config.ALREADY_SENT_EDI_MESSAGES_FOLDER+File.separator+ediFileFromPendigToBeProcesedFolder.getName());
	 
	    	    inStream = new FileInputStream(ediFileFromPendigToBeProcesedFolder);
	    	    outStream = new FileOutputStream(bfile);
	 
	    	    byte[] buffer = new byte[1024];
	 
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	 
	    	    	outStream.write(buffer, 0, length);
	 
	    	    }
	 
	    	    inStream.close();
	    	    outStream.close();	 
	    	    //delete the original file
	    	    ediFileFromPendigToBeProcesedFolder.delete();	    	    
	 
	    	}catch(IOException e){
	    	    e.printStackTrace();
	    	}
	    }

	/**
	 * busca y devuelve el fichero edi
	 * @param idEmpresa
	 * @param fileName
	 * @return
	 */
	private File getEDIFileFromPendigToBeProcesedFolder(String fileName) {
		File ediFile = null;		
		String idEmpresaOrigen = fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("-"));
		String searchPath = Config.EDI_MESSAGES_FOLDER+System.getProperty("file.separator")+idEmpresaOrigen;				
		File ediFilesFolder = new File (searchPath);
		if (!ediFilesFolder.exists() || !ediFilesFolder.isDirectory())
				return ediFile;
		String transactionId = fileName.substring(fileName.lastIndexOf("_")+1, fileName.indexOf("."));
		
		File[] files = ediFilesFolder.listFiles(new FilterFile(transactionId) );
		if (files!=null)
			ediFile=files[0];
		
		return ediFile;
	}

	/**
	 * @param idEmpresa
	 * @param ediType 
	 * @param EDIfileInputStream
	 * copia el fichero EDI recibido a la carpeta de la empresa correspondiente, adem�s de esto crea un xml con los datos del fichero EDI
	 * y a partir de este y mediante el uso de xsl genera un html leible por el usuario con los datos m�s relevantes de la transacci�n con el 
	 * objetivo de que se conozca el contenido de la transacci�n especialmente para aquellos casos en los que es necesaria una aprobaci�n manual
	 * @param transactionId 
	 * @return 
	 */
	private File copyEDIReceivedFileToPendingTransactionsFolder(
			String idEmpresaOrigen, String idEmpresaDestinataria, String ediType, InputStream EDIfileInputStream, String transactionId) {
		File ediInHtmlFormatFile = null;
		OutputStream outputStream = null;
		
		String fileName = ediType+"-"+idEmpresaOrigen+"-"+idEmpresaDestinataria+"_"+transactionId+".edi";
		try {
			outputStream = new FileOutputStream(new File(
					Config.EDI_MESSAGES_FOLDER+File.separator+idEmpresaOrigen+File.separator+fileName));
		
		int read = 0;
		
		byte[] bytes = new byte[1024];
		
		while ((read = EDIfileInputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}		
		outputStream.close();			
		ediInHtmlFormatFile = generateHTMLToUpload (ediType);		
		return(ediInHtmlFormatFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ediInHtmlFormatFile;
		
	}

	private File generateHTMLToUpload(String ediType) {
		File htmlFile = null; 
	
		switch (ediType) {
		case Config.ORDER_EDI_ID:
			htmlFile = new File (Config.HTML_FILES_FOLDER+System.getProperty("file.separator")+"order.html");			
			break;
		case Config.INVOIC_EDI_ID:
			htmlFile = new File (Config.HTML_FILES_FOLDER+System.getProperty("file.separator")+"invoic.html");
			break;
		case Config.RECADV_EDI_ID:
			htmlFile = new File (Config.HTML_FILES_FOLDER+System.getProperty("file.separator")+"recadv.html");
			break;
		case Config.DESADV_EDI_ID:
			htmlFile = new File (Config.HTML_FILES_FOLDER+System.getProperty("file.separator")+"desadv.html");
			break;
		default:
			break;
		}
				
		return htmlFile;
	}


	/** M�todo que crea un xml (paso intermedio) y un html a partir del fichero edi y los almacena en la misma carpeta 
	 * @param completeEDIFileName
	 * @param idEmpresa
	 * @return 
	 */
	private synchronized File generateXMLANDHtmlFromEDIFIle(String completeEDIFileName, String idEmpresa) {
		File ediInHtmlFormatFile = null;		
		String completeXMLFilename = completeEDIFileName.replace(".edi", ".xml");
				
		String completeHTMLFilename = "/home/cloudlab/KUDEDATA/DESARROLLO/MIDDLEWARE/VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER/EMP1/order.html";
		EDItoXML eDItoXML = new EDItoXML(completeEDIFileName, completeXMLFilename);		
		eDItoXML.run();		
		TransformerFactory factory = TransformerFactory.newInstance();		
        Source xslt = new StreamSource(new File("/home/cloudlab/KUDEDATA/DESARROLLO/MIDDLEWARE/TRANSFORMATIONS/EMP1/XSLT/orders.xslt"));               
        Transformer transformer = null;        
		try {
			transformer = factory.newTransformer(xslt);
			if (transformer==null)
				log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle transformer es null");					
			Source text = new StreamSource(new File(completeXMLFilename));			
			if (text.equals(null))
				log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle text es null");
			ediInHtmlFormatFile = new File(completeHTMLFilename);						
			transformer.transform(text, new StreamResult(ediInHtmlFormatFile));				        
			ediInHtmlFormatFile = new File(completeHTMLFilename);
		} catch (TransformerException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		if (ediInHtmlFormatFile==null)
			log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.10 ediInHtmlFormatFile es nulo");
					
		return ediInHtmlFormatFile;
		
	};
	
}
