package com.gtone.cfclient.build.param;

/*
 * @(#)JavaParam.java
 *
 * Copyright (c) 1998-2008 GTOne Co., Ltd. All Rights Reserved.
 */

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.gtone.cf.rt.build.BuildDAO;
import com.gtone.cf.rt.deploy.IDeployHook;
import com.gtone.cf.rt.deploy.IDynaParam;
import com.gtone.cf.util.ICFConstants;
import com.gtone.express.server.helper.DBHelper;

import jspeed.base.dao.CommonDAO;
import jspeed.base.jdbc.CacheResultSet;
import jspeed.base.query.QueryService;
import jspeed.base.util.StringHelper;



/**
 * JavaParam.java
 * JAVA 파라메터 자동생성
 * 
 * @since	2009. 12. 14
 * @version	1.0 초기 작성
 * @version 1.1 모든 리소스의 빌드 파라메터 메소드 추가 [2010.07.29]
 * @author	ashyaris (hyun sik Kim)
 * 
 * 
 * {DEV_VERSION_ID=1, RETURN_VALUE=BUILD SUCCESSFUL, VERSION_ID=1, CONNECT_ID=102, SYS_TYPE=R, COL7=null, COL6=null, COL5=null, COL4=null, COL3=null, FTP_ID=null, COL2=null, COL1=null, TARGET_PORT=30502, 
RES_ID=323144, COL9=null, COL8=null, INST_ID=118, COMMON_NAME=2, BUILD_CALL_SEQ=2, LOG_PARSE_CLASS=null, BUILD_OUTPUT=null, BUILD_TYPE=S, RES_PATH=/listeners/, BIZ_LOC_ID=148, 
BUILD_ORDER=1, BUILD_EACH_RES=Y, ALL_SCRIPT_ID=1, FTP_PASSWORD=null, BUILD_LOC=D:/50_INSTALL/SampleBiz/real/build.bat, TARGET_PATH=D:/50_INSTALL/SampleBiz/real/java, CONNECT_TYPE=A, 
DEPLOY_UNIT_ID=20120711094423, BUILD_ID=51, IS_BEFORE=N, MACHINE_TYPE=S, TARGET_IP=127.0.0.1, IS_SELECT=Y, BUILD_OPTION=null, COLLECT_ID=20120524142810, DEPLOY_ID=129, 
START_DT=20130213172526, JOB_GROUP=COMPILE, 


ALL_RESOURCE_RS=[DEPLOY_ID][TARGET_PATH][DEPLOY_ID][VERSION_ID][DEV_VERSION_ID][CHG_TYPE][RES_ID][RES_NAME][ORG_RES_PATH][RES_PATH][IS_RELATIVE][MACHINE_TYPE][COMPILE_ORDER][PARALLEL_YN][SERVER_EXPORT_YN][EXPORT_TYPE][PASS_DEPLOY_ERR][COLLECT_ID][BIZ_LOC_ID][MAJORITY][USE_DB_TYPE][COL1][COL2][COL3][COL4][COL5][COL6][COL7][COL8][COL9][COL10]
129, D:/50_INSTALL/SampleBiz/real/java, 129, 1, 1, 11, 323144, SessionListener.java, /src/listeners/, /listeners/, null, S, 1, Y, N, B, N, 20120524142810, 148, null, null, null, null, null, null, null, null, null, null, null, null, 
129, D:/50_INSTALL/SampleBiz/real/java, 129, 2, 1, 21, 323145, HTMLFilter.java, /src/util/, /util/, null, S, 1, Y, N, B, N, 20120524142810, 148, null, null, null, null, null, null, null, null, null, null, null, null, 
, RES_NAME=SessionListener.java, RUN_START_DT=20130213172526987, USER_ID=0, SUCCESS_YN_TYPE=O, COMPILE_ORDER=1, COL10=null, SCRIPT_EXE_KUBUN=BUILD, IS_ROLLBACK=null, PASS_DEPLOY_ERR=N}
 */

public class JavaParam implements IDynaParam {

	public String getParam(CommonDAO dao, HashMap param) throws Exception {
//		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		 String result =  ((String)param.get("RES_PATH")).substring(1) + param.get("RES_NAME");
//		 if(isWindows)
//		 {
//			 return StringHelper.replaceStr(result, "/", "\\");
//		 }
//		 else
//		 {
			 return result;
//		 }
	}
	
	 
}