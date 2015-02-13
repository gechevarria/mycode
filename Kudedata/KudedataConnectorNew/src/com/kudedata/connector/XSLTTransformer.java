package com.kudedata.connector;

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
	public static void main(String[] args) throws IOException, URISyntaxException, TransformerException {
		//File newFile = new File("order.html");
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("/home/cloudlab/git/mycode/Kudedata/XSLTransformer/xslts/order.xslt"));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new File("/home/cloudlab/git/mycode/Kudedata/XSLTransformer/xml/order.xml"));
        transformer.transform(text, new StreamResult(new File("/home/cloudlab/git/mycode/Kudedata/XSLTransformer/html/order.html")));
        System.out.println(System.getProperty("user.name"));
    }

}
