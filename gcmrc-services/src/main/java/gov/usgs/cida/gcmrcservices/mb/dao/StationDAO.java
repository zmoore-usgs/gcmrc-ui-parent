package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.StationBs;
import gov.usgs.cida.gcmrcservices.mb.model.StationCredits;
import gov.usgs.cida.gcmrcservices.mb.model.StationDischargeError;
import gov.usgs.cida.gcmrcservices.mb.model.StationParam;
import gov.usgs.cida.gcmrcservices.mb.model.StationPubs;
import gov.usgs.cida.gcmrcservices.mb.model.StationQW;
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
			result = session.selectList( queryPackage + ".StationMapper.getSites", params);
		}
		
		return result;
	}
	
	public List<StationQW> getSiteQW(String site) {
		List<StationQW> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteQW", params);
		}
		
		return result;
	}
	
	public List<StationPubs> getSitePubs(String site) {
		List<StationPubs> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSitePubs", params);
		}
		
		return result;
	}
	
	public List<StationCredits> getSiteCredits(String site) {
		List<StationCredits> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteCredits", params);
		}
		
		return result;
	}
	
	public List<StationParam> getSiteParams(String site) {
		List<StationParam> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteParams", params);
		}
		
		return result;
	}
	
	public List<StationBs> getSiteBs(String site) {
		List<StationBs> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteBs", params);
		}
		
		return result;
	}
	
	public List<StationDischargeError> getSiteDischargeError(String site) {
		List<StationDischargeError> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", site);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteDischargeError", params);
		}
		
		return result;
	}
}
