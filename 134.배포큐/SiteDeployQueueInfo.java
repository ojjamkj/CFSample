package site.sample.queue;

import java.math.BigDecimal;
/*
 * 
 * - [기능추가] 배포> 배포 큐 추가 (신협)
		1) 사이트 업무로직에 맞는 배포큐 정의 클래스 작성(아래 git 리파지토리 샘플 참고)
			* CFSample/134.배포큐/SiteDeployQueueInfo.java 
		2) 코드관리>코드[CLASS_DEPLOY_RUN][GET_DEPLOY_QUEUE] 에 1)에서 작성한 클래스를 등록
 */
public class SiteDeployQueueInfo extends AbstractDeployQueueInfo {

	@Override
	public void run(BigDecimal instId, BigDecimal deployId, BigDecimal userId, String sysType) {
		// TODO Auto-generated method stub
		boolean isQueue = true;
		if(deployId.toString().equals("1664")) isQueue = false;
		if(isQueue) {
			this.setQueueName("KMS_"+sysType);
			this.setConCurrentCount(1);
		}
		
	}
	
	
}
