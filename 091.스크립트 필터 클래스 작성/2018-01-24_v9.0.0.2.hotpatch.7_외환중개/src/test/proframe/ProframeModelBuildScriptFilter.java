package test.proframe;

import java.util.ArrayList;

import jspeed.base.dao.CommonDAO;
import jspeed.base.jdbc.CacheResultSet;

import com.gtone.cf.rt.deploy.IBuildScriptFilter;
import com.gtone.cf.util.DBHelper;

public class ProframeModelBuildScriptFilter implements IBuildScriptFilter{

	public CacheResultSet getFilteredSources(CommonDAO dao, CacheResultSet rscRs)
			throws Exception {
		// TODO Auto-generated method stub
		
		CacheResultSet filteredSourcesCrs = DBHelper.copyOnlyFieldName(rscRs);
		ArrayList distinctKeyList = new ArrayList();
		while(rscRs.next()){
			if(!distinctKeyList.contains(rscRs.getString("RES_PATH"))){
				distinctKeyList.add(rscRs.getString("RES_PATH"));
				filteredSourcesCrs.addRow(rscRs.getObjectArr());
			}
		}
		return filteredSourcesCrs;
	}

}
