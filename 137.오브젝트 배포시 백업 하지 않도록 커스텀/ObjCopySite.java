package com.gtone.cf.rt.run.copy;

import java.util.HashMap;
/*
 * 커스터마이징 내용 : 오브젝트 복사 전 백업하지 않도록 처리
 * 클래스 작성 후 하단의 코드를 수정 및 등록 후 사용하세요.
 * 
 * 1. [CF_OPTIONS][OBJ_COMPILE_RESULT_ROLLBACK] 코드를 Y로 변경
 * 2. [CLASS_COPY][COPY_OBJ_CLASS_A][com.gtone.cf.rt.run.copy.Copy.ObjCopySite] 코드 등록 (사이트에서 작성한 클래스로)
 */

public class ObjCopySite extends ObjCopy {
	protected boolean backup(String backupPath, String targetPath, HashMap deployHashMap) throws Exception
	{
		return false;
		//return super.backup(backupPath, targetPath, deployHashMap);
	}
}
