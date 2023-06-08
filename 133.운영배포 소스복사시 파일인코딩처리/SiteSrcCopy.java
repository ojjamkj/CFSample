package XXX.copy;

import java.util.HashMap;

import com.gtone.cf.daemon.cmd.BaseCommand;
import com.gtone.cf.rt.file.FileTransfer;
import com.gtone.cf.rt.resources.run.ResourceRunner;
import com.gtone.cf.util.ICFConstants;
import com.gtone.cf.util.PathHelper;

import jspeed.base.log.LogLevel;

/*
 * 
 * 코드관리> 그룹코드[CLASS_COPY] 코드[COPY_SRC_CLASS_S] XXX.copy.SiteSrcCopy 코드로 등록 후 처리
 * 아래 주석부분에 파일인코딩 로직을 추가 바랍니다.
 */
public class SiteSrcCopy extends SrcCopy {
	
	protected BaseCommand executeCopyCommand(HashMap deployHashMap, String targetPath) throws Exception
	{
		
		return encryptRepositorySrcToRemote(inHash, deployHashMap,targetPath) ;
		
	}
	
	
	public BaseCommand encryptRepositorySrcToRemote(HashMap globalMap, HashMap connHash, String targetPath ) throws Exception{
		try{
			
			String sourcePath = PathHelper.getDevRepositoryFilePath(connHash);
			byte[] source = ResourceRunner.getInstance().getViewSourceRun().getSource(sourcePath, null, connHash);
			
			/// 파일 인코딩 로직 추가
			/// 파일 인코딩 로직 추가
			/// 파일 인코딩 로직 추가			
			
			connHash.put(ICFConstants.FILE_SOURCE, source);
			connHash.put(ICFConstants.TARGET_FILE, targetPath);
			getLogger().println(LogLevel.INFO, "* [REMPOSITORY PATH]=" + sourcePath);
			getLogger().println(LogLevel.INFO, "* [REMOTE PATH]=" + targetPath);
			getLogger().println(LogLevel.INFO, "* [TARGET IP]=" + connHash.get(ICFConstants.TARGET_IP));
			getLogger().println(LogLevel.INFO, "* [TARGET PORT]=" + connHash.get(ICFConstants.TARGET_PORT));

			return FileTransfer.getInstance().createFile(globalMap, connHash);
			
		}catch(Exception e){
			throw e;
		}
	}
}
