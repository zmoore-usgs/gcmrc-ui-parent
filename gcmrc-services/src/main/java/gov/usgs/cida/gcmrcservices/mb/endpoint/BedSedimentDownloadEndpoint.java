package gov.usgs.cida.gcmrcservices.mb.endpoint;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
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

import org.glassfish.jersey.server.JSONP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.cida.gcmrcservices.mb.dao.BedSedimentDownloadDAO;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.FailureEnvelope;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.GCMRCResponse;
import gov.usgs.cida.gcmrcservices.mb.endpoint.response.SuccessEnvelope;
import gov.usgs.cida.gcmrcservices.mb.model.BedSedimentDownload;
import gov.usgs.cida.gcmrcservices.mb.model.DurationCurve;
import gov.usgs.cida.gcmrcservices.mb.service.BedSedimentDownloadService;
import gov.usgs.cida.gcmrcservices.mb.service.DurationCurveService;

/**
 *
 * @author kmschoep
 */
@Path("bedsediment")
public class BedSedimentDownloadEndpoint {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BedSedimentDownloadEndpoint.class);
	
	@Path("download")
	@GET
	@Produces("application/tsv")
	public void getBedSedimentDownload(@QueryParam("stationNum") String siteName, @QueryParam("beginPosition") String beginDate, @QueryParam("endPosition") String endDate, @Context HttpServletResponse response) {
		try {
			//Get Bed Sediment Data
			List<BedSedimentDownload> bedSedimentDownloadResults = new BedSedimentDownloadDAO().getBedSedimentDownloadResult(siteName, beginDate, endDate);
						
			//Create output file
			List<BedSedimentDownloadService.COLUMNS> outputColumns = Arrays.asList(BedSedimentDownloadService.COLUMNS.STATION_NAME, BedSedimentDownloadService.COLUMNS.STATION_NUM, BedSedimentDownloadService.COLUMNS.BED_DT, BedSedimentDownloadService.COLUMNS.BED_TM, BedSedimentDownloadService.COLUMNS.SAMPLE_SET, BedSedimentDownloadService.COLUMNS.NOTES, BedSedimentDownloadService.COLUMNS.STATION_LOCATION, BedSedimentDownloadService.COLUMNS.SAMPLE_WEIGHT, BedSedimentDownloadService.COLUMNS.SAND_D50, BedSedimentDownloadService.COLUMNS.FINES_D50, BedSedimentDownloadService.COLUMNS.TOTAL_D50, BedSedimentDownloadService.COLUMNS.PCT_BTWN_063_125, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_037, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_044, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_053, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_063, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_074, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_088, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_105, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_125, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_149, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_177, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_210, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_250, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_297, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_354, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_420, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_500, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_595, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_707, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_841, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_1_00, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_1_41, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_2_00, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_2_80, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_4_00, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_5_60, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_8_00, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_11_3, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_16_0, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_22_6, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_32_0, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_45_0, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_64_0, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_91_0, BedSedimentDownloadService.COLUMNS.SIZE_DIST_LT_128_0);
			String result = BedSedimentDownloadService.getTSVForBedSedimentDownload(bedSedimentDownloadResults, outputColumns);
			
			// Write the header line
			response.addHeader("Content-Disposition", "attachment; filename=GCMRC.tsv");
			response.addHeader("Pragma", "public");
			response.addHeader("Cache-Control", "max-age=0");
			response.setContentType("application/ms-excel"); 
			OutputStream out = response.getOutputStream();
			out.write(result.getBytes());
			out.flush();
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain").entity("Could not download bed sediment data. Error: " + e.getMessage()).build());
		}
	}

}
