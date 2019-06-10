package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;

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
	
	public List<Object> getSites(String network, String sites) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("net", network);
		params.put("sites", sites);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSites", params);
		}
		
		return result;
	}
	
	public List<Object> getSiteQW(String site) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteQW", params);
		}
		
		return result;
	}
	
	public List<Object> getSitePubs(String site) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSitePubs", params);
		}
		
		return result;
	}
	
	public List<Object> getSiteCredits(String site) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteCredits", params);
		}
		
		return result;
	}
	
	public List<Object> getSiteParams(String site) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteParams", params);
		}
		
		return result;
	}
	
	public List<Object> getSiteBs(String site) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteBs", params);
		}
		
		return result;
	}
	
	public List<Object> getSiteDischargeError(String site) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteDischargeError", params);
		}
		
		return result;
	}
}
