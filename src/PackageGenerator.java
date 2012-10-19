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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class PackageGenerator {
	
	// XPath expressions for retrieving newsItem elements

	private final static String GUID_XPATH = "/newsItem/@guid"; 
	private final static String VERSION_XPATH = "/newsItem/@version"; 
	private final static String VERSION_CREATED_XPATH = "/newsItem/itemMeta/versionCreated";
	private final static String TYPE_ROLE_XPATH = "/newsItem/itemMeta/role/name";
	private final static String URGENCY_XPATH = "/newsItem/contentMeta/urgency";
	private final static String DEPARTMENT_XPATH = "/newsItem/contentMeta/subject[@type='cpnat:department']/name";
	private final static String CATEGORIES_XPATH = "/newsItem/contentMeta/subject[@type='cpnat:category']/name";
	private final static String SERVICE_NAME_XPATH = "/newsItem/itemMeta/service/name";
	private final static String LOCATION_XPATH = "/newsItem/contentMeta/located/name";
	private final static String CLASS_XPATH = "/newsItem/itemMeta/itemClass/@qcode";
	private final static String HEADLINE_XPATH = "/newsItem/contentMeta/headline";
	
	private String newsItemFolder;
	private ArrayList<NewsItem> newsItems;
	private PackageItem packageItem;
	
	private Document xmlPackageFile;
	
	public PackageGenerator(String newsItemFolder) {
		this.newsItemFolder = newsItemFolder;
		listItems();
		this.packageItem = generatePackage();
		writePackageToFile("package.xml");
	}
	
	private void listItems() {
		
		this.newsItems = new ArrayList<NewsItem>();
		
		// List all the files that end with '.xml' in the given folder
		File[] allNewsItems = new File(this.newsItemFolder).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if (file.isFile() && file.getName().endsWith(".xml")) return true;
				return false;
			}
		});
		
		// Process all newsItem XML documents using Java DOM (Document Object Model)
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		// Reads all the XML documents listed
		Document xmlDocument = null;
		XPath xpath = XPathFactory.newInstance().newXPath();
		for (File newsItemFile : allNewsItems) {
			
			try {
				xmlDocument = documentBuilder.parse(newsItemFile);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
                e.printStackTrace();
            }
		
			XPathExpression expr;
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
				String version_created = nodes.item(0).getTextContent();
				newsItem.getItemMeta().setVersionCreated(version_created);
				
				//Get type of news item article
				expr = xpath.compile(TYPE_ROLE_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String role = nodes.item(0).getTextContent();
				newsItem.getItemMeta().setRole(role);
				
				expr = xpath.compile(DEPARTMENT_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String department = nodes.item(0).getTextContent();
				newsItem.getContentMeta().getSubject().setDepartment(department);
				
				//Get NewsItem categories
				expr = xpath.compile(CATEGORIES_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				ArrayList<String> categories = new ArrayList<String>();
				for (int i = 0; i < nodes.getLength(); i++) {
					categories.add(nodes.item(i).getTextContent());
				}
				newsItem.getContentMeta().getSubject().setCategories(categories);
				

				
				//Get name of news item article
				expr = xpath.compile(SERVICE_NAME_XPATH);
				nodes =(NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String service_name = nodes.item(0).getTextContent();
				newsItem.getItemMeta().setServiceName(service_name);
				
				//Get location of news item article
				expr = xpath.compile(LOCATION_XPATH);
				nodes =(NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String location = nodes.item(0).getTextContent();
				newsItem.getContentMeta().setLocation(location);
				
				//Get the class of news item
				expr = xpath.compile(CLASS_XPATH);
				nodes =(NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String item_class = nodes.item(0).getTextContent();
				newsItem.getItemMeta().setItemClass(item_class);
				
				//Get headline of news item
				expr = xpath.compile(HEADLINE_XPATH);
				nodes =(NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String headline = nodes.item(0).getTextContent();
				newsItem.getContentMeta().setHeadline(headline);
				
				//Get NewsItem urgency
				expr = xpath.compile(URGENCY_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String urgency = nodes.item(0).getTextContent();
				newsItem.getContentMeta().setUrgency(urgency);
				
				// Adds current news item to newsItems-list
				newsItems.add(newsItem);
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Method for generating packageItem from newsItem list.
	 */
	
	private PackageItem generatePackage() {

		// Finds all items from specific department
	    ArrayList<NewsItem> newsItems = getNewsItemsByDepartment("Kotimaa");
		
		//Collections.sort(packageItems, new NewsItemComparator());
		Collections.sort(newsItems, new NewsItemComparator());
		
		// Creates packageItem containing first 10 items
		int items = 10;
		if (newsItems.size() < 10) items = newsItems.size();
		
		PackageItem packageItem = new PackageItem();
		
		for (int i = 0; i < items; i++) {
			System.out.println("Adding news item " + newsItems.get(i).getGuid() + " (" + newsItems.get(i).getItemMeta().getVersionCreated() + ")");
			packageItem.addNewsItem(newsItems.get(i));
		}
		return packageItem;
	}
	
	public PackageItem getPackage() {
	    return this.packageItem;
	}
	
	public ArrayList<NewsItem> getNewsItemsByDepartment(String department) {
	    ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
        for (int i = 0; i < newsItems.size(); i++) {
            NewsItem item = newsItems.get(i);
            System.out.println(item.getContentMeta().getSubject().getDepartment());
            if (item.getContentMeta().getSubject().getDepartment().equals(department)) { // You can use your own rules here.
                newsItems.add(item);
            }
        }
        return newsItems;
	}

	/*
	 * Method for storing packageItem as a XML document.
	 */
	
	private void writePackageToFile(String filePath) {
		
		try {
            /////////////////////////////
            //Creating an empty XML Document

            //We need a Document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            this.xmlPackageFile = docBuilder.newDocument();

            ////////////////////////
            //Creating the XML tree

            //create the root element (packageItem in our case)
            Element root = this.xmlPackageFile.createElement("packageItem");
            //add all attributes
            root.setAttribute("standard", this.packageItem.getStandard());
            root.setAttribute("standardversion", this.packageItem.getStandardVersion());
            root.setAttribute("conformance", this.packageItem.getConformance());
            root.setAttribute("xmlns", this.packageItem.getXlmns());
            root.setAttribute("xmlns:xsi", this.packageItem.getXlmnsXsi());
            root.setAttribute("xsi:schemaLocation", this.packageItem.getXsiSchemaLocation());
            root.setAttribute("guid", this.packageItem.getGuid());
            this.xmlPackageFile.appendChild(root);

            //create a comment and put it in the root element
            //Comment comment = this.xmlPackageFile.createComment("Just a thought");
            //root.appendChild(comment);

            //create child element, add an attribute, and add to root
            Element itemMeta = this.xmlPackageFile.createElement("itemMeta");
            Element contentMeta = this.xmlPackageFile.createElement("contentMeta");
            root.appendChild(itemMeta);
            root.appendChild(contentMeta);
            
            //itemMeta elements
            Element itemClass = this.xmlPackageFile.createElement("itemClass");
            itemClass.setAttribute("qcode", this.packageItem.getItemClass());
            Element provider = this.xmlPackageFile.createElement("provider");
            provider.setAttribute("literal", this.packageItem.getProvider());
            Element versionCreated = this.xmlPackageFile.createElement("versionCreated");
            versionCreated.setTextContent(this.packageItem.getVersionCreated());
            itemMeta.appendChild(itemClass);
            itemMeta.appendChild(provider);
            itemMeta.appendChild(versionCreated);
            

            /////////////////
            //Output the XML

            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(this.xmlPackageFile);
            trans.transform(source, result);
            String xmlString = sw.toString();

            //print xml
            System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlString);

        } catch (Exception e) {
            System.out.println(e);
        }
	}
	
	/**
	 * Comparator class for sorting NewsItems by date they were sent to customers.
	 */
	
	private class NewsItemComparator implements Comparator<NewsItem>{
		@Override
		public int compare(NewsItem item1, NewsItem item2) {
			return item1.getItemMeta().getVersionCreatedDate().compareTo(item2.getItemMeta().getVersionCreatedDate());
		}
	}

	
	/*
     * Main method
     */
	public static void main(String[] args) {
		PackageGenerator packageGenerator = new PackageGenerator("./stt_lehtikuva_newsItems");
		//System.out.println(packageGenerator.getPackage().getVersionCreated());
	}

	
	
}
