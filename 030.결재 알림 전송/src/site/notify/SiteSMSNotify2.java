package site.notify;

import java.util.HashMap;

import com.gtone.express.server.dao.MCommonDAO;
import com.gtone.wfapplication.notify.impl.SMSNotify2;

public class SiteSMSNotify2 extends SMSNotify2 {
	/*
	 * SMS의 제목을 설정합니다.
	 */
	protected String getSubject(MCommonDAO dao, HashMap param)
	{
		//아래 코드는 샘플이니 참고하세요.
		
		//String SUBJECT = CodeServiceUtil.findCode("EMAIL", "SUBJECT", "CF Notify");
		return "";
	}
	
	/*
	 * SMS의 내용을 설정합니다.
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