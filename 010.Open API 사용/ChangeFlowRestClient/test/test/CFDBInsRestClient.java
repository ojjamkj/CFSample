package test;


import java.util.HashMap;

import net.sf.json.JSONObject;

import com.gtone.cfclient.rest.RestClient;


public class CFDBInsRestClient 
{
  
  public static void main(String[] args) {
	  try{
		  //오류발생시
		  //{"code":"500-00","error":"InternalError","description":"등록된 사용자가 아닙니다."}
		  
		  
		  
		  
		  if("테스트계 배포요청 목록 조회".equals(args[0])){ //테스트계 배포요청 목록 조회
			//make parameter
			  HashMap requestObject = new HashMap();
			  HashMap paramObject = new HashMap();
			  
			  paramObject.put("LOGIN_ID", "0104551"); //로그인 id
			  paramObject.put("CHECKIN_DT_FROM", "20181102"); //체크인 일자 from
			  paramObject.put("CHECKIN_DT_TO", "20181103"); //체크인 일자 to
			  
			  
			  //call changeflow by auth
			  String userAuthToken = "ZXlKMGVYQWlPaUpLVjFRaUxDSnBjM04xWlVSaGRHVWlPaUl5TURFNE1ESXdPREUwTkRVMU15SXNJbVY0Y0dseVpXUkVZWFJsSWpvaWJtVjJaWElpTENKaGJHY2lPaUpJVXpVeE1pSjkuZXlKemRXSWlPaUo3WENKVlUwVlNYMGxFWENJNlhDSXdYQ0lzWENKTVQwZEpUbDlKUkZ3aU9sd2lZMjFoWkcxcGJsd2lmU0o5LkNQMEg2SnAzZGhGSHVIZEVpTjkxS0NrUHEwSlR2LUtjY3k1T1BlZmduQkhmTEVwRkhPbzBEQmc5WmxVRHpIOUdCYUh2ejRVZV9ZZ2dzaXpJc3RzN0FB";
			  RestClient client = new RestClient("http://localhost:8092", false, userAuthToken);
			  
			  JSONObject resultObject = client.getJsonByJson("/rest/dbins/staging/apply/resources", requestObject);
			  System.out.println("리턴 : " + resultObject);		  
			  
		  }
		  
		  
	  }catch(Exception e){
		  e.printStackTrace();
	  }
  }
   
}

