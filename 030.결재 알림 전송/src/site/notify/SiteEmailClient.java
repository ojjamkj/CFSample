package site.notify;

import java.util.List;

import jspeed.base.jdbc.QueryHelper;

import com.gtone.cf.web.services.IMailClient;

public class SiteMailClient implements IMailClient{
	
	/*
	 * 메일을 전송합니다.
	 */
	public void sendMail(QueryHelper qHelper, List toAddr , String fromAddr, String fromName, String subject, String content) throws Exception {
		// TODO Auto-generated method stub
		
	}

}