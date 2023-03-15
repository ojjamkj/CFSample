package test.biglook;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class X509TrustManagerImpl implements X509TrustManager {

	private String jssecacerts = "";
	
	public X509TrustManagerImpl(String jssecacerts) {
		this.jssecacerts = jssecacerts;
	}
	
	public void checkClientTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
		// TODO Auto-generated method stub
		
	}  

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {   
		try {   
			// Get trust store   
			KeyStore trustStore = KeyStore.getInstance("JKS");   
			
			if(jssecacerts.length() == 0) {
				jssecacerts = System.getProperty("java.home") + "/lib/security/cacerts";
			}
			
			trustStore.load(new FileInputStream(jssecacerts), "changeit".toCharArray()); // Use default certification validation   

			// Get Trust Manager   
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());   
			tmf.init(trustStore);   
			TrustManager[] tms = tmf.getTrustManagers();   
			((X509TrustManager)tms[0]).checkServerTrusted(chain, authType);   
		} catch (KeyStoreException e) {   
			e.printStackTrace();   
		} catch (NoSuchAlgorithmException e) {   
			e.printStackTrace();   
		} catch (IOException e) {   
			e.printStackTrace();   
		}   
	}

	public X509Certificate[] getAcceptedIssuers() {   
		return null;
	}

 
}
