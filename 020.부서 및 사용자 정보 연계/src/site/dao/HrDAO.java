package site.dao;

import java.sql.Connection;

import com.gtone.express.server.dao.AbstractMCommonDAO;

public class HrDAO extends AbstractMCommonDAO {
	
	public HrDAO() throws Exception {
		super();
	}
	
	public HrDAO(Connection conn) throws Exception {
		super(conn);
	}

	@Override
	public String getSQLMapConfigName() throws Exception {
		return "sqlMapConfigHr.xml";
	}
}
