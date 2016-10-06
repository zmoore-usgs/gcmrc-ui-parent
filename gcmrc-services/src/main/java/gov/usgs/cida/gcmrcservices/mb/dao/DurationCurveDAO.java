package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurvePoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley, zmoore
 */
public class DurationCurveDAO {
	private static final Logger log = LoggerFactory.getLogger(DurationCurveDAO.class);
	
	private final SqlSessionFactory sqlSessionFactory;

	public DurationCurveDAO() {
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public DurationCurveDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public static final String queryPackage = "gov.usgs.cida.gcmrcservices.mb.mappers";
	
	public List<DurationCurvePoint> getDurationCurve(String siteId, String startTime, String endTime, String binCount, String groupId) {		
		List<DurationCurvePoint> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("siteId", siteId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("binCount", binCount);
		params.put("groupId", groupId);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".DurationCurveMapper.getDurationCurve", params);
		}
				
		return result;
	}
}
