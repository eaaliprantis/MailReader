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
import java.util.*;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class MailReader
{

	ArrayList<String> fromList = new ArrayList<String>();
	ArrayList<String> subjectList = new ArrayList<String>();
	ArrayList<String> sentDateList = new ArrayList<String>();
	ArrayList<String> messageList = new ArrayList<String>();
	
	int fromListCounter = 0;
	
	InputStream is;
	File emailFileList;
	FileWriter fileWritter;
	BufferedWriter output;
	
	boolean isOpen = false;
	
	Folder inbox;
	
	// Constructor of the class.
	public MailReader()
	{
		
	}
	
	public static void main(String[] args) 
	{
		MailReader fitbot = new MailReader();
		fitbot.runProgram();
	}
	
	public void runProgram()
	{
		refreshMail();
		//getMail4FIT();
	}
	
	public void refreshMail()
	{
		Timer timer=  new Timer();
		int startingTime=10000; //millisecond 10 seconds=10000
		int delayTime=1000; // millisecond 1 second
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				getMail4FIT();
			}
		},startingTime,delayTime);
	}
	
	
	public void getMail4FIT()
	{
		System.out.println("Inside MailReader()...");
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		
		/* Set the mail properties */
		Properties props = System.getProperties();
		// Set manual Properties
		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", "995");
		props.setProperty("mail.pop3.socketFactory.port", "995");
		props.put("mail.pop3.host", "pop.gmail.com");
		
		try
		{
			/* Create the session and get the store for read the mail. */
			Session session = Session.getDefaultInstance(System.getProperties(), null);
		
			Store store = session.getStore("pop3");
			store.connect("pop.gmail.com", 995, "fit-bot@devcab.com","finish@IT");
		
			/* Mention the folder name which you want to read. */
		
			// inbox = store.getDefaultFolder();
			// inbox = inbox.getFolder("INBOX");
			inbox = store.getFolder("INBOX");
		
			/* Open the inbox using store. */
			inbox.open(Folder.READ_ONLY);
		
			/* Get the messages which is unread in the Inbox */
			Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			System.out.println("No. of Unread Messages : " + messages.length);
		
			/* Use a suitable FetchProfile */
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.CONTENT_INFO);
		
			inbox.fetch(messages, fp);
			try
			{
				System.out.println("Printing all emails");
				printAllMessages(messages);
				inbox.close(true);
				store.close();
			}
			catch (Exception ex)
			{
				System.out.println("Exception arise at the time of read mail");
				ex.printStackTrace();
			}
		}
		/*
		* catch (NoSuchProviderException e)
		* 
		* {
		* 
		* e.printStackTrace();
		* 
		* System.exit(1);
		* 
		* }
		*/
		catch (MessagingException e)
		{
			System.out.println("Exception while connecting to server: "
			+ e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(2);
		}
	}

	public void printAllMessages(Message[] msgs) throws Exception
	{
		for (int i = 0; i < msgs.length; i++)
		{
			System.out.println("MESSAGE #" + (i + 1) + ":");
			printEnvelope(msgs[i]);
		}
	}

	/* Print the envelope(FromAddress,ReceivedDate,Subject) */
	public void printEnvelope(Message message) throws Exception
	{
		Address[] a;
		// FROM
		
		if ((a = message.getFrom()) != null)
		{
			for (int j = 0; j < a.length; j++)
			{
				String fromListString = "FROM: " + a[j].toString();
				System.out.println(fromListString);
				fromList.add(fromListString);
			}
		}
		
		// TO
		if ((a = message.getRecipients(Message.RecipientType.TO)) != null)
		{
			for (int j = 0; j < a.length; j++)
			{
				System.out.println("TO: " + a[j].toString());
			}
		}
		
		String subject = message.getSubject();
		
		Date receivedDate = message.getReceivedDate();
		Date sentDate = message.getSentDate(); // receivedDate is returning
                                                // null. So used getSentDate()
																
		String content = message.getContent().toString();
		String subjectInfo = "Subject: " + subject;
		System.out.println(subjectInfo);
		subjectList.add(subjectInfo);
		if (receivedDate != null)
		{
			System.out.println("Received Date : " + receivedDate.toString());
		}
		
		String sentDateString = "Sent Date : " + sentDate.toString();
		System.out.println(sentDateString);
		sentDateList.add(sentDateString);
		
		System.out.println("Content : " + content);
		
		getContent(message);
		
		//Adds one to the counter
		fromListCounter++;
		writetoTextFile();
	}
	
	String globalMessageBodyString = "";
	public void getContent(Message msg)
	{
		try
		{
			String contentType = msg.getContentType();
			System.out.println("Content Type : " + contentType);
			Multipart mp = (Multipart) msg.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
			{
				dumpPart(mp.getBodyPart(i));
			}
		}
		catch (Exception ex)
		{
			System.out.println("Exception arise at get Content");
			ex.printStackTrace();
		}
	}
	
	public void dumpPart(Part p) throws Exception
	{
		// Dump input stream ..
		is = p.getInputStream();
		// If "is" is not already buffered, wrap a BufferedInputStream
		// around it.
		if (!(is instanceof BufferedInputStream))
		{
			is = new BufferedInputStream(is);
		}
		
		int c;
		System.out.println("Message : ");
		String aChar = "";
		while ((c = is.read()) != -1)
		{
			System.out.write(c);
			String getValueOfInt = String.valueOf(c);
			aChar += new Character((char)c).toString();
			globalMessageBodyString += new Character((char)c).toString();
		}
		System.out.println("aChar: " + aChar);
		System.out.println("globalMessageBodyString: " + globalMessageBodyString);
		messageList.add(globalMessageBodyString);
		globalMessageBodyString = "";
		aChar = "";
	}
	
	public void writetoTextFile()
	{
		try
		{
			emailFileList = new File("fitbotApp.txt");
			if(!emailFileList.exists())
			{
				emailFileList.createNewFile();
    		}
			//true = append file
			if(!isOpen)
			{
    			fileWritter = new FileWriter(emailFileList.getName(),true);
				output = new BufferedWriter(fileWritter);
				isOpen = true;
			}
			output.write("\r\n");
			output.write(fromList.get((fromListCounter-1)));
			output.write("\r\n");
			output.write(subjectList.get((fromListCounter-1)));
			output.write("\r\n");
			output.write(sentDateList.get((fromListCounter-1)));
			output.write("\r\n");
			output.write(messageList.get((fromListCounter-1)));
			output.write("\r\n");
			output.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}