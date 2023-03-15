package site.mig;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class PVCSVerDown extends CommonVerDown{
	
	public static void main(String args[]) throws Exception {
		PVCSVerDown pvcsVerDown = null;
		
		try{
			if(args.length < 3){
				System.out.println("argument' length is " + args.length);
				System.out.println("[1] PVCS ROOT =  /pvcs");
				System.out.println("[2] BIZ DIR NAME =  BizName");
				System.out.println("[3] SAVE DIR ROOT =  /pvcs/cf");
				System.exit(1);
			}
			
			
			System.out.println("[1] PVCS ROOT DIR =  " + args[0]);
			System.out.println("[2] BIZ DIR NAME =  " + args[1]);
			System.out.println("[3] SAVE DIR ROOT =  " + args[2]);
			
			
			String pvcsRootDir = args[0];//PVCS Root
			String bizRootDir = args[1]; //업무 Root
			String saveRootDir = args[2];//파일저장 Root
			
			
			pvcsVerDown = new PVCSVerDown(pvcsRootDir, bizRootDir, saveRootDir);
//			pvcsVerDown.executeCommand(pvcsVerDown.getCheckoutCommand(), null);
//			System.out.println("Module Checkout is finished...");
			Date dt = new Date();
			dt.setTime(System.currentTimeMillis());
			
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
	
	public PVCSVerDown(){
		
	}
	
	public PVCSVerDown(String pvcsRootDir, String bizRootDir, String saveRootDir){
		PVCS_ROOT_DIR = pvcsRootDir;
		BIZ_ROOT_DIR = bizRootDir;
		SAVE_ROOT_DIR = saveRootDir;
	}
	
	protected void doLocalCollect(String curDir) throws Exception
	{
		File curFile = new File(curDir);
		File[] listFiles = curFile.listFiles();
		
		for(int a=0; a<listFiles.length; a++)
		{
			File fi = listFiles[a];
			
			if( !fi.getAbsolutePath().endsWith("-arc")  || fi.isDirectory()) continue;
			String arFilePath = fi.getAbsolutePath();
			
			String relativeFilePath = arFilePath.replaceAll("\\\\", "/").replaceAll(PVCS_ROOT_DIR+"/", "");
			
			String revisionInfo = executeCommand1(getMaxRevisionNoCommand(arFilePath), null);
			
			if(revisionInfo==null || "".equals(revisionInfo))
				continue;
			
			int maxRevisionNo = parseMaxRevision(revisionInfo);
			System.out.println("MAX_REVISION_NO IS " + maxRevisionNo);
			String realFileName = relativeFilePath.replaceFirst(BIZ_ROOT_DIR , "").replaceFirst("-arc", "").replaceFirst("/archives", "");
			master1.write(realFileName +'\n');
			
			
			HashMap revisionInfoMap = parseRevisionInfo(revisionInfo);
			
			for(int i=0 ;i<maxRevisionNo; i++)
			{
				String version = ("1."+i);
				String cfVersion = ((i+1)+".0");
				String downFilePath = SAVE_ROOT_DIR + "/" + relativeFilePath.replaceFirst("-arc", "").replaceFirst("/archives", "") + "." + cfVersion;
				
				File f = new File(downFilePath);
				if(f.getParentFile().exists()==false)
					f.getParentFile().mkdirs();
				
				File f1 = new File(PVCS_ROOT_DIR+"/"+relativeFilePath);
				if(fi.exists()==false)
					executeCommand1(getFileWithVersionCommand(relativeFilePath, downFilePath, version, f1), f1.getParentFile());
				
				String checkinTime = "";
				String checkiUser = "";
				String checkinComment = "";
				
				if(revisionInfoMap.get(version) != null)
				{
					String[] revi = (String[])revisionInfoMap.get(version);
					
					checkinTime = parseCheckInTime(revi[9]);
					checkinComment = revi[12].replaceAll("\t", " ").replaceAll("\n", " ");
					if(checkinComment.length()>2)
						checkinComment=checkinComment.substring(0,checkinComment.length()-2);
				}
				
				master2.write(cfVersion +"\t" + realFileName +"\t" + checkinTime + "\t" + checkiUser  + "\t"+ checkinComment+ '\n');
			}
			
			
		}
	}
	
	protected List getFileWithVersionCommand(String filePath, String downFilePath, String revisionNo, File f) throws Exception {
		ArrayList command = new ArrayList();
		command.add("GetSourceView");
		command.add(filePath);
		command.add(downFilePath);
		command.add(revisionNo);
		return (List)command;
    }

	protected List getMaxRevisionNoCommand(String relativeFilePath) throws Exception {
		
		ArrayList command = new ArrayList();
		command.add("GetRevisionInfo");
		command.add(relativeFilePath);
		return (List)command;
    }
	
	protected int parseMaxRevision(String result)
	{
		int startPos = result.indexOf("@@@dRevCount=") + 13;
		String maxRevision = result.substring(startPos , result.indexOf("\n", startPos));
		System.out.println("parseMaxRevision() " + maxRevision);
		return Integer.parseInt(maxRevision.trim());
	}
	
	protected String parseCheckInTime(String result)
	{
		result = result.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
		System.out.println("parseCheckInTime() " + result);
		return result; 
	}
	
	protected HashMap parseRevisionInfo(String result)
	{
		HashMap inHash = new HashMap();
		try{
			String[] versionInfo = result.split("\n");
			int size = versionInfo.length;
			boolean isRevision = false;
			for(int i=0; i<size; i++)
			{
				if(versionInfo[i].startsWith("@@@CHECK_YN@@@")){
					isRevision=true;
					continue;
				}
				
				if(isRevision){
					String[] st = versionInfo[i].split("@");
					String version = st[6];
					String checkindt = st[9];
					String comment = st[12];
					if(comment==null)
						st[12]="";
					
					inHash.put(version, st);
					
					System.out.println("version : " + version+" , checkindt : " + checkindt +", comment : " + comment);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return inHash;
	}
	
	
	
}