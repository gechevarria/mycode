package com.kudedata.aux;

import java.io.File;
import java.io.FilenameFilter;

/** clase filtro utilizada para buscar ficheros por nombre
 * @author alfresco
 *
 */
public class FilterFile implements FilenameFilter{
	private String filter = "";
    public FilterFile(String filter) {
    	this.filter=filter;		
	}

	public boolean accept(File dir, String nameFile)
    {
        return nameFile.contains(filter);
}
}