package com.gtone.cf.rt.resources.run;

import java.math.BigDecimal;
import java.util.HashMap;

import com.gtone.cf.util.ExceptionHandler;
import com.gtone.express.server.dao.MCommonDAO;
import com.gtone.express.server.helper.MessageHelper;

import jspeed.base.jdbc.CacheResultSet;
import jspeed.base.log.LogService;
import jspeed.base.log.Logger;
import jspeed.base.util.StringHelper;

/*
 * 
 * - [기능추가] MDD 체크인시 관련파일 doma정보 저장 (sgi)
		2) 코드관리>코드[CLASS_DEPLOY_RUN][RUN_CHECKIN] 를 사이트에서 작성한 클래스로 변경
 */

public class SiteCheckinRun extends CheckInRun {
	private static final Logger logger = LogService.getInstance().getLogger();

	public SiteCheckinRun(BigDecimal instId, BigDecimal taskId, BigDecimal userId, String locale) throws Exception {
		super(instId, taskId, userId, locale);
		// TODO Auto-generated constructor stub
	}

	protected void onBeforeCheckIn(ResourceBean resourceBean) throws Exception {

		boolean isNew = false;
		HashMap param = new HashMap(); 		// 사용자 직무 조회용 HashMap
		HashMap param_del = new HashMap(); 	// 요청서 리소스 매핑 해제용 HashMap
		String ROLE_DEV = "N"; 				// 사용자 Role 중 [개발자]가 있는지 체크

		String groupIds = StringHelper.evl(resourceBean.getGroupId(), null);
		String resName = StringHelper.evl(resourceBean.getResName(), null);
		String collectId = StringHelper.evl(resourceBean.getCollectId(), null);
		String userId = StringHelper.evl(resourceBean.getUserId(), null);
		String resId = StringHelper.evl(resourceBean.getResId(), null);
		String instId = StringHelper.evl(resourceBean.getInstId(), null);

		param.put("USER_ID", userId);

		param_del.put("RES_ID", resId);
		param_del.put("INST_ID", instId);

//		logger.println(LogLevel.INFO, "groupIds  >>>> " + groupIds);
//		logger.println(LogLevel.INFO, "resName  >>>> " + resName);
//		logger.println(LogLevel.INFO, "collectId  >>>> " + collectId);
//		logger.println(LogLevel.INFO, "userId  >>>> " + userId);
//		logger.println(LogLevel.INFO, "resId  >>>> " + resId);
//		logger.println(LogLevel.INFO, "instId  >>>> " + instId);

		StringBuffer sbRes = new StringBuffer();

		MCommonDAO dao = new MCommonDAO(this.resourceDAO.getQueryHelper().getConnection());

		// 사용자 Role 조회
		CacheResultSet rs = dao.executeQuery("site_select_user_role", param);

		/*
		 * SELECT DISTINCT B.ROLE_NM, B.ROLE_ID FROM C_USER_GROUP_ROLE A, C_ROLE B WHERE
		 * A.ROLE_ID = B.ROLE_ID AND A.USER_ID = #{USER_ID}
		 */

		while (true) {
			if (!rs.next())
				break;
			if (rs.getString("ROLE_NM").equals("개발자")) {
				ROLE_DEV = "Y";
			}
		}

		// 연관 체크인이 아닐시 개발자 권한을 가지고 Doma의 Java 프로젝트에 .java 파일을 단독으로 체크인하는지 체크
		if (groupIds == null && collectId.equals("20230818175051") && resName.endsWith(".java")
				&& ROLE_DEV.equals("Y")) {
			sbRes.append(resName).append("\n").toString();
		} else {
			if (StringHelper.isNull(resourceBean.getResId())) {
				isNew = true;
				resourceBean.setResId((new StringBuilder()).append("-").append(++newResIdx).toString());
			}
			executeScript("Y", resourceBean);
			if (isNew)
				resourceBean.setResId(null);
		}

		// 권한 없이 Doma의 Java 프로젝트에 .java 체크인하는걸 막고 요청서에 해당 .java 매핑해제
		if (sbRes.length() > 0) {
			MCommonDAO dao1 = new MCommonDAO();

			dao1.begin();
			dao1.executeUpdate("site_delete_cf_resource_lock", param_del);
			// DELETE FROM CF_RESOURCE_LOCK WHERE RES_ID = #{RES_ID} AND INST_ID = #{INST_ID} AND LOCK_TYPE = 'CR'
			dao1.executeUpdate("site_delete_cf_resource_log", param_del);
			// DELETE FROM CF_RESOURCE_LOG WHERE RES_ID = #{RES_ID} AND INST_ID = #{INST_ID}
			dao1.executeUpdate("site_delete_cf_resource_cr_map", param_del);
			// DELETE FROM CF_RESOURCE_CR_MAP WHERE RES_ID = #{RES_ID} AND INST_ID = #{INST_ID}
			dao1.commit();
			dao1.close();

			throw new Exception((new StringBuilder()).append(MessageHelper.getInstance().getMessage("B226", locale))
					.append("\n\n").append(sbRes.toString()).toString());

		}
	}

	protected void onEndAfterCommit(String[] resIds) {

		super.onEndAfterCommit(resIds);

		try {
			MCommonDAO dao = new MCommonDAO(this.resourceDAO.getQueryHelper().getConnection());

			HashMap paramMap = new HashMap();
			for (ResourceBean bean : this.beans) {
				if (StringHelper.evl(bean.getResId(), null) != null && StringHelper.evl(bean.getGroupId(), null) != null
						&& bean.getErrCode() == null) {

					String resName = bean.getResName().toString();

					if (!resName.endsWith(".doma")) {
						paramMap.put("RES_ID", bean.getResId());
						paramMap.put("GROUP_ID", bean.getGroupId());
						// 쿼리 내용 site_resource_meta_insert_update
						/*
						 * MERGE INTO CF_RESOURCE_META A USING (SELECT #{RES_ID} AS RES_ID, #{GROUP_ID}
						 * AS RES_DESC FROM DUAL) B ON (A.RES_ID = B.RES_ID) WHEN MATCHED THEN UPDATE
						 * SET A.RES_DESC = B.RES_DESC WHEN NOT MATCHED THEN INSERT (A.RES_ID,
						 * A.RES_DESC) VALUES (B.RES_ID, B.RES_DESC)
						 */
						dao.executeUpdate("site_resource_meta_insert_update", paramMap);
					}
				}
			}
		} catch (Exception ignore) {
			ExceptionHandler.handleException(ignore);
		}
	}
}
