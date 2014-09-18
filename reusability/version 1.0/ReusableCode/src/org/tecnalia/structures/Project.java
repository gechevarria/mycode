/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tecnalia.structures;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


/**
 *
 * @author 106363
 */
@XmlRootElement(name="Project")
public class Project {

    private String name = "";
    private String presentationPath = "";
    private String businessPath = "";
    private ArrayList<SourceFile> sourcefiles = new ArrayList<SourceFile>();
    
@XmlElement(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="presentationPath")
    public String getPresentationPath() {
        return presentationPath;
    }

    public void setPresentationPath(String presentationPath) {
        this.presentationPath = presentationPath;
    }

    @XmlElement(name="businessPath")
    public String getBusinessPath() {
        return businessPath;
    }

    public void setBusinessPath(String businessPath) {
        this.businessPath = businessPath;
    }

@XmlElement(name="sourcefile")
    public ArrayList<SourceFile> getSourceFile() {
        return sourcefiles;
    }

    public void setSourceFile(ArrayList<SourceFile> methods) {
        this.sourcefiles = methods;
    }
    
    public void addSourceFile(SourceFile l) {
        this.sourcefiles.add(l);
    }
    
    
    
}
