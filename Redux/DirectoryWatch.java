import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.io.FileFilter.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import java.net.InetAddress.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

//MailServer
import java.io.File;
import java.util.Date;
import java.util.Properties;
 
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class DirectoryWatch
{
	//Global Variables of ArrayList
	ArrayList<String> fileExtensions = new ArrayList<String>();
	ArrayList<String> specificFileExtensions = new ArrayList<String>();
	ArrayList<String> sourceFiles = new ArrayList<String>();
	
	//Variables for current working directory
	File[] currentFileList;
	File folder;
	String path = "";
	String strDirectory = "";
	String[] splitTaskString;
	String folderWorkingDir;
	String globalImageString;
	boolean dirExist = false;
	boolean subDirExist = false;
	
	//Variables for new folder directory
	File[] newDirFileList;
	File newDirFolder;
	String dirPath = "";
	int picFileCounter = 1;
	String zipFile = "sitepics.zip";
	String subDirFile = "pics" + picFileCounter;
	boolean zipFilePathExist = false;
	final int FILE_COUNTER = 10;
	double kilobyteCounter = 0.00;
	
	//Variables for resizing images
	int IMG_WIDTH = 200;
	int IMG_HEIGHT = 200;
	int imgSizeWidth = 0; 		//500
	int imgSizeHeight = 0; 		//100
	int boundaryWidth = 800;	//200
	int boundaryHeight = 600;	//200
	Dimension imgSize;
	Dimension boundary;
 
 	//Main Method called when running the program individually
	public static void main(String[] args) 
	{
		DirectoryWatch fileWatch = new DirectoryWatch(".");
	}
	
	//Method that takes in a String of Path and assigns it to the global path
	public DirectoryWatch(String _path)
	{
		System.out.println("Current user is: " + getUserLoggedIn());
		System.out.println("Hostname of Machine is: " + getHostName());
		setPath(_path);
		setFilePath();
		setListFiles();
		System.out.println("Relative Path: " + getPath() );
		System.out.println("Absolute Path: " + getFilePath() );
		getListOfFiles();
		printListOfFiles(currentFileList);
		getSpecificFile(currentFileList, "DirectoryWatch", "java");
	}

	//Method that does not take in a String of path and gives back a message that:
	//"No direct path (in DirectoryWatch.java"
	public DirectoryWatch()
	{
		if(path.equalsIgnoreCase("") || path.isEmpty())
		{
			System.out.println("No direct path (in DirectoryWatch.java)");
		}
	}
	
	public void setZipFileName(String _zipFileName)
	{
		zipFile = _zipFileName + ".zip";
		System.out.println("ZipFile: " + zipFile);
	}
	
	public void removeSourceFiles()
	{
		sourceFiles.clear();
		kilobyteCounter = 0.00;
		zipFilePathExist = false;
		setSubDirExist(false);
	}
	
	public String getZipFileName()
	{
		return zipFile;
	}
	
	//Creates the ZipFile
	public void createZipFile()
	{
		int zipSourceFiles = sourceFiles.size();
		
		//Max of zipping files is 10 or kilobytes must be less than 25MB and more than 10KB
		if( kilobyteCounter <= 9728.00)
		{
			try
			{
				//create byte buffer
				byte[] buffer = new byte[1024];
				
				/*
				 * To create a zip file, use
				 *
				 * ZipOutputStream(OutputStream out)
				 * constructor of ZipOutputStream class.
				*/
				File zipFileFile = new File(dirPath + "\\" + zipFile);
				System.out.println("dirPath in createZipFile: " + dirPath);
				System.out.println("zipFile in createZipFile: " + zipFile);
				System.out.println("zipFileDirectPath: " + dirPath + zipFile);
				System.out.println("zipFileFile: " + zipFileFile.getName());
				
				//create object of FileOutputStream
				FileOutputStream fout = new FileOutputStream(zipFileFile);
				
				//create object of ZipOutputStream from FileOutputStream
				ZipOutputStream zout = new ZipOutputStream(fout);
				
				for(int i = 0; i < sourceFiles.size(); i++)
				{
				
					System.out.println("Adding " + sourceFiles.get(i));
					
					//create object of FileInputStream for source file
					FileInputStream fin = new FileInputStream(sourceFiles.get(i));
					
					/*
				 	 * To begin writing ZipEntry in the zip file, use
				 	 *
				 	 * void putNextEntry(ZipEntry entry)
				 	 * method of ZipOutputStream class.
				 	 *
				 	 * This method begins writing a new Zip entry to
				 	 * the zip file and positions the stream to the start
				 	 * of the entry data.
					*/
					File tempFile = new File(sourceFiles.get(i));
					zout.putNextEntry(new ZipEntry(tempFile.getName()));
				
				 	/*
				 	 * After creating entry in the zip file, actually
				 	 * write the file.
					*/
				
					int length;
					while((length = fin.read(buffer)) > 0)
					{
						zout.write(buffer, 0, length);
					}
				
					/*
				 	 * After writing the file to ZipOutputStream, use
				 	 *
				 	 * void closeEntry() method of ZipOutputStream class to
				 	 * close the current entry and position the stream to
				 	 * write the next entry.
					*/
					zout.flush();
					zout.closeEntry();
				
					//close the InputStream
					fin.close();
				}
			
				//close the ZipOutputStream
				zout.close();
				System.out.println("Zip file has been created!");
			}
			catch(IOException ioe)
			{
				System.out.println("IOException :" + ioe);
			}
		}
	}
	
	//Method that returns a boolean if the wantedFileName exists in the array of sourceFile
	public boolean getZipFileString(String wantedFileName)
	{
		if(sourceFiles.size() == 1)
		{
			if(wantedFileName.equalsIgnoreCase(sourceFiles.get(0)))
			{
				return true;
			}
			return false;
		}
		else
		{
			for(int i = 0; i < sourceFiles.size(); i++)
			{
				if(wantedFileName.equalsIgnoreCase(sourceFiles.get(i)))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	//Method that displays the size of the file/image
	public void getSizeOfFile(String fileName)
	{
		boolean fileExist = getZipFileString(fileName);
		if(fileExist == true)
		{
			String zipThisFilePath = fileName;
			System.out.println("zipthisFilePath: " + zipThisFilePath);
			File zipThisFile = new File(zipThisFilePath);
			//NOTE: GET THE FILE NAME FROM THE FILELIST IN THE DIRECTORY OF THE NEW FILE
			double bytes = zipThisFile.length();
			double kilobytes = (bytes / 1024);
			countKilobytesInZipFile(kilobytes);
// 			double megabytes = (kilobytes / 1024);
// 			double gigabytes = (megabytes / 1024);
// 			double terabytes = (gigabytes / 1024);
// 			double petabytes = (terabytes / 1024);
// 			double exabytes = (petabytes / 1024);
// 			double zettabytes = (exabytes / 1024);
// 			double yottabytes = (zettabytes / 1024);
// 	 		System.out.println("bytes : " + bytes);
// 			System.out.println("kilobytes : " + kilobytes);
// 			System.out.println("megabytes : " + megabytes);
// 			System.out.println("gigabytes : " + gigabytes);
// 			System.out.println("terabytes : " + terabytes);
// 			System.out.println("petabytes : " + petabytes);
// 			System.out.println("exabytes : " + exabytes);
// 			System.out.println("zettabytes : " + zettabytes);
// 			System.out.println("yottabytes : " + yottabytes);
		}
		else
		{
			System.out.println("File does not exist in " + (getPath() + "\\" + getDirectoryString()) + ".");
		}
	}
	
	//Method that counts the number of kilobytes in the Zip file before Zipping
	public void countKilobytesInZipFile(double kiloBytes)
	{
		kilobyteCounter += kiloBytes;
	}
	
	public double getKiloCounter()
	{
		return kilobyteCounter;
	}
	
	//Method that returns the sourceFile size of the array
	public int getSourceFileCount()
	{
		return sourceFiles.size();
	}
	
	//Method that adds the fileName of the file to the sourceFile array
	public void addZipFile(String fileName)
	{
		sourceFiles.add(fileName);
	}
	
	//Method taht removes the fileName (if exists) in the sourceFile array
	public void removeZipFile(String fileName)
	{
		if(sourceFiles.size() == 0)
		{
			System.out.println("No file exists in SourceFile arraylist");
		}
		else if(sourceFiles.size() == 1)
		{
			if(sourceFiles.get(0).equalsIgnoreCase(fileName))
			{
				sourceFiles.remove(0);
			}
			else
			{
				System.out.println("File does not exist in SourceFile arraylist");
			}
		}
		else
		{
			for(int i = 0; i < sourceFiles.size(); i++)
			{
				if(sourceFiles.get(i).equalsIgnoreCase(fileName))
				{
					sourceFiles.remove(i);
				}
			}
		}
	}

	//Method that sets the ZipFilePathDirectory to the global directoryPath
	public void setZipFilePath(String _path)
	{
		dirPath = _path;
	}
	
	//Method that returns the String of the ZipFilePath
	public String getZipFilePath()
	{
		return dirPath;
	}
	
	//Method that sets up the new Directory file
	public void setUpNewDir()
	{
		newDirFolder = new File(getPath() + "\\" + getDirectoryString());; 
		newDirFileList = newDirFolder.listFiles();
	}
	
	//Method that refreshes the directory list of files being added in
	public void refreshDirListOfFiles()
	{
		File[] tempFileList = newDirFolder.listFiles();
		newDirFileList = tempFileList;
	}
	
	//Method that refreshes the current directory list of files
	public void refreshListOfFiles()
	{
		File[] tempFileList = folder.listFiles();
		currentFileList = tempFileList;
	}
	
	//Method that returns the String directory
	public String getDirectoryString()
	{
		return strDirectory;
	}
	
	public boolean compareDirectory(String _workingDir, String _createNewDir)
	{
		String tempWorkDir = _workingDir;
		String tempCreateNewDir = _createNewDir;
		if(tempWorkDir.equalsIgnoreCase(tempCreateNewDir))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Method that returns true if the directory exists and is created
	//Method that returns false if the directory does not exist and is not created
	public boolean createDirectory(String createNewDir)
	{
		System.out.println("createDirectory String: " + createNewDir);
		System.out.println("currentDirectory: " + strDirectory);
		dirExist = compareDirectory(strDirectory, createNewDir);
		if(dirExist == false)
		{
			strDirectory = createNewDir;
			File newDirFile = new File(strDirectory);
			boolean success = newDirFile.mkdir();
			if(success == true)
			{
				System.out.println("Directory: " + strDirectory + " created");
				dirExist = true;
				setUpNewDir();
				return dirExist;
			}
			else
			{
				System.out.println("Directory: " + strDirectory + " is not created");
			}		
		}
		else
		{
			System.out.println("Directory: " + strDirectory + " already exists");
			return dirExist;
		}
		return dirExist;
	}
	
	public void setSubDirExist(boolean _subDirExist)
	{
		subDirExist = _subDirExist;
	}
	
	public boolean getSubDirExist()
	{
		return subDirExist;
	}
	
	public boolean createSubDirectories(String newPath, String pathName)
	{
		String subDirPath = newPath + "\\" + pathName;
		String subDirPathName = pathName;
		System.out.println("newPath: " + newPath); //sitepics
		System.out.println("pathName: " + pathName); //pics1, pics2
		File directory = new File(subDirPath);
		System.out.println("directoryParent: " + directory.getParentFile());
		if(subDirExist == false)
		{
			if (directory.mkdir())
			{
				System.out.println("Success using alternative 1");
				subDirExist = true;
				setSubDirectoryName(subDirPathName);
				return subDirExist;
			}
			else
			{
				System.out.println("Failed using alternative 1, trying alternative 2");
				
				//Alternative 2: If ancestors doesn\'t exist, they will be created.
				if (directory.mkdirs())
				{
					System.out.println("Success using alternative 2");
					subDirExist = true;
					setSubDirectoryName(subDirPathName);
					return subDirExist;
				}
				else
				{
					//Alternative 3: If ancestor exists, then only create the directory.
					if(directory.getParentFile().mkdirs())
					{
						System.out.println("Success using alternative 3");
						subDirExist = true;
						setSubDirectoryName(subDirPathName);
						return subDirExist;
					}
					else
					{
						System.out.println("Failed using both alternative 1 and alternative 2");
						subDirExist = false;
						return subDirExist;
					}
				}
			}
		}
		else
		{
			System.out.println("Sub Directory already exists");
			return subDirExist;
		}
	}
	
	public String getParentFileName(String filePathName)
	{
		File tempFileName = new File(filePathName);
		return tempFileName.getParentFile().getName();
	}
	
	public void setSubDirectoryName(String pathName)
	{
		subDirFile = pathName;
	}
	
	public String getSubDirectoryName()
	{
		return subDirFile;
	}
	
	public void setPicFileCounter()
	{
		picFileCounter += 1;
	}
	
	public int getPicFileCounter()
	{
		return picFileCounter;
	}
	
	//Method that scales the incoming image with the new wanted reduced size image
	//Method that returns the new Dimensions scaling
	public Dimension getScaledDimensions(Dimension imgSize, Dimension boundary)
	{
		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = 0;
		int new_height = 0;
		
		// first check if we need to scale width
		if (original_width > bound_width)
		{
			//scale width to fit
			new_width = bound_width;
			//scale height to maintain aspect ratio
			new_height = (new_width*original_height)/original_width;
		}
		
		// then check if we need to scale even with the new height
		if (new_height > bound_height)
		{
			//scale height to fit instead
			new_height = bound_height;
			//scale width to maintain aspect ratio
			new_width = (new_height*original_width)/original_height;
		}
		return new Dimension(new_width, new_height);
	}
	
	public String splitString(String taskString)
	{
		splitTaskString = taskString.split("\\\\");
		String tempString = "";
		System.out.println("splitTaskString: " + splitTaskString.length);
		int jpgStringIndex = 0;
		for(int i = 0; i < splitTaskString.length; i++)
		{
			System.out.println(splitTaskString[i]);
			if(splitTaskString[i].contains("png") || 	splitTaskString[i].contains("PNG") ||
				splitTaskString[i].contains("jpg") || 	splitTaskString[i].contains("JPG") ||
				splitTaskString[i].contains("jpeg") || 	splitTaskString[i].contains("JPEG") )
			{
				System.out.println("Name of JPG: " + splitTaskString[i]);
				setImgString(splitTaskString[i]);
				jpgStringIndex = i;
				System.out.println("jpgStringIndex: " + jpgStringIndex);
				break;
			}
		}
		for(int k = 0; k < (jpgStringIndex); k++)
		{
			tempString += splitTaskString[k] + "\\";
		}
		System.out.println("tempString: " + tempString);
		folderWorkingDir = tempString;
		return folderWorkingDir;
	}
	
	public String getImgString()
	{
		return globalImageString;
	}
	
	public void setImgString(String image)
	{
		globalImageString = image;
	}


	//Method that gets the name of the image
	//Method that gets the path of the image
	//Method that gets the new path (of the new directory file)
	public void getImageString(String _imageName, String _path, String _newPath, String zipName)
	{
		String workingDirString = splitString(_imageName);
		System.out.println("WorkingDirectoryString in getImageString: " + workingDirString);
		String oldPath = _path;
		System.out.println("oldPath in getImageString: " + oldPath);
		String imageName = getImgString();
		System.out.println("imageName in getImageString: " + imageName);
		String newPath = _newPath;
		System.out.println("newPath in getImageString: " + newPath);
		String imagePathString = oldPath + "\\" + workingDirString + imageName;
		System.out.println("subDirectoryName(): " + getSubDirectoryName());
		String newImagePathString = newPath + "\\" + getSubDirectoryName() + "\\" + "r-" + imageName;
		System.out.println("PathString: " + newImagePathString);
		
		//Verifies if the zipFilePathExist exists
		//If the zipFile Path does exist and the folder exists, then it assigns the zipfile path
		if(zipFilePathExist == false)
		{
			System.out.println("zipName in getImageString: " + zipName);
			String zipPath = newPath;
			System.out.println("zipPath in getImageString: " + zipPath);
			setZipFilePath(zipPath);
			setZipFileName(zipName);
			zipFilePathExist = true;
		}
		System.out.println("imagePathString in getImageString: " + imagePathString);
		
		//Compresses the image to the new dimensions given
		try
		{
			BufferedImage originalImage = ImageIO.read(new File(imagePathString));
			int originalImageWidth 	= originalImage.getWidth();
			int originalImageHeight = originalImage.getHeight();
			imgSizeWidth = originalImageWidth;
			imgSizeHeight = originalImageHeight;
			System.out.println("originalImageWidth: " + originalImageWidth);
			System.out.println("originalImageHeight: " + originalImageHeight);
	
			//NOTE: NO ADJUSTMENT NECESSARY IF FILE IS ALREADY SMALL
			//(if the boundaryWidth and boundaryHeight is close enough to imgSizeWidth and imgSizeHeight)
			imgSize = new Dimension(imgSizeWidth,imgSizeHeight);
			boundary = new Dimension(boundaryWidth,boundaryHeight);
			Dimension newDimension = getScaledDimensions(imgSize,boundary);
			int newDimWidth = newDimension.width;
			int newDimHeight = newDimension.height;
			IMG_WIDTH = newDimWidth;
			IMG_HEIGHT = newDimHeight;
			
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			
			//Creates the new image and throws it into the "ReducedImageSize" file
			BufferedImage resizeImageJpg = resizeImage(originalImage, type);
			System.out.println(newImagePathString + "");
			File tempFile = new File(newImagePathString);
			ImageIO.write(resizeImageJpg, "jpg", tempFile); 
			
// 			BufferedImage resizeImagePng = resizeImage(originalImage, type);
// 			ImageIO.write(resizeImagePng, "png", new File(newImagePathString +"_png.jpg"));
// 			
// 			BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
// 			ImageIO.write(resizeImageHintJpg, "jpg", new File(newImagePathString +"_hint_jpg.jpg"));
// 			
// 			BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
// 			ImageIO.write(resizeImageHintPng, "png", new File(newImagePathString +"_hint_png.jpg"));

			System.out.println("Successfully wrote image to new directory");
// 			addZipFile("." + "\\" + newPath + "\\" + imageName);
// 			getSizeOfFile("." + "\\" + newPath + "\\" + imageName);
			addZipFile(newPath + "\\" + getSubDirectoryName() + "\\" + "r-" + imageName);
			getSizeOfFile(newPath + "\\" + getSubDirectoryName() + "\\" + "r-" + imageName);
// 			addZipFile("." + "\\" + newPath + "\\" + imageName);
// 			getSizeOfFile("." + "\\" + newPath + "\\" + imageName);
			createZipFile();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	//Method that returns the resized Image (without much quality)
	private BufferedImage resizeImage(BufferedImage originalImage, int type)
	{
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		
		return resizedImage;
	}
	
	//Method that returns the resized image with hints (of quality)
	private BufferedImage resizeImageWithHint(BufferedImage originalImage, int type)
	{
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		
		return resizedImage;
	}
	
	
	public void sendEmail(String host, String port, 
	String userName, String passWord, String[] toAddress, String subject, String message, String attachFile)
	throws AddressException, MessagingException
	{
		System.out.println("Host: " + host);
		System.out.println("Port: " + port);
		System.out.println("userName: " + userName);
		System.out.println("Password: " + passWord);
		for(int i = 0; i < toAddress.length; i++)
		{
			System.out.println("toAddress: " + toAddress[i]);
		}
		System.out.println("subject: " + subject);
		System.out.println("message: " + message);
		System.out.println("attachFile: " + attachFile);
		
		/**
		 * Sends an e-mail with attachments.
		 * @param host address of the server
		 * @param port port number of the server
		 * @param userName email address used to send mails
		 * @param password password of the email account
		 * @param toAddress email address to send
		 * @param subject title of the email
		 * @param message content of the email
		 * @param attachFiles an array of file paths
		 * @throws AddressException
		 * @throws MessagingException
		*/
		
		// sets SMTP properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", passWord);
		
		// creates a new session with an authenticator
		Authenticator auth = new SMTPAuthenticator(userName, passWord);
		Session session = Session.getInstance(properties, auth);
		
		// creates a new e-mail message
		MimeMessage msg = new MimeMessage(session);
		
		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = new InternetAddress[toAddress.length];
		for(int emailParticipantsCounter = 0; emailParticipantsCounter < toAddress.length; emailParticipantsCounter++)
		{
			toAddresses[emailParticipantsCounter] = new InternetAddress(toAddress[emailParticipantsCounter]);
		}
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.addRecipient(Message.RecipientType.BCC, new InternetAddress("info@finishitnow.com"));
// 		msg.addRecipient(Message.RecipientType.CC, new InternetAddress("info@finishitnow.com"));
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		
		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");
		
		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		
		
		// adds attachments
		
		addAttachment(multipart, attachFile);
// 		if (attachFiles != null && attachFiles.length > 0)
// 		{
// 			for (String filePath : attachFiles)
// 			{
// 				addAttachment(multipart, filePath);
// 			}
// 		}
		
		// sets the multi-part as e-mail's content
		msg.setContent(multipart);
		
		// sends the e-mail
		Transport.send(msg);
	}
	
	/**
	  * Adds a file as an attachment to the email's content
	  * @param multipart
	  * @param filePath
	  * @throws MessagingException
	*/
	private void addAttachment(Multipart multipart, String filePath) throws MessagingException
	{
		System.out.println("File Attachment: " + filePath);
		MimeBodyPart attachPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filePath);
		attachPart.setDataHandler(new DataHandler(source));
		attachPart.setFileName(new File(filePath).getName());
		
		multipart.addBodyPart(attachPart);
	}
	
	/**
	  * This class provides authentication information.
	  * 
	  *
	 */
	 
	 private class SMTPAuthenticator extends javax.mail.Authenticator
	 {
	 	private String userName;
		private String password;
		
		public SMTPAuthenticator(String userName, String password)
		{
			this.userName = userName;
			this.password = password;
		}
		
		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(userName, password);
		}
	}   
	
	//Method that returns the String username of the machine
	public String getUserLoggedIn()
	{
		String currentUser = System.getProperty("user.name");

		return currentUser;
	}
	
	//Method that returns the String of the host name of the computer
	public String getHostName()
	{
		String hostName = "";
		try
		{
			InetAddress localMachine = InetAddress.getLocalHost();
			hostName = localMachine.getHostName();
		}
		catch(UnknownHostException uhe)
		{
			uhe.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return hostName;
	}
	
	//Method that sets the file path of the current working directory
	public void setFilePath()
	{
		folder = new File(getPath());
	}
	
	//Method that sets the List of files once the file is created
	public void setListFiles()
	{
		currentFileList = folder.listFiles();
	}
	
	//Method that returns the path of the current working directory (or specified working directory)
	public String getPath()
	{
		return path;
	}
	
	//Method that returns the absoluteFilePath of the current working directory
	public String getFilePath()
	{
		String absoluteFilePath = folder.getAbsolutePath();
		
		return absoluteFilePath;
	}
	
	//Method that adds the specific file extensions to an array
	public void addSpecificFileExt(String fileExt)
	{
		boolean extExist = verifySpecificExtExist(fileExt);
		if(extExist == false)
		{
			specificFileExtensions.add(fileExt);
		}
		else
		{
			System.out.println("Specific file Extension exists already.\nTry adding a different one");
		}
	}
	
	//Method that removes the specific file extension (if it exists in the array)
	public void removeSpecificFileExt(String fileExt)
	{
		boolean extExist = verifySpecificExtExist(fileExt);
		if(extExist == true)
		{
			if(specificFileExtensions.size() == 1)
			{
				specificFileExtensions.remove(0);
			}
			else
			{
				for(int i = 0; i < specificFileExtensions.size(); i++)
				{
					if(specificFileExtensions.get(i).equalsIgnoreCase(fileExt))
					{
						specificFileExtensions.remove(i);
					}
				}
			}
		}
		else
		{
			System.out.println("File extension does not exist");
		}
	}
	
	//Method that returns true if the specific file extension exists before removing the file
	public boolean verifySpecificExtExist(String _fileExt)
	{
		String fileExt = _fileExt;
		if(specificFileExtensions.size() == 0)
		{
			return false;
		}
		else
		{
			for(int i = 0; i < specificFileExtensions.size(); i++)
			{
				if(specificFileExtensions.get(i).equalsIgnoreCase(fileExt))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	//Method that sets the path of the working directory
	public void setPath(String desiredPath)
	{
		if(desiredPath.isEmpty() || 
			desiredPath.equalsIgnoreCase("") ||
			desiredPath.equalsIgnoreCase("\\.") )
		{
			path = ".";
		}
		else
		{
			path = desiredPath;
		}
	}
	
	//Method that gets the list of files in the working directory
	public void getListOfFiles()
	{
		String files;
	
		for (int i = 0; i < currentFileList.length; i++)
		{
			if (currentFileList[i].isFile())
			{
				files = currentFileList[i].getName();
				String[] splitExtension = currentFileList[i].getName().split("\\.");
				int splitExtLength = splitExtension.length;
				boolean extensionExistAlready = isInFileExtension(splitExtension[ (splitExtLength - 1) ] );
				if(extensionExistAlready == false)
				{
					fileExtensions.add(splitExtension[ (splitExtLength - 1) ] );
				}
				else
				{
					//Does not add to the fileExtenion array (not filter)
				}
			}
		}
	}
	
	//Method that is called when wanting to print the list of files and the specific extensions
	//to command line
	public void printListOfFiles(File[] tempFiles, String specificExtension)
	{
		if(specificExtension.equalsIgnoreCase("") || specificExtension.isEmpty())
		{
			System.out.println("Specific Extension is blank; not searching for any specific extensions");
		}
		else
		{
			System.out.println("Print list of files that have " + specificExtension );
			for(File f : tempFiles)
			{
				if(f.getName().endsWith("." + specificExtension) )
				{
					System.out.println("File Extensions that end with " + specificExtension + ": " + f.getName() );
				}
				if(f.getName().contains(specificExtension) )
				{
					System.out.println("File Extensions that contain " + specificExtension + ": " + f.getName() );
				}
			}
		}
	}
	
	//Method that is called when wanting to print the list of files, the file request, and the specific extenion 
	//to command line
	public void getSpecificFile(File[] tempFiles, String _fileRequest, String _specificExt)
	{
		String fileName = _fileRequest;
		String specificExt = _specificExt;
		if(fileName.isEmpty() || fileName.equalsIgnoreCase(""))
		{
			System.out.println("No fileName selected");
		}
		else
		{
			for(File f : tempFiles)
			{
				String[] tempFileName = f.getName().split("\\.");
				int tempFileNameLength = tempFileName.length;
				if(tempFileNameLength == 2)
				{
					if(tempFileName[0].equalsIgnoreCase(fileName) && tempFileName[1].equalsIgnoreCase("Java") )
					{
						System.out.println("FOUND: " + fileName);
					}
				}
			}
		}
	}
	
	//Method that is called to print the list of file that end in .java (for testing purposes)
	public void printListOfFiles(File[] tempFiles)
	{
		System.out.println("Extensions: " + fileExtensions.size());
		for(File f : tempFiles)
		{
			if(f.getName().endsWith(".java"))
			{
				System.out.println("File Extensions that end with .java: " + f.getName());
			}
		}
		for(File fFile : tempFiles)
		{
			if(fFile.getName().endsWith(".class"))
			{
				System.out.println("File Extensions that end with .class: " + fFile.getName());
			}
		}

	}
	
	//Method that is called to print the specific list of extensions
	public void printSpecificListOfExtensions()
	{
		System.out.println("Specific Extension count: " + specificFileExtensions.size());
		if(specificFileExtensions.size() == 1)
		{
			System.out.println("Specific Extension #1: " + specificFileExtensions.get(0));
		}
		else
		{
			for(int i = 0; i < specificFileExtensions.size(); i++)
			{
				System.out.println("Specific Extension # " + i + ": " + specificFileExtensions.get(i));
			}
		}
	}
	
	public void printListOfExtensions()
	{
		System.out.println("Extension count: " + fileExtensions.size());
		if(fileExtensions.size() == 1)
		{
			System.out.println("Extension #1: " + fileExtensions.get(0));
		}
		else
		{
			for(int i = 0; i < fileExtensions.size(); i++)
			{
				System.out.println("Extension #" + i + ": " + fileExtensions.get(i));
			}
		}
	}
	
	public boolean isInFileExtension(String _fileExtension)
	{
		String fileExtension = _fileExtension;
		boolean doesExist = false;
		if(fileExtensions.size() == 0)
		{
			return false;
		}
		else
		{
			for(int i = 0; i < fileExtensions.size(); i++)
			{
				if(fileExtensions.get(i).equalsIgnoreCase(fileExtension))
				{
					doesExist = true;
				}
			}
			if(doesExist == true)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}