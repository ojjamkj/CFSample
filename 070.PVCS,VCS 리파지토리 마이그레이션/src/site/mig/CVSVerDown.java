package site.mig;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CVSVerDown extends CommonVerDown{
	
	public static void main(String args[]) throws Exception {
		CVSVerDown pvcsVerDown = null;
		
		try{
			if(args.length < 3){
				System.out.println("argument' length is " + args.length);
				System.out.println("[1] WORKSPACE ROOT DIR =  C:/TEMP" );
				System.out.println("[2] CVS MODULE NAME =  CFEngine");
				System.out.println("[3] SAVE DIR ROOT =  C:/TEMP/Save");
				System.out.println("[4] CVS CONNECTION INFO =  :pserver:userid:userpwd@127.0.0.1:/CVSRepositoryName");
				System.exit(1);
			}
			
			
			System.out.println("[1] WORKSPACE ROOT DIR =  " + args[0]);
			System.out.println("[2] CVS MODULE NAME =  " + args[1]);
			System.out.println("[3] SAVE DIR ROOT =  " + args[2]);
			System.out.println("[4] CVS CONNECTION INFO =  " + args[3]);
			
			
			String pvcsRootDir = args[0];//PVCS Root
			String bizRootDir = args[1]; //업무 Root
			String saveRootDir = args[2];//파일저장 Root
			String cvsRoot = args[3];//파일저장 Root
			
			
			Date dt = new Date();
			dt.setTime(System.currentTimeMillis());
			
			pvcsVerDown = new CVSVerDown(pvcsRootDir, bizRootDir, saveRootDir, cvsRoot);
			pvcsVerDown.executeCommand(pvcsVerDown.getCheckoutCommand(), null);
			System.out.println("Module Checkout is finished...");
			
			pvcsVerDown.init();
			System.out.println("init...");
			pvcsVerDown.start();
			System.out.println("start time : " + dt.toLocaleString());
			dt.setTime(System.currentTimeMillis());
			System.out.println("end time : " + dt.toLocaleString());
			System.out.println("finished...");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pvcsVerDown.release();
		}
	}
	
	public CVSVerDown(){
		
	}
	
	public CVSVerDown(String pvcsRootDir, String bizRootDir, String saveRootDir, String cvsRoot){
		PVCS_ROOT_DIR = pvcsRootDir;
		BIZ_ROOT_DIR = bizRootDir;
		SAVE_ROOT_DIR = saveRootDir;
		CVS_ROOT = cvsRoot;
	}
	

	String CVS_ROOT;

	protected void doLocalCollect(String curDir) throws Exception
	{
		File curFile = new File(curDir);
		File[] listFiles = curFile.listFiles();
		
		int size  = listFiles.length;
		
		for(int a=0; a<size; a++)
		{
			File fi = listFiles[a];
			if(fi.getAbsolutePath().indexOf("CVS") > -1 || fi.isDirectory()) continue;
			String arFilePath = fi.getAbsolutePath();
			
			String relativeFilePath = arFilePath.replaceAll("\\\\", "/").replaceAll(PVCS_ROOT_DIR+"/", "");
			
			int maxRevisionNo = parseMaxRevision(executeCommand(getMaxRevisionNoCommand(relativeFilePath), null));
			System.out.println("MAX_REVISION_NO IS " + maxRevisionNo);
			master1.write(relativeFilePath.replaceFirst(BIZ_ROOT_DIR , "")+'\n');
			
			for(int i=1 ;i<=maxRevisionNo; i++)
			{
				String version = ("1."+i);
				String cfVersion = (i+".0");
				String downFilePath = SAVE_ROOT_DIR + "/" + relativeFilePath + "." + cfVersion;
				
				File f = new File(downFilePath);
				if(f.getParentFile().exists()==false)
					f.getParentFile().mkdirs();
				
				File f1 = new File(PVCS_ROOT_DIR+"/"+relativeFilePath);
				if(f.exists()==false)
					executeCommand(getFileWithVersionCommand(relativeFilePath, downFilePath, version, f1), f1.getParentFile());
				String checkinInfo = executeCommand(getFileInfoCommand(relativeFilePath, downFilePath, version, f1), f1.getParentFile());
				String checkinTime = parseCheckInTime(checkinInfo).replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
				String checkiUser = parseUser(checkinInfo);
				String checkinComment = parseComment(checkinInfo, fi.getName());
				
				master2.write(cfVersion +"\t" + relativeFilePath.replaceFirst(BIZ_ROOT_DIR , "") +"\t" + checkinTime + "\t" + checkiUser + "\t"+ checkinComment+ '\n');
			}
			
			
		}
	}
	

	
	protected List getFileInfoCommand(String filePath, String downFilePath, String revisionNo, File f) throws Exception {
		
		ArrayList command = new ArrayList();
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		if(isWindows){
			command.add("cmd");
			command.add("/C");
		}
		command.add("cvs");
		command.add("-d");
		command.add(CVS_ROOT);
		command.add("log");
		command.add("-r" + revisionNo);
		command.add(f.getName());
		return (List)command;
    }

	protected List getCheckoutCommand() throws Exception {
			
		ArrayList command = new ArrayList();
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		if(isWindows){
			command.add("cmd");
			command.add("/C");
		}
		command.add("cvs");
		command.add("-d");
		command.add(CVS_ROOT);//":pserver:ojjamkj:simple@172.16.17.110:/ChangeFlow/CF7"
		command.add("checkout");
		command.add(BIZ_ROOT_DIR);
		return (List)command;
	}

	protected List getFileWithVersionCommand(String filePath, String downFilePath, String revisionNo, File f) throws Exception {
		ArrayList command = new ArrayList();
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		if(isWindows){
			command.add("cmd");
			command.add("/C");
		}
		command.add("cvs");
		command.add("-d");
		command.add(CVS_ROOT);
		command.add("up");
		command.add("-p");
		command.add("-r" + revisionNo);
		command.add(f.getName());
		command.add(">");
		command.add(downFilePath);
		return (List)command;
    }

	protected List getMaxRevisionNoCommand(String relativeFilePath) throws Exception {
		
		ArrayList command = new ArrayList();
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		if(isWindows){
			command.add("cmd");
			command.add("/C");
		}
		command.add("cvs");
		command.add("-d");
		command.add(CVS_ROOT);
		command.add("log");
		command.add(relativeFilePath);
		return (List)command;
    }
	
	protected int parseMaxRevision(String result)
	{
		int startPos = result.indexOf("head:") + 6;
		String maxRevision = result.substring(startPos , result.indexOf("\n", startPos));
		System.out.println("parseMaxRevision() " + maxRevision);
		return Integer.parseInt(maxRevision.trim().substring(maxRevision.indexOf(".")+1));
	}
	protected String parseCheckInTime(String result)
	{
		int startPos = result.indexOf("date:") +6;
		String maxRevision = result.substring(startPos, result.indexOf(";", startPos));
		System.out.println("parseCheckInTime():" + maxRevision);
		return maxRevision; 
	}
	protected String parseUser(String result)
	{
		int startPos = result.indexOf("author: ") + 8;
		String maxRevision = result.substring(startPos, result.indexOf(";", startPos));
		System.out.println("parseUser():" + maxRevision);
		return maxRevision;
	}
	protected String parseComment(String result, String fileName)
	{
		String comment=" ";
		try{
			int startPos = result.indexOf(fileName+";")+fileName.length()+1;
			int endPos = result.indexOf("Committed on the Free", startPos);
			comment = result.substring(startPos, endPos ==-1 ? result.length():endPos);
			
			comment = comment.replaceAll("\r\n", " ");
			comment = comment.replaceAll("\n", " ");
			comment = comment.replaceAll("\t", "  ");
			System.out.println("Comment():" + comment);
		}catch(Exception e){
			
		}
		return comment;
	}
	
	
}