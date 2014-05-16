
package com.ticket2cloud.test;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.ticket2cloud.db.DBManager;
import com.ticket2cloud.rest.bean.FacturaSimplificada;
import com.ticket2cloud.rest.bean.Producto;


public class Test
{
public static void main(String[] args)
{

// Create Client (consumes WebService using Restful
ClientConfig config = new DefaultClientConfig();
Client client = Client.create(config);
WebResource service = client.resource(getBaseURI());

ClientResponse response;


FacturaSimplificada factura = new FacturaSimplificada();
factura.setEmisor("A28654321");
factura.setNumeroFactura(new Long(15));
factura.setNumeroSerie(new Long(2));
factura.setFechaHoraFactura("15/05/2014");
factura.setIdCliente("30680454H");
factura.setImporteTotalSinIVA(null);
factura.setImporteTotalConIVA(new Float("18.77"));
factura.setCuotaIVA(null);
factura.setCodigoBarras(new Long("2500015018772"));
factura.setNumeroProductos(3);

ArrayList<Producto> arrayProductos = new ArrayList<Producto>();

Producto producto1 = new Producto();
producto1.setOrden(1);
producto1.setNombre("MANZANA REINETA");
producto1.setNumeroUnidadesProducto(1);
producto1.setPesoKgProducto(new Float("1.230"));
producto1.setIvaAplicadoProducto(new Float("4.0"));
producto1.setPrecioSinIvaProducto(new Float("2.23"));
producto1.setPrecioConIvaProducto(new Float("2.32"));
producto1.setPrecioSinIvaProductoKg(null);
producto1.setPrecioConIvaProductoKg(new Float("1.89"));
producto1.setCuotaIva(new Float("0.09"));

arrayProductos.add(producto1);

Producto producto2 = new Producto();
producto2.setOrden(2);
producto2.setNombre("LOMO EMBUCHADO");
producto2.setNumeroUnidadesProducto(1);
producto2.setPesoKgProducto(new Float("0.155"));
producto2.setIvaAplicadoProducto(new Float("10.0"));
producto2.setPrecioSinIvaProducto(new Float("3.23"));
producto2.setPrecioConIvaProducto(new Float("3.55"));
producto2.setPrecioSinIvaProductoKg(null);
producto2.setPrecioConIvaProductoKg(new Float("22.90"));
producto2.setCuotaIva(new Float("0.32"));
arrayProductos.add(producto2);

Producto producto3 = new Producto();
producto3.setOrden(3);
producto3.setNombre("RON AÑEJO");
producto3.setNumeroUnidadesProducto(1);
producto3.setPesoKgProducto(null);
producto3.setIvaAplicadoProducto(new Float("21.0"));
producto3.setPrecioSinIvaProducto(new Float("10.66"));
producto3.setPrecioConIvaProducto(new Float("12.90"));
producto3.setPrecioSinIvaProductoKg(null);
producto3.setPrecioConIvaProductoKg(null);
producto3.setCuotaIva(new Float("2.24"));

arrayProductos.add(producto3);
/*
 private String nombre;
	private int numeroUnidadesProducto;
	private Long pesoKgProducto;
	private Float ivaAplicadoProducto;
	private Float precioSinIvaProducto;
	private Float precioConIvaProducto;
	
	preparedStmt.setInt(1, 3);//orden
      preparedStmt.setString (2, "RON AÑEJO");
      preparedStmt.setInt(3, 1); 
      preparedStmt.setNull(4, 1);
      preparedStmt.setFloat(5, new Float("21.0"));
      preparedStmt.setFloat(6, new Float("10.66"));
      preparedStmt.setFloat(7, new Float("12.90"));
      preparedStmt.setNull(8, 1);
      preparedStmt.setNull(9, 1);     
      preparedStmt.setFloat(10, new Float("2.24")); 
      preparedStmt.setFloat(11, facturaSimplificadaID); 
      // execute the preparedstatement
      preparedStmt.executeUpdate();
      
      
      private static String queryLineaFactura = "INSERT INTO tickcloud.linea_factura (Orden, Producto, Numero_Unidades, Peso_kg, IVA_Aplicado, Precio_Unidad_Sin_IVA, Precio_Unidad_Con_IVA, Precio_Kg_Sin_IVA, Precio_Kg_Con_IVA, Cuota_IVA, Id_Factura)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
 */
factura.setArrayProductos(arrayProductos);

/*ArrayList<Producto> arrayProductos = new ArrayList<Producto>();
Producto producto = new Producto();
producto.setProducto("Mando a distancia");
producto.setNumeroUnidadesProducto(1);
producto.setIvaAplicadoProducto(new Float("21"));
producto.setPrecioSinIvaProducto(new Float("10"));
producto.setPrecioConIvaProducto(new Float("11.5"));


//productos.put(new Integer(index), producto);
arrayProductos.add(producto);
factura.setArrayProductos(arrayProductos);
factura.setImporteTotalFactura(new Float("11.5"));


System.out.println("Enviar datos factura");*/
try
{
	
	response = service.path("webservice").path("factura").type(MediaType.APPLICATION_XML_TYPE).post(ClientResponse.class, factura);	
	readResponse(response);
	
}
catch (Exception e) {}
}
// Add new Book
/*readAction("Add New Book");
String newBook = "Rafael San VidalISBN 2: 0-111-22222-0Introducción a RestFull usando Jersey y Tomcat1";
try
{
response = service.path("bookresource").path("add").type(MediaType.APPLICATION_XML).post(ClientResponse.class, newBook);
readResponse(response);
}
catch (Exception e) {}

// Add new Book
readAction("Add New Book");
newBook = "Brais Omar Moreno CidrasISBN 2: 0-111-33333-0Curso Avanzado RestFull usando Jersey y Tomcat2";
try
{
response = service.path("bookresource").path("add").type(MediaType.APPLICATION_XML).post(ClientResponse.class, newBook);
readResponse(response);
}
catch (Exception e) {}

// Get Books
readAction("Get Books");
try
{
response = service.path("bookresource").get(ClientResponse.class);
readResponse(response);
}
catch (Exception e) {}

// Get Book id = 0
readAction("Get Book id = 0");
try
{
response = service.path("bookresource").path("0").get(ClientResponse.class);
readResponse(response);
}
catch (Exception e) {}

// Get Book id = 1 (accept: Application/xml, text/plain, text/xml)
readAction("Get Book id = 1 (accept: Application/xml, text/plain, text/xml)");
try
{
response = service.path("bookresource").path("1").accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML, MediaType.TEXT_XML).get(ClientResponse.class);
readResponse(response);
}
catch (Exception e) {}

}*/

private static URI getBaseURI() { return UriBuilder.fromUri("http://localhost:8080/Ticket2Cloud/ticket/").build(); }

/**
* read Action
* @param mensaje
*/
private static void readAction(String mensaje)
{
System.out.println("————————————————————–");
System.out.println(mensaje + " …");
}

/**
* read Response
* @param response
*/
private static void readResponse(ClientResponse response)
{
// Response
System.out.println();
System.out.println("Response: " + response.toString());

}

}
