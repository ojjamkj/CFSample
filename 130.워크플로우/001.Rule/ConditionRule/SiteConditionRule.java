import java.util.HashMap;

import com.itplus.wf.inst.rule.impl.DefaultConditionRule;

import jspeed.base.jdbc.CacheResultSet;
import jspeed.websvc.WSParam;

public class SiteConditionRule extends DefaultConditionRule {

	@Override
	public boolean checkRule(WSParam params) throws Exception {
		String instId = params.getParameter("INST_ID");
		
		//기본 DB가 아닌 다른 DB를 접속하기 위해
		/* 1. 다른 DB의 데이터소스명은 WAS의 데이터 소스에 바인드 되어 있어야합니다. 
		 * 2. 구성하는 방법
		 * 	2-1. Tomcat일 경우 신규 데이터소스를 server.xml에 추가합니다. 
		 *  2-2. Jeus나 다른 WAS일 경우 WAS 담당자 에게 추가 요청해아합니다.
		 * 3. 타 DB 접속을 위한 소스 작성
		 * 	3-1. 2번에서 만들어진 DB에 접속하기 위해 파일을 작성합니다. sqlMapConfigTest.xml 참고
		 *  3-2. DAO를 작성합니다. TestDAO.java 참고
		 *  3-3. 쿼리를 작성합니다.. Test.xml 참고
		 */
		TestDAO dao = null;
		try {
			dao = new TestDAO();
			HashMap param = params.toOneParameterMap();
			CacheResultSet crs = dao.executeQuery("Test_select_all", param);
			
			if(crs.next()) {
				return true;
			}
		}catch(Exception e) {
			throw(e);
		}
		
		return false;
	}

}
