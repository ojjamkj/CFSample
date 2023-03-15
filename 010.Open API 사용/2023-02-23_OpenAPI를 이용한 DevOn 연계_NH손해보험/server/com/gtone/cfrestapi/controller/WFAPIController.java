package com.gtone.cfrestapi.controller;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gtone.cf.rt.common.CFCommonDAO;
import com.gtone.cf.rt.resources.run.ResourceBean;
import com.gtone.cf.util.ExceptionHandler;
import com.gtone.cf.util.UserHelper;
import com.gtone.cf.util.WFHelper;
import com.gtone.cfrestapi.service.AttachService;
import com.gtone.cfrestapi.service.WFInterfaceService;
import com.gtone.cfrestapi.vo.RemoteCheckInVO;
import com.gtone.express.server.dao.ExpressMap;
import com.gtone.express.server.dao.MCommonDAO;
import com.gtone.express.server.helper.DBHelper;
import com.gtone.express.util.LocaleHelper;
import com.gtone.wfclient.work.WorkList;

import jspeed.base.jdbc.CacheResultSet;
import jspeed.base.log.LogLevel;
import jspeed.base.log.LogService;
import jspeed.base.log.Logger;
import jspeed.base.util.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import site.run.SiteCheckInNotCRRunForCheckinAPI;


@Controller
@RequestMapping("/v1/wf")
public class WFAPIController extends RestWFCommonController
{
	private static final Logger logger = LogService.getInstance().getLogger();
	
	@Autowired
	private WFInterfaceService wfService;
	@Autowired
	private AttachService attachService;
	
	public WFAPIController()
	{
		super();
	}
	
	/** NH ********************************************************************************************/
	
	/**========================================================================
	 * @usage : 문서목록
	 * ========================================================================*/
	@RequestMapping(value="/doclist", method = RequestMethod.POST)
	public  @ResponseBody ResponseEntity getDocList(HttpServletRequest request, HttpServletResponse response, ModelAndView model, @RequestBody Map<String, Object> paramMap) throws Exception
	{
		logger.println("INFO", "REST Start :: Get DocList >> paramMap : " + paramMap.toString());
		
		HashMap returnMap = new HashMap();
		
		MCommonDAO mdao = null;
		try {
			mdao = new MCommonDAO();
			HashMap param = new HashMap();
			
			CacheResultSet crs =mdao.executeQuery("site_rest_getdoclist", (HashMap) paramMap);
			
			List docList = new ArrayList();
			while(crs.next()) {
				HashMap aMap = (HashMap) crs.getMap();
				aMap.put("INST_ID", aMap.get("INST_ID").toString());
				docList.add(aMap);
			}
			
			WFHelper.convertWFTitle(docList, LocaleHelper.getLangCD(request));
			
			returnMap.put("DOC_LIST", docList);
			returnMap.put("RESULT", "SUCCESS");
			
			logger.println("INFO", "REST End :: Get DocList >> paramMap : " + paramMap.toString());
			return new ResponseEntity(returnMap, HttpStatus.OK);
		} catch(Exception e) {
			ExceptionHandler.handleException(e);
			String message = e.getMessage();
			if(message != null) {
				message = message.replaceAll("\\n", " ");
			}
			returnMap.put("ERRMSG", message);
			returnMap.put("RESULT", "FAIL");
			ExceptionHandler.handleException(e);
			return new ResponseEntity(returnMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			com.gtone.cf.util.DBHelper.release(mdao);
		}
	}
	
	@RequestMapping(value = "/remotecheckin", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE}, consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE, "multipart/form-data"})
	@ResponseBody
	public Map<String, Object> remotecheckin(HttpServletRequest request, HttpServletResponse response, @ModelAttribute RemoteCheckInVO paramVO) throws Exception {
		
		HashMap returnMap = new HashMap();
		try {
			CFCommonDAO dao = null;
			MCommonDAO mdao = null;
			
			try {
				dao = new CFCommonDAO();
				dao.begin();
				
				mdao = new MCommonDAO(dao.getQueryHelper().getConnection());
				
				String paramString = paramVO.getParamString();
				List<CommonsMultipartFile[]> paramFiles = paramVO.getFileList();
				
				JSONObject paramJSON = JSONObject.fromObject(paramString);
				String instId = (String) paramJSON.get("INST_ID");
				String loginId = (String) paramJSON.get("LOGIN_ID");
				String collectName = (String) paramJSON.get("COLLECT_NAME");
				String rootResPath = (String) paramJSON.get("ROOT_RES_PATH");
				
				int fileCount = (Integer) paramJSON.get("FILE_COUNT");
				String[] fileResPaths = new String[fileCount];
				String[] fileFullPaths = new String[fileCount];
				String[] fileNames = new String[fileCount];
				CommonsMultipartFile[] files = new CommonsMultipartFile[fileCount];
				JSONArray paramFileResPathArray = (JSONArray)paramJSON.get("FILE_RES_PATHS");
				JSONArray paramFileFullPathArray = (JSONArray)paramJSON.get("FILE_FULL_PATHS");
				JSONArray paramFileNamesPathArray = (JSONArray)paramJSON.get("FILE_NAMES");
				
				for(int i = 0; i < fileCount; i++) {
					/*
					//httpCore 4.0
					Method getter = paramVO.getClass().getMethod("getFILE_" + (i + 1));
					CommonsMultipartFile aFile = (CommonsMultipartFile) getter.invoke(paramVO);
					if(aFile != null) {
						files[i] = aFile;
						fileResPaths[i] = paramFileResPathArray.getString(i);
						fileFullPaths[i] = paramFileFullPathArray.getString(i);
						fileNames[i] = paramFileNamesPathArray.getString(i);
					}
					*/
					files[i] = paramFiles.get(i)[0];
					fileResPaths[i] = paramFileResPathArray.getString(i);
					fileFullPaths[i] = paramFileFullPathArray.getString(i);
					fileNames[i] = paramFileNamesPathArray.getString(i);
				}
				
				String[] collectIds = new String[fileCount];
				BigDecimal userId = this.getUserIdByLoginId(mdao, loginId);
				
				String collectId = this.getCollectIdByCollectName(mdao, collectName);
				for(int i = 0; i < fileCount; i++) {
					collectIds[i] = collectId;
				}
				
				BigDecimal instId_BigDecimal = null;
				if(instId != null && !"".equals(instId)) {
					instId_BigDecimal = new BigDecimal(instId);
				}
				
				SiteCheckInNotCRRunForCheckinAPI checkinNotCRRun = new SiteCheckInNotCRRunForCheckinAPI(instId_BigDecimal, null, userId, "ko-KR");
				ResourceBean[] beans = checkinNotCRRun.run("N", collectIds, fileNames, fileResPaths, files);
				for(int i = 0; i < beans.length; i++) {
					ResourceBean aBean = beans[i];
					if(aBean.getErrCode() != null) {
						throw new Exception(aBean.getCollectId() + ">" + aBean.getResPath() + aBean.getResName() + " : " + aBean.getMessage());
					}
				}
				
				dao.commit();
			} catch(Exception e) {
				dao.rollback();
				throw e;
			} finally {
				DBHelper.release(dao);
			}
		} catch(Exception e) {
			ExceptionHandler.handleException(e);
			String message = e.getClass().getName();
			if(message != null) {
				message = "SERVER ERROR : " + message.replaceAll("\\n", " ");
			}
			returnMap.put("ERRMSG", message);
			returnMap.put("RESULT", "FAIL");
			ExceptionHandler.handleException(e);
			return returnMap;
		}
		
		returnMap.put("RESULT", "SUCCESS");
		
		// logger.println("INFO", "REST End :: Remote CheckIn >> paramMap : " + paramMap.toString());
		// return new ResponseEntity(returnMap, HttpStatus.OK);
		return returnMap;
	}
	
	private BigDecimal getUserIdByLoginId(MCommonDAO mdao, String loginId) throws Exception {
		BigDecimal userId = null;
		
		HashMap paramMap = new HashMap();
		paramMap.put("LOGIN_ID", loginId);
		
		CacheResultSet crs = mdao.executeQuery("site_rest_getUserByLoginId", paramMap);
		while(crs.next()) {
			HashMap aMap = (HashMap) crs.getMap();
			userId = (BigDecimal) aMap.get("USER_ID");
		}
		
		if(userId == null) {
			throw new Exception("E1:Err no.1 사용자가 등록되어있는지 확인 바랍니다 (LOGIN_ID = " + paramMap.get("LOGIN_ID") + ")");
		}
		
		return userId;
	}
	
	private String getCollectIdByCollectName(MCommonDAO mdao, String collectName) throws Exception {
		String collectId = null;
		
		HashMap paramMap = new HashMap();
		paramMap.put("COLLECT_NAME", collectName);
		
		CacheResultSet crs = mdao.executeQuery("site_rest_getCollectIdByCollectName", paramMap);
		if(crs.getRow() > 1) {
			throw new Exception("E1:Err no.2 리파지토리명이 중복되었습니다 (COLLECT_NAME = " + paramMap.get("COLLECT_NAME") + ")");
		}
		
		while(crs.next()) {
			HashMap aMap = (HashMap) crs.getMap();
			collectId = ((BigDecimal) aMap.get("COLLECT_ID")).toString();
		}
		
		if(collectId == null) {
			throw new Exception("E1:Err no.2 리파지토리가 유효하지 않습니다 (COLLECT_NAME = " + paramMap.get("COLLECT_NAME") + ")");
		}
		
		return collectId;
	}
	
	private HashMap genParamForRemoteCheckIn(HashMap paramMap, boolean isDirectory, ArrayList<File> fileList, int fileListSize) throws Exception {
		String rootResPath = (String)paramMap.get("ROOT_RES_PATH");
		String rootFilePath = (String)paramMap.get("ROOT_FILE_PATH");
		if ("".equals(rootResPath)) {
			rootResPath = "/";
		} else if ('/' != rootResPath.charAt(rootResPath.length() - 1)) {
			rootResPath = String.valueOf(rootResPath) + "/";
		} 
		String[] fileResPaths = new String[fileListSize];
		String[] fileFullPaths = new String[fileListSize];
		String[] fileNames = new String[fileListSize];
		File[] files = new File[fileListSize];
		// ArrayList<byte[]> fileContents = (ArrayList)new ArrayList<byte[]>();
		SimpleDateFormat sf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
		for (int i = 0; i < fileListSize; i++) {
			File aFile = fileList.get(i);
			if (aFile.isFile()) {
				files[i] = aFile;
				fileNames[i] = aFile.getName();
				String fileResPath = aFile.getCanonicalPath().replaceAll("\\\\", "/").replaceAll("//", "/");
				if (isDirectory) {
					fileResPath = fileResPath.replaceFirst(rootFilePath, "");
				} else {
					fileResPath = rootResPath;
				} 
				fileResPath = fileResPath.substring(0, fileResPath.lastIndexOf("/") + 1);
				// fileContents.add(viewFile(aFile, 0));
				String fileFullPath = "";
				if (isDirectory) {
					fileFullPath = String.valueOf(rootResPath) + fileResPath;
					fileResPath = String.valueOf(rootResPath) + fileResPath;
				} else {
					fileFullPath = aFile.getCanonicalPath();
				} 
				fileResPaths[i] = fileResPath.replace("//", "/");
				fileFullPaths[i] = fileFullPath.replace("//", "/");
			} 
			System.out.println(String.valueOf(sf.format(new Date())) + " CheckIn FROM [" + aFile.getCanonicalPath() + "] TO <" + fileResPaths[i] + fileNames[i] + ">");
		} 
		paramMap.put("FILE_RES_PATHS", fileResPaths);
		paramMap.put("FILE_FULL_PATHS", fileFullPaths);
		paramMap.put("FILES", files);
		paramMap.put("FILE_NAMES", fileNames);
		// paramMap.put("FILE_CONTENTS", fileContents);
		paramMap.put("FILE_COUNT", Integer.valueOf(fileNames.length));
		return paramMap;
	}
}
