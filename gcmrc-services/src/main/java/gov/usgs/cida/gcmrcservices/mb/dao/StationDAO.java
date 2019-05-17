package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.StationSite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kmschoep
 */
public class StationDAO {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(StationDAO.class);
	
	private final SqlSessionFactory sqlSessionFactory;

	public StationDAO() {
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public StationDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public static final String queryPackage = "gov.usgs.cida.gcmrcservices.mb.mappers";
	
	public List<StationSite> getSites(String network, String sites) {
		List<StationSite> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("net", network);
		params.put("sites", sites);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationSiteMapper.getSites", params);
		}
		
		return result;
	}
}
