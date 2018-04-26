package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.Reach;
import gov.usgs.cida.gcmrcservices.mb.model.ReachDetail;
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
 * @author dmsibley
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
	
	public List<Reach> getReaches(String network) {
		List<Reach> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("network", network);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".ReachMapper.getReaches", params);
		}
		
		return result;
	}
	
	public List<Reach> getReach(String network, String upstreamStation, String downstreamStation) {
		List<Reach> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("network", network);
		params.put("upstream", upstreamStation);
		params.put("downstream", downstreamStation);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".ReachMapper.getReaches", params);
		}
		
		return result;
	}
	
	public List<ReachDetail> getReachDetails(String network, String upstreamStation, String downstreamStation) {
		List<ReachDetail> result = new ArrayList<ReachDetail>();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("network", network);
		params.put("upstream", upstreamStation);
		params.put("downstream", downstreamStation);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList(queryPackage + ".ReachDetailMapper.getReachDetails", params);
		}
		
		return result;
	}
}
