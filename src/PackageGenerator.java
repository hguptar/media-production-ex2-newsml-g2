/**
 * PackageGenerator class that includes methods for
 * listing files in folder and reading XML documents, and
 * processing of their contents.
 * 
 * Add more code for NewsML-G2 package generation.
 */

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

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
	private final static String TOPICS_XPATH = "/newsItem/contentMeta/subject[@type='cpnat:topic']/name";
	private final static String TOPICS_CODE_XPATH = "/newsItem/contentMeta/subject[@type='cpnat:topic']/@qcode";
	private final static String SERVICE_NAME_XPATH = "/newsItem/itemMeta/service/name";
	private final static String LOCATION_XPATH = "/newsItem/contentMeta/located/name";
	private final static String CLASS_XPATH = "/newsItem/itemMeta/itemClass/@qcode";
	private final static String HEADLINE_XPATH = "/newsItem/contentMeta/headline";
	
	//Id of the root group
	private final static String ROOT_GROUP = "root";
	
	//Type of groupItem
	private final static String ROLE_ROOT = "group:root";
	private final static String ROLE_CLASSIC = "group:package";
	
	private int type_attribute;
	private String value_attribute;
	
	
	private String newsItemFolder;
	private ArrayList<NewsItem> newsItems;
	private PackageItem packageItem;
	
	private Document xmlPackageFile;
	
	public PackageGenerator(String newsItemFolder,int type_attribute, String value_attribute, String file_name) {
		this.type_attribute = type_attribute;
		this.value_attribute = value_attribute;
		this.newsItemFolder = newsItemFolder;
		listItems();
		this.packageItem = generatePackage();
		writePackageToFile("./" + file_name);
		System.out.print("Your package xml file has been generated.\n");
	}
	
	private void listItems() {
		
		this.newsItems = new ArrayList<NewsItem>();
		System.out.print("The application is reading the news items in the folder : " +  this.newsItemFolder +"\n");
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
				
				//Get NewsItem topic
				expr = xpath.compile(TOPICS_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				if(nodes.item(0) != null) {
					String topic = nodes.item(0).getTextContent();
					newsItem.getContentMeta().getSubject().setTopic(topic);
				} else {
					newsItem.getContentMeta().getSubject().setTopic("");
				}
				
				//Get NewsItem topic_code
				expr = xpath.compile(TOPICS_CODE_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				if(nodes.item(0) != null) {
					String topic_code = nodes.item(0).getTextContent();
					newsItem.getContentMeta().getSubject().setTopicCode(topic_code);
				} else {
					newsItem.getContentMeta().getSubject().setTopicCode("");
				}
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
				
				
				newsItem.setSize(newsItemFile.getTotalSpace());
				
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
		System.out.print("The application is starting the generation of your package\n");
		ArrayList<NewsItem> newsItems;
		int items = 0;
		// Finds all items from specific department
		switch(this.type_attribute) {
			case 1:
				newsItems = getNewsItemsByTopic(this.value_attribute);
				break;
				
			case 2:
				newsItems = getNewsItemsByDepartment(this.value_attribute);
				break;
				
			case 3:
				newsItems = getNewsItemsByCategories(this.value_attribute);
				break;
				
			case 4:
				newsItems = getNewsItemsByCategories(this.value_attribute);
				break;
				
			default:
				newsItems = getNewsItemsByTopic(this.value_attribute);
				break;
		}
	    
		
		//Collections.sort(packageItems, new NewsItemComparator());
		Collections.sort(newsItems, new NewsItemComparator());
		
		//If we want to generate a package of last news
		if(this.type_attribute == 2 || this.type_attribute == 3)  {
			items = 10;
			if (newsItems.size() < 10) items = newsItems.size();
		} else {
			items = newsItems.size();
		}
		
		PackageItem packageItem = new PackageItem();
		
		//Initialize package ItemMeta
		packageItem.getItemMeta().setItem_class("ninat:composite");
		packageItem.getItemMeta().setProvider("Aalto University Group Media");
		packageItem.getItemMeta().setVersion_created(new Date());
		packageItem.getItemMeta().setFirst_created(new Date());
		packageItem.getItemMeta().setPub_status("stat:usable");
		packageItem.getItemMeta().setGenerator_version("1.0");
		packageItem.getItemMeta().setGenerator_text("Package Generator v1");
		packageItem.getItemMeta().setProfile_version("1.0");
		packageItem.getItemMeta().setProfile_text("ranked_idref_list");
		packageItem.getItemMeta().setService_code("svc:aaltotop");
		packageItem.getItemMeta().setService_name("Aalto University");
		packageItem.getItemMeta().setTitle("MY PACKAGE");
		packageItem.getItemMeta().setEd_note("DEFINE A NOTE");
		packageItem.getItemMeta().setSignal_code("act:replacePrev");
		packageItem.getItemMeta().setLing_residref("irel:previousVersion");
		packageItem.getItemMeta().setLink_rel("NO TAG");
		packageItem.getItemMeta().setLink_version("1");
		
		//Initialize package ContentMeta
		packageItem.getContentMeta().getContributor().setJobtitle("staffjobs:cpe");
		packageItem.getContentMeta().getContributor().setJob_name("Chief Packaging Editor");
		packageItem.getContentMeta().getContributor().setName("Maxime Andre");
		packageItem.getContentMeta().getContributor().setQcode("mystaff:MAndre");
		packageItem.getContentMeta().getContributor().setNote_text("Available everyday");
		packageItem.getContentMeta().getContributor().setNote_validto("2013-12-31T17:30:00Z");
		packageItem.getContentMeta().getContributor().setDef_validto("2013-12-31T17:30:00Z");
		packageItem.getContentMeta().getContributor().setDef_text("Duty Packaging Editor");
		
		packageItem.addNewGroup(ROOT_GROUP, ROLE_ROOT, "");
		
		if(items > 0) {
			String groupName = packageItem.addNewGroup("", ROLE_CLASSIC, ROOT_GROUP);

			for (int i = 0; i < items; i++) {
				System.out.println("Adding news item " + newsItems.get(i).getGuid() + " (" + newsItems.get(i).getItemMeta().getVersionCreated() + ")");
				packageItem.addNewsItem(newsItems.get(i), groupName);
			}
		} else {
			System.out.print("YOUR PACKAGE DOESN'T CONTAIN ANY NEWSITEM\n");
		}
		
		return packageItem;
	}
	
	public PackageItem getPackage() {
	    return this.packageItem;
	}
	
	public ArrayList<NewsItem> getNewsItemsByDepartment(String department) {
		System.out.print("The application is selecting news item where the department is :" + department + "\n");
	    ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
        for (int i = 0; i < this.newsItems.size(); i++) {
            NewsItem item = this.newsItems.get(i);
            if (item.getContentMeta().getSubject().getDepartment().equals(department)) {
                newsItems.add(item);
            }
        }
        return newsItems;
	}
	
	public ArrayList<NewsItem> getNewsItemsByTopic(String topic) {
		System.out.print("The application is selecting news item where the topic is :" + topic + "\n");
	    ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
        for (int i = 0; i < this.newsItems.size(); i++) {
            NewsItem item = this.newsItems.get(i);
            if (item.getContentMeta().getSubject().getTopic().equals(topic)) {
                newsItems.add(item);
            } else if(item.getContentMeta().getSubject().getTopicCode().equals(topic)) {
            	 newsItems.add(item);
            }
        }
        return newsItems;
	}
	
	public ArrayList<NewsItem> getNewsItemsByCategories(String category) {
		System.out.print("The application is selecting news item where the category is :" + category + "\n");
		ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
		boolean addedItem = false;
        for (int i = 0; i < this.newsItems.size(); i++) {
            NewsItem item = this.newsItems.get(i);
            for(String cat : item.getContentMeta().getSubject().getCategories()) {
	            if (cat.equals(category) && addedItem == false) {
	                newsItems.add(item);
	            }
            }
        }
        return newsItems;
	}
	/*
	 * Method for storing packageItem as a XML document.
	 */
	
	private void writePackageToFile(String filePath) {
		System.out.print("The application is now writing the XML File of your package\n");
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


            //create child element, add an attribute, and add to root
            Element itemMeta = generateItemMetaXml();
            Element contentMeta = generateContentMetaXml();
            Element groupSet = generateGroupSetXml();
            
            root.appendChild(itemMeta);
            root.appendChild(contentMeta);
            root.appendChild(groupSet);            
            
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
            try{
                PrintWriter out  = new PrintWriter(new FileWriter(filePath));
                out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlString);
                out.close();
            } catch(Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
	}
	
	//Generate the Xml for the ItemMeta element
	private Element generateItemMetaXml() {
		Element itemMeta = this.xmlPackageFile.createElement("itemMeta");
		
		Element itemClass = this.xmlPackageFile.createElement("itemClass");
        itemClass.setAttribute("qcode", this.packageItem.getItemMeta().getItem_class());
        
        Element provider = this.xmlPackageFile.createElement("provider");
        provider.setAttribute("literal", this.packageItem.getItemMeta().getProvider());
        
        Element versionCreated = this.xmlPackageFile.createElement("versionCreated");
        versionCreated.setTextContent(this.packageItem.getItemMeta().getVersion_created().toString());
        
        Element firstCreated = this.xmlPackageFile.createElement("firstCreated");
        firstCreated.setTextContent(this.packageItem.getItemMeta().getFirst_created().toString());
        
        Element pubStatus = this.xmlPackageFile.createElement("pubStatus");
        pubStatus.setAttribute("qcode",this.packageItem.getItemMeta().getPub_status());
        
        Element generator = this.xmlPackageFile.createElement("generator");
        generator.setAttribute("versioninfo", this.packageItem.getItemMeta().getGenerator_version());
        generator.setTextContent(this.packageItem.getItemMeta().getGenerator_text());
        
        Element profile = this.xmlPackageFile.createElement("profile");
        profile.setAttribute("versioninfo", this.packageItem.getItemMeta().getProfile_version());
        profile.setTextContent(this.packageItem.getItemMeta().getProfile_text());
        
        Element service = this.xmlPackageFile.createElement("service");
        Element serviceName = this.xmlPackageFile.createElement("name");
        service.setAttribute("qcode", this.packageItem.getItemMeta().getService_code());
        serviceName.setTextContent(this.packageItem.getItemMeta().getService_name());
        service.appendChild(serviceName);
        
        Element title = this.xmlPackageFile.createElement("title");
        title.setTextContent(this.packageItem.getItemMeta().getTitle());
        
        Element edNote = this.xmlPackageFile.createElement("edNote");
        edNote.setTextContent(this.packageItem.getItemMeta().getEd_note());
        
        Element signal = this.xmlPackageFile.createElement("signal");
        signal.setAttribute("qcode", this.packageItem.getItemMeta().getSignal_code());
        
        Element link = this.xmlPackageFile.createElement("link");
        link.setAttribute("rel", this.packageItem.getItemMeta().getLink_rel());
        link.setAttribute("residref", this.packageItem.getItemMeta().getLing_residref());
        link.setAttribute("version", this.packageItem.getItemMeta().getLink_version());
            		
        
        itemMeta.appendChild(itemClass);
        itemMeta.appendChild(provider);
        itemMeta.appendChild(versionCreated);
        itemMeta.appendChild(firstCreated);
        itemMeta.appendChild(pubStatus);
        itemMeta.appendChild(generator);
        itemMeta.appendChild(profile);
        itemMeta.appendChild(service);
        itemMeta.appendChild(title);
        itemMeta.appendChild(edNote);
        itemMeta.appendChild(signal);
        itemMeta.appendChild(link);
        
        return itemMeta;
	}
	
	//Generate the Xml for the ContentMeta element
	private Element generateContentMetaXml() {
		
		Element contentMeta = this.xmlPackageFile.createElement("contentMeta");
		
		Element contributor = this.xmlPackageFile.createElement("contributor");
		Element headline = this.xmlPackageFile.createElement("headline");
		
		//Define the contributor element
		Element name = this.xmlPackageFile.createElement("name");
		Element nameJob = this.xmlPackageFile.createElement("name");
		name.setTextContent(this.packageItem.getContentMeta().getContributor().getName());
		nameJob.setTextContent(this.packageItem.getContentMeta().getContributor().getJob_name());
		Element definition = this.xmlPackageFile.createElement("definition");
		definition.setAttribute("validto", this.packageItem.getContentMeta().getContributor().getDef_validto());
		definition.setTextContent(this.packageItem.getContentMeta().getContributor().getDef_text());
		Element note = this.xmlPackageFile.createElement("note");
		note.setAttribute("validto", this.packageItem.getContentMeta().getContributor().getNote_validto());
		note.setTextContent(this.packageItem.getContentMeta().getContributor().getNote_text());
		
		contributor.setAttribute("jobtitle", this.packageItem.getContentMeta().getContributor().getJobtitle());
		contributor.setAttribute("qcode", this.packageItem.getContentMeta().getContributor().getQcode());
		contributor.appendChild(name);
		contributor.appendChild(nameJob);
		contributor.appendChild(definition);
		contributor.appendChild(note);
		
		//Define the headline element
		headline.setAttribute("xml:lang", this.packageItem.getContentMeta().getHeadline().getHeadline_lang());
		headline.setTextContent(this.packageItem.getContentMeta().getHeadline().getHeadline_text());
		
		
		contentMeta.appendChild(contributor);
		contentMeta.appendChild(headline);
		
		return contentMeta;
	}
	
	//Generate the Xml for the GroupSet element
	private Element generateGroupSetXml() {
		Element groupSet = this.xmlPackageFile.createElement("groupSet");
		
		groupSet.setAttribute("root", ROOT_GROUP);
		
		for(PackageItem.GroupItem group : packageItem.getGroupItems()) {
			Element groupElement = this.xmlPackageFile.createElement("group");
			groupElement.setAttribute("id", group.getId());
			groupElement.setAttribute("role", group.getRole());
			
			for(PackageItem.GroupRef group_ref : group.getGroupRef()) {
				Element groupRefElement = this.xmlPackageFile.createElement("groupRef");
				groupRefElement.setAttribute("idref", group_ref.getId());
				
				groupElement.appendChild(groupRefElement);
			}
			
			for(PackageItem.ItemRef item_ref : group.getItemRef()) {
				Element itemRefElement = this.xmlPackageFile.createElement("itemRef");
				itemRefElement.setAttribute("residred",item_ref.getResidref());
				itemRefElement.setAttribute("contenttype",item_ref.getContentType());
				itemRefElement.setAttribute("size",item_ref.getSize());
				
				Element itemClass = this.xmlPackageFile.createElement("itemClass");
				itemClass.setAttribute("qcode", item_ref.getItemClass());
				
				Element provider = this.xmlPackageFile.createElement("provider");
				provider.setAttribute("literal", item_ref.getProvider());
				
				Element versionCreated = this.xmlPackageFile.createElement("versionCreated");
				versionCreated.setTextContent(item_ref.getVersion_created());
				
				Element pubStatus = this.xmlPackageFile.createElement("pubStatus");
				pubStatus.setAttribute("qcode", item_ref.getPubStatus());
				
				Element headline = this.xmlPackageFile.createElement("headline");
				headline.setTextContent(item_ref.getHeadline());
				
				Element description = this.xmlPackageFile.createElement("description");
				description.setTextContent(item_ref.getDescription());
				
				itemRefElement.appendChild(itemClass);
				itemRefElement.appendChild(provider);
				itemRefElement.appendChild(versionCreated);
				itemRefElement.appendChild(pubStatus);
				itemRefElement.appendChild(headline);
				
				groupElement.appendChild(itemRefElement);
			}
			
			groupSet.appendChild(groupElement);
		} 
		
		return groupSet;
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
	public static void main(String[] args) {;
		boolean notAnInt = true;
		Scanner scanner = new Scanner(System.in);
		int type_attribute_idx = 1;
		String value_attribute;
		String file_name;
		ArrayList<String> attributes = new ArrayList<String>();
		
		attributes.add("All news items related to a specific topic");
		attributes.add("Most recent news items from a specific department");
		attributes.add("Most recent news items related to a specific category"); 
		attributes.add("All news items related to a specific category"); 
		System.out.print("PACKAGE GENERATOR V1 -- This package generator generates package of last ten news\n");
		System.out.print("You can choose which type of package do you want to create\n");
		System.out.print("Available types are :\n");
		for(int i = 0; i < attributes.size(); i++)
		{
		    System.out.println(" - "+attributes.get(i)+" "+(i+1));
		}
		System.out.println("");
		System.out.print("Which type of package do you want to create ?\n");
		
		while(notAnInt) {
			try {
				type_attribute_idx = scanner.nextInt();
				
				if(type_attribute_idx > 0 && type_attribute_idx < 5) {
					notAnInt = false;
				} else {
					System.out.println("This option doesn't exist");
					scanner.nextLine();
				}
			} catch(Exception e) {
				System.out.println("This option doesn't exist");
				scanner.nextLine();
			}
		}
		
		scanner.nextLine();
		
		switch(type_attribute_idx) {
			case 1:
				System.out.print("What is the name or the qcode of the topic (e.g : \"stttopic:75182\" or \"Saksalaisten niukka enemmistö haluaisi eroon eurosta\"?\n");
				break;
				
			case 2:
				System.out.print("What is the name of the department (e.g : \"Talous\" ?\n");
				break;
				
			case 3:
				System.out.print("What is the name of the category ? (e.g : \"Politiikka\" \n");
				break;
				
			case 4:
				System.out.print("What is the name of the category ? (e.g : \"Politiikka\" \n");
				break;
				
			default:
				System.out.print("What is the name or the qcode of the topic (e.g : \"stttopic:75182\" or \"Saksalaisten niukka enemmistö haluaisi eroon eurosta\"?\n");
				break;
		}
		value_attribute = scanner.nextLine();
		System.out.print("Choose the name of your package xml file (e.g. : package.xml) :\n");
		file_name = scanner.nextLine();
		
		PackageGenerator packageGenerator = new PackageGenerator("./stt_lehtikuva_newsItems",type_attribute_idx,value_attribute,file_name);
	}
}
