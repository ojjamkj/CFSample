package com.gtone.cf.rt.resources.run;

import java.math.BigDecimal;

import jspeed.base.query.DBAssistant;

public class SiteCheckoutCancelRun extends CheckOutCancelRun {

	public SiteCheckoutCancelRun(BigDecimal instId, BigDecimal taskId,BigDecimal userId, String locale) throws Exception	
	{
		super(instId, taskId, userId, locale);
		
	}
	public SiteCheckoutCancelRun(BigDecimal instId, BigDecimal taskId, BigDecimal userId, String locale, Boolean authCheck) throws Exception {
		super(instId, taskId, userId, locale, authCheck);
		// TODO Auto-generated constructor stub
	}
	
	protected  void onEndBeforeCommit(DBAssistant dbAssistant, String resIds[] ) throws Exception
	{
		super.onEndBeforeCommit(dbAssistant, resIds);
		
		//사이트내용
		
		SiteUtil.makeFileAndSend(this.instId);
	}

}
