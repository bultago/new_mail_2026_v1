package com.terracetech.tims.webmail.util;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class XMLUtils {

	public static void writeTo(Document document, HttpServletResponse response) throws Exception {
		if(document != null){
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
	
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
			DOMSource source = new DOMSource(document);
			response.setHeader("Content-Type", "application/xml; charset=UTF-8");
			PrintWriter wout = response.getWriter();
			StreamResult result = new StreamResult(wout);
	
			transformer.transform(source, result);
		}
	}

	public static void writeTo(Document document, HttpServletResponse response, String xsl) throws Exception {
		if(document != null){
			Source xslSource = new StreamSource(xsl);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer(xslSource);
	
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
			DOMSource source = new DOMSource(document);
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			PrintWriter wout = response.getWriter();
			StreamResult result = new StreamResult(wout);
	
			transformer.transform(source, result);
		}
	}
}
