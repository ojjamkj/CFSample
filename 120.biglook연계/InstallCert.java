package test.biglook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class InstallCert {

	private String host;
	private int port;
	private char[] passphrase = "changeit".toCharArray();
	private KeyStore ks;	
	private X509Certificate[] chain;
	public final String jssecacerts = "jssecacerts";
	public String jssecacertsPath;
	
	private final File lockFile = new File("InstallCert.lock");
	
	public InstallCert(URL url) {
		this.host = url.getHost();
		this.port = url.getPort();
		
		if(this.port == -1) {
			if("http".equals(url.getProtocol())) {
				this.port = 80;
			} else if("https".equals(url.getProtocol())) {
				this.port = 443;
			}
		}
	}
	
	public InstallCert(String url) throws MalformedURLException {
		this(new URL(url));
	}
	
	public InstallCert(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void install() throws Exception {		
		FileLocker fileLocker = new FileLocker(lockFile);
		
		try {
			// 파일 락 획득 (멀티 프로세스 동작시 파일 락 필요)
			if(!fileLocker.lock()) {
				throw new Exception("Error: the acquired file lock timeout. (time > 60 sec)");
			}
			
			createKeyStore();
			
			if(!checkTrust()) {
				createJssecacerts();
			}			
		} finally {
			// 파일 락 해제
			fileLocker.unlock();
		}
	}
	
	private void createJssecacerts() throws Exception {
		int k=0;
		X509Certificate cert = chain[k];
		String alias = host + "-" + (k + 1);
		ks.setCertificateEntry(alias, cert);
		
		try {
			FileOutputStream out = new FileOutputStream(jssecacerts);
			ks.store(out, passphrase);
		}catch(Exception e){
			throw(e);
		}
	}
	
	private boolean checkTrust() throws Exception {
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
		SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] {tm}, null);
		SSLSocketFactory factory = context.getSocketFactory();

		SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		
		try {
			socket.startHandshake();	
			return true;
			
		}catch (SSLException e) {
			//System.out.println(e.getMessage());
		}finally{
			if(socket != null)
			{
				socket.close();
			}			
		}		
		
		chain = tm.chain;
		if (chain == null) {
			throw new Exception("Could not obtain server certificate chain");
		}
		return false;
	}
	
	private void createKeyStore() throws Exception {
		File file = new File(jssecacerts);
		jssecacertsPath = file.getAbsolutePath();

		if (file.isFile() == false) {
			char SEP = File.separatorChar;
			File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
			file = new File(dir, "jssecacerts");
			if (file.isFile() == false) {
				file = new File(dir, "cacerts");
			}
		}
		
		try {
			FileInputStream in = new FileInputStream(file);;
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(in, passphrase);
		}catch(Exception e){
			
		}
	}
	
	private class SavingTrustManager implements X509TrustManager {
		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SavingTrustManager(X509TrustManager tm) {
			this.tm = tm;
		}

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}
}
