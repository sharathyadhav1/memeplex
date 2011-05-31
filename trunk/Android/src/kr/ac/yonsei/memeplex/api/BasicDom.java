package kr.ac.yonsei.memeplex.api;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.util.Log;

public class BasicDom {
	// Parses an XML file and returns a DOM document.
    // If validating is true, the contents is validated against the DTD
    // specified in the file.
    public static Document parseUri(String uri, boolean validating) {
        try {
            // Create the builder and parse the file
            URL url = new URL(uri);
            URLConnection urlconn = url.openConnection();

            Document doc = parseInputStream(urlconn.getInputStream(), validating);
            
            return doc;
        }
        catch(Exception e)
        {
        	Log.w("parse", e);
        }
        
        return null;
    }
    
    public static Document parseInputStream(InputStream is, boolean validating) {
    	try {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validating);

            // Create the builder and parse the file
            Document doc = factory.newDocumentBuilder().parse(is);
            return doc;
        }
        catch(Exception e)
        {
        	Log.w("parse", e);
        }
        
        return null;
    }
}
