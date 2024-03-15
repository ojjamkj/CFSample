package checksum;

import java.security.MessageDigest;
import java.util.HashMap;

import com.gtone.cf.rt.resources.run.ResourceRunner;

public class MD5CheckSumTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			/*
			SELECT A.REP_TYPE, A.COLLECT_ID, A.USE_COM_CRYPT,
			   B.SRC_ID, B.SPATH AS RES_PATH, B.SNAME AS RES_NAME, B.SPATH AS ORG_RES_PATH, B.CHANGE_REASON_CODE,
			   C.VERSION_ID, C.DEV_VERSION_ID 
			FROM 
			CF_COLLECT_INFO A, CF_SRC B, CF_RESOURCE_CR_MAP C  
			WHERE A.COLLECT_ID =B.COLLECT_ID AND B.SRC_ID =C.RES_ID AND C.INST_ID =1
			*/
		
			//특정 버전 파일의 체크썸을 구하는 샘플
			HashMap sourceMap = new HashMap();
			sourceMap.put("REP_TYPE","");
			sourceMap.put("VERSION_ID","");
			sourceMap.put("DEV_VERSION_ID","");
			sourceMap.put("USE_COM_CRYPT","");
			sourceMap.put("COLLECT_ID","");
			sourceMap.put("RES_PATH","");
			sourceMap.put("RES_NAME","");
			sourceMap.put("ORG_RES_PATH","");
			sourceMap.put("CHANGE_REASON_CODE","");
			sourceMap.put("SRC_ID","");
			
			
			byte[] sourceByte = ResourceRunner.getInstance().getViewSourceRun().getSource(sourceMap);
			String checkSum = md5(sourceByte);
		}catch(Exception e) {
			
		}
	}
	
	
	public static String md5(byte[] source) throws Exception
	{
		
		  String checksumValue = "";
		  try
		  {
		      MessageDigest algorithm = MessageDigest.getInstance("MD5");
		      algorithm.reset();
		      algorithm.update(source);
		      byte messageDigest[] = algorithm.digest();
		      checksumValue = convertToHex(messageDigest);
		  }
		  catch(Exception e)
		  {
		      throw e;
		  }
		  return checksumValue;
	}
	
	public static String convertToHex(byte[] messageDigest)
	{
		StringBuffer hexString = new StringBuffer();
	    for(int i = 0; i < messageDigest.length; i++)
	    {
	         String hexCode = Integer.toHexString(0xff & messageDigest[i]);
	         if(hexCode.length() > 1)
	             hexString.append(hexCode);
	         else
	             hexString.append("0" + hexCode);
	    }
	    
	    return hexString.toString().toUpperCase();
	}

}
