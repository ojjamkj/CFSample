package test.biglook;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * 
 * 
 * 안녕하세요. 
정보라인 지준석 입니다.

금주 미팅 후 요청 내용 정리하여 보내드립니다.
1.	Request 명세
1-1. 요청 url : [biglook SERVER url]/linkage/event_pps
           ex) https://[ipaddr]/biglook/linkage/event_pps
           1-2. 요청 parameter [ srCode, projectCode ] - "utf-8" 인코딩
return value       
"ERROR : srCode. can not found."             - parameter srCode 없음
"ERROR : projectCode. can not found."      - parameter projectCode 없음
"ERROR : not found source file [srCode]"    - 분석 대상 파일 찾을 수 없음
"ERROR : file extract fail!!! [srCode]"          - 압축파일 해제 중 에러 발생
"ERROR : analysis error [srCode]"             - 분석 실패
"SUCCESS : [srCode]"                            - 정상 완료

2.	Reponse 명세
srCode : 시리얼 코드
ApprovalYn : biglook 정책(보안약점 승인 기준) 통과여부[ Y / N ]
WeaknessCritical : 보안약점 심각 건수 
WeaknessMajor : 보안약점 높음 건수 
WeaknessMinor : 보안약점 중간 건수 
WeaknessInfo : 보안약점 정보 건수 
resultUrl : 결과화면 소스취약점 분석결과를 html 형태로 전송
- https://[ipaddr]/biglook/project/projectWeaknessRevision/projectWeaknessRevisionDialog/프로젝트아이디/srCode
ex) "sr123|Y|0|1|0|5|https://[ipaddr]/biglook/project/projectWeaknessRevision/projectWeaknessRevisionDialog/50/sr123"

3.	분석 대상 파일 사용 후 삭제 가능 여부
보내주신 경로의 분석 파일들을 저희쪽으로 이동하는 과정에서
삭제할 예정입니다.

4.	TLSv1.2 인증서 관련 (파일 첨부)
<< 파일설명 >>
HttpTest.java
jUnit  테스트 파일 입니다.
FileLocker.java
다중 프로세스에서 동작하는 상황을 고려한 파일잠금 유틸리티 입니다.
InstallCert.java
서버 인증서를 로컬 키저장소에 저장하는 유틸리티 입니다.
X509TrustManagerImpl.java
신뢰된 인증서 관리자 인터페이스 구현체 입니다.

5.	비동기 / 동기 방식.
해당 건은 비동기로 진행하는 것이 추후 운영에서 문제가 없을 것으로 보입니다.
           미팅 시 동기 방식으로 말씀드렸으나 비동기로 진행하는 것이 옳다고 판단됩니다.

6.	미팅에서 말씀드린 내용 중 SR넘버가 다중 프로젝트를 가질 수 있는지 질문드립니다.
아마도 그럴 경우가 없을 것으로 말씀하신 것으로 기억이 납니다.
다시 한번 확인 부탁드립니다.

문의사항 있으시면 회신 또는 연락바랍니다.
감사합니다.

 */


public class BigLookCall {
	
	public static void call(HashMap inHash) throws Exception
	{
		try{
			URL url = new URL("https://192.168.1.151/biglook");
			
			HttpsURLConnection urlConn = null;
			urlConn = (HttpsURLConnection)url.openConnection();
			urlConn.setUseCaches(false);
			urlConn.setInstanceFollowRedirects(true);
			
			urlConn.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sslsession) {
					// TODO Auto-generated method stub
					return false;
				}  			     
			   });   
//			urlConn.setHostnameVerifier((hostname, session) -> true);
			urlConn.setSSLSocketFactory(createSSLSocketFactory(url));
			
			urlConn.setRequestMethod("POST");
			
			String param = "srCode="+inHash.get("INST_ID")+"&projectCode="+inHash.get("BIGLOOK_PRJ_CD");
			
			urlConn.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
			dos.writeBytes(param);
			dos.flush();
			dos.close();
			
//			urlConn.connect();
			
			if (urlConn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				String inputLine=null;
				String allInputLine = null;
				while((inputLine=br.readLine()) != null){
					allInputLine += inputLine.trim();							
				}
				
				if(allInputLine.indexOf("ERROR")>-1){
					throw new Exception(allInputLine);
				}
					
					
					/**
					 * return value       
"ERROR : srCode. can not found."             - parameter srCode 없음
"ERROR : projectCode. can not found."      - parameter projectCode 없음
"ERROR : not found source file [srCode]"    - 분석 대상 파일 찾을 수 없음
"ERROR : file extract fail!!! [srCode]"          - 압축파일 해제 중 에러 발생
"ERROR : analysis error [srCode]"             - 분석 실패
"SUCCESS : [srCode]"                            - 정상 완료

					 */
			}else{
				throw new Exception("biglook call error");
			}
		}catch(Exception e){
			throw(e);
		}
	}
	
	static SSLSocketFactory createSSLSocketFactory(URL url) throws Exception {
		InstallCert installCert = new InstallCert(url);
		installCert.install();
		
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(null, new TrustManager[] { new X509TrustManagerImpl(installCert.jssecacerts) }, null);
		return context.getSocketFactory();
	}
}
