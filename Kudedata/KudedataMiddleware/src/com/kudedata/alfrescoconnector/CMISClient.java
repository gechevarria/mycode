package com.kudedata.alfrescoconnector;


/**
* This code uses Apache Chemistry (http://chemistry.apache.org/).
* License accords to Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
*/

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.lang3.StringUtils;


public class CMISClient {

	private static Session session;	
	private static final String ALFRSCO_ATOMPUB_URL = "http://localhost:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
	private static final String REPOSITORY_ID = "default";
	private static final String TEST_FOLDER_NAME = "chemistryTestFolder1";
	private static final String TEST_DOCUMENT_NAME_1 = "chemistryTest1.txt";
	private static final String TEST_DOCUMENT_NAME_2 = "chemistryTest2.txt";

	public static void main (String[] args){
		getDocumentNameInFolder("CAMEPACK");
	}

	/**
	 * returns the names first pending to be processed document
	 * @param idEmpresa
	 * @return
	 */
	protected static String getDocumentNameInFolder(String idEmpresa) {
		connect();
		String pendingDocumentName = "";
		String companyFolderId = getCompanyFolderId (idEmpresa);
				

		CmisObject object = session.getObject(session.createObjectId(companyFolderId));
		if (object==null)
			return pendingDocumentName;
		Folder folder = (Folder)object;	
		
		ItemIterable<CmisObject> children = folder.getChildren();
		if (children==null)
			return pendingDocumentName;
		if (children.getTotalNumItems()==0)
			return pendingDocumentName;
		Iterator<CmisObject> iterator = children.iterator();
		if (iterator.hasNext()){
			CmisObject document = iterator.next();
			pendingDocumentName = document.getName();
			document.rename(pendingDocumentName.replaceAll(".html", "_Processed.html"));
		}
		return pendingDocumentName;
	}

	/**
	 * returns the company pending to be processed folder given the id
	 * @param idEmpresa
	 * @return
	 */
	private static String getCompanyFolderId(String idEmpresa) {
		String folderId = null;
		switch (idEmpresa) {
		case "UKABI":
			folderId = "48b3544c-9967-4ccf-98a9-50cba912baa6";			
			break;
		case "LOIOLA":
			folderId = "a3e57d51-60cd-42b1-8a39-dfb00893f24c";
			break;
		case "TECNALIA":
			folderId = "0caa6c14-e28c-423e-bb36-1f2b596025c4";
			break;
		case "CAMEPACK":
			folderId = "f78326e8-fa19-46aa-ac99-3e25439b18d2";
			break;
		case "ALBERDI":
			folderId = "d0ce2b46-c94f-418d-b131-c6cbedb966f7";
			break;
		case "P3SUMINISTROS":
			folderId = "5f6151c6-75b3-4187-88ed-eea748dab335";
			break;
		default:
			break;
		}
				
		return folderId;
		
	}

	/**
	 * Clean up test folder before executing test
	 * 
	 * @param target
	 * @param delFolderName
	 */
	private static void cleanup(Folder target, String delFolderName) {
		try {
			CmisObject object = session.getObjectByPath(target.getPath()
					+ delFolderName);
			Folder delFolder = (Folder) object;
			delFolder.deleteTree(true, UnfileObject.DELETE, true);
		} catch (CmisObjectNotFoundException e) {
			System.err.println("No need to clean up.");
		}
	}

	/**
	 * 
	 * @param target
	 */
	private static void listFolder(int depth, Folder target) {
		String indent = StringUtils.repeat("\t", depth);
		for (Iterator<CmisObject> it = target.getChildren().iterator(); it
				.hasNext();) {
			CmisObject o = it.next();
			if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
				System.out.println(indent + "[Docment] " + o.getName());
			} else if (BaseTypeId.CMIS_FOLDER.equals(o.getBaseTypeId())) {
				System.out.println(indent + "[Folder] " + o.getName());
				listFolder(++depth, (Folder) o);
			}
		}

	}

	/**
	 * Delete test document
	 * 
	 * @param target
	 * @param delDocName
	 */
	private static void DeleteDocument(Folder target, String delDocName) {
		try {
			CmisObject object = session.getObjectByPath(target.getPath()
					+ delDocName);
			Document delDoc = (Document) object;
			delDoc.delete(true);
		} catch (CmisObjectNotFoundException e) {
			System.err.println("Document is not found: " + delDocName);
		}
	}

	/**
	 * Create test document with content
	 * 
	 * @param target
	 * @param newDocName
	 */
	private static void createDocument(Folder target, String newDocName) {
		Map<String, String> props = new HashMap<String, String>();
		props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		props.put(PropertyIds.NAME, newDocName);
		System.out.println("This is a test document: " + newDocName);
		String content = "aegif Mind Share Leader Generating New Paradigms by aegif corporation.";
		byte[] buf = null;
		try {
			buf = content.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream input = new ByteArrayInputStream(buf);
		ContentStream contentStream = session.getObjectFactory()
				.createContentStream(newDocName, buf.length,
						"text/plain; charset=UTF-8", input);
		target.createDocument(props, contentStream, VersioningState.MAJOR);
	}

	/**
	 * Create test folder directly under target folder
	 * 
	 * @param target
	 * @param createFolderName
	 * @return newly created folder
	 */
	private static Folder createFolder(Folder target, String newFolderName) {
		Map<String, String> props = new HashMap<String, String>();
		props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		props.put(PropertyIds.NAME, newFolderName);
		Folder newFolder = target.createFolder(props);
		return newFolder;
	}

	/**
	 * Connect to alfresco repository
	 * 
	 * @return root folder object
	 */
	private static Folder connect() {
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put(SessionParameter.USER, "admin");
		parameter.put(SessionParameter.PASSWORD, "admin");
		parameter.put(SessionParameter.ATOMPUB_URL, ALFRSCO_ATOMPUB_URL);
		parameter.put(SessionParameter.BINDING_TYPE,
				BindingType.ATOMPUB.value());		
		Repository repository = sessionFactory.getRepositories(parameter).get(0);
		session = repository.createSession();
		return session.getRootFolder();
	}
	
	

}
