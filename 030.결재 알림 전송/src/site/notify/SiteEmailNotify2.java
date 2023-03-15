package site.notify;

import java.util.HashMap;

import jspeed.base.util.StringHelper;

import com.gtone.cf.rt.file.FileManager;
import com.gtone.cf.util.CodeServiceUtil;
import com.gtone.cf.web.CFAppInit;
import com.gtone.express.server.dao.MCommonDAO;
import com.gtone.wfapplication.notify.impl.EmailNotify2;
import com.itplus.wf.util.CommonUtil;

public class SiteEmailNotify2 extends EmailNotify2 {
	/*
	 * 이메일의 제목을 설정합니다.
	 * @see com.gtone.wfapplication.notify.impl.EmailNotify2#getSubject(com.gtone.express.server.dao.MCommonDAO, java.util.HashMap)
	 */
	protected String getSubject(MCommonDAO dao, HashMap param)
	{
		//아래 코드는 샘플이니 참고하세요.
		
		//String SUBJECT = CodeServiceUtil.findCode("EMAIL", "SUBJECT", "CF Notify");
		return "";
	}
	
	/*
	 * 이메일의 내용을 설정합니다.
	 * @see com.gtone.wfapplication.notify.impl.EmailNotify2#getContent(com.gtone.express.server.dao.MCommonDAO, java.util.HashMap)
	 */
	protected String getContent(MCommonDAO dao, HashMap param)
	{
		
		try
		{
			
		}
		catch(Exception ignore)
		{
			return "";
		}
		
		return "";
	}
}