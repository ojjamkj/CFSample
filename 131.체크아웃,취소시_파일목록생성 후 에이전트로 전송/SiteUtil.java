package com.gtone.cf.rt.resources.run;

import java.math.BigDecimal;
import java.util.HashMap;

import com.gtone.cf.daemon.cmd.BaseCommand;
import com.gtone.cf.rt.connect.ConnectorFactory;
import com.gtone.cf.rt.file.FileTransfer;

public class SiteUtil {
	
	public static void makeFileAndSend(DBAssistant dbAssistant, BigDecimal instId) throws Exception
	{
		
		HashMap copyMap = new HashMap();
		try {
			
			String copyString = "/a/b/c/A.java\\n/a/b/c/B.java\\n/a/b/c/C.java";
			String connId = "2";
			
			//연결정보를 직접 넣던가 아님 조회를 해서 hashmap을 받던가
			//연결정보 조회해서 사용하기
			//copyMap.put(ICFConstants.CONNECT_ID, connId);
			//copyMap = ConnectManager.getInstance().getConnectionInfoById(copyMap);
			//연결정보 직접 넣기
			copyMap.put("MACHINE_TYPE", "S");
			copyMap.put("CONNECT_TYPE", "A");
			copyMap.put("TARGET_IP", "127.0.0.1");
			copyMap.put("TARGET_PORT", "30502");
			
			//복사할 내용
			copyMap.put("FILE_SOURCE", copyString.getBytes());
			//생성파일 위치
			copyMap.put("TARGET_FILE", "/copy_loc/"+ instId+".txt");
			//파일생성
			BaseCommand resultCmd = FileTransfer.getInstance().createFile(copyMap, copyMap);
			
			boolean copyResult = (resultCmd == null || !resultCmd.isSuccess()) ? false :  true;
			
			if(copyResult) {
				throw new Exception("파일생성 실패");
			}
		}catch(Exception e) {
			throw(e);
		}finally {
			ConnectorFactory.getInstance().allConnectorClose(copyMap);
		}
	}
}
