package com.gtone.cfclient.rest;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class CFClient {
	
	String SERVER_FULL_PATH = null;
	String SERVER_IP_DOMAIN = null;
	String SERVER_PORT = null;
	boolean SERVER_USE_SSL = false;
	String API_TOKEN = null;
	String API_URL_BASE = null;
	String API_URL_DOCLIST = null;
	String API_URL_REMOTECHEKIN = null;
	int OPTION_REMOTECHEKIN_SEND_SIZE;
	int OPTION_REMOTECHEKIN_SEND_SIZE_DEFAULT = 10485760; // 10 * 1024 * 1024 BYTE
	int OPTION_REMOTECHEKIN_SEND_COUNT;
	int OPTION_REMOTECHEKIN_SEND_COUNT_DEFAULT = 500;
	
	public enum CALL_TYPE {
		GET_DOC_LIST, REMOTE_COMMIT;
	}
	
	RestClient CLIENT = null;
	
	public CFClient(String propertyFilePath) {
		
		Properties dbProp = readProperties(propertyFilePath);
		this.SERVER_IP_DOMAIN = dbProp.getProperty("cf.server.ipdomain");
		this.SERVER_PORT = dbProp.getProperty("cf.server.port");
		
		if ("Y".equals(dbProp.get("cf.server.usessl"))) {
			this.SERVER_USE_SSL = true;
			this.SERVER_FULL_PATH = "https://" + this.SERVER_IP_DOMAIN + ":" + this.SERVER_PORT;
		} else {
			this.SERVER_USE_SSL = false;
			this.SERVER_FULL_PATH = "http://" + this.SERVER_IP_DOMAIN + ":" + this.SERVER_PORT;
		}
		
		this.API_TOKEN = dbProp.getProperty("cf.server.token");
		this.API_URL_BASE = dbProp.getProperty("cf.server.api.url.base");
		this.API_URL_DOCLIST = dbProp.getProperty("cf.server.api.url.doclist");
		this.API_URL_REMOTECHEKIN = dbProp.getProperty("cf.server.api.url.remotecheckin");
		
		try {
			String optionCheckInSizeAtOnce = dbProp.getProperty("cf.option.remotecheckin.send.size");
			if(optionCheckInSizeAtOnce != null && !"".equals(optionCheckInSizeAtOnce) && !"0".equals(optionCheckInSizeAtOnce)) {
				this.OPTION_REMOTECHEKIN_SEND_SIZE = Integer.valueOf(optionCheckInSizeAtOnce);
			} else {
				this.OPTION_REMOTECHEKIN_SEND_SIZE = OPTION_REMOTECHEKIN_SEND_SIZE_DEFAULT;
			}
		} catch(Exception e) {
			this.OPTION_REMOTECHEKIN_SEND_SIZE = OPTION_REMOTECHEKIN_SEND_SIZE_DEFAULT;
		}
		
		try {
			String optionCheckInCountAtOnce = dbProp.getProperty("cf.option.remotecheckin.send.count");
			if(optionCheckInCountAtOnce != null && !"".equals(optionCheckInCountAtOnce) && !"0".equals(optionCheckInCountAtOnce)) {
				this.OPTION_REMOTECHEKIN_SEND_COUNT = Integer.valueOf(optionCheckInCountAtOnce);
			} else {
				this.OPTION_REMOTECHEKIN_SEND_COUNT = OPTION_REMOTECHEKIN_SEND_COUNT_DEFAULT;
			}
		} catch(Exception e) {
			this.OPTION_REMOTECHEKIN_SEND_COUNT = OPTION_REMOTECHEKIN_SEND_COUNT_DEFAULT;
		}
	}
	
	private Properties readProperties(String filePath) {
		InputStream is = null;
		Properties props = null;
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				System.err.println("File not found : " + file.getAbsolutePath());
				return null;
			} 
			is = new FileInputStream(file);
			props = new Properties();
			props.load(is);
			close(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(is);
		} 
		return props;
	}
	
	private void close(InputStream is) {
		try {
			if (is != null) {
				is.close(); 
			}
		} catch (Exception exception) {
		
		} finally {
			is = null;
		} 
	}
	
	public HashMap run(HashMap paramMap) throws Exception {
		HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
		CALL_TYPE API_CALL_TYPE = (CALL_TYPE)paramMap.get("CALL_TYPE");
		try {
			
			if (CALL_TYPE.GET_DOC_LIST.equals(API_CALL_TYPE)) {
				this.SERVER_FULL_PATH = String.valueOf(this.SERVER_FULL_PATH) + this.API_URL_BASE + this.API_URL_DOCLIST;
				returnMap = this.send(paramMap, returnMap);
			} else if (CALL_TYPE.REMOTE_COMMIT.equals(API_CALL_TYPE)) {
				long checkInStartTime = System.currentTimeMillis();
				int totalFileCount = 0;
				int totalSendCount = 0;
				
				this.SERVER_FULL_PATH = String.valueOf(this.SERVER_FULL_PATH) + this.API_URL_BASE + this.API_URL_REMOTECHEKIN;
				String rootFilePath = (String)paramMap.get("ROOT_FILE_PATH");
				ArrayList<File> allFileList = new ArrayList<File>();
				boolean isDirectory = getFileList(allFileList, rootFilePath);
				totalFileCount = allFileList.size();
				
				if (totalFileCount == 0) {
					returnMap.put("RESULT", "FAIL");
					returnMap.put("ERRMSG", "체크인 할 대상 파일이 없습니다.");
					return returnMap;
				}
				
				ArrayList<File> sendFileList = new ArrayList<File>();
				int sendSize = 0;
				for(int i = 0; i < totalFileCount; i++) {
					File aFile = allFileList.get(i);
					sendFileList.add(aFile);
					sendSize += aFile.length();
					if(sendSize > this.OPTION_REMOTECHEKIN_SEND_SIZE || i == (totalFileCount - 1) || sendFileList.size() >= this.OPTION_REMOTECHEKIN_SEND_COUNT) {
						long sendStartTime = System.currentTimeMillis();
						genParamForRemoteCheckIn(paramMap, isDirectory, sendFileList, sendFileList.size());
						System.out.println(">> Sending  " + sendFileList.size() + " Files for Remote-CheckIn");
						returnMap = this.sendWithFiles(paramMap, returnMap);
						if("SUCCESS".equals(returnMap.get("RESULT"))) {
							long sendEndTime = System.currentTimeMillis();
							totalSendCount += sendFileList.size();
							sendSize = 0;
							sendFileList = new ArrayList<File>();
							System.out.println(">> CheckIn Successful in " + ((sendEndTime - sendStartTime) / 1000) + " Seconds (" + totalSendCount + "/" + totalFileCount + ") Files");
						} else {
							System.out.println("!!!! CheckIn Failed !!!! ");
							throw new Exception(returnMap.get("ERRMSG").toString());
						}
					}
				}
				
				long checkInEndTime = System.currentTimeMillis();
				System.out.println("[CHECKIN COMPLETED] " + ((checkInEndTime - checkInStartTime) / 1000) + " Seconds (" + totalSendCount + "/" + totalFileCount + ") Files");
			}
			
			return returnMap;
		} catch (Exception e) {
			returnMap.put("RESULT", "FAIL");
			returnMap.put("ERRMSG", e.getMessage());
			e.printStackTrace();
			return returnMap;
		} 
	}
	
	private HashMap send(HashMap<String, ?> paramMap, HashMap returnMap) throws Exception {
		RestClient client = new RestClient(this.SERVER_FULL_PATH, this.SERVER_USE_SSL, this.API_TOKEN);
		String returnStr = client.getTextByJson("", paramMap);
		Gson gson = new Gson();
		returnMap = (HashMap)gson.fromJson(returnStr, HashMap.class);
		if (!"SUCCESS".equals(returnMap.get("RESULT"))) {
			throw new Exception(returnMap.get("ERRMSG").toString()); 
		}
		return returnMap;
	}
	
	private HashMap sendWithFiles(HashMap<String, ?> paramMap, HashMap returnMap) throws Exception {
		RestClient client = new RestClient(this.SERVER_FULL_PATH, this.SERVER_USE_SSL, this.API_TOKEN);
		File[] files = (File[]) paramMap.get("FILES");
		paramMap.remove("FILES");
		String returnStr = client.getTextByMultipart("", paramMap, files);
		Gson gson = new Gson();
		returnMap = (HashMap)gson.fromJson(returnStr, HashMap.class);
		if (!"SUCCESS".equals(returnMap.get("RESULT"))) {
			throw new Exception(returnMap.get("ERRMSG").toString()); 
		}
		return returnMap;
	}
	
	private HashMap genParamForRemoteCheckIn(HashMap paramMap, boolean isDirectory, ArrayList<File> fileList, int fileListSize) throws Exception {
		String rootResPath = (String)paramMap.get("ROOT_RES_PATH");
		String rootFilePath = (String)paramMap.get("ROOT_FILE_PATH");
		if ("".equals(rootResPath)) {
			rootResPath = "/";
		} else if ('/' != rootResPath.charAt(rootResPath.length() - 1)) {
			rootResPath = String.valueOf(rootResPath) + "/";
		} 
		String[] fileResPaths = new String[fileListSize];
		String[] fileFullPaths = new String[fileListSize];
		String[] fileNames = new String[fileListSize];
		File[] files = new File[fileListSize];
		SimpleDateFormat sf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
		for (int i = 0; i < fileListSize; i++) {
			File aFile = fileList.get(i);
			if (aFile.isFile()) {
				files[i] = aFile;
				fileNames[i] = aFile.getName();
				String fileResPath = aFile.getCanonicalPath().replaceAll("\\\\", "/").replaceAll("//", "/");
				if (isDirectory) {
					fileResPath = fileResPath.replaceFirst(rootFilePath, "");
				} else {
					fileResPath = rootResPath;
				} 
				fileResPath = fileResPath.substring(0, fileResPath.lastIndexOf("/") + 1);
				String fileFullPath = "";
				if (isDirectory) {
					fileFullPath = String.valueOf(rootResPath) + fileResPath;
					fileResPath = String.valueOf(rootResPath) + fileResPath;
				} else {
					fileFullPath = aFile.getCanonicalPath();
				} 
				fileResPaths[i] = fileResPath.replace("//", "/");
				fileFullPaths[i] = fileFullPath.replace("//", "/");
			} 
			System.out.println(String.valueOf(sf.format(new Date())) + " CheckIn From [" + aFile.getCanonicalPath() + "] To <" + fileResPaths[i] + fileNames[i] + ">");
		} 
		paramMap.put("FILE_RES_PATHS", fileResPaths);
		paramMap.put("FILE_FULL_PATHS", fileFullPaths);
		paramMap.put("FILES", files);
		paramMap.put("FILE_NAMES", fileNames);
		paramMap.put("FILE_COUNT", Integer.valueOf(fileNames.length));
		return paramMap;
	}
	
	private boolean getFileList(ArrayList<File> allFileList, String rootFilePath) {
		File root = new File(rootFilePath);
		if (root.isDirectory()) {
			File[] fileList = root.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					allFileList.add(fileList[i]);
				} else if (fileList[i].isDirectory()) {
					getFileList(allFileList, fileList[i].getPath());
				} 
			} 
			return true;
		}
		
		if (root.isFile()) {
			allFileList.add(root);
		}
		
		return false;
	}
}
