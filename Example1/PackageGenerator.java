package aalto.media.newsml;

/**
 * PackageGenerator class that includes methods for
 * listing files in folder and reading XML documents, and
 * processing of their contents.
 * 
 * Add more code for NewsML-G2 package generation.
 */

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PackageGenerator {
	
	//XPath expressions for retrieving rnewsItem elements
	private final static String GUID_XPATH = "/newsItem/@guid"; 
	private final static String VERSION_XPATH = "/newsItem/@version"; 
	private final static String VERSION_CREATED_XPATH = "/newsItem/itemMeta/versionCreated";
	private final static String TYPE_ROLE_XPATH = "/newsItem/itemMeta/role/name";
	private final static String CATEGORIES_XPATH = "/newsItem/contentMeta/subject[@type='cpnat:category']/name";
	
	private String newsItemFolder;
	private ArrayList<NewsItem> newsItems;
	
	public PackageGenerator(String newsItemFolder) {
		this.newsItemFolder = newsItemFolder;
		listItems();
	}
	
	private void listItems() {
		
		newsItems = new ArrayList<NewsItem>();
		
		// List all file in given folder that ends with '.xml'
		File[] allNewsItems = new File(newsItemFolder).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if (file.isFile() && file.getName().endsWith(".xml")) return true;
				return false;
			}
		});
		
		// Process all newsItem XML documents using Java DOM
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document xmlDocument = null;
		XPath xpath = XPathFactory.newInstance().newXPath();
		// Read all the XML documents
		for (File newsItemFile : allNewsItems) {
			try {
				xmlDocument = documentBuilder.parse(newsItemFile);
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			XPathExpression expr;
			Object result;
			NodeList nodes;
			
			NewsItem newsItem = new NewsItem();
			try {
				//Get guid of the NewsItem
				expr = xpath.compile(GUID_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String guid = nodes.item(0).getTextContent();
				newsItem.setGuid(guid);
				
				//Get version of the NewsItem
				expr = xpath.compile(VERSION_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String version = nodes.item(0).getTextContent();
				newsItem.setVersion(version);
				
				//Get date and time when current version of the NewsItem was sent
				expr = xpath.compile(VERSION_CREATED_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String versionCreated = nodes.item(0).getTextContent();
				newsItem.setVersion_created(versionCreated);
				
				//Get type of news item article
				expr = xpath.compile(TYPE_ROLE_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String typeRole = nodes.item(0).getTextContent();
				newsItem.setType_role(typeRole);
				
				//Get NewsItem categories
				expr = xpath.compile(CATEGORIES_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String[] categories = new String[nodes.getLength()];
				for (int i = 0; i < nodes.getLength(); i++) {
					categories[i] = nodes.item(i).getTextContent();
				}
				newsItem.setCategories(categories);
				
				/*
				 * Add more here
				 */
				
				// Add current news item to newsItems-list
				newsItems.add(newsItem);
				System.out.println(newsItem.toString());
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PackageGenerator packageGenerator = 
				new PackageGenerator("path_to_news_items");
	}

}
