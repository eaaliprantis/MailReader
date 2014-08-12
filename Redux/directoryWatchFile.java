import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat.*;
import java.text.DecimalFormat.*;
import java.util.concurrent.TimeUnit.*;
import java.io.*;
import java.lang.Object.*;
import com.sun.nio.file.ExtendedWatchEventModifier;

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

public class directoryWatchFile 
{
	DirectoryWatch directoryWatch = new DirectoryWatch();
	ReadWriteXMLFile readXMLFile = new ReadWriteXMLFile();
	boolean fileListExist = false;
	boolean threadStarted = false;
	double getSizeOfFile = 0.00;
	int subDirCounter = 1;
	int jpgStringIndex = 0;
	String[] splitTaskString;
	String folderWorkingDir = "";
	String globalImageString = "";
	MyThread myThread;
	double currentVersion = 1;
	
	ArrayList<String> rootDir = new ArrayList<String>();
	File[] roots = File.listRoots();
	
	
	//Main method that starts to run
	//This file can be placed anywhere in any folder and will run in that folder specifically.
	public static void main(String[] args)
	{
		//Calls the method
		directoryWatchFile dWatchFile = new directoryWatchFile();
		dWatchFile.setRootDir();
		System.out.println("argsLength: " + args.length);		
		
		for(int i = 0; i < args.length; i++)
		{
			System.out.println("Arg " + i + ": " + args[i]);
		}
		switch(args.length)
		{			
			case 0:
				try
				{
					//Assign to watch a specific directory (current working directory)
					dWatchFile.watchSpecificDirectory(".");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
				
			case 1:
				//Method that needs to be tested/worked on for validation
				String tempString = args[0];
				boolean fileExist = dWatchFile.checkFileExist(tempString);
				if(fileExist == true)
				{
					boolean rootRequest = dWatchFile.checkRootDir(tempString);
					if(rootRequest == true)
					{
						String watchDirChosen = "";
						while(!(rootRequest == false && fileExist == true))
						{
							if(rootRequest == true && fileExist == true)
							{
								System.out.println("ERROR: cannot look at the root directory alone");
							}
							else if(rootRequest == false && fileExist == false)
							{
								System.out.println("ERROR: cannot look at a non-existing file");
							}
							else if(rootRequest == false && fileExist == false)
							{
								System.out.println("ERROR: cannot look at a directory that does not exist");
							}
							Scanner scan = new Scanner(System.in);
							System.out.println("What directory would you like to watch? ");
							String watchDir = scan.nextLine();
							rootRequest = dWatchFile.checkRootDir(watchDir);
							int slashApp = 0;
							if(rootRequest == true)
							{
								System.out.println("rootRequest: YES");
								fileExist = dWatchFile.checkFileExist(watchDir);
								rootRequest = dWatchFile.checkRootDir(watchDir);
								System.out.println("fileExist: " + fileExist);
								System.out.println("rootRequest: " + rootRequest);
							}
							else
							{
								System.out.println("rootRequest: NO");
								fileExist = dWatchFile.checkFileExist(watchDir);
								rootRequest = dWatchFile.checkRootDir(watchDir);
								System.out.println("fileExist: " + fileExist);
								System.out.println("rootRequest: " + rootRequest);
							}
							if(rootRequest == false && fileExist == true)
							{
								watchDirChosen = watchDir;
							}
						}
						dWatchFile.caseFunctions(1,watchDirChosen);
					}
					else
					{
						fileExist = dWatchFile.checkFileExist(args[0]);
						if(fileExist == true)
						{
							dWatchFile.caseFunctions(1,args[0]);
						}
						else
						{
							String watchDirChosen = "";
							while(!(rootRequest == false && fileExist == true))
							{
								System.out.println("ERROR: cannot look at the root directory alone");
								Scanner scan = new Scanner(System.in);
								System.out.println("What directory would you like to watch? ");
								String watchDir = scan.nextLine();
								fileExist = dWatchFile.checkFileExist(watchDir);
								rootRequest = dWatchFile.checkRootDir(watchDir);
								System.out.println("fileExist: " + fileExist);
								System.out.println("rootRequest: " + rootRequest);
								if(rootRequest == false && fileExist == true)
								{
									watchDirChosen = watchDir;
								}
							}
							dWatchFile.caseFunctions(1,watchDirChosen);
						}
					}
				}
				else
				{
				
				}
				break;
				
			case 2:
				dWatchFile.caseFunctions(2,args[0]);
				//Do stuff with ARGS one
				//Call method to swap with new and improved version
				break;
		}
	}
	
	public boolean checkSlashFollowUp(String word, String guess)
	{
		ArrayList<Integer> slashIndex = new ArrayList<Integer>();
		
		int index = word.indexOf(guess);
		while (index >= 0)
		{
			System.out.println(index);
			slashIndex.add(index);
			index = word.indexOf(guess, index + 1);
		}
		
		//NOTE CHECK HERE
		for(int i = 0; i < slashIndex.size(); i++)
		{
		
		}
		return true;
	}
	
	public boolean checkFileExist(String filePathString)
	{
		System.out.println("File: " + filePathString);
		File file = new File(filePathString);
		boolean fileExist = file.exists();
		if(fileExist == true)
		{
			System.out.println("return True");
			return true;
		}
		System.out.println("return False");
		return false;
	}
	
	public boolean checkRootDir(String argString)
	{
		String tempString = argString;
		for(int rootList = 0; rootList < rootDir.size(); rootList++)
		{
			if(tempString.equalsIgnoreCase(rootDir.get(rootList)))
			{
				return true;
			}
		}
		return false;
	}
	
	public void setRootDir()
	{
		for(int i=0;i<roots.length;i++)
		{
			System.out.println("Root["+i+"]:" + roots[i]);
			rootDir.add(roots[i] + "");
		}
	}
	
	public void caseFunctions(int caseNum, String _dirWatch)
	{
		String dirStringWatch = _dirWatch;
		File testFile = new File(dirStringWatch);
		boolean fileExist = testFile.exists();
		
		//If file does not exist
		if(!fileExist)
		{
			System.out.println("Path does not exist");
			try
			{
				watchSpecificDirectory(".");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("File Exists: path will be watched");
			try
			{
				readXMLFile.writeXMLFile(dirStringWatch + "");
				//NOTE: Create a new java program to compare what is read into the program
				//with a list of valid license key
				//Method return true if valid paid for license key
				//Method reurtn false if valid license key is not paid for (Exit Program)
				//	System.exit(1); 
				readXMLFile.readXMLFILE(dirStringWatch + "");
				watchSpecificDirectory(dirStringWatch);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//Method to create a logFile for any errors and save log in active (current) directory
	public void createLogFile(String logName)
	{
	
	}
	
	public void checkVersionINIFile(String filePath)
	{
	
	}
	
	//Method that takes in no value but just starts the program
	public directoryWatchFile()
	{
	
	}
	
	//Method that returns any <?> (wildcard) event and returns the watchEvent event
	public <T> WatchEvent<T> castEvent(WatchEvent<?> event)
	{
		return (WatchEvent<T>) event;
	}
	
	//Method that adds the fileString extension to an array
	//Method that calls another java program to add it to the specific extensions
	public void addFileStringExtensions(String fileExt)
	{
		directoryWatch.addSpecificFileExt(fileExt);
	}
	
	//Method that removes the fileString extension out of the array
	//Method that calls another java program to remove the specific extensions
	public void removeFileStringExtensions(String fileExt)
	{
		directoryWatch.removeSpecificFileExt(fileExt);
	}
	
	//Method that runs the watchSpecific Directory using the string path assigned
	//Throws exception when there is an error in the program
	public void watchSpecificDirectory(String path) throws Exception
	{
		//Path that it listens to
		Path directoryToWatch;
		
		//Verifies if the String path coming in is either empty or blank
		//If blank or empty, assigns it to the working directory class
		if(path.isEmpty() || path.equalsIgnoreCase("") || path.equalsIgnoreCase(".") )
		{
			//Sets default path to itself/folder
			directoryToWatch = Paths.get(".");
			directoryWatch.setPath(".");
			directoryWatch.setFilePath();
			directoryWatch.setListFiles();
		}
		else
		{
			System.out.println("Specific Path Watching: " + path);
			directoryToWatch = Paths.get(path);
			directoryWatch.setPath(path);
			directoryWatch.setFilePath();
			directoryWatch.setListFiles();
		}
		
		//Adds all of the fileStringExtensions to the Arraylist of accepted file Strings
		addFileStringExtensions("jpg");
		addFileStringExtensions("JPG");
		addFileStringExtensions("jpeg");
		addFileStringExtensions("JPEG");
		
		//Actual watchservice to watch for any changes in the directory path that is provided
		WatchService watcherSvc = FileSystems.getDefault().newWatchService();
		WatchEvent.Kind[] standardEvents = {StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE};
		WatchKey watchKey = directoryToWatch.register(watcherSvc, standardEvents, ExtendedWatchEventModifier.FILE_TREE);
// 		WatchKey watchKey = directoryToWatch.register(watcherSvc, 
// 		StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
		
		//Gives the watched path that it is watching
		Path watchedPath = (Path) watchKey.watchable();
		
		while (true)
		{
			if(myThread == null)
			{
			
			}
			else
			{
				if(!myThread.isAlive())
				{
					System.out.println("Thread has stopped. Adding one to the subDirCounter file");
					subDirCounter += 1;
					threadStarted = false;
					directoryWatch.removeSourceFiles();
					directoryWatch.setSubDirExist(threadStarted);
				}
			}

			watchKey=watcherSvc.take();
			//For loop that runs continuously and will never end unless the program is exited
			for (WatchEvent<?> event: watchKey.pollEvents())
			{
				WatchEvent<Path> watchEvent = castEvent(event);
				System.out.println(event.kind().name().toString() + " "
				+ directoryToWatch.resolve(watchEvent.context()));
				watchKey.reset();
				if(myThread == null)
				{
			
				}
				else
				{
					if(!myThread.isAlive())
					{
						System.out.println("Thread has stopped. Adding one to the subDirCounter file");
						subDirCounter += 1;
						threadStarted = false;
						directoryWatch.removeSourceFiles();
						directoryWatch.setSubDirExist(threadStarted);
					}
				}
				//If an event of overflow (if deleted by mistake),
				//it throws a message stating "Event overflow occurred"
				if (event.kind() == StandardWatchEventKinds.OVERFLOW)
				{
					System.out.println("Event overflow occurred");
				}
				//If an event of a new Entry is created (like a new file)
				//It throws a message stating that a new file is created
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
				{
					System.out.println("Created: " + event.context().toString());
					//Assigns the new file created to a string and compares it to others
					String documentString = event.context().toString();
					if( (
							documentString.contains("png") || 	documentString.contains("PNG") ||
							documentString.contains("jpg") || 	documentString.contains("JPG") ||
							documentString.contains("jpeg") || 	documentString.contains("JPEG")
			  			 ) 
						 && (!documentString.contains("sitepics") || !documentString.contains("pics"))
						)
					{
						if(threadStarted == false)
						{
							myThread = new MyThread();
							//myThread.setSeconds(5);
							myThread.setMinutes(15);
							System.out.println("threadSeconds: " + myThread.getSeconds());
							threadStarted = true;
							System.out.println("Thread has started");
							myThread.start();
							//NOTE: myThread.isAlive() will return true if the thread is still active.
							//NOTE: myThread.isAlive() will return false if the thread is not alive.
							doTask(documentString);
						}
						else
						{
							if(!myThread.isAlive())
							{
								System.out.println("Thread has stopped. Adding one to the subDirCounter file");
								subDirCounter += 1;
								threadStarted = false;
								directoryWatch.removeSourceFiles();
								directoryWatch.setSubDirExist(threadStarted);
							}
							else
							{
								doTask(documentString);
							}
						}
					}
					else if(documentString.contains("EmailTo.txt"))
					{
						System.out.println("--------EMAILING FILE--------");
						doTask(documentString);
					}
					else
					{
						File tempFile = new File(documentString);
						if(!(documentString.contains("sitepics") || documentString.contains("pics")))
						{
							boolean checkDir = tempFile.exists();
							if(checkDir == true)
							{
								System.out.println("Is a DIRECTORY");
								//threadStarted = false;
								directoryWatch.removeSourceFiles();
								directoryWatch.setSubDirExist(false);
							}
							else
							{
								System.out.println("IS NOT A DIRECTORY");
							}						
						}
					}
				}
				
				//When an entry is deleted
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
				{
					System.out.println("Delete: " + event.context().toString());
					String documentString = event.context().toString();
					if( (
							documentString.contains("png") || 	documentString.contains("PNG") ||
							documentString.contains("jpg") || 	documentString.contains("JPG") ||
							documentString.contains("jpeg") || 	documentString.contains("JPEG")
			  			 ) 
						 && (!documentString.contains("sitepics") || !documentString.contains("pics"))
						)
						{
							System.out.println("Calling DirectoryWatch java file");
						}
						else
						{
							System.out.println("Did not call DirectoryWatch java file");
						}
				}
				
				//When an entry is modified
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
				{
					System.out.println("Modify: " + event.context().toString());
					String documentString = event.context().toString();
					if( (
							documentString.contains("png") || 	documentString.contains("PNG") ||
							documentString.contains("jpg") || 	documentString.contains("JPG") ||
							documentString.contains("jpeg") || 	documentString.contains("JPEG")
			  			 ) 
						 && (!documentString.contains("sitepics") || !documentString.contains("pics"))
						)
						{
							System.out.println("Calling DirectoryWatch java file");
							
						}
						else
						{
							System.out.println("Did not call DirectoryWatch java file");
						}
				}
			}
		}
	}
	
	//Method that splits the incoming taskString and folder change and splits it at slashes
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
			if(splitTaskString[i].equals("EmailTo.txt"))
			{
				for(int k = 0; k < i; k++)
				{
					tempString += splitTaskString[k] + "\\";
				}
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
	
	//Method that returns the globalImageString as a String
	public String getImgString()
	{
		return globalImageString;
	}
	
	//Method to set the imageString to the globalImageString
	public void setImgString(String image)
	{
		globalImageString = image;
	}
	
	//Method to do the assigned task
	public void doTask(String taskString)
	{
		//If the file created contains the following:
		//1.	PNG (not case sensitive)
		//2.	JPG (not case sensitive)
		//3.	JPEG (not case sensitive)
		if( (
				taskString.contains("png") || 	taskString.contains("PNG") ||
				taskString.contains("jpg") || 	taskString.contains("JPG") ||
				taskString.contains("jpeg") || 	taskString.contains("JPEG")
			  ) && (!taskString.contains("sitepics") || !taskString.contains("pics"))
			)

			{
				//Calls directory watch file to create the directory called "ReducedImageSize"
				//If directory is already created, it will return true
				//if Directory exist == true, then the file will be grabbed by the ImageString method
				//And created the newly reduced image size of the file added in
				System.out.println("Calling DirectoryWatch java file");
				File f = new File(directoryWatch.getPath()); //Gets current Directory
				String pathString = "";
				try
				{
					pathString = f.getCanonicalPath() + "";
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				String folderDirString = splitString(taskString);
				System.out.println("Path: " + pathString);
				String newDirPath = directoryWatch.getPath() + "\\" + folderDirString + "sitepics";
				System.out.println("newDirPath: " + newDirPath);

				//Creates the directory inside the working directory
				boolean dirExist = directoryWatch.createDirectory(newDirPath);
				System.out.println("Directory Exist: " + dirExist);
				if(dirExist == true)
				{
					String partZipName = directoryWatch.getParentFileName(pathString + "\\" + folderDirString + "\\" + newDirPath);
					System.out.println("Parent FileName: " + partZipName);
				
					boolean subDirExist = directoryWatch.createSubDirectories(newDirPath, "pics" + subDirCounter);
					String fullZipName = partZipName + "-" + "pics" + subDirCounter;
					System.out.println("fullSubZipName: " + fullZipName);
					System.out.println("subDirExist: " + subDirExist);
					//NOTE: Zip-FILE method is working but with a long directory name :/
					System.out.println("TaskString: " + taskString);
					System.out.println("PathString: " + pathString);
					System.out.println("newDirPath: " + newDirPath);
					System.out.println("fullZipName: " + fullZipName);
					directoryWatch.getImageString(taskString, pathString, newDirPath, fullZipName);
					getSizeOfFile = directoryWatch.getKiloCounter();
					System.out.println("Size of File: " + getSizeOfFile);
				}
				else
				{
					System.out.println("Directory Does Not Exists");
				}
			}
			else if(taskString.contains("EmailTo.txt"))
			{
				File f2 = new File(directoryWatch.getPath()); //Gets current Directory
				String pathString2 = "";
				try
				{
					pathString2 = f2.getCanonicalPath() + "";
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				String folderDirString = splitString(taskString);
				System.out.println("Path: " + pathString2);
				String newDirPath = directoryWatch.getPath() + "\\" + folderDirString;
				System.out.println("newDirPath: " + newDirPath);
				
				System.out.println("TaskString: " + taskString);
				String folderDirString2 = splitString(taskString);
				System.out.println("Path: " + pathString2);
				String newDirPath2 = folderDirString2 + "sitepics";
								
				String parentName = directoryWatch.getParentFileName(pathString2 + "\\" + folderDirString2);
				System.out.println("Parent FileName: " + parentName);
				String fullZipName2 = parentName + "-" + "pics" + subDirCounter;
				
				String getFullPathName = newDirPath + fullZipName2 + ".zip";
				System.out.println("fullZipName: " + fullZipName2);
				System.out.println("getFullPathName: " + getFullPathName);
				//All Credentials for Emailing
				String readTxtFile = directoryWatch.getPath() + "\\" + taskString;
				readTextFile(readTxtFile, fullZipName2, getFullPathName);
			}
			else
			{
				System.out.println("Did not call DirectoryWatch java file");
			}
	}
	
	ArrayList<String> emailParticipants = new ArrayList<String>();
	ArrayList<String> emailToList = new ArrayList<String>();
	ArrayList<String> SubjectList = new ArrayList<String>();
	ArrayList<String> bodyList		= new ArrayList<String>();

	
	public void addParticipants(String participant)
	{
		System.out.println("Participants: " + participant);
		if(!participant.contains("EmailTo: "))
		{
			System.out.println("ERROR: NO EMAILTO");
		}
		else
		{
			String[] splitBody = participant.split("EmailTo:");
			
			String emailString = splitBody[1].trim();
			System.out.println("emailString: " + emailString);
			
			int findCommas = emailString.indexOf(",");
			if(findCommas == -1)
			{
				System.out.println("DOES NOT CONTAIN COMMA");
				System.exit(1);
			}
			else
			{
				String[] splitComma = emailString.split(",\\s*");
				for(int k = 0; k < splitComma.length; k++)
				{
					emailParticipants.add(splitComma[k]);
				}
			}
		}
	}

	public void readTextFile(String filePath, String zipName, String directZipPath)
	{
		System.out.println("filePath in readTextFile: " + filePath);
		System.out.println("zipName in readTextFile:  " + zipName);
		System.out.println("directZipPath in readTextFile : " + directZipPath);
	
		System.out.println("filePath: " + filePath);
		String host = "smtp.gmail.com";
		String port = 587 + "";
		String mailFrom = "<youremail@domain.com>";
		String password = "<yourpassword>";
		String mailTo = "";
		
		String subject = "";
		
		String bodyMessage = "";
		
		try
		{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(filePath);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			ArrayList<String> textArray = new ArrayList<String>();
			int countLines = 0;
			//Read File Line By Line
			while ((strLine = br.readLine())!= null)
			{
				if(strLine.trim().length() > 0)
				{
					// Print the content on the console
					//System.out.println (strLine);
					textArray.add(strLine);
					countLines++;
				}
			}
			
			//Close the input stream
			in.close();
			
			//Command Line print
			System.out.println("");
			System.out.println("Number of fileLines: " + countLines);
			
			for(int i = 0; i < textArray.size(); i++)
			{
				System.out.println("i: " + i + "  " + textArray.get(i));
			}
			
			System.out.println("Adding participants for EMAILING");
			addParticipants(textArray.get(0));
			
			int addParticipantCounter = 0;
			for(int k = 0; k < emailParticipants.size(); k++)
			{
				for(int j = 0; j < textArray.size(); j++)
				{
					System.out.println("Line " + j + ": " + textArray.get(j));
					String word = textArray.get(j);
					System.out.println("Word: " + word);
					String[] guess = {"EmailTo:","Subject:","Body:"};
					if(word.contains("EmailTo:"))
					{
						mailTo = emailParticipants.get(addParticipantCounter);
						addParticipantCounter++;
						System.out.println("mailTo: " + mailTo);
					}
					else if(word.contains("Subject:"))
					{
						String[] splitSubject = word.split("Subject:");
						if(splitSubject.length == 0)
						{
							subject = zipName;
						}
						else
						{
							subject = splitSubject[(splitSubject.length - 1)];
						}
					}
					else
					{
						if(word.contains("Body:"))
						{
							String[] splitBody = word.split("Body:");
							if(splitBody.length == 0)
							{
								bodyMessage = "Thank you for choosing FinishIT.  It has been a pleasure working with you and servicing your client." +
								"  Be sure to check us out at www.finishitnow.com";
							}
							else
							{
								bodyMessage += splitBody[(splitBody.length - 1)];
							}
						}
						else
						{
							bodyMessage += "\n" + word;
						}
					}
				}
				System.out.println("EmailTo: " + mailTo);
				System.out.println("Subject: " + subject);
				System.out.println("Body: " + bodyMessage);
				emailToList.add(mailTo);
				SubjectList.add(subject);
				bodyList.add(bodyMessage);
			}
			
			//NOTE: NEED TO LOOK IN DIRECTORY OF WHERE THE FILE WAS CREATED TO GET THE ZIP FILE OF IT
			try
			{
				String[] mailingTo = new String[emailToList.size()];
    			mailingTo = emailToList.toArray(mailingTo);
    
				directoryWatch.sendEmail(host, port, mailFrom, password, mailingTo, subject, bodyMessage, directZipPath);
			}
			catch(AddressException ae)
			{
				ae.printStackTrace();
			}
			catch(MessagingException me)
			{
				me.printStackTrace();
			}
			//resetting all the global used arrays
			textArray.clear();
			resetEmailInformation();
		}
		catch(Exception e)
		{
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void resetEmailInformation()
	{
		emailToList.clear();
		SubjectList.clear();
		bodyList.clear();
		emailParticipants.clear();
	}
	
	//Method that does the same thing but watches only the current Directory
	public void watchDirectory(String myDir)
	{
		Path path;
		//define a folder root
		if(myDir.isEmpty())
		{
			path = Paths.get(".");
		}
		else
		{
			path = Paths.get(myDir + "");
		}
		try
		{
			WatchService watcher = path.getFileSystem().newWatchService();
			path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
			StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
			
			WatchKey watckKey = watcher.take();
			
			List<WatchEvent<?>> events = watckKey.pollEvents();
			
			for (WatchEvent event : events)
			{
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
				{
					System.out.println("Created: " + event.context().toString());
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
				{
					System.out.println("Delete: " + event.context().toString());
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
				{
					System.out.println("Modify: " + event.context().toString());
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}	
	}
}