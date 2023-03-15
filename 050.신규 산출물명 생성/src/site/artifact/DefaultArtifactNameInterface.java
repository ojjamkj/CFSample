package site.artifact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gtone.cf.util.CodeServiceUtil;

public class SiteArtifactNameInterface implements IArtifactName{

	public void makeArtifactName(String instId, Object userId, HashMap[] inHash) throws Exception {
		
		
		/* ####################################################################
		 * 샘플 산출물  명명규칙 (다르면 사이트에서 구현하세요.)
		 * 기본 제공 산출물 명명규칙 : [시스템 ID]_[산출물타입코드]_[산출물명]_[프로젝트번호].[산출물 확장자]
		 * ####################################################################    
		 */
	
		//산출물을 위한 공통 정보를 가져온다.
		//01.업무시스템 약자
		//String BIZ_ID = bizId;
		String delim = "_";
		
		int forSize = inHash.length;
		
		for(int i=0; i< forSize ; i++ )
		{
			String artifactTypeCd =  (String)inHash[i].get("ARTIFACT_TYPE");
			String artifactTypeNm = CodeServiceUtil.findCode("ARTIFACT_TYPE", artifactTypeCd);
			String extName = ((String)inHash[i].get("ATTACH_FILE_NAME")).substring(((String)inHash[i].get("ATTACH_FILE_NAME")).lastIndexOf(".")+1);
			
			String artifactName = (String)inHash[i].get("BIZ_ID") + delim +  
							  artifactTypeCd + delim + 
							  artifactTypeNm + delim +
							  instId + "." + extName;
			
			inHash[i].put("RENAME_ATTACH_FILE", artifactName); //실제 산출물명
		}
}


}