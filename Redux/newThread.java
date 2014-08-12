public class newThread extends Thread
{
	private int seconds, minutes, hours;
	private String fileName;
	private volatile boolean threadDone = false;
	
	public newThread()
	{
	
	}
	
	public newThread(String _fileName, int secs)
	{
	
	}
	
	public newThread(int secs)
	{
		seconds = secs;
	}
	
	public void setSeconds(int secs)
	{
		seconds = secs;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setMinutes(int mins)
	{
		minutes = mins;
		seconds = minutes*60;
	}
	
	public void setHours(int hrs)
	{
		hours = hrs;
		minutes = hours*60;
		seconds = minutes*60;
	}
	
	public int getSeconds()
	{
		return seconds;
	}
	
	public int getMinutes()
	{
		return minutes;
	}
	
	public int getHours()
	{
		return hours;
	}
	
	public newThread(int secs, int mins, int hrs)
	{
		seconds = secs;
		minutes = mins;
      hours = hrs;
   }
	
	public void run()
	{
		/*
		 1000 milliseconds = 1 second
		 60 seconds = 1 minute
		 60  * 1000 * (int) seconds = 300,000 milliseconds
		 Reduce the thousands place, and you get 300 increments of 1000 milliseconds
		*/
		while(threadDone == false)
		{
			for(int i = seconds; i >= 0; i -= 1)
			{
				if(i == 0)
				{
					threadDone = true;
					break;
				}
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		System.out.println("THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED THREAD FINISHED");
	}
}