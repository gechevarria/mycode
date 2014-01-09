/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tecnalia.structures;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author 106363
 */
@XmlRootElement(name="Method")
public class Method {

    private String name = "";
    private String code = "";
    private String bline = "";
    private String eline = "";
    private String reusable = "";
    private String automaticReusabilityChangedByUser =""; 

@XmlElement(name="code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
@XmlElement(name="bline")  
    public String getBline() {
		return bline;
	}

	public void setBline(String bline) {
		this.bline = bline;
	}
	
@XmlElement(name="eline")
	public String getEline() {
		return eline;
	}

	public void setEline(String eline) {
		this.eline = eline;
	}

@XmlElement(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



@XmlElement(name="reusable")
    public String getReusable() {
        return reusable;
    }

    public void setReusable(String reusable) {
        this.reusable = reusable;
    }

@XmlElement(name="automaticReusabilityChangedByUser")
	public String getAutomaticReusabilityChangedByUser() {
		return automaticReusabilityChangedByUser;
	}

	public void setAutomaticReusabilityChangedByUser(
			String automaticReusabilityChangedByUser) {
		this.automaticReusabilityChangedByUser = automaticReusabilityChangedByUser;
	}

    
}
