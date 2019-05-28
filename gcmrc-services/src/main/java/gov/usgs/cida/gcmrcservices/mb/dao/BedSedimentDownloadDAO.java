package gov.usgs.cida.gcmrcservices.mb.dao;

import gov.usgs.cida.gcmrcservices.mb.MyBatisConnectionFactory;
import gov.usgs.cida.gcmrcservices.mb.model.BedSedimentDownload;
import gov.usgs.cida.gcmrcservices.mb.model.StationBs;
import gov.usgs.cida.gcmrcservices.mb.model.StationCredits;
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
public class BedSedimentDownloadDAO {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BedSedimentDownloadDAO.class);
	
	private final SqlSessionFactory sqlSessionFactory;

	public BedSedimentDownloadDAO() {
		this.sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public BedSedimentDownloadDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public static final String queryPackage = "gov.usgs.cida.gcmrcservices.mb.mappers";
	
	public List<BedSedimentDownload> getBedSedimentDownloadResult(String siteName, String beginDate, String endDate) {
		List<BedSedimentDownload> result = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("site", siteName);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			result = session.selectList( queryPackage + ".BedSedimentDownloadMapper.getBedSedimentDownloadResult", params);
		}
		
		return result;
	}
}
