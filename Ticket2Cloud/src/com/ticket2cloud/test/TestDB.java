package com.ticket2cloud.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import com.mysql.jdbc.Statement;


public class TestDB
{

  public static void main(String[] args)
  {
    try
    {
      // create a mysql database connection
      String myDriver = "com.mysql.jdbc.Driver";
      String myUrl = "jdbc:mysql://localhost/tickcloud";
      Class.forName(myDriver);
      Connection conn = DriverManager.getConnection(myUrl, "root", "root");
      
    
      // create a sql date object so we can use it in our INSERT statement
      Calendar calendar = Calendar.getInstance();
      java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

      // the mysql insert statements
      String queryFacturaSimplificada = " INSERT INTO tickcloud.factura_simplificada (NIF_Emisor, Numero_Factura, Numero_Serie, Fecha_Hora_Factura, Id_Cliente, Importe_Total_Sin_IVA, Importe_Total_Con_IVA, Cuota_IVA, Codigo_Barras, Numero_Productos)"
        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      

      String queryLineaFactura ="INSERT INTO tickcloud.linea_factura (Orden, Producto, Numero_Unidades, Peso_kg, IVA_Aplicado, Precio_Unidad_Sin_IVA, Precio_Unidad_Con_IVA, Precio_Kg_Sin_IVA, Precio_Kg_Con_IVA, Cuota_IVA, Id_Factura)"
    		  + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      

      String queryEmisorFactura = " INSERT INTO tickcloud.emisor_factura (NIF, Nombre, Apellido1, Apellido2, Razon_Social)"
    	+ " values (?, ?, ?, ?, ?)";
      String queryCliente = " INSERT INTO tickcloud.cliente (ID, Nombre, Apellido1, Apellido2, Sexo, Fecha_Nacimiento, Forma_Contacto, Contacto)"
    	+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

      //ESTO OK !!!!
      
      /*PreparedStatement preparedStmt = conn.prepareStatement(queryCliente);
      preparedStmt.setString (1, "30680454H");
      preparedStmt.setString (2, "Gorka Mikel");
      preparedStmt.setString (3, "Echevarría");
      preparedStmt.setString (4, "Vélez");
      preparedStmt.setInt(5, 1);
      DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
      java.util.Date javaUtilDate = df.parse("1974/03/23");     
      
      preparedStmt.setDate(6, new Date(javaUtilDate.getTime()));
      preparedStmt.setInt (7, 1);
      preparedStmt.setString (8, "gmeva@yahoo.es");
      preparedStmt.execute();
      
      //ESTO OK !!!!
      preparedStmt = conn.prepareStatement(queryEmisorFactura);
      preparedStmt.setString (1, "A28654321");
      preparedStmt.setString (2, "SUPERSTAR, S.A");
      preparedStmt.setString (3, "");
      preparedStmt.setString (4, "");
      preparedStmt.setString (5, "Tienda 27-Alcobendas (MADRID)");
      preparedStmt.execute();*/

      // create the mysql insert preparedstatement
      Long facturaSimplificadaID = null;
      
      PreparedStatement preparedStmt = conn.prepareStatement(queryFacturaSimplificada,Statement.RETURN_GENERATED_KEYS);     
      preparedStmt.setString (1, "A28654321");
      preparedStmt.setLong(2, 15);
      preparedStmt.setLong(3, 2);
      preparedStmt.setDate(4, new Date(System.currentTimeMillis()));
      preparedStmt.setString (5, "30680454H");
      preparedStmt.setNull(6, 1);
      preparedStmt.setFloat(7, new Float("18.77")); //importe
      preparedStmt.setNull(8, 1);
      preparedStmt.setDouble(9, new Double("2500015018772"));
      preparedStmt.setInt(10, 3);
      // execute the preparedstatement
      preparedStmt.executeUpdate();
      ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
      if (generatedKeys.next())
    	  facturaSimplificadaID = generatedKeys.getLong(1);
     
      
      preparedStmt = conn.prepareStatement(queryLineaFactura);       
      preparedStmt.setInt(1, 1);
      preparedStmt.setString (2, "MANZANA REINETA");
      preparedStmt.setInt(3, 1);
      preparedStmt.setFloat(4, new Float("1.230"));
      preparedStmt.setFloat(5, new Float("4.0"));
      preparedStmt.setFloat(6, new Float("2.23"));
      preparedStmt.setFloat(7, new Float("2.32"));
      preparedStmt.setNull(8, 1);
      preparedStmt.setFloat(9, new Float("1.89"));     
      preparedStmt.setFloat(10, new Float("0.09")); 
      preparedStmt.setFloat(11, facturaSimplificadaID); 
      // execute the preparedstatement
      preparedStmt.executeUpdate();
     
      preparedStmt.setInt(1, 2);//orden
      preparedStmt.setString (2, "LOMO EMBUCHADO");
      preparedStmt.setInt(3, 1); 
      preparedStmt.setFloat(4, new Float("0.155"));
      preparedStmt.setFloat(5, new Float("10.0"));
      preparedStmt.setFloat(6, new Float("3.23"));
      preparedStmt.setFloat(7, new Float("3.55"));
      preparedStmt.setNull(8, 1);
      preparedStmt.setFloat(9, new Float("22.90"));     
      preparedStmt.setFloat(10, new Float("0.32")); 
      preparedStmt.setFloat(11, facturaSimplificadaID); 
      // execute the preparedstatement
      preparedStmt.executeUpdate();
      
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
       
/*INSERT INTO tickcloud.linea_factura
(ID, Orden, Producto, Numero_Unidades, Peso_kg, IVA_Aplicado, Precio_Unidad_Sin_IVA, Precio_Unidad_Con_IVA, Precio_Kg_Sin_IVA, Precio_Kg_Con_IVA, Cuota_IVA, Id_Factura)
VALUES(0, 0, '', 0, 0, 0, 0, 0, 0, 0, 0, 0);
*/


      conn.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception!");
      e.printStackTrace();
    }
  }
}