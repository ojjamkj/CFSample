package com.gtone.cfclient.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.gtone.cfclient.rest.util.SSLUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import sun.net.www.protocol.https.Handler;

public class RestClient {
	
	private String url;
	private boolean ssl;
	private String userAuthToken;
	
	public RestClient(String paramString, boolean paramBoolean, String userAuthToken) {
		this.url = paramString;
		this.ssl = paramBoolean;
		this.userAuthToken = userAuthToken;
	}
	
	public JSONObject getJsonByJson(String paramString, Map<String, ?> paramMap) throws IOException {
		return getJson("application/json", paramString , JSONObject.fromObject(paramMap).toString());
	}
	
	public String getTextByJson(String paramString, Map<String, ?> paramMap) throws IOException {
		return getText("application/json", paramString, JSONObject.fromObject(paramMap).toString());
	}
	
	public JSONObject getJsonByMultipart(String paramString, Map<String, ?> paramMap, File[] files) throws IOException {
		return getJson("application/x-www-form-urlencoded", paramString, JSONObject.fromObject(paramMap).toString(), files);
	}
	
	public String getTextByMultipart(String paramString, Map<String, ?> paramMap, File[] files) throws IOException {
		return getText("application/x-www-form-urlencoded", paramString, JSONObject.fromObject(paramMap).toString(), files);
	}
	
	public byte[] getByteByJson(String paramString, Map<String, ?> paramMap) throws IOException {
		return getHttpFileDownload("application/json", paramString , JSONObject.fromObject(paramMap).toString());
	}
	
	public JSONObject getJsonByText(String paramString) throws IOException {
		return getJson("application/text", paramString , null);
	}
	
	public JSONObject getJsonByText(String paramString1, String paramString2) throws IOException {
		return getJson("application/text", paramString1 + "?" + paramString2, null);
	}
	
	public JSONObject getJson(String paramString1, String paramString2, String paramString3) throws IOException {
		String str = getText(paramString1, paramString2, paramString3);
		if (StringUtils.isNotEmpty(str))
			try {
				return (JSONObject)JSONSerializer.toJSON(str);
			} catch (JSONException localJSONException) {
				throw localJSONException;
			}
		return null;
	}
	
	public JSONObject getJson(String paramString1, String paramString2, String paramString3, File[] files) throws IOException {
		String str = getText(paramString1, paramString2, paramString3, files);
		if (StringUtils.isNotEmpty(str))
			try {
				return (JSONObject)JSONSerializer.toJSON(str);
			} catch (JSONException localJSONException) {
				throw localJSONException;
			}
		return null;
	}
	
	public String getText(String paramString1, String paramString2, String paramString3) throws IOException {
		String str = null;
		if (this.url.startsWith("http://")) {
			str = getHttpContents(paramString1, paramString2, paramString3);
		} else if (this.url.startsWith("https://")) {
			str = getHttpsContents(paramString1, paramString2, paramString3);
		} else if (this.ssl) {
			str = getHttpsContents(paramString1, paramString2, paramString3);
		} else {
			str = getHttpContents(paramString1, paramString2, paramString3);
		}
		return str;
	}
	
	public String getText(String paramString1, String paramString2, String paramString3, File[] files) throws IOException {
		String str = null;
		if (this.url.startsWith("http://")) {
			str = getHttpContents(paramString1, paramString2, paramString3, files);
		} else if (this.url.startsWith("https://")) {
			str = getHttpsContents(paramString1, paramString2, paramString3); // TO-DO
		} else if (this.ssl) {
			str = getHttpsContents(paramString1, paramString2, paramString3); // TO-DO
		} else {
			str = getHttpContents(paramString1, paramString2, paramString3, files);
		}
		return str;
	}
	
	private byte[] getHttpFileDownload(String paramString1, String paramString2, String paramString3) throws IOException {
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		byte[] source = null;
		InputStream is = null;
		BufferedReader localBufferedReader = null;
		InputStreamReader localInputStreamReader = null;
		try {
			String str1 = null;
			if (this.url.startsWith("http://")) {
				str1 = this.url + paramString2;
				
			} else {
				str1 = "http://" + this.url + paramString2;
			}
			HttpPost localHttpPost = new HttpPost(str1);
			localHttpPost.addHeader("content-type", paramString1);
			localHttpPost.addHeader("X-CustomAuthToken", this.userAuthToken);
			if (StringUtils.isNotEmpty(paramString3)) {
				localHttpPost.setEntity(new StringEntity(paramString3, "UTF-8"));
			}
			HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpPost);
			
			Header header1 = localHttpResponse.getFirstHeader("Content-Type");
			if(header1.getValue().equals("application/octet-stream")) {
				Header header2 = localHttpResponse.getFirstHeader("Content-Length");
				
				int length = Integer.parseInt(header2.getValue());
				source = new byte[length];
				is = localHttpResponse.getEntity().getContent();  
				is.read(source);
			}else {
				StringBuffer localStringBuffer = new StringBuffer();
				
				localInputStreamReader = new InputStreamReader(localHttpResponse.getEntity().getContent(), "UTF-8");
				localBufferedReader = new BufferedReader(localInputStreamReader);
				String str2 = null;
				while ((str2 = localBufferedReader.readLine()) != null) {
					localStringBuffer.append(str2);
				}
				
				System.out.println("response text = " + localStringBuffer.toString());
				
				throw new IOException("couldn't get the source bytes");
			}
		}
		catch (HttpHostConnectException localHttpHostConnectException) {
			throw localHttpHostConnectException;
		} finally {
			if(is != null) is.close();
			localDefaultHttpClient.getConnectionManager().shutdown();
		}
		return source;
	}

	private String getHttpContents(String paramString1, String paramString2, String paramString3) throws IOException {
		StringBuffer localStringBuffer = new StringBuffer();
		BufferedReader localBufferedReader = null;
		InputStreamReader localInputStreamReader = null;
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		try
		{
			String str1 = null;
			if (this.url.startsWith("http://")) {
				str1 = this.url + paramString2;
			} else {
				str1 = "http://" + this.url + paramString2;
			}
			HttpPost localHttpPost = new HttpPost(str1);
			localHttpPost.addHeader("content-type", paramString1);
			localHttpPost.addHeader("X-CustomAuthToken", this.userAuthToken);
			if (StringUtils.isNotEmpty(paramString3)) {
				localHttpPost.setEntity(new StringEntity(paramString3, "UTF-8"));
			}
			HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpPost);
			localInputStreamReader = new InputStreamReader(localHttpResponse.getEntity().getContent(), "UTF-8");
			localBufferedReader = new BufferedReader(localInputStreamReader);
			String str2 = null;
			while ((str2 = localBufferedReader.readLine()) != null) {
				localStringBuffer.append(str2);
			}
		} catch (HttpHostConnectException localHttpHostConnectException) {
			throw localHttpHostConnectException;
		} finally {
			try {
				if (localBufferedReader != null) {
					localBufferedReader.close();
				}
			} catch (IOException localIOException3) {
				throw localIOException3;
			}
			
			try {
				if (localInputStreamReader != null) {
					localInputStreamReader.close();
				}
			} catch (IOException localIOException4) {
				throw localIOException4;
			}
			localDefaultHttpClient.getConnectionManager().shutdown();
		}
		return localStringBuffer.toString();
	}
	
	private String getHttpContents(String paramString1, String paramString2, String paramString3, File[] files) throws IOException {
		StringBuffer localStringBuffer = new StringBuffer();
		BufferedReader localBufferedReader = null;
		InputStreamReader localInputStreamReader = null;
		//DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(); //httpclient 4.0
		CloseableHttpClient localDefaultHttpClient = new DefaultHttpClient();
		try {
			String str1 = null;
			if (this.url.startsWith("http://")) {
				str1 = String.valueOf(this.url) + paramString2;
			} else {
				str1 = "http://" + this.url + paramString2;
			}
			
			HttpPost localHttpPost = new HttpPost(str1);
			// localHttpPost.addHeader("content-type", paramString1); // multipart일때는 content-type 을 설정하지 않아야 boundary 데이터가 생성됨
			localHttpPost.addHeader("X-CustomAuthToken", this.userAuthToken);
			
			if (files != null && files.length > 0) {
				/*
				//httpclient 4.0
					MultipartEntity multipartEntity = new MultipartEntity();
					multipartEntity.addPart("PARAMS", new StringBody(paramString3));
				for(int i = 0; i < files.length; i++) {
					FileBody fileBody = new FileBody(files[i], "FILE_"+(i+1));
					multipartEntity.addPart("FILE_"+(i+1), fileBody);
				}
				localHttpPost.setEntity((HttpEntity) multipartEntity);
				*/
				RequestConfig config = 
						RequestConfig.custom()
						.setConnectionRequestTimeout(20 * 1000)
						.setSocketTimeout(20 * 1000)
						.setConnectionRequestTimeout(20 * 1000)
						.build();
				localHttpPost.setConfig(config);
				
				ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
				MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
				
				if (StringUtils.isNotEmpty(paramString3)) {
					multipartEntityBuilder.addTextBody("paramString", paramString3, contentType);
				} else {
					multipartEntityBuilder.addTextBody("dummy", "dummy", contentType);
				}
				
				for(int i = 0; i < files.length; i++) {
					multipartEntityBuilder.addBinaryBody("fileList", files[i]);
				}
				
				localHttpPost.setEntity(multipartEntityBuilder.build());
			}
			
			//HttpResponse localHttpResponse = localDefaultHttpClient.execute((HttpUriRequest)localHttpPost);
			CloseableHttpResponse localHttpResponse = localDefaultHttpClient.execute((HttpUriRequest)localHttpPost);
			localInputStreamReader = new InputStreamReader(localHttpResponse.getEntity().getContent(), "UTF-8");
			localBufferedReader = new BufferedReader(localInputStreamReader);
			String str2 = null;
			while ((str2 = localBufferedReader.readLine()) != null) {
				localStringBuffer.append(str2); 
			}
		} catch (HttpHostConnectException localHttpHostConnectException) {
			throw localHttpHostConnectException;
		} finally {
			try {
				if (localBufferedReader != null)
					localBufferedReader.close(); 
			} catch (IOException localIOException3) {
				throw localIOException3;
			}
			try {
				if (localInputStreamReader != null)
					localInputStreamReader.close(); 
			} catch (IOException localIOException4) {
				throw localIOException4;
			}
			localDefaultHttpClient.close();
		}
		return localStringBuffer.toString();
	}
	
	private String getHttpsContents(String paramString1, String paramString2, String paramString3) throws UnsupportedEncodingException, IOException {
		StringBuffer localStringBuffer = new StringBuffer();
		HttpsURLConnection localHttpsURLConnection = null;
		OutputStreamWriter localOutputStreamWriter = null;
		InputStreamReader localInputStreamReader = null;
		BufferedReader localBufferedReader = null;
		try
		{
			SSLUtil.init();
			String str1 = null;
			if (this.url.startsWith("https://")) {
				str1 = this.url + paramString2;
			} else {
				str1 = "https://" + this.url + paramString2;
			}
			URL localURL = new URL(null, str1, new Handler());
			localHttpsURLConnection = (HttpsURLConnection)localURL.openConnection();
			localHttpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String paramAnonymousString, SSLSession paramAnonymousSSLSession) {
					return true;
				}
			});
			localHttpsURLConnection.setRequestProperty("Content-Type", paramString1);
			localHttpsURLConnection.setRequestProperty("X-CustomAuthToken", this.userAuthToken);
			localHttpsURLConnection.setDoOutput(true);
			localHttpsURLConnection.setDoInput(true);
			localHttpsURLConnection.setUseCaches(false);
			localOutputStreamWriter = new OutputStreamWriter(localHttpsURLConnection.getOutputStream(), "UTF-8");
			if (StringUtils.isNotEmpty(paramString3)) {
				localOutputStreamWriter.write(paramString3);
			}
			localOutputStreamWriter.flush();
			localOutputStreamWriter.close();
			localInputStreamReader = new InputStreamReader(localHttpsURLConnection.getInputStream(), "UTF-8");
			localBufferedReader = new BufferedReader(localInputStreamReader);
			String str2 = null;
			while ((str2 = localBufferedReader.readLine()) != null) {
				localStringBuffer.append(str2.trim());
			}
		} finally {
			try {
				if (localBufferedReader != null) {
					localBufferedReader.close();
				}
			} catch(IOException localIOException4) {
				throw localIOException4;
			}
			
			try {
				if (localInputStreamReader != null) {
					localInputStreamReader.close();
				}
			} catch (IOException localIOException5) {
				throw localIOException5;
			}
			
			try {
				if (localOutputStreamWriter != null) {
					localOutputStreamWriter.close();
				}
			} catch (IOException localIOException6) {
				throw localIOException6;
			}
			
			if (localHttpsURLConnection != null) {
				localHttpsURLConnection.disconnect();
			}
		}
		return localStringBuffer.toString();
	}
	
	protected String getString(JSONObject paramJSONObject, String paramString) {
		Object localObject = paramJSONObject.get(paramString);
		if (localObject != null) {
			return localObject.toString();
		}
		return null;
	}
}