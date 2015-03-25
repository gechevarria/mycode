package com.ticket2cloud.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.sourceforge.barbecue.BarcodeFactory;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.model.api.ElementFactory;

import com.ticket2cloud.config.Configuration;
import com.ticket2cloud.db.DBManager;
import com.ticket2cloud.rest.bean.FacturaSimplificada;

/**
 * @author GM Echevarria
 *
 */

@Path("/webservice")
public class WebController {
private final static Logger LOGGER = Logger.getLogger(WebController.class .getName());
static {
	LOGGER.setLevel(Level.INFO);
}
	@POST
	@Path("factura")
	@Produces("text/plain")
	@Consumes("application/xml")
	public String recibirFactura(FacturaSimplificada facturaSimplificada)
	{	
		LOGGER.log(Level.INFO, "Factura recibida correctamente \"" + facturaSimplificada.getNumeroFactura());
		Long idFactura = almacenarFactura(facturaSimplificada);
		generarFactura(idFactura,facturaSimplificada.getIdCliente(),"TIPO1",facturaSimplificada.getCodigoBarras().toString());
		return "Factura recibida correctamente \"" + facturaSimplificada.getNumeroFactura();
	}
	/**
	 * @param facturaSimplificada
	 */
	private Long almacenarFactura(FacturaSimplificada facturaSimplificada) {
		Long idFactura = null;
		DBManager dbManager = new DBManager();
		idFactura = dbManager.insertarFactura(facturaSimplificada);
		return(idFactura);
	}
	/**
	 * @param  
	 * @param facturaSimplificada
	 */
	private void generarFactura(Long longIdFactura,String strIdCliente, String strTipoFactura, String strCodigoBarras ) {
		IReportEngine engine = null;
	    EngineConfig config = null;
	    ElementFactory designFactory = null;
	    //se leen los valores de configuración del fichero config.xml
	    //Configuration.setConfigurationFromConfigFile("D:/Projects/TICKCLOUD/TRABAJO/birt/eclipse/workspace/BIRTTicketsGenerator/conf/config.xml");
	    Configuration.setConfigurationFromConfigFile(System.getProperty("catalina.base")+ System.getProperty("file.separator")+"webapps"+System.getProperty("file.separator")+"Ticket2Cloud"+System.getProperty("file.separator")+"WEB-INF"+System.getProperty("file.separator")+"ticket2cloud_config.xml");	    

	     
	    try {
	    	net.sourceforge.barbecue.Barcode barcode = BarcodeFactory.createCode128B(strCodigoBarras);
	    	
	        config = new EngineConfig();          
	        config.setEngineHome(Configuration.birtenginehome);	       
			config.setLogConfig(Configuration.logPath, Level.FINE);			
	        Platform.startup(config);	        
	        final IReportEngineFactory FACTORY = (IReportEngineFactory) Platform
	            .createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
	        engine = FACTORY.createReportEngine(config);       	        
	        // Open the report design
	        IReportRunnable report = null;
	       
	        report = engine.openReportDesign(Configuration.plantillasFacturasPath+System.getProperty("file.separator")+strTipoFactura+System.getProperty("file.separator")+"factura_report.rptdesign");	        
	        IRunAndRenderTask task = engine.createRunAndRenderTask(report);        	        
	        report.getDesignInstance().getDataSet("lineaFacturaDataSet").setQueryText("select * from linea_factura where Id_Factura="+longIdFactura);
	        report.getDesignInstance().getDataSet("facturaSimplificadaDataSet").setQueryText("select Codigo_Barras from factura_simplificada where ID="+longIdFactura);
	 
	        final PDFRenderOption PDF_OPTIONS = new PDFRenderOption();
	        PDF_OPTIONS.setOutputFileName(Configuration.facturasPdfPath+System.getProperty("file.separator")+"factura.pdf");	        
	        PDF_OPTIONS.setOutputFormat("pdf");
	 	        
	        task.setRenderOption(PDF_OPTIONS);	        
	        task.run();	        
	        task.close();
	        engine.destroy();
	    } catch(final Exception EX) {
	        EX.printStackTrace();
	    } finally {
	       Platform.shutdown();
	    }
	}
	
	 
}
