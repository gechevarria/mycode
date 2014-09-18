package org.tecnalia.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.tecnalia.structures.Project;

import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;

public class MarshallCode {

	public static void marshallProjectXML(Project p) throws Exception{
		// Output XML
		JAXBContext jc = JAXBContext.newInstance(Project.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		 
		marshaller.marshal(p, System.out);

	}
	public static void marshallProjectJSON(Project p) throws Exception{
	    // Output JSON
	    JAXBContext jcontext = JSONJAXBContext.newInstance( Project.class );

	    Marshaller jmar = jcontext.createMarshaller();
	    JSONMarshaller jmarshaller = JSONJAXBContext.getJSONMarshaller( jmar, jcontext );
	    jmarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
	    jmarshaller.marshallToJSON( p , System.out );

	}
	
}
