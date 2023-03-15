package site.run;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.gtone.cf.rt.resources.IResourceManager;
import com.gtone.cf.rt.resources.ResourceDAO;
import com.gtone.cf.rt.resources.run.AddDeleteResource;
import com.gtone.cf.rt.resources.run.CheckInRun;
import com.gtone.cf.rt.resources.run.CheckOutRun;
import com.gtone.cf.rt.resources.run.ResourceBean;
import com.gtone.cf.rt.resources.run.ResourceRunner;
import com.gtone.cf.rt.run.RunClassFactory;
import com.gtone.cf.util.ExceptionHandler;
import com.gtone.cf.util.ValidationHelper;

import jspeed.base.jdbc.CacheResultSet;
import jspeed.base.log.Logger;

public class SiteCheckInNotCRRunForCheckinAPI {
	private Logger resourceLogger = new Logger("cf.resource.log");
	String loggerPreFix = "<Resource>";
	protected BigDecimal instId;
	protected BigDecimal userId;
	protected BigDecimal taskId;
	protected String locale;
	protected HashMap inHash;
	ResourceDAO resourceDAO;
	List oldResList;
	List newResList;
	String isLock;
	CacheResultSet rs;
	IResourceManager resourceMgr;
	protected AddDeleteResource addDeleteResource;
	
	public Logger getLogger() {
		return this.resourceLogger;
	}
	
	public SiteCheckInNotCRRunForCheckinAPI(BigDecimal instId, BigDecimal taskId, BigDecimal userId, String locale) throws Exception {
		this.instId = instId;
		this.taskId = taskId;
		this.userId = userId;
		this.locale = locale;
		ValidationHelper.validNull(userId, "USER ID IS NULL");
	}
	
	public ResourceBean[] run(String isLock, String[] collectIds, String[] resNames, String[] resPaths, CommonsMultipartFile[] files) throws Exception {
		CacheResultSet crset = null;
		this.isLock = isLock;
		try {
			this.resourceDAO = new ResourceDAO();
			this.resourceDAO.begin();
			
			this.addDeleteResource = RunClassFactory.getInstance().getAddDeleteResource();
			this.addDeleteResource.locale = this.locale;
			ResourceRunner runner = ResourceRunner.getInstance();
			
			this.findResId(collectIds, resNames, resPaths, files);
			
			ResourceBean[] beans = new ResourceBean[this.oldResList.size()];
			String[] oldResIds = new String[this.oldResList.size()];
			String[] oldBizIds = new String[this.oldResList.size()];
			
			if(oldResList.size() > 0) {
				
				for(int i = 0; i < this.oldResList.size(); i++) {
					ResourceBean bean = (ResourceBean) this.oldResList.get(i);
					beans[i] = bean;
				}
				
				for(int i = 0; i < beans.length; i++) {
					ResourceBean bean = (ResourceBean) beans[i];
					oldResIds[i] = bean.getResId();
					oldBizIds[i] = bean.getBizId();
				}
				
				runner.mappingCR(this.resourceDAO.getDBAssistant(), this.instId, this.taskId, this.userId, this.locale, oldResIds, oldBizIds);
				
				CheckOutRun checkOutRun = RunClassFactory.getInstance().getCheckOutRun(this.instId, this.taskId, this.userId, this.locale);
				checkOutRun.run(this.resourceDAO, "Y", oldResIds);
				
				Thread.sleep(1000);
				
			}
			
			int beansLength = beans.length;
			int newResListSize = this.newResList.size();
			ResourceBean[] allResBeans = new ResourceBean[beans.length + newResListSize];
			for(int i = 0; i < beans.length ; i++) {
				allResBeans[i] = beans[i];
			}
			for(int i = beansLength; i < (newResListSize + beansLength) ; i++) {
				ResourceBean bean = (ResourceBean) this.newResList.get(i - beansLength);
				allResBeans[i] = bean;
			}
			
			CheckInRun checkInRun = RunClassFactory.getInstance().getCheckInRun(this.instId, this.taskId, this.userId, this.locale);
			checkInRun.run(true, this.resourceDAO, allResBeans);
			
			this.resourceDAO.commit();
			return beans;
		} catch(Exception e) {
			this.resourceDAO.rollback();
			ExceptionHandler.handleException(e);
			//onError(e)
			getLogger().print(2, this.loggerPreFix + "* SiteCheckInNotCRRunForCheckinAPI IS TERMINATED BY FOLLOWING CAUSE=" + e.getMessage());
			
			throw e;
		} finally {
			if(crset != null)
				try {
					crset.close();
				} catch(Exception localException2) {
					//ignore
				}
			this.resourceDAO.close();
		}
	}
	
	public ResourceBean[] run(String isLock, String[] collectIds, String[] resNames, String[] resPaths, ArrayList fileByteArrayList) throws Exception {
		CacheResultSet crset = null;
		this.isLock = isLock;
		try {
			this.resourceDAO = new ResourceDAO();
			this.resourceDAO.begin();
			
			this.addDeleteResource = RunClassFactory.getInstance().getAddDeleteResource();
			this.addDeleteResource.locale = this.locale;
			ResourceRunner runner = ResourceRunner.getInstance();
			
			this.findResId(collectIds, resNames, resPaths, fileByteArrayList);
			
			ResourceBean[] beans = new ResourceBean[this.oldResList.size()];
			String[] oldResIds = new String[this.oldResList.size()];
			String[] oldBizIds = new String[this.oldResList.size()];
			
			if(oldResList.size() > 0) {
				
				for(int i = 0; i < this.oldResList.size(); i++) {
					ResourceBean bean = (ResourceBean) this.oldResList.get(i);
					beans[i] = bean;
				}
			
				for(int i = 0; i < beans.length; i++) {
					ResourceBean bean = (ResourceBean) beans[i];
					oldResIds[i] = bean.getResId();
					oldBizIds[i] = bean.getBizId();
				}
				
				runner.mappingCR(this.resourceDAO.getDBAssistant(), this.instId, this.taskId, this.userId, this.locale, oldResIds, oldBizIds);
				
				CheckOutRun checkOutRun = RunClassFactory.getInstance().getCheckOutRun(this.instId, this.taskId, this.userId, this.locale);
				checkOutRun.run(this.resourceDAO, "Y", oldResIds);
				
				Thread.sleep(1000);
				
			}
			
			int beansLength = beans.length;
			int newResListSize = this.newResList.size();
			ResourceBean[] allResBeans = new ResourceBean[beans.length + newResListSize];
			for(int i = 0; i < beans.length ; i++) {
				allResBeans[i] = beans[i];
			}
			for(int i = beansLength; i < (newResListSize + beansLength) ; i++) {
				ResourceBean bean = (ResourceBean) this.newResList.get(i - beansLength);
				allResBeans[i] = bean;
			}
			
			CheckInRun checkInRun = RunClassFactory.getInstance().getCheckInRun(this.instId, this.taskId, this.userId, this.locale);
			checkInRun.run(true, this.resourceDAO, allResBeans);
			
			this.resourceDAO.commit();
			return beans;
		} catch(Exception e) {
			this.resourceDAO.rollback();
			ExceptionHandler.handleException(e);
			//onError(e)
			getLogger().print(2, this.loggerPreFix + "* SiteCheckInNotCRRunForCheckinAPI IS TERMINATED BY FOLLOWING CAUSE=" + e.getMessage());
			
			throw e;
		} finally {
			if(crset != null)
				try {
					crset.close();
				} catch(Exception localException2) {
					//ignore
				}
			this.resourceDAO.close();
		}
	}
	
	private void findResId(String[] collectIds, String[] resNames, String[] resPaths, CommonsMultipartFile[] files) throws Exception {
		this.oldResList = new ArrayList();
		this.newResList = new ArrayList();
		
		int size = resNames.length;
		
		for(int i = 0; i < size; i++) {
			ResourceBean bean = new ResourceBean();
			
			bean.setResPath(resPaths[i]);
			bean.setResName(resNames[i]);
			bean.setCollectId(collectIds[i]);
			bean.setUserId(this.userId);
			bean.setInstId(this.instId);
			bean.setIsLock(this.isLock);
			bean.setChangeComment("Remote CheckIn");
			
			HashMap map = bean.toHashMap();
			map.put("SPATH", bean.getResPath());
			map.put("SNAME", bean.getResName());
			
			if(files[i] != null) {
				bean.setSource(files[i].getBytes());
			}
			
			CacheResultSet crResId = this.resourceDAO.executeQuery("cf.resource.getSrcId", map);
			if(crResId.next()) {
				bean.setResId(crResId.getString("SRC_ID"));
				bean.setChgType("M");
				this.oldResList.add(bean);
			} else {
				bean.setResId("");
				bean.setChgType("N");
				this.newResList.add(bean);
			}
		}
	}
	
	private void findResId(String[] collectIds, String[] resNames, String[] resPaths, ArrayList fileByteArrayList) throws Exception {
		this.oldResList = new ArrayList();
		this.newResList = new ArrayList();
		
		int size = resNames.length;
		
		for(int i = 0; i < size; i++) {
			ResourceBean bean = new ResourceBean();
			
			bean.setResPath(resPaths[i]);
			bean.setResName(resNames[i]);
			bean.setCollectId(collectIds[i]);
			bean.setUserId(this.userId);
			bean.setInstId(this.instId);
			bean.setIsLock(this.isLock);
			bean.setChangeComment("Remote CheckIn");
			
			HashMap map = bean.toHashMap();
			map.put("SPATH", bean.getResPath());
			map.put("SNAME", bean.getResName());
			
			if(fileByteArrayList != null) {
				ArrayList objByteArr = (ArrayList)fileByteArrayList.get(i);
				int objByteArrSize = objByteArr.size();
				byte[] fileByte = new byte[objByteArrSize];
				for(int j = 0; j < objByteArrSize; j++) {
					fileByte[j] = (byte)((int)objByteArr.get(j));
				}
				bean.setSource(fileByte);
			}
			
			CacheResultSet crResId = this.resourceDAO.executeQuery("cf.resource.getSrcId", map);
			if(crResId.next()) {
				bean.setResId(crResId.getString("SRC_ID"));
				bean.setChgType("M");
				this.oldResList.add(bean);
			} else {
				bean.setResId("");
				bean.setChgType("N");
				this.newResList.add(bean);
			}
		}
	}
}
