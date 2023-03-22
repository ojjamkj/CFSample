package site.script;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.gtone.cf.rt.deploy.IDeployHook;
import com.gtone.cf.util.CodeServiceUtil;
import com.gtone.cf.util.ExceptionHandler;
import com.gtone.cf.util.ICFConstants;

import jspeed.base.dao.CommonDAO;
import jspeed.base.log.LogService;
import jspeed.base.log.Logger;
import jspeed.base.util.StringHelper;

/**
 * 
 * @author Gtone LHM
 * @since 2023-03-22
 * @version 작업중...
 * @see 애큐온저축은행 BXM 연계 중 .dat 파일을 배포하면서 파일 내 내용을 파라미터로 하여 원격지의 서비스를 호출하는 스크립트 클래스
 *      사이트 가져가서 BXM이랑 맞춰봐야 됨
 *
 */

public class BXMTRXDeploy implements IDeployHook {
	
	private Logger logger = LogService.getInstance().getLogger();
	
	private static final String ENC_LANG = "UTF-8";
	private static final String LOG_LEVEL = "INFO";
	
	public HashMap executeHook(CommonDAO commonDao, HashMap inHash) throws Exception {
		
		String resultString = "";
		HashMap resultHash = new HashMap();
		String resultCode = "S";
		
		try {
			String url = "";
			String system = "";
			String sysType = "";
			
			try {
				system = ((String[]) inHash.get("BUILD_PARAM"))[0];
				sysType = (String) inHash.get("SYS_TYPE");
			} catch(Exception e) {
				throw new Exception("BXTRX 연동 스크립트 파라미터가 부적합합니다.");
			}
			
			url = CodeServiceUtil.getCodesNames("SITE_BXM_OPTIONS", "TRX_URL_" + system + "_" + sysType);
			if (url == null || "".equals(url)) {
				throw new Exception("BXMTRX 연동 가능한 URL이 없습니다.");
			}
			
			logger.println(LOG_LEVEL, "BXMTRX_Param > system : " + system + " / sysType : " + sysType + " / url : " + url);
			
			String[] urlArr = url.split(",");
			int urlArrLength = urlArr.length;
			
			String resListParam = system = ((String[])inHash.get("BUILD_PARAM"))[1];
			String[] resListArr = resListParam.split(",");
			int resListArrLength = resListArr.length;
			
			for (int i = 0; i < resListArrLength; i++) {
				String fileFullPath = "";
				fileFullPath = (String) inHash.get("TARGET_PATH");
				fileFullPath = fileFullPath + "/" + resListArr[i];
				fileFullPath.replaceAll("//", "/");
				
				logger.println(LOG_LEVEL, "BXMTRX_Deploy_Param > fileFullPath :" + fileFullPath);
				
				File file = new File(fileFullPath);
				
				byte[] fileSource = Files.readAllBytes(file.toPath());
				
				byte[] resBytes = null;
				
				String str = new String(fileSource, ENC_LANG);
				logger.println(LOG_LEVEL, "<BXMTRXDeploy> requestString : " + str);
				
				for (int j = 0; j < urlArrLength; j++) {
					resBytes = sendRestPost(urlArr[j], str, byte[].class);
					
					resultString = new String(resBytes, ENC_LANG);
					logger.println(LOG_LEVEL, "<BXMTRXDeploy> resultString : " + resultString);
					
					JSONParser parser = new JSONParser();
					JSONObject jsonObj = (JSONObject) parser.parse(resultString);
					
					if (jsonObj.get("ResponseCode") == null) {
						throw new Exception("1000");
					} else {
						JSONObject responseCode = (JSONObject) jsonObj.get("ResponseCode");
						
						Object codeObj = responseCode.get("code");
						
						if(codeObj == null) {
							throw new Exception("1000");
						} else if (codeObj instanceof Long) {
							if(((Long)codeObj).intValue() != 200 && ((Long)codeObj).intValue() != 202) {
								throw new Exception("1000");
							}
						} else if (codeObj instanceof Integer) {
							if((Integer)codeObj != 200 && (Integer)codeObj != 202) {
								throw new Exception("1000");
							}
						} else if (codeObj instanceof String) {
							if(!"200".equals(codeObj) && !"202".equals(codeObj)) {
								throw new Exception("1000");
							}
						}
					}
				}
			}
		} catch(HttpStatusCodeException e) {
			ExceptionHandler.handleException(e, logger);
			resultCode = "F";
			resultString = "HttpStatusCodeException : " + new String(e.getResponseBodyAsByteArray());
		} catch(Exception e) {
			ExceptionHandler.handleException(e, logger);
			resultCode = "F";
			
			if (!"1000".equals(e.getMessage())) {
				resultString = e.getMessage();
			}
		}
		
		resultHash.put("RESULT", resultCode);
		resultHash.put(ICFConstants.BUILD_RESULT_CONSOLE, resultString);
		
		return resultHash;
	}
	
	public boolean isTarget (CommonDAO commonDao, HashMap rscInfo) throws Exception {
		return true;
	}
	
	public static <T> T sendRestPost (String url, Object request, Class<T> responseType) throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName(ENC_LANG)));
		
		HttpEntity param = new HttpEntity(request, headers);
		
		URI uri = URI.create(url);
		
		return postForObject(uri, param, responseType);
	}
	
	public static <T> T postForObject(URI uri, Object request, Class<T> responseType) throws Exception {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		
		Integer timeout = Integer.parseInt(StringHelper.evl(CodeServiceUtil.findCode("SITE_OPTIONS", "RESTAPI_TIMEOUT"), "10"));
		
		factory.setConnectTimeout(timeout * 1000);
		factory.setReadTimeout(timeout * 1000);
		
		RestTemplate restTemplate = new RestTemplate(factory);
		
		T result = restTemplate.postForObject(uri, request, responseType);
		
		return result;
	}
}
