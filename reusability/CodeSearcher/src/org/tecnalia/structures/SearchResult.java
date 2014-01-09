package org.tecnalia.structures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SearchResult")
public class SearchResult {
	
    private String fileName = "";
    private ArrayList<Integer> lines = new ArrayList<Integer>();

    @XmlElement(name="packageName")
    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@XmlElement(name="lines")
	public ArrayList<Integer> getLines() {
		return lines;
	}
	public void setLines(ArrayList<Integer> lines) {
		this.lines = lines;
	}
	
    public void addLinea(Integer l) {
        this.lines.add(l);
    }
	
	

}
