package site.repository.logical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jspeed.base.dao.CommonDAO;
import jspeed.base.jdbc.CacheResultSet;
import com.gtone.cf.rt.repository.IRepositoryAdaptor;
import com.gtone.cf.util.ICFConstants;
import com.gtone.express.server.dao.MCommonDAO;
import com.gtone.express.server.helper.DBHelper;

public class DevOnRepositoryAdaptor implements IRepositoryAdaptor {

	public String getBaseDir(CommonDAO dao, HashMap inHash) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List getMasterChildSources(CommonDAO dao, String collectId, String physicalCollectId) throws Exception {

		MCommonDAO mdao = new MCommonDAO(dao.getQueryHelper().getConnection());
		ArrayList modelResourceList = new ArrayList();

		HashMap inHash = new HashMap();
		inHash.put("COLLECT_ID", physicalCollectId);
		
		//모델리소스
		HashMap map1 = new HashMap();
		map1.put(ICFConstants.SPATH, "/PROBUILDER/service_module/");
		map1.put(ICFConstants.SNAME, "SAUT10000A");
		
		inHash.put("SNAME", "SAUT10000A");
		map1.put("CHILD_RESOURCE_LIST", getChildResourceIds(mdao, inHash));
		modelResourceList.add(map1);
		
		//모델리소스
		HashMap map2 = new HashMap();
		map2.put(ICFConstants.SPATH, "/PROBUILDER/batch_module/");
		map2.put(ICFConstants.SNAME, "BCOM15002A");
                 
                 inHash.put("SNAME", "BCOM15002A");
		map2.put("CHILD_RESOURCE_LIST", getChildResourceIds(mdao, inHash));
		modelResourceList.add(map2);
		
		//모델리소스
		HashMap map3 = new HashMap();
		map3.put(ICFConstants.SPATH, "/PROBUILDER/module/");
		map3.put(ICFConstants.SNAME, "maut_apvfru_pi");inHash.put("SNAME", "maut_apvfru_pi");
		map3.put("CHILD_RESOURCE_LIST", getChildResourceIds(mdao, inHash));
		modelResourceList.add(map3);
                
                 //모델리소스
		HashMap map4 = new HashMap();
		map4.put(ICFConstants.SPATH, "/PROBUILDER/module/");
		map4.put(ICFConstants.SNAME, "BAUT15002A01_dt"); 
                 inHash.put("SNAME", "BAUT15002A01_dt");
		map4.put("CHILD_RESOURCE_LIST", getChildResourceIds(mdao, inHash));
		modelResourceList.add(map4);
		
		//모델리소스
		HashMap map5 = new HashMap();
		map5.put(ICFConstants.SPATH, "/DBIO/");
		map5.put(ICFConstants.SNAME, "AAUTB1006_PF218");
		inHash.put("SNAME", "AAUTB1006_PF218");
		map5.put("CHILD_RESOURCE_LIST", getChildResourceIds(mdao, inHash));
		modelResourceList.add(map5);
		
		return modelResourceList;
	}
	
         public List getChildResourceIds(MCommonDAO dao, HashMap inHash) throws Exception
	{
		ArrayList list = new ArrayList();
		CacheResultSet  crs = dao.executeQuery("com.gtone.cf.rt.repository.interface.getChildResourceId", inHash);
		while(crs.next())
		{
			list.add(crs.getObject("SRC_ID"));
		}
		
		DBHelper.release(crs);
		
		return list;
	}
      }