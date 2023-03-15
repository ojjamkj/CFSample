package com.gtone.cfclient.main;

import com.gtone.cfclient.rest.CFClient;
import java.util.HashMap;

public class NHCFClient {
	public static void main(String[] args) throws Exception {
		HashMap<Object, Object> resultMap = null;
		if (args != null && args.length > 0 && args[1] != null) {
			if (args[1].equals("getDocList")) {
				resultMap = getDocList(args);
				System.out.println(resultMap.toString());
			} else if (args[1].equals("remoteCheckIn")) {
				resultMap = remoteCheckIn(args);
				System.out.println(resultMap.toString());
				if(!"SUCCESS".equals(resultMap.get("RESULT"))) {
					System.exit(1);
				} else {
					System.exit(0);
				}
			} 
		} else {
			resultMap = new HashMap<Object, Object>();
			resultMap.put("RESULT", "FAIL");
			resultMap.put("ERRMSG", "No Parameter");
			System.out.println(resultMap.toString());
		} 
	}
	
	private static HashMap getDocList(String[] args) throws Exception {
		HashMap<Object, Object> paramMap = new HashMap<Object, Object>();
		paramMap.put("CONFIG_FILE_PATH", args[0]);
		paramMap.put("LOGIN_ID", args[2]);
		paramMap.put("STEP_TYPE", "");
		paramMap.put("BIZ_CD", "");
		if (args.length == 3) {
			paramMap.put("LOGIN_ID", args[2]);
		} else if (args.length == 5) {
			paramMap.put("LOGIN_ID", args[2]);
			paramMap.put(args[3], args[4]);
		} else if (args.length == 7) {
			paramMap.put("LOGIN_ID", args[2]);
			paramMap.put(args[3], args[4]);
			paramMap.put(args[5], args[6]);
		} 
		return getDocList(paramMap);
	}
	
	public static HashMap getDocList(HashMap paramMap) throws Exception {
		paramMap.put("CALL_TYPE", CFClient.CALL_TYPE.GET_DOC_LIST);
		CFClient cfClient = new CFClient((String)paramMap.get("CONFIG_FILE_PATH"));
		return cfClient.run(paramMap);
	}
	
	private static HashMap remoteCheckIn(String[] args) throws Exception {
		try {
			HashMap<Object, Object> paramMap = new HashMap<Object, Object>();
			paramMap.put("CONFIG_FILE_PATH", args[0]);
			paramMap.put("LOGIN_ID", args[2]);
			paramMap.put("INST_ID", args[3]);
			paramMap.put("COLLECT_NAME", args[4]);
			String rootResPath = args[5];
			if ("/".equals(rootResPath)) {
				rootResPath = ""; 
			}
			rootResPath.replaceAll("//", "/");
			rootResPath.replaceAll("\\\\", "/");
			paramMap.put("ROOT_RES_PATH", rootResPath);
			String rootFilePath = args[6];
			rootFilePath.replaceAll("//", "/");
			rootFilePath.replaceAll("\\\\", "/");
			paramMap.put("ROOT_FILE_PATH", rootFilePath);
			return remoteCheckIn(paramMap);
		} catch (Exception e) {
			HashMap returnMap = new HashMap();
			returnMap.put("RESULT", "FAIL");
			returnMap.put("ERRMSG", e.getMessage());
			return returnMap;
		} 
	}
	
	public static HashMap remoteCheckIn(HashMap paramMap) throws Exception {
		paramMap.put("CALL_TYPE", CFClient.CALL_TYPE.REMOTE_COMMIT);
		CFClient cfClient = new CFClient((String)paramMap.get("CONFIG_FILE_PATH"));
		return cfClient.run(paramMap);
	}
}
