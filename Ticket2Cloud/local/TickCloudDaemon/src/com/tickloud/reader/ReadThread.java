package com.tickloud.reader;

import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.tickloud.beans.FacturaSimplificada;
import com.tickloud.beans.Producto;

public class ReadThread implements Runnable{
	Connection conn;
	ResultSet rs;
	Statement statement;
	private static String selectSQL = "SELECT ItemNames.ShortDescription, TransactionPropDetails.Units,\n"
			+ "TransactionPropDetails.TotalNetAmount, TransactionPropDetails.TotalOtherCostsAmount,\n"
			+ "SaleTransaction.PartyName,ItemPricesProperty.UnitPrice,\n"
			+ "TransactionPropDetails.LineItemID,TransactionPropDetails.LineItemSubID,\n"
			+ "ItemCostChange.TaxIncludedPrice,SaleTransaction.CreateDate,SaleTransaction.CreateTime,\n"
			+ "SaleTransaction.TotalGrossAmount,\n"
			+ "SaleTransaction.VoucherGiftTransDocNumber,SaleTransaction.LineItemsCount FROM TransactionPropDetails,ItemNames, ItemPricesProperty,\n"
			+ "ItemCostChange, SaleTransaction WHERE ItemCostChange.ItemID=TransactionPropDetails.ItemID and\n"
			+ "ItemPricesProperty.ItemID=TransactionPropDetails.ItemID and(SaleTransaction.TransSerial=TransactionPropDetails.TransSerial and\n"
			+ "SaleTransaction.TransDocument=TransactionPropDetails.TransDocument and SaleTransaction.TransDocNumber=TransactionPropDetails.TransDocNumber)\n"
			+ "and ItemNames.ItemID=TransactionPropDetails.ItemID and   TransactionPropDetails.LineItemID > ";
	private static String selectSQLParameter = "";
	//private static String selectSQL = "SELECT * FROM TransactionPropDetails";
	private Float itemID = new Float(0);
	
    @Override
    public void run() {
        while(true){
        	testIfthereAreNewRecords();
        }
    }
 
    private void testIfthereAreNewRecords() {    	
        try {
        	if (conn==null){
        		try {
        			conn=getConnection();
        			statement = conn.createStatement();
                	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	try {               		
				//rs = statement.executeQuery(selectSQL+itemID.toString());
        		selectSQLParameter = selectSQL+itemID.toString();
        		System.out.println(selectSQLParameter);
        		rs = statement.executeQuery(selectSQLParameter);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	try {
        		FacturaSimplificada factura = null;
        		Producto producto = null;
        		ArrayList<Producto> arrayProductos = null;
        		int contador=1;
				while (rs.next()) {
					//se dan valores generales a la factura
					if (contador==1){
						arrayProductos = new ArrayList<Producto>();
						factura= crearFactura(rs);
					}
					producto = crearProducto(rs, contador);
					arrayProductos.add(producto);
					contador ++;
				}
				factura.setArrayProductos(arrayProductos);
				sendTicket (factura);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	        	        	
            Thread.sleep(5000);
            //de momento 10 seg, este tiempo hay que ampliarlo en producción
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Producto crearProducto(ResultSet rs, int contador) {
    	Producto producto = new Producto();
    	
    	try {
    		producto.setOrden(contador);
			producto.setNombre(rs.getString("shortdescription"));//ItemNames.ShortDescription		
	    	producto.setNumeroUnidadesProducto(1);
	    	producto.setPesoKgProducto(rs.getFloat("units")); // TransactionPropDetails.Units
	    	producto.setIvaAplicadoProducto(rs.getFloat("taxincludedprice")); // ItemCostChange.TaxIncludedPrice
	    	producto.setPrecioSinIvaProducto(rs.getFloat("totalnetamount"));// TransactionPropDetails.TotalNetAmount
	    	producto.setPrecioConIvaProducto(new Float(0));
	    	producto.setPrecioSinIvaProductoKg(new Float(0));
	    	producto.setPrecioConIvaProductoKg(rs.getFloat("unitprice")); //ItemPricesProperty.UnitPrice
	    	producto.setCuotaIva(rs.getFloat("totalothercostsamount")); //TransactionPropDetails.TotalOtherCostsAmount
    	} catch (SQLException e) {	
			e.printStackTrace();
		} 
		return producto;
	}

	private FacturaSimplificada crearFactura(ResultSet rs) {
    	FacturaSimplificada factura = new FacturaSimplificada();
    	try {
		factura.setEmisor(rs.getString("partyname"));		
		factura.setNumeroFactura(new Float(rs.getFloat("lineitemid")).longValue()); //TransactionPropDetails.LineItemID
		factura.setNumeroSerie(new Float(rs.getFloat("lineitemsubid")).longValue()); //TransactionPropDetails.LineItemSubID
		factura.setFechaHoraFactura(rs.getDate("createdate").toString());//SaleTransaction.CreateDate + SaleTransaction.CreateTime
		factura.setIdCliente("30680454H");
		factura.setImporteTotalSinIVA(new Float(0));
		factura.setImporteTotalConIVA(rs.getFloat("totalgrossamount"));//SaleTransaction.TotalGrossAmount
		factura.setCuotaIVA(new Float(0));
		factura.setCodigoBarras(new Float(rs.getFloat("vouchergifttransdocnumber")).longValue());//SaleTransaction.VoucherGiftTransDocNumber
		factura.setNumeroProductos(new Float(rs.getFloat("lineitemscount")).intValue());//SaleTransaction.LineItemsCount
    	
    	
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return factura;
	}

	/** a través de este método se envían los datos de la factura simplificada a un servicio web, que la almacenará en la nube posibilitando su posterior recuperación
     * @param factura
     */
    private void sendTicket(FacturaSimplificada factura) {
    	// Create Client (consumes WebService using Restful
    	ClientConfig config = new DefaultClientConfig();
    	Client client = Client.create(config);
    	WebResource service = client.resource(getBaseURI());

    	ClientResponse response;
    	try
    	{
    		
    		response = service.path("webservice").path("factura").type(MediaType.APPLICATION_XML_TYPE).post(ClientResponse.class, factura);	
    		readResponse(response);
    		
    	}
    	catch (Exception e) {}
    	}

    private static URI getBaseURI() { 
    	return UriBuilder.fromUri("http://localhost:8080/Ticket2Cloud/ticket/").build(); 
    }

	private static Connection getConnection() throws Exception {
        String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        String url = "jdbc:odbc:dstickcloud";
        String username = "";
        String password = "";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
      }
		
	/** lee la respuesta del servicio web
	 * @param response
	 */
	private static void readResponse(ClientResponse response)
	{
	// Response
	System.out.println();
	System.out.println("Response: " + response.toString());

	}
     
}
