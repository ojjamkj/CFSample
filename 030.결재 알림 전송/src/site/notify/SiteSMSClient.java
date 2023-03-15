package site.notify;

import java.util.List;

import jspeed.base.jdbc.QueryHelper;

import com.gtone.cf.web.services.ISMSClient;

public class SiteSMSClient implements ISMSClient{
	
	/*
	 * SMS를 전송합니다.
	 */
	public void sendMsg(QueryHelper qHelper, List toPhones, String fromPhone,
			String fromName, String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}