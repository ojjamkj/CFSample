package site.filter;

import java.util.HashMap;

import com.gtone.cf.rt.deploy.IBuildScriptFilter;
import com.gtone.cf.util.DBHelper;
import com.gtone.cf.util.ExceptionHandler;
import com.gtone.express.server.dao.MCommonDAO;

import jspeed.base.dao.CommonDAO;
import jspeed.base.jdbc.CacheResultSet;

public class NHRebootScriptFilter implements IBuildScriptFilter {
	
	public CacheResultSet getFilteredSources(CommonDAO dao, CacheResultSet rscRs) throws Exception {
		
		MCommonDAO mdao = null;
		try {
			boolean excuteScriptFlag = false;
			
			if(rscRs.next()) {
				HashMap paramMap = (HashMap) rscRs.getMap();
				
				mdao = new MCommonDAO(dao.getQueryHelper().getConnection());
				CacheResultSet crs = mdao.executeQuery("site_script_id", paramMap); //쿼리 아이디 사이트걸로 수정
				/* WF_site.xml 예시
					<select id="site_script_id" parameterType="hashMap" resultType="ExpressMap">
						SELECT
							REQ.*
						FROM
							  CF_RESOURCE_VERSION_DEV RVD
							, CF_USER_REQUEST REQ
						WHERE
							RVD.RES_ID = #{RES_ID}
							AND RVD.VERSION_ID = #{VERSION_ID}
							AND RVD.DEV_VERSION_ID = #{DEV_VERSION_ID}
							AND RVD.INST_ID = REQ.INST_ID
							AND REQ.BIZ_ID FLAG = 'Y' --배포 여부 쓸 컬럼
							AND ROWNUM = 1
					</select>
				 */
				if(crs.next()) {
					excuteScriptFlag = true;
				}
			}
			
			if(excuteScriptFlag) {
				return rscRs; //스크립트 보여줌
			} else {
				return null; //스크립트 안보여줌
			}
			
		} catch(Exception e) {
			ExceptionHandler.handleException(e);
			throw e;
		} finally {
			DBHelper.release(mdao);
			rscRs.initRow();
		}
		
	}

}
