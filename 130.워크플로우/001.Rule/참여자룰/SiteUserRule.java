import java.util.ArrayList;
import java.util.HashMap;

import com.itplus.wf.inst.rule.impl.DefaultUserRule;

import jspeed.websvc.WSParam;

public class SiteUserRule extends DefaultUserRule {
	public ArrayList getUserList(WSParam params) throws Exception {
		
		ArrayList result = new ArrayList();
		com.itplus.wf.internal.dao.CommonDAO dao = null;
	 
		String condition =  getRuleContent(params);
		condition = jspeed.base.util.StringHelper.replaceStr(condition, "GROUP", "GROUP_ID");
		condition = jspeed.base.util.StringHelper.replaceStr(condition, "ROLE", "ROLE_ID");
		condition = jspeed.base.util.StringHelper.replaceStr(condition, "&", "AND");
		condition = jspeed.base.util.StringHelper.replaceStr(condition, "|", "OR");
		dao = new com.itplus.wf.internal.dao.CommonDAO(super.getDBAssistant());
		HashMap cond  = new HashMap();
		cond.put("condition", condition);
		String sql = jspeed.base.query.QueryService.getInstance().getSQL("wf.DefaultUserRule.getUserList", cond);
		jspeed.base.jdbc.CacheResultSet rs = dao.executeQuery("wf.DefaultUserRule.getUserList"  , sql , new Object[] {});
		while(rs.next())
		{
			result.add(rs.getObject("USER_ID"));
		}
		return result;
		 
	}
}
