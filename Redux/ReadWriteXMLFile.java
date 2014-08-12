import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;
import java.io.*;
import java.io.BufferedWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReadWriteXMLFile
{
	String licenseKeyString = "";
	String licensedToString = "";
	String fullPath = "";
	double versionNum = 0.00;
	int unlicensedCounter = 0;
	ArrayList<String> fileList = new ArrayList<String>();
	ArrayList<Integer> fileListOldPos = new ArrayList<Integer>();
	ArrayList<Integer> fileListNewPos = new ArrayList<Integer>();
	ArrayList<Integer> fileListInt = new ArrayList<Integer>();
	ArrayList<String> fileNewList = new ArrayList<String>();
	
	public ReadWriteXMLFile()
	{
	
	}
	
	public void modifyXMLFILE(String filePath)
	{
		try
		{
			String filepath = "c:\\file.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			// Get the root element
			Node company = doc.getFirstChild();
			
			// Get the staff element , it may not working if tag has spaces, or
			// whatever weird characters in front...it's better to use
			// getElementsByTagName() to get it directly.
			// Node staff = company.getFirstChild();
			
			// Get the staff element by tag name directly
			Node staff = doc.getElementsByTagName("staff").item(0);
			
			// update staff attribute
			NamedNodeMap attr = staff.getAttributes();
			Node nodeAttr = attr.getNamedItem("id");
			nodeAttr.setTextContent("2");
			
			// append a new node to staff
			Element age = doc.createElement("age");
			age.appendChild(doc.createTextNode("28"));
			staff.appendChild(age);
			
			// loop the staff child node
			NodeList list = staff.getChildNodes();
			
			for (int i = 0; i < list.getLength(); i++)
			{
				Node node = list.item(i);
				
				// get the salary element, and update the value
				if ("salary".equals(node.getNodeName()))
				{
					node.setTextContent("2000000");
				}
				
				//remove firstname
				if ("firstname".equals(node.getNodeName()))
				{
					staff.removeChild(node);
				}
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
			
			System.out.println("Done");
		}
		catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
	   }
		catch (TransformerException tfe)
		{
			tfe.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (SAXException sae)
		{
			sae.printStackTrace();
		}
	}
	
	public void seeCurrentListFiles(String filePath)
	{
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
		for (File listOfFile : listOfFiles)
		{
			if (listOfFile.isDirectory())
			{
// 				System.out.println(listOfFile.getName());
			}
			if(listOfFile.isFile())
			{
// 				System.out.println("File: " + listOfFile.getName());
				fileList.add(listOfFile.getName() + "");
				fileNewList.add(listOfFile.getName() + "");
			}
		}
		sortArray();
	}
	
	public void sortArray()
	{
		for(int i = 0; i < fileList.size(); i++)
		{
			int num = 0;
			String tempString = fileList.get(i);
			String[] tempSplit = tempString.split(".txt");
			for(int tempLength = 0; tempLength < tempSplit[0].length(); tempLength++)
			{
				if (Character.isDigit(tempSplit[0].charAt(tempLength)))
				{
// 					System.out.println("Name: " + tempSplit[0]);
// 					System.out.println("tempLength: " + tempLength);
// 					System.out.println("tempCharAt: " + tempSplit[0].charAt(tempLength));
					num++;
					if(num == 2)
					{
// 						System.out.println("char: " + tempSplit[0].charAt(tempLength-1) + tempSplit[0].charAt(tempLength));
						int charNum = 10*Integer.parseInt(tempSplit[0].charAt(tempLength-1) + "") + Integer.parseInt(tempSplit[0].charAt(tempLength) + "");
						fileListInt.add(charNum);
					}
					else if(num == 1 && (tempLength+1) == tempSplit[0].length())
					{
// 						System.out.println("char: " + tempSplit[0].charAt(tempLength));
						int charNum = Integer.parseInt(tempSplit[0].charAt(tempLength) + "");
						fileListInt.add(charNum);
					}
				}
			}
		}
		for(int k = 0; k < fileListInt.size(); k++)
		{
			fileListOldPos.add(fileListInt.get(k));
		}
		Collections.sort(fileListInt);
		for(int j = 0; j < fileListInt.size(); j++)
		{
			fileListNewPos.add(fileListInt.get(j));
		}
// 		for(int j1 = 0; j1 < fileListInt.size(); j1++)
// 		{
// 			System.out.println("J Loop: " + fileListInt.get(j1));
// 		}
// 	
// 		for(int k1 = 0; k1 < fileListOldPos.size(); k1++)
// 		{
// 			System.out.println("k1 Loop: " + fileListOldPos.get(k1));
// 		}
		swapPos();
	}
	
	public void updateOldList()
	{
		for(int i = 0; i < fileNewList.size(); i++)
		{
			int num = 0;
			String tempString = fileNewList.get(i);
			String[] tempSplit = tempString.split(".txt");
			for(int tempLength = 0; tempLength < tempSplit[0].length(); tempLength++)
			{
				if (Character.isDigit(tempSplit[0].charAt(tempLength)))
				{
// 					System.out.println("Name: " + tempSplit[0]);
// 					System.out.println("tempLength: " + tempLength);
// 					System.out.println("tempCharAt: " + tempSplit[0].charAt(tempLength));
					num++;
					if(num == 2)
					{
// 						System.out.println("char: " + tempSplit[0].charAt(tempLength-1) + tempSplit[0].charAt(tempLength));
						int charNum = 10*Integer.parseInt(tempSplit[0].charAt(tempLength-1) + "") + Integer.parseInt(tempSplit[0].charAt(tempLength) + "");
						fileListInt.add(charNum);
					}
					else if(num == 1 && (tempLength+1) == tempSplit[0].length())
					{
// 						System.out.println("char: " + tempSplit[0].charAt(tempLength));
						int charNum = Integer.parseInt(tempSplit[0].charAt(tempLength) + "");
						fileListInt.add(charNum);
					}
				}
			}
		}
		for(int k = 0; k < fileListInt.size(); k++)
		{
			fileListOldPos.add(fileListInt.get(k));
		}
	}
	
	public void swapPos(int origIndex, int wantedIndex, int indexLook)
	{
// 		System.out.println("Swapping " + indexLook + " at " + origIndex + " with " + wantedIndex);
		Collections.swap(fileNewList,origIndex,wantedIndex);
		fileListInt.clear();
		fileListOldPos.clear();
		updateOldList();
	}
	
	public void printNewList()
	{
		for(int i = 0; i < fileNewList.size(); i++)
		{
// 			System.out.println("fileSwap: " + fileNewList.get(i));
		}
	}
	
	public void swapPos()
	{
		int oldPosIndex = 0;
		int newPosIndex = 0;
// 		int lookNumIndex = 1;
		if(fileListOldPos.size() == fileListNewPos.size() && fileList.size() == fileNewList.size())
		{
			for(int lookNumIndex = 1; lookNumIndex <= fileListOldPos.size(); lookNumIndex++)
			{
				for(int i = 0; i < fileListOldPos.size(); i++)
				{
					if(lookNumIndex == fileListOldPos.get(i))
					{
						oldPosIndex = i;
						break;
					}
				}
				for(int k = 0; k < fileListNewPos.size(); k++)
				{
					if(lookNumIndex == fileListNewPos.get(k))
					{
						newPosIndex = k;
						break;
					}
				}
				if(oldPosIndex == newPosIndex)
				{

				}
				else
				{
					swapPos(oldPosIndex, newPosIndex, lookNumIndex);
				}
				oldPosIndex = 0;
				newPosIndex = 0;
			}
		}
		printNewList();
	}
	
	public void readTXTFile(String filePath)
	{
		try
		{
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(filePath);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{
				// Print the content on the console
// 				System.out.println (strLine);
				String[] row = strLine.split("\t");
				String licenseKey = "";
				String licensedTo = "";
				String version = "";
				
				int rowLength = row.length;
				//Layout of TextFile
				//1.  LicenseKey
				//2.	LicensedTo
				//3.	Version
				switch(rowLength)
				{
					case 1:
// 						System.out.println("No licensedTo nor version");
						licenseKey = row[0];
// 						System.out.println("row[0]: " + licenseKey);
						licensedTo = "";
						version = "";
						break;
					
					case 2:
						licenseKey = row[0];
						licensedTo = row[1];
						version = "";
						break;
					
					case 3:
						licenseKey = row[0];
						licensedTo = row[1];
						version = row[2];
						break;
				
					default:
						licenseKey = "";
						licensedTo = "";
						version = "";
						break;
				}
				writeSpecificXMLFile(filePath, licenseKey, licensedTo, version);
			}
			
			//Close the input stream
			in.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	
	public void writeXMLFile(String filePath)
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("program");
			doc.appendChild(rootElement);
			
			// licensee elements
			Element licensee = doc.createElement("licensee");
			rootElement.appendChild(licensee);
			
			// firstname elements
			Element firstname = doc.createElement("licenseKey");
			firstname.appendChild(doc.createTextNode("0000-0000-0000-0000-0000-0000-0000-0000"));
			licensee.appendChild(firstname);
			
			// lastname elements
			Element lastname = doc.createElement("licensedTo");
			lastname.appendChild(doc.createTextNode("Unlicensed"));
			licensee.appendChild(lastname);
			
			// version elements
			Element versionElement = doc.createElement("version");
			versionElement.appendChild(doc.createTextNode("0.00"));
			rootElement.appendChild(versionElement);
			
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath + "\\drm.xml"));
			
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			
			transformer.transform(source, result);
			
// 			System.out.println("File saved!");
		}
		catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch (TransformerException tfe)
		{
			tfe.printStackTrace();
		}
	}
	
	public boolean createNewDir(String filePath)
	{
		boolean createDir = new File(filePath).mkdir();		
		return createDir;
	}
	
	public void writeSpecificXMLFile(String filePath, String _licenseKey, String _licensedTo, String _version)
	{
		File file = new File(filePath);
		String fileName = file.getName();
		String[] fileSplitName = fileName.split(".txt");
		for(int i = 0; i < fileSplitName.length; i++)
		{
// 			System.out.println(fileSplitName[i]);
		}
		String parentPath = file.getParent();
// 		System.out.println("parentPath: " + parentPath);
		
		boolean createNewDir2 = createNewDir(parentPath + "\\" + fileSplitName[0]);
		if(createNewDir2 == true)
		{
// 			System.out.println("New Directory created");
			fullPath = parentPath + "\\" + fileSplitName[0];
		}
// 		System.out.println("boolean: " + createNewDir2);
// 		System.out.println("fullPath: " + fullPath);

		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("program");
			doc.appendChild(rootElement);
			
			// licensee elements
			Element licensee = doc.createElement("licensee");
			rootElement.appendChild(licensee);
			
			// firstname elements
			Element firstname = doc.createElement("licenseKey");
			if(_licenseKey.equalsIgnoreCase("") || _licenseKey.isEmpty())
			{
				firstname.appendChild(doc.createTextNode("0000-0000-0000-0000-0000-0000-0000-0000"));
			}
			else
			{
				firstname.appendChild(doc.createTextNode(_licenseKey));			
			}
			licensee.appendChild(firstname);
			
			// lastname elements
			Element lastname = doc.createElement("licensedTo");
			if(_licensedTo.equalsIgnoreCase("") || _licensedTo.isEmpty())
			{
				lastname.appendChild(doc.createTextNode("Unlicensed"));
				unlicensedCounter++;
			}
			else
			{
				lastname.appendChild(doc.createTextNode(_licensedTo));
			}
			licensee.appendChild(lastname);
			
			// version elements
			Element versionElement = doc.createElement("version");
			if(_version.equalsIgnoreCase("") || _version.isEmpty())
			{
				versionElement.appendChild(doc.createTextNode("0.00"));
			}
			else
			{
				versionElement.appendChild(doc.createTextNode(_version));
			}
			rootElement.appendChild(versionElement);
			
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File fileDirPath = new File(fullPath + "\\key" + unlicensedCounter + ".xml");
// 			System.out.println(fileDirPath.getAbsolutePath());
			StreamResult result = new StreamResult(fileDirPath);
			
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			
			transformer.transform(source, result);
			
// 			System.out.println("File saved!");
		}
		catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch (TransformerException tfe)
		{
			tfe.printStackTrace();
		}
	}
	
	public void readXMLFILE(String filePath)
	{
		String filePathXML = filePath + "\\drm.xml";
		try
		{
			String tempLicenseKey = "";
			String tempLicenseToString = "";
			String tempVersionString = "";
			File fXmlFile = new File(filePathXML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
// 			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("program");
// 			System.out.println("-----------------------");
			
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
					
// 					System.out.println("LicenseKey : " + getTagValue("licenseKey", eElement));
					tempLicenseKey = getTagValue("licenseKey", eElement);
// 					System.out.println("LicenseTo  : " + getTagValue("licensedTo", eElement));
					tempLicenseToString = getTagValue("licensedTo", eElement);
// 					System.out.println("Version : " + getTagValue("version", eElement));
					tempVersionString = getTagValue("version", eElement);
				}
			}
			licenseKeyString = tempLicenseKey;
			licensedToString = tempLicenseToString;
			double tempVersionNum = Double.parseDouble(tempVersionString);
			setVersionNum(tempVersionNum);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getLicenseKey()
	{
		return licenseKeyString;
	}
	
	public String getLicensedTo()
	{
		return licensedToString;
	}
	
	public void setVersionNum(double _versionNum)
	{
		versionNum = _versionNum;
	}
	
	public double getVersionNum()
	{
		return versionNum;
	}
	
	private static String getTagValue(String sTag, Element eElement)
	{
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		
		Node nValue = (Node) nlList.item(0);
		
		return nValue.getNodeValue();
	}
	
	public void getFileNewList()
	{
		for(int i = 0; i < fileNewList.size(); i++)
		{
			readTXTFile(".\\masterKeyDir\\" + fileNewList.get(i));
		}
	}
	
	public static void main(String[] args)
	{
		ReadWriteXMLFile rwXMLFile = new ReadWriteXMLFile();
		rwXMLFile.seeCurrentListFiles(".\\masterKeyDir\\");
		rwXMLFile.getFileNewList();
		rwXMLFile.writeXMLFile(".");
	}
}