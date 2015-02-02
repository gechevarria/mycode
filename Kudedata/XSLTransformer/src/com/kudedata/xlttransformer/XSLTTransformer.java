package com.kudedata.xlttransformer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTTransformer {
	private static String EDIMessage = "invoic";
	
	
	public static void main(String[] args) throws IOException, URISyntaxException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/XSLTransformer/xslts/"+EDIMessage+".xslt"));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new File("D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/XSLTransformer/xml/"+EDIMessage+".xml"));
        transformer.transform(text, new StreamResult(new File("D:/Projects/KUDETADA/eclipse-standard-luna-R-win32-x86_64/workspace/XSLTransformer/html/"+EDIMessage+".html")));
    }

}
