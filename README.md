MailReader
==========
This project was a project that I have worked on in my spare time.
It does the following:
  1.  Watches a directory for constant changes
  2.  If any files in jpeg and png are pushed into any folder, it resizes them to a smaller image and is put into a zip file (maxing at 19.5MB)
  3.  If EmailTo file is filled out correctly and placed in that particular file, it will email it to that address from the email that you provide into that particular folder

Vector<MyThread> list (whatever name necessary) = new Vector<MyThread>(); 
ArrayList<Boolean> listBoolean = new ArrayList<Boolean>();

(Globalize Vector<MyThread>/ArrayList<Boolean> in main class (directoryWatchFile.java)

Vector of threads

Define a thread
	File name and length of delay (String, int)
	thread.sleep(delay - - length of delay or int)
	Run a method in directoryWatch that will process picsX (x = integer for counting subdirectory)

Before defining/writing thread
	Check the Vector if the file location/file name/directory already exists
		Solve this problem: search using indexOf, same for boolean true/false arraylist
	If there isn't, make one
		list.add(new MyThread(String directoryName, int delay)
			directoryName - - file location ("C:\Users\eaaliprantis\Dropbox\FINISH_IT\WORKORDERS\test\testEmail\")
			delay - - how long to delay/wait until process can begin

Processing (process - - not official)
	Process images (does methods already created)
	Checks vector for the directory that it is supposed to be watching
	Removes irrelevent thread
		
		----------REMOVE THREAD----------
		for(MyThread t : List)
		{
			if(FileNamein* == t.getFileName())
			{
				int x = list.indexOf(t);
				List.remove(t) OR List.remove(x);
				listBoolean.remove(x);
			}
		}

		* - FileNameIn is a String that method was instantiated/started the first time

	

Once thread is done processing
	set boolean arraylist to true for that particular thread
	check vector where the arraylist says true
	And remove entries from both sides
	



Analogy/Process:

	When imageA is a folder
	Image01 is added to the folder
	Create a thread for ImageA (String directoryFileLocation, int delay)
		Add it to the vector and arraylist 
		(set arraylist boolean for false)
	After x amount of time is up (when the delay is finished)
		Thread will call the directory watcher's image - - process image
		Look at the directoryFileLocation's name (getFileName) - - grab string part
		Makes image method run (for all the files that are in that folder, process images (reduce image) for that folder/directoryFileLocation (string in the thread)
			After image method run is complete,
				------call REMOVE THREAD--------
				(destroy the thread relevant to that folder && boolean as well)
					
