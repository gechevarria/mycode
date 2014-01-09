package org.tecnalia.artist.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.tecnalia.structures.SearchResult;


public class Searcher {
	
	private List<String> fileTypes= new ArrayList<String>();
	private List<String> searchParam= new ArrayList<String>();
	private List<SearchResult> searchResult= new ArrayList<SearchResult>();
	private List<SearchResult> searchNoResult= new ArrayList<SearchResult>();
	
	public void addType(String obj) {
		fileTypes.add(obj);
	}
	
	public void addParam(String obj) {
		searchParam.add(obj);
	}

	private void addResult(SearchResult obj) {
		searchResult.add(obj);
	}

	private void addNoResult(SearchResult obj) {
		searchNoResult.add(obj);
	}

	public List<SearchResult> getResult() {
		return searchResult;
	}
	public List<SearchResult> getNoResult() {
		return searchNoResult;
	}

	
	public void searchInFolder(File folder) throws Exception{
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	searchInFolder(fileEntry);
	        } else {
	        	for (String ext : fileTypes) {
	        		String extension = fileEntry.getName().substring((fileEntry.getName().lastIndexOf('.')+1));
	        		if(extension.equals(ext)){
	        			ArrayList<Integer> lines =searchInFile(fileEntry);
	        			SearchResult obj= new SearchResult();
	        			obj.setFileName(fileEntry.getPath());
	        			obj.setLines(lines);
	        			if(lines.size()>0){
		        			System.out.println(fileEntry.getPath());
		        			addResult(obj);
	        			}else{
		        			addNoResult(obj);
	        			}
	        			
	        		}
	    		}
	        }
	    }
	}
	
	private ArrayList<Integer> searchInFile(File file) throws Exception{
		
		ArrayList<Integer> retorno= new ArrayList<Integer>();
		Scanner scanner = new Scanner(file);
		
		int linNum=0;
		while(scanner.hasNext())
		{
			linNum++;
			String line=scanner.nextLine();
        	for (String param : searchParam) {
				int linePos=line.indexOf(param);
				if(linePos>=0)
			    {
			        System.out.println("Line "+linNum+":  "+line);
			        retorno.add(linNum);
			    }
			}
		}	
		return retorno;
	}
	
}
