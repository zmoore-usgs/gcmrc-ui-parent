package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.Reach;
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
	private static final Logger log = LoggerFactory.getLogger(ReachDAO.class);
	
	private final SqlSessionFactory sqlSessionFactory;

	public ReachDAO() {
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public ReachDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
//	public Reach getReach(String upstreamStation, String downstreamStation) {
//		Reach result = null;
//		
//		try (SqlSession session = sqlSessionFactory.openSession()) {
//			result = session.<Reach>selectOne("gov.usgs.cida.gcmrcservices.mb.mappers.ReachMapper.getReach");
//		}
//
//		return result;
//	}
	
	public List<Reach> getReaches(String network) {
		List<Reach> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("network", network);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList("gov.usgs.cida.gcmrcservices.mb.mappers.ReachMapper.getReaches", params);
		}
		
		return result;
	}
}
