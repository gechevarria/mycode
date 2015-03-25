package com.ticket2cloud.config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Configuration {	
	
	public static String birtenginehome = "";
	public static String logPath = "";
	public static String facturasPdfPath = "";
	public static String plantillasFacturasPath = "";
	public static String recursosPath = "";
	public static String codigosBarrasPath = "";
	
	
	public static void main (String[] args){
		String strConfigurationFilePath = "conf/config.xml";
		setConfigurationFromConfigFile(strConfigurationFilePath);
		
	}

	public static void setConfigurationFromConfigFile(String strConfigurationFilePath){
		
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
                     
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {				
				e.printStackTrace();
			}
            File file = new File(strConfigurationFilePath);
            if (file.exists()) {
                Document doc = null;
				try {
					doc = db.parse(file);
				} catch (SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                Element docEle = doc.getDocumentElement();
                
                
                NodeList birtenginehomeNodeList = docEle.getElementsByTagName("birt-engine-home");
                birtenginehome = birtenginehomeNodeList.item(0).getTextContent();
                
                NodeList logPathhomeNodeList = docEle.getElementsByTagName("log-path");
                logPath = logPathhomeNodeList.item(0).getTextContent();
                
                NodeList plantillasFacturasPathNodeList = docEle.getElementsByTagName("plantillas-facturas-path");
                plantillasFacturasPath = plantillasFacturasPathNodeList.item(0).getTextContent();         
                
                NodeList facturasPdfPathNodeList = docEle.getElementsByTagName("facturas-pdf-path");
                facturasPdfPath = facturasPdfPathNodeList.item(0).getTextContent();
                
                NodeList recursosPathNodeList = docEle.getElementsByTagName("recursos-path");
                recursosPath = recursosPathNodeList.item(0).getTextContent();
                
                NodeList codigosBarrasPathNodeList = docEle.getElementsByTagName("codigos-barras-path");
                codigosBarrasPath = codigosBarrasPathNodeList.item(0).getTextContent();
                                   
            }
}
	}
	
