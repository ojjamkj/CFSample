import java.util.HashMap;
import java.util.List;

import com.gtone.express.server.dao.AbstractMCommonDAO;

import jspeed.base.jdbc.CacheResultSet;

public class TestDAO extends AbstractMCommonDAO{

	public TestDAO() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getSQLMapConfigName() throws Exception {
		// TODO Auto-generated method stub
		return "sqlMapConfigTest.xml";
	}

	
	public void insertGtoCrMappingList(List list) throws Exception{
		try {
			
			int size = list.size();
			for(int i=0; i<size; i++){
				this.executeUpdate("CMSP_GTO_deleteGtoCrMappingList",(HashMap)list.get(i));
			}
		
		} catch(Exception e){
			throw e;
		}
	}

	public void startLog(HashMap input) throws Exception
	{
		int row = this.executeUpdate("CMSP_GTO_updateGtoCrMapping",input);
		if(row==0)
		{
			this.executeUpdate("CMSP_GTO_insertGtoCrMapping",input);
		}
	}
	
	public CacheResultSet findSourceSelfQualityLoadListByInstIdNUserId(HashMap param) throws Exception
	{
		String queryKey = "CMSP_GTO_findSourceSelfQualityLoadListByInstIdNUserId";
		
		return this.executeQuery(queryKey, param); 
	}
		
}
