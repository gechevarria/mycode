/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tecnalia.structures;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author 106363
 */
@XmlRootElement(name="Class")
public class SourceFile {

    private String name = "";
    private String packageName = "";
    private ArrayList<Method> methods = new ArrayList<Method>();

@XmlElement(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

@XmlElement(name="packageName")
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


@XmlElement(name="method")
    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }
    
    public void addLinea(Method l) {
        this.methods.add(l);
    }
    
    
    
}
