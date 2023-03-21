package site.run;

import java.math.BigDecimal;

import com.gtone.cf.rt.resources.run.CheckInRun;
import com.gtone.cf.rt.resources.run.ResourceBean;

/*
	[로컬 체크인 시 checksum이 null일 경우 체크인 불가토록 사이트 커스터마이징]
	
	1. 코드관리의 CLASS_DEPLOY_RUN > RUN_CHECKIN 코드명을 확인합니다.
	2. 코드명이 com.gtone.cf.rt.resources.run.CheckInRun 과 일치하는지 확인 후
	2-1) 코드명이 일치하면(Site 처리 한 CheckInRun 클래스가 없으면) 첨부된 java 파일을 src.site.run 폴더에 복사
	2-2) 코드명이 일치하지 않으면(Site 처리 한 CheckInRun 클래스가 존재하면) 첨부된 java 파일 내 onBeforeCheckIn 안 내용을 기존 class에 병합 * 클래스명, 패키지명, onBeforeCheckIn 내 Exception 내 메시지는 사이트에 맞게 변경하시면 됩니다.
	3. 클래스 배포 후 재기동 (사용자에게 체크인 오류가 발생할 수 있으므로 공지 후 이용 없는 시간에 배포,변경 바랍니다.)
	4. 코드관리의 CLASS_DEPLOY_RUN > RUN_CHECKIN 를 site.run.SiteCheckInRun(사이트에 맞게 변경한 경우 해당 패키지.클래스명)으로 변경
	5. 로컬 체크인 테스트
*/

public class SiteCheckInRun extends CheckInRun {

	public SiteCheckInRun(BigDecimal instId, BigDecimal taskId, BigDecimal userId, String locale) throws Exception {
		super(instId, taskId, userId, locale);
	}
	
	@Override
	protected void onBeforeCheckIn(ResourceBean resourceBean) throws Exception {
		
		super.onBeforeCheckIn(resourceBean);
		
		if(resourceBean.getCheckSum() == null) {
			throw new Exception("Empty files cannot be Check-In : " + resourceBean.getResPath() + resourceBean.getResName());
		}
	}
	
}
