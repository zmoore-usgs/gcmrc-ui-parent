package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.AncillaryData;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zmoore
 */
public class LookupDAO {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(LookupDAO.class);
	
	private final SqlSessionFactory sqlSessionFactory;

	public LookupDAO() {
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public LookupDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public static final String queryPackage = "gov.usgs.cida.gcmrcservices.mb.mappers";
	
	public List<AncillaryData> getAncilliaryData() {
		List<AncillaryData> result = null;
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList(queryPackage + ".LookupMapper.getAncillaryData");
		}
		
		return result;
	}
}
