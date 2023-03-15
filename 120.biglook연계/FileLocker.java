package test.biglook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLocker {
	private final int tryLockMaxCount = 120;
	
	private File file;
	private FileChannel channel;
	private FileLock fileLock;
	
	public FileLocker(File file) {
		this.file = file;
	}
	
	public boolean lock() throws FileNotFoundException {
		FileOutputStream out = null;
		
		try {
			out = new FileOutputStream(file);
			channel = out.getChannel();
			
			int tryLockCount = 0;
			while(fileLock == null) {
				tryLockCount++;
				
				// 파일 락 획득
				fileLock = channel.tryLock();
				
				if(fileLock == null) {					
					// 1분동안 락을 획득 할 수 있도록 한다. (sleep:500, 횟수: 120번 = 60초)  
					if(tryLockCount >= tryLockMaxCount) {
						break;
					} else {
						try {
							Thread.sleep(500); 
						} catch(Exception e) {}
					}
				}
			}
		} catch(FileNotFoundException fnfe) {
			throw fnfe;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(fileLock == null && channel != null) {
				try {
					channel.close();
				} catch(Exception e) {}
			}
		}
		return fileLock != null;
	}
	
	public void unlock() {
		if(fileLock != null) {
			try {
				fileLock.release();
				fileLock = null;
			} catch(Exception e) {}
		}
		
		if(channel != null) {
			try {
				channel.close();
				channel = null;
			} catch(Exception e) {}
		}
	}
}