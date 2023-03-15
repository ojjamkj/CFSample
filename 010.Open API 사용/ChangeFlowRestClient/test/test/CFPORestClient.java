package test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONObject;

import com.gtone.cfclient.rest.RestClient;


public class CFPORestClient extends RestClient
{
  public CFPORestClient(String paramString, boolean useSSL, String userAuthToken) throws IOException
  {
    super(paramString, useSSL, userAuthToken);
  }

  public JSONObject checkout(HashMap paramMap) throws IOException
  {
	  JSONObject resultObject = null;
	  try
	  {
		  resultObject = getJsonByJson("/rest/po/v1/checkout", paramMap);
		  
	  }
	  catch (IOException localIOException)
	  {
		  throw localIOException;
	  }
	  return resultObject;
  }  
  
  
  public JSONObject checkin(HashMap paramMap) throws IOException
  {
	  JSONObject resultObject = null;
	  try
	  {
		  resultObject = getJsonByJson("/rest/po/v1/checkin", paramMap);
		  
	  }
	  catch (IOException localIOException)
	  {
		  throw localIOException;
	  }
	  return resultObject;
  }  
 
  
  public static void main(String[] args) {
	  try{
		  //오류발생시
		  //{"code":"500-00","error":"InternalError","description":"등록된 사용자가 아닙니다."}
		  
		  
		  
		  
		  if("checkout".equals(args[0])){
			//make parameter
			  HashMap requestObject = new HashMap();
			  ArrayList fileArray = new ArrayList();
			  
			  requestObject.put("CHECKOUT_USER_ID", "cmadmin");
			  requestObject.put("GROUP_ID", "E_EVL");
			  requestObject.put("TYPE_ID", "DATA_OBJECT");
			  requestObject.put("SERVICE", "TZC301_I001");
			  requestObject.put("FILE_PATH", "kodit/ob/online/z_comn/zc_comngenr/zcc_pcoff/dao");
			  requestObject.put("REVISION", "52");
			  requestObject.put("CHECKOUT_LIST", fileArray);
			  
			  
			  HashMap fileObject1 = new HashMap();
			  fileArray.add(fileObject1);
			  fileObject1.put("FILE", "/metas/PFM_DBIO/kodit/ob/online/z_comn/zc_comngenr/zcc_pcoff/dao/TZC301_I001.xml");
			  
			  
			  HashMap fileObject2 = new HashMap();
			  fileArray.add(fileObject2);
			  fileObject2.put("FILE", "/srcs/PFM_DBIO/kodit/ob/online/z_comn/zc_comngenr/zcc_pcoff/dao/TZC301_I001.java");
			  
			  
			  //call changeflow
			  String userAuthToken = "ZXlKMGVYQWlPaUpLVjFRaUxDSnBjM04xWlVSaGRHVWlPaUl5TURFNE1ESXdPREUwTkRVMU15SXNJbVY0Y0dseVpXUkVZWFJsSWpvaWJtVjJaWElpTENKaGJHY2lPaUpJVXpVeE1pSjkuZXlKemRXSWlPaUo3WENKVlUwVlNYMGxFWENJNlhDSXdYQ0lzWENKTVQwZEpUbDlKUkZ3aU9sd2lZMjFoWkcxcGJsd2lmU0o5LkNQMEg2SnAzZGhGSHVIZEVpTjkxS0NrUHEwSlR2LUtjY3k1T1BlZmduQkhmTEVwRkhPbzBEQmc5WmxVRHpIOUdCYUh2ejRVZV9ZZ2dzaXpJc3RzN0FB";
			  CFPORestClient client = new CFPORestClient("http://localhost:8092", false, userAuthToken);
			  
			  JSONObject resultObject = client.checkout(requestObject);
			  System.out.println(resultObject);		  
			  System.out.println("체크아웃여부 :" + resultObject.get("CAN_CHECKOUT"));
			  System.out.println("문서번호 :" + resultObject.get("INST_ID"));
			  
		  }else if("checkin".equals(args[0])){
			//make parameter
			  HashMap requestObject = new HashMap();
			  ArrayList fileArray = new ArrayList();
			  
			  requestObject.put("CHECKIN_USER_ID", "cmadmin");
			  requestObject.put("GROUP_ID", "E_EVL");
			  requestObject.put("TYPE_ID", "DATA_OBJECT");
			  requestObject.put("SERVICE", "TZC301_I001");
			  requestObject.put("FILE_PATH", "kodit/ob/online/z_comn/zc_comngenr/zcc_pcoff/dao");
			  requestObject.put("INST_ID", "617");
			  requestObject.put("COMMENT", "주석입니다.");
			  requestObject.put("REVISION", "52");
			  requestObject.put("CHECKIN_LIST", fileArray);
			  
			  
			  HashMap fileObject1 = new HashMap();
			  fileArray.add(fileObject1);
			  fileObject1.put("FILE", "/metas/PFM_DBIO/kodit/ob/online/z_comn/zc_comngenr/zcc_pcoff/dao/TZC301_I001.xml");
			  
			  
			  HashMap fileObject2 = new HashMap();
			  fileArray.add(fileObject2);
			  fileObject2.put("FILE", "/srcs/PFM_DBIO/kodit/ob/online/z_comn/zc_comngenr/zcc_pcoff/dao/TZC301_I001.java");
			  
			  
			  //call changeflow
			  String userAuthToken = "ZXlKMGVYQWlPaUpLVjFRaUxDSnBjM04xWlVSaGRHVWlPaUl5TURFNE1ESXdPREUwTkRVMU15SXNJbVY0Y0dseVpXUkVZWFJsSWpvaWJtVjJaWElpTENKaGJHY2lPaUpJVXpVeE1pSjkuZXlKemRXSWlPaUo3WENKVlUwVlNYMGxFWENJNlhDSXdYQ0lzWENKTVQwZEpUbDlKUkZ3aU9sd2lZMjFoWkcxcGJsd2lmU0o5LkNQMEg2SnAzZGhGSHVIZEVpTjkxS0NrUHEwSlR2LUtjY3k1T1BlZmduQkhmTEVwRkhPbzBEQmc5WmxVRHpIOUdCYUh2ejRVZV9ZZ2dzaXpJc3RzN0FB";
			  CFPORestClient client = new CFPORestClient("http://localhost:8092", false, userAuthToken);
			  
			  JSONObject resultObject = client.checkin(requestObject);
			  System.out.println(resultObject);		  

		  }
		  
		  
	  }catch(Exception e){
		  e.printStackTrace();
	  }
  }
   
}
