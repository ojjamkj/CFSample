package site.schedule;

import java.util.HashMap;

import com.gtone.cf.schedule.IMMJobNode;
import com.gtone.cf.schedule.ScheduleModel;
import com.gtone.cf.util.CodeServiceUtil;
import com.gtone.cf.util.DBHelper;
import com.gtone.express.server.dao.MCommonDAO;

import jspeed.base.jdbc.CacheResultSet;
import jspeed.base.log.LogLevel;
import jspeed.base.log.Logger;
import jspeed.base.util.Cipher;
import jspeed.base.util.DateHelper;
import jspeed.base.util.StringHelper;
import site.dao.HrDAO;

public class SiteHRSyncBatch implements IMMJobNode{
	private Logger logger = new Logger("cf.user.log");
	
	int idx=0;
	boolean interrupted=false;
	
	public void run(ScheduleModel model) throws Exception {
		logger.println(LogLevel.INFO, "Start Job");
		String currentDt = DateHelper.currentTime("yyyyMMddHHmmss");
		
		MCommonDAO mDao = null;
		HrDAO hrDao = null;
		
		try {
			mDao = new MCommonDAO();
			hrDao = new HrDAO();
			
			mDao.begin();
			
			/**
			 * 1. 부서 동기화
			 * HR에서 부서 정보를 조회해서 CF에 insert 합니다.
			 * HR 테이블에 존재하지 않으면 삭제된 부서로 처리하는 예시입니다.
			 * C_DEP_INFO 테이블 DEP_CD 칼럼을 key로 사용합니다. 
			 */
			//모든 부서을 삭제 상태로 업데이트합니다. DEP_CD IS NULL은 자체 생성으로 간주하여 업데이트 대상에서 제외합니다.
			mDao.executeUpdate("SiteUserDeptBatch_CF_updateDeptDelY", new HashMap<Object, Object>());
			//HR 부서 정보를 조회합니다.
			CacheResultSet deptCrs = hrDao.executeQuery("SiteUserDeptBatch_HR_getDepts", new HashMap<Object, Object>());
			while(deptCrs.next()) {
				HashMap<String, Object> hrDeptMap = (HashMap) deptCrs.getMap();
				HashMap<String, Object> cfDeptMap = new HashMap<String, Object>();
				cfDeptMap.put("DEP_CD", hrDeptMap.get("DEPT_ID"));
				cfDeptMap.put("PAR_DEP_CD", hrDeptMap.get("PARENT_ID"));
				cfDeptMap.put("DEP_TITLE", hrDeptMap.get("DEPT_NM"));
				cfDeptMap.put("DISPLAY_ORDER", hrDeptMap.get("DISP_ORDER"));
				cfDeptMap.put("UPD_ID", 0);
				cfDeptMap.put("UPD_DT", currentDt);
				//HR 부서정보로 CF 부서를 업데이트합니다. DEP_CD 칼럼을 key로 활용합니다.
				int i = mDao.executeUpdate("SiteUserDeptBatch_CF_updateDept", cfDeptMap);
				//업데이트 결과가 없으면 insert를 수행합니다.
				if(i < 1) {
					cfDeptMap.put("DEP_TP_CD", "2001");
					cfDeptMap.put("CRE_ID", 0);
					cfDeptMap.put("CRE_DT", currentDt);
					cfDeptMap.put("DEL_YN", "N");
					mDao.executeUpdate("SiteUserDeptBatch_CF_insertDept", cfDeptMap);
				}
				idx++;
			}
			mDao.commit();
			
			//부모 부서 업데이트
			//DEP_CD 로 부모 부서를 찾아야하므로 insert 완료 후 별도로 처리
			deptCrs.first();
			while(deptCrs.next()) {
				HashMap<String, Object> hrDeptMap = (HashMap) deptCrs.getMap();
				HashMap<String, Object> cfDeptMap = new HashMap<String, Object>();
				cfDeptMap.put("DEP_CD", hrDeptMap.get("DEPT_ID"));
				cfDeptMap.put("PAR_DEP_CD", StringHelper.evl(hrDeptMap.get("PARENT_ID"), ""));
				mDao.executeUpdate("SiteUserDeptBatch_CF_updateParDept", cfDeptMap);
			}
			mDao.commit();
			
			/**
			 * 2. 직급 동기화
			 * 직급 테이블이 별도로 존재하지 않는 경우에 대한 예시입니다.
			 * HR 사원 테이블에서 직급 정보를 DISTINCT 하여 조회합니다.
			 * CF에 존재하지 않는 직급만 C_CD, C_CD_NM 테이블에 insert 합니다.
			 */
			CacheResultSet positionCrs = hrDao.executeQuery("SiteUserDeptBatch_HR_selectPositions", new HashMap<Object, Object>());
			while(positionCrs.next()) {
				HashMap<String, Object> hrPositionMap = (HashMap) positionCrs.getMap();
				HashMap<String, Object> cfPositionMap = new HashMap<String, Object>();
				
				String positionNm = (String) hrPositionMap.get("POS_NM");
				cfPositionMap.put("CD_ID", positionNm);
				cfPositionMap.put("CD_DESC", positionNm);
				//cfPositionMap.put("DISPLAY_ORDER", "");
				//cfPositionMap.put("CD_ETC", "");
				//cfPositionMap.put("DEL_YN", "N");
				cfPositionMap.put("CD_GRP_ID", "2100"); //직급 그룹코드
				CacheResultSet crs = mDao.executeQuery("SiteUserDeptBatch_CF_selectPosition", cfPositionMap);
				if(crs.getRowCount() == 0) {
					mDao.executeUpdate("SiteUserDeptBatch_CF_insertPosition", cfPositionMap);
					cfPositionMap.put("DISPLAY_NM", positionNm);
					mDao.executeUpdate("SiteUserDeptBatch_CF_insertPositionNm", cfPositionMap);
					idx++;
				}
				
			}
			mDao.commit();
			
			/**
			 * 3. 사용자 동기화
			 * HR에서 사용자 정보를 조회해서 CF에 insert 합니다.
			 * C_USER 테이블 LOGIN_ID 칼럼을 key로 사용합니다.
			 */
			CacheResultSet userCrs = hrDao.executeQuery("SiteUserDeptBatch_HR_getUsers", new HashMap<Object, Object>());
			while(userCrs.next()) {
				HashMap<String, Object> hrUserMap = (HashMap) userCrs.getMap();
				HashMap<String, Object> cfUserMap = new HashMap<String, Object>();
				
				String loginId = hrUserMap.get("EMP_ID").toString();
				cfUserMap.put("LOGIN_ID", loginId);
				cfUserMap.put("POSITION_CODE", hrUserMap.get("POS_NM"));
				cfUserMap.put("DEP_CD", hrUserMap.get("DEPT_ID"));
				cfUserMap.put("USER_NM", hrUserMap.get("EMP_NM"));
				cfUserMap.put("EMAIL", hrUserMap.get("EMAIL"));
				cfUserMap.put("CELL_PHONE", hrUserMap.get("PHONE"));
				
				String delYn = "1".equals(hrUserMap.get("DUTY_YN")) ? "Y" : "N";
				cfUserMap.put("DEL_YN", delYn);
				
				int i = mDao.executeUpdate("SiteUserDeptBatch_CF_updateUser", cfUserMap);
				if(i < 1) {
					cfUserMap.put("USER_PWD", Cipher.parseCipher(loginId));
					cfUserMap.put("PWD_FAIL_COUNT", 0);
					cfUserMap.put("CRE_ID", 0);
					cfUserMap.put("CRE_DT", currentDt);
					cfUserMap.put("UPD_ID", 0);
					cfUserMap.put("UPD_DT", currentDt);
					cfUserMap.put("ACTIVE_YN", "Y");
					mDao.executeUpdate("SiteUserDeptBatch_CF_insertUser", cfUserMap);
					
					//기본 권한을 부여합니다.
					//설정 > 사용자/권한관리 메뉴에서 미리 생성해놓은 그룹/직무 ID를 세팅합니다.
					cfUserMap.put("GROUP_ID", CodeServiceUtil.findCode("SITE_OPTIONS", "DEFAULT_GROUP_ID", "7"));
					cfUserMap.put("ROLE_ID", CodeServiceUtil.findCode("SITE_OPTIONS", "DEFAULT_ROLE_ID", "4"));
					mDao.executeUpdate("SiteUserDeptBatch_CF_insertDefaultGroupRole", cfUserMap);
					mDao.executeUpdate("SiteUserDeptBatch_CF_insertDefaultGroupRoleHist", cfUserMap);
				}
				idx++;
			}
			mDao.commit();
			
		} catch(Exception e) {
			if(mDao != null) mDao.rollback();
			throw e;
		} finally{
			DBHelper.release(hrDao);
			DBHelper.release(mDao);
		}
		
		logger.println(LogLevel.INFO, "End Job");
	}

	public void interrupt() {
		interrupted = true;
	}

	public long getCheckCount() {
		return idx;
	}
}
