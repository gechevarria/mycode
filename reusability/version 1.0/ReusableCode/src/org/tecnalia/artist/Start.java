package org.tecnalia.artist;


import org.tecnalia.structures.Method;
import org.tecnalia.structures.Project;
import org.tecnalia.structures.SourceFile;
import org.tecnalia.utils.MarshallCode;

public class Start {

	public static void main(String[] args) throws Exception{
		
		Project p= new Project();
		p.setName("nombreProyecto");
		Method m= new Method();
		m.setName("nombreMetodo");
		
		m.setCode("codigo del metodo");
		SourceFile c= new SourceFile();
		c.setName("nombreClase");
		c.setPackageName("org.package");
		c.addLinea(m);
		c.addLinea(m);
		c.addLinea(m);
		p.addSourceFile(c);
		c= new org.tecnalia.structures.SourceFile();
		c.setName("nombreClase2");
		c.setPackageName("org.package2");
		c.addLinea(m);
		p.addSourceFile(c);
		
		MarshallCode.marshallProjectXML(p);
		MarshallCode.marshallProjectJSON(p);
	    
	}

}
