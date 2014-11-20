package com.kudedata.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

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
		
	InputStream EDIfileInputStream = a_fileInputStream;
	
	copyEDIReceivedFileToPendintTransactionsFolder(idEmpresa,EDIfileInputStream);			
	
	return (strResponse);

	}

	private void copyEDIReceivedFileToPendintTransactionsFolder(
			String idEmpresa, InputStream EDIfileInputStream) {
		OutputStream outputStream = null;
		
		if (Config.VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER==null)
			Config.init();
		
		String fileName = "ediTransaction_"+idEmpresa+"_"+System.currentTimeMillis()+".edi";
		
		// write the inputStream to a FileOutputStream
		try {
			outputStream = new FileOutputStream(new File(
					Config.VALIDATION_PENDIGN_RECEIVED_EDI_FOLDER+File.separator+fileName));
		

		int read = 0;
		byte[] bytes = new byte[1024];
		
		while ((read = EDIfileInputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		
		outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	};

}
