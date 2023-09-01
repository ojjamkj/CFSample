package com.gtone.cf.rt.resources.run;

import java.math.BigDecimal;
import java.util.HashMap;

import com.gtone.cf.util.ExceptionHandler;
import com.gtone.express.server.dao.MCommonDAO;

import jspeed.base.util.StringHelper;

/*
 * 
 * - [기능추가] MDD 체크인시 관련파일 doma정보 저장 (sgi)
		2) 코드관리>코드[CLASS_DEPLOY_RUN][RUN_CHECKIN] 를 사이트에서 작성한 클래스로 변경
 */

public class SiteCheckinRun extends CheckInRun {

	public SiteCheckinRun(BigDecimal instId, BigDecimal taskId, BigDecimal userId, String locale) throws Exception {
		super(instId, taskId, userId, locale);
		// TODO Auto-generated constructor stub
	}

	
	protected void onEndAfterCommit(String[] resIds)   {
		
		super.onEndAfterCommit(resIds);
		
		
		try {
			MCommonDAO dao = new MCommonDAO(this.resourceDAO.getQueryHelper().getConnection());
			
			HashMap paramMap = new HashMap();
			for(ResourceBean bean : this.beans)
			{
				if(StringHelper.evl(bean.getResId(), null) != null && StringHelper.evl(bean.getGroupId(), null) != null && bean.getErrCode() == null) {
					paramMap.put("RES_ID", bean.getResId());
					paramMap.put("GROUP_ID", bean.getGroupId());
					// 쿼리 내용 site_resource_meta_insert_update
					/*
					 * MERGE INTO CF_RESOURCE_META A
						USING (SELECT #{RES_ID} AS RES_ID, #{GROUP_ID} AS RES_DESC 
						       FROM DUAL) B
						   ON (A.RES_ID = B.RES_ID)
						 WHEN MATCHED THEN
						   UPDATE SET
						     A.RES_DESC = B.RES_DESC
						 WHEN NOT MATCHED THEN
						   INSERT (A.RES_ID, A.RES_DESC)
						   VALUES (B.RES_ID, B.RES_DESC)
					 * 
					 * 
					 */
					dao.executeUpdate("site_resource_meta_insert_update", paramMap);
				}
			}
		}catch(Exception ignore) {
			ExceptionHandler.handleException(ignore);
		}
	}
}
