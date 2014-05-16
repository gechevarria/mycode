/**
 * @author G.M Echevarria
 *
 */
package com.ticket2cloud.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Statement;
import com.ticket2cloud.rest.bean.FacturaSimplificada;
import com.ticket2cloud.rest.bean.Producto;

public class DBManager {
	private final static Logger LOGGER = Logger.getLogger(DBManager.class .getName());
	static {
		LOGGER.setLevel(Level.INFO);
	}

	private static String queryFacturaSimplificada = " INSERT INTO tickcloud.factura_simplificada (NIF_Emisor, Numero_Factura, Numero_Serie, Fecha_Hora_Factura, Id_Cliente, Importe_Total_Sin_IVA, Importe_Total_Con_IVA, Cuota_IVA, Codigo_Barras, Numero_Productos)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static String queryLineaFactura = "INSERT INTO tickcloud.linea_factura (Orden, Producto, Numero_Unidades, Peso_kg, IVA_Aplicado, Precio_Unidad_Sin_IVA, Precio_Unidad_Con_IVA, Precio_Kg_Sin_IVA, Precio_Kg_Con_IVA, Cuota_IVA, Id_Factura)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static String myDriver = "com.mysql.jdbc.Driver";
	private static String myUrl = "jdbc:mysql://localhost/tickcloud";

	public void insertarFactura(FacturaSimplificada facturaSimplificada) {
		try {
			Class.forName(myDriver);
			Connection conn = DriverManager
					.getConnection(myUrl, "root", "root");

			Long facturaSimplificadaID = null;

			PreparedStatement preparedStmt = conn.prepareStatement(
					queryFacturaSimplificada, Statement.RETURN_GENERATED_KEYS);
			preparedStmt.setString(1, facturaSimplificada.getEmisor());
			preparedStmt.setLong(2, facturaSimplificada.getNumeroFactura());
			preparedStmt.setLong(3, facturaSimplificada.getNumeroSerie());
			preparedStmt.setString(4, facturaSimplificada.getFechaHoraFactura());
			preparedStmt.setString(5, facturaSimplificada.getIdCliente());
			if (facturaSimplificada.getImporteTotalSinIVA()==null)
				preparedStmt.setNull(6, 1);
			else
				preparedStmt.setFloat(6, facturaSimplificada.getImporteTotalSinIVA());
			preparedStmt.setFloat(7, facturaSimplificada.getImporteTotalConIVA());
			if (facturaSimplificada.getCuotaIVA()==null)
					preparedStmt.setNull(8, 1);
			else
					preparedStmt.setFloat(8, facturaSimplificada.getCuotaIVA());
			preparedStmt.setFloat(9, facturaSimplificada.getCodigoBarras());
			preparedStmt.setInt(10, facturaSimplificada.getNumeroProductos());
			// execute the preparedstatement			
			preparedStmt.executeUpdate();
			ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
			if (generatedKeys.next())
				facturaSimplificadaID = generatedKeys.getLong(1);
			
			//insertar lineas de factura
			preparedStmt = conn.prepareStatement(queryLineaFactura);
			ArrayList<Producto> arrayProductos = facturaSimplificada.getArrayProductos();
			LOGGER.log(Level.INFO, "Numero de l�neas en la factura \"" + arrayProductos.size());
			Producto producto = null;
			Iterator<Producto> iterator = arrayProductos.iterator();
			while (iterator.hasNext()){
				producto = iterator.next();
				if (producto.getOrden()==null)
					preparedStmt.setNull(1, 1);
				else
					preparedStmt.setInt(1, producto.getOrden());//orden				
			    preparedStmt.setString (2, producto.getNombre());
			    preparedStmt.setInt(3, producto.getNumeroUnidadesProducto());
			    if (producto.getPesoKgProducto()==null)
					preparedStmt.setNull(4, 1);
				else
					preparedStmt.setFloat(4, producto.getPesoKgProducto());			    
			    preparedStmt.setFloat(5, producto.getIvaAplicadoProducto());
			    preparedStmt.setFloat(6, producto.getPrecioSinIvaProducto());
			    preparedStmt.setFloat(7, producto.getPrecioConIvaProducto());
			    if (producto.getPrecioSinIvaProductoKg()==null)
					preparedStmt.setNull(8, 1);
				else
					preparedStmt.setFloat(8, producto.getPrecioSinIvaProductoKg());
			    if (producto.getPrecioConIvaProductoKg()==null)
					preparedStmt.setNull(9, 1);
				else
					preparedStmt.setFloat(9, producto.getPrecioConIvaProductoKg());     
			    preparedStmt.setFloat(10, producto.getCuotaIva()); 
			    preparedStmt.setFloat(11, facturaSimplificadaID);
			    preparedStmt.executeUpdate();
			}
			preparedStmt.close();
			conn.close();
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "Error \"" + e.toString());
		}
	}
}