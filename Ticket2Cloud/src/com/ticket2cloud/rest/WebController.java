package com.ticket2cloud.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
		almacenarFactura(facturaSimplificada);
		return "Factura recibida correctamente \"" + facturaSimplificada.getNumeroFactura();
	}
	/**
	 * @param facturaSimplificada
	 */
	private void almacenarFactura(FacturaSimplificada facturaSimplificada) {
		DBManager dbManager = new DBManager();
		dbManager.insertarFactura(facturaSimplificada);
	}	
	 
}
