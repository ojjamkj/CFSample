package com.gtone.cf.rt.resources.run;

import java.math.BigDecimal;
import java.util.HashMap;

import jspeed.base.query.DBAssistant;

public class SiteCheckOutToServerRun extends CheckOutToServerRun {

	public SiteCheckOutToServerRun(BigDecimal instId, BigDecimal taskId, BigDecimal userId, String locale)
			throws Exception {
		super(instId, taskId, userId, locale);
		// TODO Auto-generated constructor stub
	}

	protected  void onEndBeforeCommit(DBAssistant dbAssistant, HashMap[] returnHashMap) throws Exception
	{
		super.onEndBeforeCommit(dbAssistant, returnHashMap);
		
		
		SiteUtil.makeFileAndSend(dbAssistant, this.instId);
	}
}
