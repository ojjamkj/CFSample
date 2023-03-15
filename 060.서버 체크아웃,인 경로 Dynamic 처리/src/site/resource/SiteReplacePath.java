package site.resource;

import java.util.HashMap;

import com.gtone.cf.rt.deploy.IReplacePath;

public class SiteReplacePath implements IReplacePath {

	public String replacePath(String targetPath, HashMap inHash) throws Exception {
		
		String path = targetPath;
		try{
			int pos = targetPath.indexOf("{CLASS:"); 
			if(pos > -1)
			{
				path = targetPath.substring(0, pos );
				path += inHash.get("INST_ID") +"/" + inHash.get("LOGIN_ID");
				
				return path;
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return path;
	}

}