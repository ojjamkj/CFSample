package site.mig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public abstract class CommonVerDown {
	
	FileWriter master1 = null;
	FileWriter master2 = null;
	
	protected ArrayList jobQueue = new ArrayList();
	String BIZ_ROOT_DIR;
	String SAVE_ROOT_DIR;
	String PVCS_ROOT_DIR;
	
	protected void init() throws Exception {

        ArrayList list = new ArrayList();
        list.add(PVCS_ROOT_DIR +"/"+ BIZ_ROOT_DIR);
		List dirList = findDirectoryOnly(PVCS_ROOT_DIR +"/"+ BIZ_ROOT_DIR, list); 

		int dirSize = dirList.size();
		for (int i = 0; dirList != null && i < dirSize; i++) {
			this.jobQueue.add(dirList.get(i).toString());
		}
		
		File f1 = new File(SAVE_ROOT_DIR+"/" + BIZ_ROOT_DIR +"/CF_SRC.txt");
		File f2 = new File(SAVE_ROOT_DIR+"/" + BIZ_ROOT_DIR +"/CF_VERSION.txt");
		if(f1.exists()) f1.delete();
		if(f2.exists()) f2.delete();
		
		f1.getParentFile().mkdirs();
		f2.getParentFile().mkdirs();
		f1.createNewFile();
		f2.createNewFile();
		
		master1 = new FileWriter(f1);
		master2 = new FileWriter(f2);

    }
	
	
	protected void start() throws Exception {
        try {
        	while(!jobQueue.isEmpty()){
        		String data = (String)jobQueue.remove(0);
        		doLocalCollect(data);
        	}
        }
        catch (Exception e) {
            throw e;
        }
    }
	
	
	protected abstract void doLocalCollect(String curDir) throws Exception;
	
	
	protected String executeCommand(List commandList, File workingDir) throws Exception
	{
		
		String resultStr = "";
		try{
			printComment(commandList);
			
			ProcessBuilder pb = new ProcessBuilder(commandList);
			
			if(workingDir != null ) {
				pb.directory(workingDir);
				System.out.println("working directory : " + pb.directory().getAbsoluteFile());
			}
			
			Process proc = pb.start();

			// error 메시지
			StreamGobbler2 errorGobbler = null;
			// output 메시지
			StreamGobbler2 outputGobbler = null;
			errorGobbler = new StreamGobbler2(proc.getErrorStream(),"");
			// output 메시지
			outputGobbler = new StreamGobbler2(proc.getInputStream(),"");
			DefaultSGListener defaultListener = new DefaultSGListener();
			errorGobbler.addDataListener(defaultListener);
			outputGobbler.addDataListener(defaultListener);

			// kick them off
			errorGobbler.start();

			outputGobbler.start();

			int exitVal = -1;
			{
				exitVal = proc.waitFor();
			}


			if(null != errorGobbler)
				errorGobbler.stop();
			if(null != outputGobbler)
				outputGobbler.stop();
			if(null != proc)
				proc.destroy();
			resultStr = defaultListener.getResult();
			
			System.out.println(resultStr);
						
		}catch(Exception e){
			throw(e);
		}finally{
			
		}
		
		
		return resultStr;
	}
	
	
	protected String executeCommand1(List commandList, File workingDir) throws Exception
	{
		
		String resultStr = "";
		try{
			printComment(commandList);
			
			
			Process proc = Runtime.getRuntime().exec((String[])commandList.toArray(new String[commandList.size()]));
			ProcessBuilder pb = new ProcessBuilder(commandList);
			
//			if(workingDir != null ) {
//				pb.directory(workingDir);
//				System.out.println("working directory : " + pb.directory().getAbsoluteFile());
//			}
			
			//Process proc = pb.start();

			// error 메시지
			StreamGobbler2 errorGobbler = null;
			// output 메시지
			StreamGobbler2 outputGobbler = null;
			errorGobbler = new StreamGobbler2(proc.getErrorStream(),"");
			// output 메시지
			outputGobbler = new StreamGobbler2(proc.getInputStream(),"");
			DefaultSGListener defaultListener = new DefaultSGListener();
			errorGobbler.addDataListener(defaultListener);
			outputGobbler.addDataListener(defaultListener);

			// kick them off
			errorGobbler.start();

			outputGobbler.start();

			int exitVal = -1;
			{
				exitVal = proc.waitFor();
			}


			if(null != errorGobbler)
				errorGobbler.stop();
			if(null != outputGobbler)
				outputGobbler.stop();
			if(null != proc)
				proc.destroy();
			resultStr = defaultListener.getResult();
			
			System.out.println(resultStr);
						
		}catch(Exception e){
			throw(e);
		}finally{
			
		}
		
		
		return resultStr;
	}
	void release()
	{
		if(master1 != null) try{ master1.close();}catch(Exception e){}
		if(master2 != null) try{ master2.close();}catch(Exception e){}
	}
	
	protected void printComment(List cmd)
	{
		System.out.println("command : " );
		for(int i=0; i<cmd.size(); i++){
			System.out.print(cmd.get(i) +" " );
		}
		System.out.println("");
	}
	
	protected List findDirectoryOnly(String path, List returnList) throws Exception{
		File f = new File(path);

		File[] list = f.listFiles();
		if (!path.endsWith(File.separator)){
			path += "/";
		}

		if (list != null){
			int listSize = list.length;
			for (int i = 0; i < listSize; i++){
				if (list[i].isDirectory()){
					returnList.add(list[i].getAbsolutePath());

					findDirectoryOnly(path + list[i].getName(), returnList);
				}
			}
		}

		return returnList;
	}
	
	
	


	/**
	 * A separate thread to listen to an InputStream and pull all the data
	 * out of it. When data is found a new event is fired share the data with
	 * @author Ryan Morse
	 */
	public class StreamGobbler2 implements Runnable {
		 
		String prefix;
		public StreamGobbler2(InputStream is , String prefix) {
			if(null != is) {
				this.is = is;
				line = new ByteArrayOutputStream();
				listeners = new ArrayList ();
			}
			this.prefix=prefix;
			locked = false;
	
		}
		 
		
		/**
		 * Spawns the new thread that this class will run in.  From the Runnable
		 * interface spawning the new thread automatically calls the run() method.
		 * This must be called by the implementing class in order to start the
		 * StreamGobbler.
		 */
		//Make sure to call this method to start the StreamGobbler
		public void start() {
			t = new Thread(this, "StreamGobbler");
			t.start();
		}
		
		/**
		 * Checks to see if the gobbler is still running.
		 * @return boolean representing whether or not it is sill running
		 */
		public boolean isRunning() {
			return (null != t);
		}
	
		/**
		 * The main method of this class. It monitors the provided thread to see
		 * when new data is available and then appends it to its current list of
		 * data.  When a new line is read it will fire a DataEvent for listeners
		 * to get ahold of the data.
		 */
		public void run() {
			Thread thisThread = Thread.currentThread();
			 
			try {
				int val=-1;
	
				while(t == thisThread) {
					while(0 < is.available()) {
						if(-1 == (val = is.read()))
							this.stop();
						else
							line.write(  val);
						if ('\n' == val)
							this.fireNewDataEvent();
						
					}
					try {
						Thread.sleep(10);
					} catch(InterruptedException ie) {}
				}
			} catch (IOException ioe) {}	//If stream closed before thread shuts down
		}
	
		/**
		 * Stops the gobbler from monitering the stream, and fires one last data event
		 * to make sure that listeners have the entire contents of what was read in
		 * from the stream.
		 */
		public synchronized void stop() {
			try {	//Make sure we don't stop while there is still data in the stream
				while(0 != is.available()) {
					Thread.sleep(10);
				}
			} catch(Exception e) {}
			t = null;
			notify();
			//Fire one last time to ensure listeners have gotten everything.
			this.fireNewDataEvent();
		}
		
		/**
		 * Method for getting the most recently read line from the stream.
		 * @return String representing the current line being read from the 

		 */
		public String readLine() {
			return line.toString();
		}
		
		/**
		 * Gets rid of all internal references to objects.
		 */
		public void dispose() {
			if(isRunning())
				stop();
			line = null;
			t = null;
			is = null;
		}
		
		/**
		 * Fires new events to everything that is monitering this stream. Then clears
		 * the current line of data.
		 */
		private void fireNewDataEvent() {
			//Implement our own lock since using synchronized causes a deadlock here
		/*	while(locked) {
				try {
					wait(10);
				} catch(Exception e) {}
			}
			locked = true;*/
			for(int i = 0; i < listeners.size(); i++)
			{
				String result = line.toString();
				if(result != null && result.length()>0)
				{
					//((IGobblerListener)(listeners.get(i))).handleDataEvent(prefix + ">"  + result  );
					((IGobblerListener)(listeners.get(i))).handleDataEvent(result  );
				}
			}
			line.reset();
			locked = false;
		}
		
		public void fireNewDataEvent(String l) {
			//Implement our own lock since using synchronized causes a deadlock here
		/*	while(locked) {
				try {
					wait(10);
				} catch(Exception e) {}
			}
			locked = true;*/
			for(int i = 0; i < listeners.size(); i++)
			{
				((IGobblerListener)(listeners.get(i))).handleDataEvent(l);
			}
	//		line.delete(0, line.length());
			locked = false;
		}
		 
		/**
		 * Registers the provided listener to get data events.
		 * @param l A listener that needs to moniter the stream.
		 */
		public void addDataListener(IGobblerListener l) {
			
			if(l != null && !listeners.contains(l))
			{
				
				listeners.add(l);
				
			}
			
		}
	
		/**
		 * Unregisters the provied listener from getting new data events.
		 * @param l A listener that is monitering the stream and should be removed
		 */
		public void removeDataListener(IGobblerListener l) {
			
			if(listeners.contains(l))
				listeners.remove(l);
		}
	
		/**
		 * are lstening for data events.
		 * @return ArrayList of all of the listeners registered.
		 */
		public ArrayList getDataListeners() {
			return listeners;
		}
		
		private ArrayList  listeners;
		private java.io.ByteArrayOutputStream line;
		private Thread t;
		private InputStream is;
		
		 
		private boolean locked;
	}


	public interface IGobblerListener {
	
		void handleDataEvent(String string);
	
	}

	public class DefaultSGListener implements IGobblerListener
	{
		StringBuffer sb = new StringBuffer();
		public void handleDataEvent(String string) {
			sb.append(  string);
			
		}
		public String getResult()
		{
			return sb.toString();
		}
		
	}
}