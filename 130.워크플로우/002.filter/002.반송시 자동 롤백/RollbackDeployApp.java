package com.사이트명.wfapplication.filters.common.post;

import java.util.HashMap;
import java.util.List;

import com.gtone.cf.rt.deploy.DeployBatchService;
import com.gtone.cf.util.ExceptionHandler;
import com.gtone.cf.util.ICFConstants;
import com.gtone.cf.web.services.DeployService;
import com.itplus.wf.def.act.WfactModel;
import com.itplus.wf.inst.app.IApplication;

import jspeed.base.query.DBAssistant;
import jspeed.websvc.WSParam;

public class RollbackDeployApp implements IApplication {
	
	
	// 반송하는 단계의 워크플로우 after act filter로 등록 후 사용하세요.
	public String execute(DBAssistant dba, WSParam params, WfactModel model)
			throws Exception {
		
		String flag = params.getRequest().getParameter("flag");
        if (flag.equals("reject")) {
        	executeRollback(dba, params, model);
        }
		
		return "success";
	}
	
	private void executeRollback(DBAssistant dba, WSParam params, WfactModel model) {
		try {
			// 배포요청서 번호 조회
			HashMap inHash = new HashMap();
			inHash.put(ICFConstants.SYS_TYPE, "S");
			List<HashMap> deployList = DeployService.getInstance().findDeployByInstIdNSysType(inHash);
			
			DeployBatchService batchService = DeployBatchService.getInstance();
			for(HashMap map : deployList)
			{
				String deployId = (String)map.get(ICFConstants.DEPLOY_ID);
				
				//이미 걸린 스케쥴이 있는 경우 skip. 반송을 반복적으로 하는 경우 생길문제 방지
				if(batchService.isScheduled(batchService.getKey(deployId))) {
					continue;
				}
				
				inHash.put(ICFConstants.DEPLOY_ID, deployId);
			
				// 배포요청서의 리소스 선택 상태를 백업이 있는 파일만 선택되도록 업데이트
				DeployService.getInstance().updateIsSelectForRollback(inHash);
			
				inHash.put("IS_ROLLBACK", "Y");
				inHash.put("IS_DEPLOY", "Y");
				inHash.put(ICFConstants.SCHEDULE_TYPE, ICFConstants.IMMEDIATE);
				
				// 배포수행 (백그라운드임)
				DeployService.getInstance().executeDeploy(false, inHash);
				
				// 최소 3초 대기 후 결재 완료 될 수 있도록 리턴, 끝날 때까지 대기하면 트랜잭션이 길어져서 문제 발생 소지가 있음
				if(batchService.isScheduled(batchService.getKey(deployId))) {
					Thread.sleep(3000); // 3초 대기
				}
			}
			// 결과판단이 불필요하면 별도의 체크없이 그대로 리턴
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ExceptionHandler.handleException(e);
		}
	}

}
