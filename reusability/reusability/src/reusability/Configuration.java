package reusability;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.tecnalia.structures.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import reusability.beans.DataStorage;
import reusability.beans.DataStorageProduct;
import reusability.beans.ProgrammingLanguage;

public class Configuration {
	private static String  fileName = "D:/TECNALIA/PROYECTOS/ARTIST/eclipse-standard-kepler-SR1/workspace/reusability/config/config.xml";
	//private static String  fileName = "/config/config.xml";
	private static ProgrammingLanguage programmingLanguage;
	private static DataStorage dataStorage;
	private static String programmingLang = "";
	private static String dataStorageName = "";
	static HashMap<String, ProgrammingLanguage> mapProgrammingLanguages = null;	
	static HashMap<String, DataStorage> mapDataStorages = null;
	private static DataStorageProduct dataStorageProduct;
	private static String dataStorageProductName;
	static HashMap<String, DataStorageProduct> mapdataStorageProducts;
	static Project projectWithReusabilityConditions;
	
	

	public static void main(String[] args) {
		setConfigurationFromConfigFile(fileName);
	}
	public static void setConfigurationFromConfigFile(String strConfigurationFilePath){
		
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            NodeList keywordsChildNodesList = null;
            ArrayList<String> keywordsList = null;
                     
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
                Node node = null;
                NodeList programmingLanguagesPropertiesNodeList = null;
                int i = 0;
                int j = 0;
                int k = 0;
                NodeList elementsByTagName = docEle.getElementsByTagName("programming_languages");
                if (elementsByTagName!=null){
                	mapProgrammingLanguages = new HashMap<String,ProgrammingLanguage>(); 
                	Node programmingLanguagesNode = elementsByTagName.item(0);                	                
                	
                	NodeList programmingLanguagesNodeList = programmingLanguagesNode.getChildNodes();
                	
                	while (i < programmingLanguagesNodeList.getLength()){
                		
                		node = programmingLanguagesNodeList.item(i);                		
                		if (node.getNodeName().equalsIgnoreCase("element")){
                			
                			programmingLanguagesPropertiesNodeList = node.getChildNodes();
                			j = 0;
                			
                			while (j < programmingLanguagesPropertiesNodeList.getLength()){
                				
                				Node item = programmingLanguagesPropertiesNodeList.item(j);                				
                				if (item.getNodeName().equalsIgnoreCase("name")){    
                					programmingLanguage = new ProgrammingLanguage();
                					programmingLang = item.getTextContent();                					
                					programmingLanguage.setName(programmingLang);                					                					
                				}
                				if (item.getNodeName().equalsIgnoreCase("source_code-file_ext")){
                					programmingLanguage.setFileExt(item.getTextContent());
                					mapProgrammingLanguages.put(programmingLang, programmingLanguage);
                				}                				                					
                				j++;
                				
                			}//end while
                			
                		}//end if
                		
                		
                		i++;
                	}//end while
                	
                	i=0;
                	
                	NodeList datastorageNodePropertiesNodeList = null;
                	elementsByTagName = docEle.getElementsByTagName("data_storage");
                    if (elementsByTagName!=null){                    	
                    	mapDataStorages = new HashMap<String,DataStorage>(); 
                    	Node datastorageNode = elementsByTagName.item(0);                	                
                    	
                    	NodeList datastorageNodeNodeList = datastorageNode.getChildNodes();
                    	
                    	
                    	while (i < datastorageNodeNodeList.getLength()){
                    		
                    		node = datastorageNodeNodeList.item(i);                		
                    		if (node.getNodeName().equalsIgnoreCase("element")){
                    			
                    			datastorageNodePropertiesNodeList = node.getChildNodes();
                    			j = 0;
                    			
                    			while (j < datastorageNodePropertiesNodeList.getLength()){
                    				
                    				Node item = datastorageNodePropertiesNodeList.item(j);    
                    				dataStorage = new DataStorage();
                    				if (item.getNodeName().equalsIgnoreCase("name")){                     					                    					
                    					dataStorageName=item.getTextContent();                    					                    					
                    				}
                    				if (item.getNodeName().equalsIgnoreCase("keywords")){ 
                    					keywordsChildNodesList = item.getChildNodes();
                    					k=0;
                    					keywordsList = new ArrayList<String>();
                    					while (k < keywordsChildNodesList.getLength()){
                    						Node item2 = keywordsChildNodesList.item(k);
                    						if (item2.getNodeName().equalsIgnoreCase("element")){
                    							keywordsList.add(item2.getTextContent());
                    							
                    						}
                    						k++;
                    					}
                    				}
                    				dataStorage.setName(dataStorageName);
                    				dataStorage.setKeywords(keywordsList);
                    				mapDataStorages.put(dataStorageName, dataStorage);
                    				j++;                    				
                    			}//end while                    			
                    		}//end if                    		                    		
                    		i++;
                    	}//end while
                    	
                    	i=0;
                    	
                    	NodeList dataStorageProductsNodeList = null;
                    	elementsByTagName = docEle.getElementsByTagName("data_storage_products");
                        if (elementsByTagName!=null){                    	
                        	mapdataStorageProducts = new HashMap<String,DataStorageProduct>(); 
                        	Node dataStorageProductNode = elementsByTagName.item(0);                	                
                        	
                        	NodeList dataStorageProductNodeNodeList = dataStorageProductNode.getChildNodes();
                        	
                        	
                        	while (i < dataStorageProductNodeNodeList.getLength()){
                        		
                        		node = dataStorageProductNodeNodeList.item(i);                		
                        		if (node.getNodeName().equalsIgnoreCase("element")){
                        			
                        			dataStorageProductsNodeList = node.getChildNodes();
                        			j = 0;
                        			
                        			while (j < dataStorageProductsNodeList.getLength()){
                        				
                        				Node item = dataStorageProductsNodeList.item(j);                				
                        				if (item.getNodeName().equalsIgnoreCase("name")){                         					
                        					dataStorageProduct = new DataStorageProduct();
                        					dataStorageProductName=item.getTextContent();
                        					dataStorageProduct.setName(dataStorageProductName);
                        					mapdataStorageProducts.put(dataStorageProductName, dataStorageProduct);
                        				}                    				              				                					
                        				j++;                    				
                        			}//end while                    			
                        		}//end if                    		                    		
                        		i++;
                        	}//end while
                	
                    }//end if
	
            }//end if
            }//end if            
	}//end method
}//end class
	public static Project getProjectWithReusabilityConditions() {
		return projectWithReusabilityConditions;
	}
	public static void setProjectWithReusabilityConditions(
			Project projectWithReusabilityConditions) {
		Configuration.projectWithReusabilityConditions = projectWithReusabilityConditions;
	}
	
}
	
