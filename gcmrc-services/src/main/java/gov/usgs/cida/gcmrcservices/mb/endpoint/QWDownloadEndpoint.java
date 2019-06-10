package gov.usgs.cida.gcmrcservices.mb.endpoint;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.cida.gcmrcservices.mb.dao.QWDownloadDAO;
import gov.usgs.cida.gcmrcservices.mb.model.QWDownload;
import gov.usgs.cida.gcmrcservices.mb.service.QWDownloadService;

/**
 *
 * @author kmschoep
 */
@Path("qw")
public class QWDownloadEndpoint {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(QWDownloadEndpoint.class);
	
	@Path("download")
	@GET
	@Produces("application/tsv")
	public void getQWDownload(@QueryParam("stationNum") String siteName, @QueryParam("beginPosition") String beginDate, @QueryParam("endPosition") String endDate, @Context HttpServletResponse response) {
		try {
			//Get Water Quality Data
			List<QWDownload> qwDownloadResults = new QWDownloadDAO().getQWDownloadResult(siteName, beginDate, endDate);
						
			//Create output file
			List<QWDownloadService.COLUMNS> outputColumns = Arrays.asList(QWDownloadService.COLUMNS.STATION_NAME, QWDownloadService.COLUMNS.STATION_NUM, QWDownloadService.COLUMNS.START_DT, QWDownloadService.COLUMNS.MEAN_DT, QWDownloadService.COLUMNS.END_DT, QWDownloadService.COLUMNS.USGS_DATA_LEAD, QWDownloadService.COLUMNS.SAMPLE_METHOD, QWDownloadService.COLUMNS.SAMPLE_LOCATION, QWDownloadService.COLUMNS.SAMPLER_NAME, QWDownloadService.COLUMNS.NOZZLE, QWDownloadService.COLUMNS.NUM_VERTICALS, QWDownloadService.COLUMNS.TRANSITS_EACH_VERTICAL, QWDownloadService.COLUMNS.CABLEWAY_STATION_LOC, QWDownloadService.COLUMNS.WATER_DEPTH, QWDownloadService.COLUMNS.ELEVATION_ABOVE_BED, QWDownloadService.COLUMNS.SAMPLING_DURATION, QWDownloadService.COLUMNS.PUMP_SAMPLER, QWDownloadService.COLUMNS.PUMP_CAROUSEL_NUM, QWDownloadService.COLUMNS.DATASET_COMPLETE, QWDownloadService.COLUMNS.CROSS_SECT_CALIB_REQ, QWDownloadService.COLUMNS.USE_FOR_LOAD_CALC, QWDownloadService.COLUMNS.NOTES, QWDownloadService.COLUMNS.AIR_TEMP, QWDownloadService.COLUMNS.WATER_TEMP, QWDownloadService.COLUMNS.SPEC_COND, QWDownloadService.COLUMNS.TDS, QWDownloadService.COLUMNS.SILT_CLAY_COLOR, QWDownloadService.COLUMNS.CONC_LABORATORY, QWDownloadService.COLUMNS.GRAIN_SIZE_LABORATORY, QWDownloadService.COLUMNS.LAB_NOTES, QWDownloadService.COLUMNS.LAB_METHOD, QWDownloadService.COLUMNS.SED_CONC_XS, QWDownloadService.COLUMNS.SILT_CLAY_CONC_XS, QWDownloadService.COLUMNS.SAND_CONC_XS, QWDownloadService.COLUMNS.SAND_D16_XS, QWDownloadService.COLUMNS.SAND_D50_XS, QWDownloadService.COLUMNS.SAND_D84_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_074_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_088_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_105_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_125_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_149_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_177_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_210_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_250_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_297_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_354_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_420_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_500_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_595_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_707_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_841_XS, QWDownloadService.COLUMNS.SAND_PCT_LT_1000_XS, QWDownloadService.COLUMNS.SEDIMENT_MASS, QWDownloadService.COLUMNS.SAMPLE_MASS, QWDownloadService.COLUMNS.MASS_LE_63, QWDownloadService.COLUMNS.MASS_GT_63, QWDownloadService.COLUMNS.CONC_LE_63, QWDownloadService.COLUMNS.CONC_GT_63, QWDownloadService.COLUMNS.SED_CONC_LAB, QWDownloadService.COLUMNS.SILT_CLAY_CONC_LAB, QWDownloadService.COLUMNS.SAND_CONC_LAB, QWDownloadService.COLUMNS.SAND_D16_LAB, QWDownloadService.COLUMNS.SAND_D50_LAB, QWDownloadService.COLUMNS.SAND_D84_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_074_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_088_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_105_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_125_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_149_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_177_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_210_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_250_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_297_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_354_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_420_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_500_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_595_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_707_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_841_LAB, QWDownloadService.COLUMNS.SAND_PCT_LT_1000_LAB, QWDownloadService.COLUMNS.SILT_CLAY_FIELD_95ER, QWDownloadService.COLUMNS.SAND_FIELD_95ER, QWDownloadService.COLUMNS.SAND_D50_FIELD_95ER, QWDownloadService.COLUMNS.SILT_CLAY_LAB_BIAS_COR, QWDownloadService.COLUMNS.SILT_CLAY_LAB_95ER, QWDownloadService.COLUMNS.SAND_LAB_95ER, QWDownloadService.COLUMNS.SAND_D50_LAB_95ER, QWDownloadService.COLUMNS.SED_TOT_95ER, QWDownloadService.COLUMNS.SILT_CLAY_TOT_95ER, QWDownloadService.COLUMNS.SAND_TOT_95ER, QWDownloadService.COLUMNS.SAND_D50_TOT_95ER);
			String result = QWDownloadService.getTSVForQWDownload(qwDownloadResults, outputColumns);
			
			// Write the header line
			response.addHeader("Content-Disposition", "attachment; filename=GCMRC.tsv");
			response.addHeader("Pragma", "public");
			response.addHeader("Cache-Control", "max-age=0");
			response.setContentType("application/ms-excel"); 
			OutputStream out = response.getOutputStream();
			out.write(result.getBytes());
			out.flush();
		} catch (Exception e) {
			log.error("Could not download water quality data. Error: ", e);
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Could not download water quality data. Error: " + e.getMessage()).build());
		}
	}

}
