package org.tecnalia.artist;

import java.io.File;

import org.tecnalia.artist.util.file.ClassExplorer;
import org.tecnalia.artist.util.file.Searcher;
import org.tecnalia.structures.Project;
import org.tecnalia.structures.SearchResult;
import org.tecnalia.utils.MarshallCode;

public class Start {

	public static void main(String[] args) throws Exception{

		File folder = new File("D:/TECNALIA/PROYECTOS/ARTIST/eclipse-modeling-kepler/workspace/javapetstore-2.0-ea5");
		
		
		Searcher s= new Searcher(); 
		s.addType("java");
		s.addParam("SELECT");
		s.searchInFolder(folder);
		
		
		
		ClassExplorer ce= new ClassExplorer();
		/*
		ce.setPath("file:///D:/Proyectos/Artist/javapetstore-2.0-ea5/bin/");
		ce.findMethods("com.sun.javaee.blueprints.petstore.captcha.BlueFilter");
		ce.findMethods("com.sun.javaee.blueprints.petstore.captcha.RandomString");
		*/
		Project p= new Project();
		p.setName(folder.getPath());
		p.setBusinessPath(folder.getPath());
		
		
		System.out.println();
		System.out.println("****** Classes reutilizables totalmente");
		for (SearchResult sr : s.getNoResult()) {
			File sampleFile = new File(sr.getFileName());
			System.out.println(sr.getFileName());
			p.addSourceFile(ce.findMethodsParser(sampleFile));
			
		}

		System.out.println();
		System.out.println("****** Classes reutilizables parcialmente");
		for (SearchResult sr : s.getResult()) {
			File sampleFile = new File(sr.getFileName());
			System.out.println(sr.getFileName());
			p.addSourceFile(ce.findMethodsParser(sampleFile, sr.getLines()));
			
		}
		
		MarshallCode.marshallProjectXML(p);
		//MarshallCode.marshallProjectJSON(p);
		
	}

}
