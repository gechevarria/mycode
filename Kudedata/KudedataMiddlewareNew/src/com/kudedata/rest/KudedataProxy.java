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

@Path("/webservice")
public class KudedataProxy {
	static Log log = LogFactory.getLog(KudedataProxy.class);

	@POST
	@Path("/receiveEDI/{idEmpresaOrigen}/{idEmpresaDestinataria}/{ediType}")
	@Consumes("application/octet-stream")
	@Produces("text/plain")
	public String receiveEDIFile(@Context HttpServletRequest a_request,
			@PathParam("idEmpresaOrigen") String idEmpresaOrigen,@PathParam("idEmpresaDestinataria") String idEmpresaDestinataria, @PathParam("ediType") String ediType, InputStream a_fileInputStream)
			throws Throwable {
	log.info("KUDEDATA-TRAZA.receiveEDIFile1");	
	String strResponse ="ok";
	log.info("KUDEDATA-TRAZA.receiveEDIFile2 "+Config.VALIDATION_PENDING_RECEIVED_EDI_FOLDER);
	if (Config.VALIDATION_PENDING_RECEIVED_EDI_FOLDER==null)
		Config.init();
	
	InputStream EDIfileInputStream = a_fileInputStream;
	
	if (a_fileInputStream==null)
		log.info("KUDEDATA-TRAZA.receiveEDIFile1 a_fileInputStream es null");
	
	File htmlFileToUpload = copyEDIReceivedFileToPendingTransactionsFolder(idEmpresaOrigen,ediType, EDIfileInputStream);
	
	AlfrescoConnector.uploadFile(htmlFileToUpload,idEmpresaDestinataria);	
	
	return (strResponse);

	}
	
	

	/**
	 * @return
	 * servicio que comprueba si hay mensajes EDI pendientes para enviar a una determinada empresa y en caso de que los haya los env�a (todos)
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
	
	
	/**
	 * @param idEmpresa
	 * @param EDIfileInputStream
	 * copia el fichero EDI recibido a la carpeta de la empresa correspondiente, adem�s de esto crea un xml con los datos del fichero EDI
	 * y a partir de este y mediante el uso de xsl genera un html leible por el usuario con los datos m�s relevantes de la transacci�n con el 
	 * objetivo de que se conozca el contenido de la transacci�n especialmente para aquellos casos en los que es necesaria una aprobaci�n manual
	 * @return 
	 */
	private File copyEDIReceivedFileToPendingTransactionsFolder(
			String idEmpresa, String ediType, InputStream EDIfileInputStream) {
		File ediInHtmlFormatFile = null;
		OutputStream outputStream = null;
		String fileName = "ediTransaction_"+idEmpresa+"_"+System.currentTimeMillis()+".edi";
		try {
			outputStream = new FileOutputStream(new File(
					Config.VALIDATION_PENDING_RECEIVED_EDI_FOLDER+File.separator+idEmpresa+File.separator+fileName));
		
		int read = 0;
		
		byte[] bytes = new byte[1024];
		
		while ((read = EDIfileInputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		
		outputStream.close();
		//ediInHtmlFormatFile = generateXMLANDHtmlFromEDIFIle (Config.VALIDATION_PENDING_RECEIVED_EDI_FOLDER+File.separator+idEmpresa+File.separator+fileName, idEmpresa);
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
		log.info("El usuario es " +System.getProperty("user.name"));
		String completeXMLFilename = completeEDIFileName.replace(".edi", ".xml");
		//String completeXMLFilename = "/home/cloudlab/KUDEDATA/DESARROLLO/MIDDLEWARE/VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER/EMP1/ediTransaction_EMP1_1420715180226.xml";
		//String completeHTMLFilename = completeEDIFileName.replace(".edi", ".html");
		String completeHTMLFilename = "/home/cloudlab/KUDEDATA/DESARROLLO/MIDDLEWARE/VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER/EMP1/order.html";
		EDItoXML eDItoXML = new EDItoXML(completeEDIFileName, completeXMLFilename);
		log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.1 completeXMLFilename"+completeXMLFilename);
		log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.2 completeHTMLFilename"+completeHTMLFilename);
		eDItoXML.run();
		log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.2");
		TransformerFactory factory = TransformerFactory.newInstance();
		log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.3");
        Source xslt = new StreamSource(new File("/home/cloudlab/KUDEDATA/DESARROLLO/MIDDLEWARE/TRANSFORMATIONS/EMP1/XSLT/orders.xslt"));
        //log.info("XSLT "+xslt.toString());
        log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.4");
        Transformer transformer = null;
        log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.5");        
        log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.6");
		try {
			transformer = factory.newTransformer(xslt);
			if (transformer==null)
				log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle transformer es null");
			log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.7");			
			Source text = new StreamSource(new File(completeXMLFilename));
			log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.8 completeHTMLFilename es "+completeHTMLFilename);	
			if (text.equals(null))
				log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle text es null");
			ediInHtmlFormatFile = new File(completeHTMLFilename);
			log.info("Se puede escribir en el fichero? "+ediInHtmlFormatFile.canWrite());
			
			transformer.transform(text, new StreamResult(ediInHtmlFormatFile));
			log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.9");  
			/*TransformerFactory factory = TransformerFactory.newInstance();
	        Source xslt = new StreamSource(new File("/home/cloudlab/KUDEDATA/DESARROLLO/MIDDLEWARE/TRANSFORMATIONS/EMP1/XSLT/orders.xslt").getAbsoluteFile());
	        Transformer transformer = factory.newTransformer(xslt);*/
	        
	        /*OutputStream baos = new ByteArrayOutputStream();
	        Result result = new StreamResult(baos);*/
	        /*log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.7");
	        Source text = new StreamSource(new File("/home/cloudlab/KUDEDATA/DESARROLLO/MIDDLEWARE/VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER/EMP1/ediTransaction_EMP1_1422884381234.xml").getAbsoluteFile());
	        //transformer.transform(text, new StreamResult(new File("/home/alfresco/order.html").getAbsoluteFile()));
	        transformer.transform(text, result);*/
	        log.info("KUDEDATA-TRAZA.generateXMLANDHtmlFromEDIFIle.7");
	        
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
