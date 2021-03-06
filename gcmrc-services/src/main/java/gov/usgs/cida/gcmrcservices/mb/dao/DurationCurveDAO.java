package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurve;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurveConsecutiveGap;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurveCumulativeGap;
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
	
	public DurationCurve getDurationCurve(String siteName, String startTime, String endTime, int groupId, int binCount, String binType) {		
		DurationCurve result;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siteName", siteName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("binCount", binCount);
		params.put("groupId", groupId);
		params.put("binType", binType);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			List<DurationCurvePoint> returnedPoints = session.selectList( queryPackage + ".DurationCurveMapper.getDurationCurve", params);
			DurationCurveConsecutiveGap consecutiveGap;
			DurationCurveCumulativeGap cumulativeGap;
			//Log claculations will sometimes return an extra bin with the values that are <= 0 so check that points == binCount or binCount + 1
			//Verify returned points are valid
			boolean valid = returnedPoints.size() > 0;
			if(valid){
				for(DurationCurvePoint point : returnedPoints){
					if(point.getCumulativeBinPerc() > 100 || point.getCumulativeBinPerc() < 0){
						valid = false;
						break;
					}
				}
			}
		
			consecutiveGap = getDurationCurveConsecutiveGap(siteName, startTime, endTime, groupId);
			cumulativeGap = getDurationCurveCumulativeGap(siteName, startTime, endTime, groupId);
			
			if(valid){
				result = new DurationCurve(returnedPoints, siteName, groupId, binType, consecutiveGap, cumulativeGap);
			} else {
				log.error("Duration curve query returned invalid data with parameters: [siteName: " + siteName + ", groupId: " + groupId + ", binType: " + binType + "]");
				result = new DurationCurve(null, siteName, groupId, binType, null, null);
			}
		} catch (Exception e) {
			log.error("Could not get duration curve with parameters: [siteName: " + siteName + ", groupId: " + groupId + ", binType: " + binType + "] Error: " + e.getMessage());
			result = new DurationCurve(null, siteName, groupId, binType, null, null);
		}
				
		return result;
	}
	
	public Double getDurationCurveGapMinutesPercent(String siteName, String startTime, String endTime, int groupId) {		
	    Double returned = 0.0;	
	    Double result = 0.0;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siteName", siteName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("groupId", groupId);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			returned = session.selectOne( queryPackage + ".DurationCurveMapper.getDurationCurveGapMinutesPercent", params);
			
			if (returned != null){
			    result = returned;
			}
		} catch (Exception e) {
			log.error("Could not get duration curve gap minutes percent with parameters: [siteName: " + siteName + ", groupId: " + groupId + "] Error: " + e.getMessage());
			result = null;
		}
				
		return result;
	}
	
	public DurationCurveConsecutiveGap getDurationCurveConsecutiveGap(String siteName, String startTime, String endTime, int groupId) {		
	    DurationCurveConsecutiveGap returnedGap;
	    DurationCurveConsecutiveGap result;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siteName", siteName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("groupId", groupId);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			returnedGap = session.selectOne( queryPackage + ".DurationCurveMapper.getDurationCurveConsecutiveGap", params);
			
			result = returnedGap;
			
		} catch (Exception e) {
			log.error("Could not get duration curve consecutive gaps with parameters: [siteName: " + siteName + ", groupId: " + groupId + "] Error: " + e.getMessage());
			result = null;
		}
				
		return result;
	}
	
	public DurationCurveCumulativeGap getDurationCurveCumulativeGap(String siteName, String startTime, String endTime, int groupId) {		
	    DurationCurveCumulativeGap returnedCumulativeGap;
	    DurationCurveCumulativeGap result;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siteName", siteName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("groupId", groupId);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			returnedCumulativeGap = session.selectOne( queryPackage + ".DurationCurveMapper.getDurationCurveCumulativeGap", params);
			
			result = returnedCumulativeGap;
			
		} catch (Exception e) {
			log.error("Could not get duration curve cumulative gaps with parameters: [siteName: " + siteName + ", groupId: " + groupId + "] Error: " + e.getMessage());
			result = null;
		}
				
		return result;
	}

}
