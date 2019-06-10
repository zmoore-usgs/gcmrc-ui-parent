package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.ReachTrib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, kmschoep
 */
public class ReachDAO {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ReachDAO.class);
	
	private final SqlSessionFactory sqlSessionFactory;

	public ReachDAO() {
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public ReachDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public static final String queryPackage = "gov.usgs.cida.gcmrcservices.mb.mappers";
	
	public List<Object> getReaches(String network) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("network", network);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".ReachMapper.getReaches", params);
		}
		
		return result;
	}
	
	public List<Object> getReach(String network, String upstreamStation, String downstreamStation) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("network", network);
		params.put("upstream", upstreamStation);
		params.put("downstream", downstreamStation);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".ReachMapper.getReaches", params);
		}
		
		return result;
	}
	
	public List<Object> getReachDetails(String network, String upstreamStation, String downstreamStation) {
		List<Object> result = new ArrayList<Object>();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("network", network);
		params.put("upstream", upstreamStation);
		params.put("downstream", downstreamStation);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList(queryPackage + ".ReachMapper.getReachDetails", params);
		}
		
		return result;
	}
	
	public List<ReachTrib> getReachTrib(String majorTribSite) {
		List<ReachTrib> result = new ArrayList<ReachTrib>();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("majorTribSite", majorTribSite);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList(queryPackage + ".ReachMapper.getReachTrib", params);
		}
		
		return result;
	}
	
	public List<Object> getReachPOR(String upstreamStation) {
		List<Object> result = new ArrayList<Object>();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("upstreamStation", upstreamStation);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList(queryPackage + ".ReachMapper.getReachPOR", params);
		}
		
		return result;
	}
	
	public List<Object> getSiteCredits(String siteUp, String siteDown) {
		List<Object> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("siteUp", siteUp);
		params.put("siteDown", siteDown);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".StationMapper.getSiteCredits", params);
		}
		
		return result;
	}
}
