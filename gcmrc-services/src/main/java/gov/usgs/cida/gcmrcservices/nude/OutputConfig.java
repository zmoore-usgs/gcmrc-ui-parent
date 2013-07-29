package gov.usgs.cida.gcmrcservices.nude;

import gov.usgs.cida.nude.out.mapping.ColumnToXmlMapping;
import gov.usgs.webservices.framework.basic.MimeType;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class OutputConfig {

	private static final Logger log = LoggerFactory.getLogger(OutputConfig.class);
	protected final String fill;
	protected final ColumnToXmlMapping[] columnMappings;
	protected final boolean isTransparent;
	protected final boolean isDownload;
	protected final MimeType style;

	public OutputConfig(String fill, ColumnToXmlMapping[] columnMappings, boolean isTransparent, boolean isDownload, MimeType style) {
		this.fill = fill;
		this.columnMappings = columnMappings;
		this.isTransparent = isTransparent;
		this.isDownload = isDownload;
		this.style = style;
	}

	public String getFill() {
		String result = this.fill;

		return result;
	}

	public ColumnToXmlMapping[] getColumnMappings() {
		ColumnToXmlMapping[] result = Arrays.copyOf(this.columnMappings, this.columnMappings.length);

		return result;
	}

	public boolean isTransparentMode() {
		boolean result = this.isTransparent;

		return result;
	}

	public MimeType getStyle() {
		MimeType result = this.style;

		return result;
	}

	public String getMimeType() {
		String result;

		switch (this.getStyle()) {
			case JSON:
			case CSV:
			case TAB:
			case EXCEL:
				if (this.isDownload()) {
					result = this.getStyle().getMimeType();
				} else {
					result = MimeType.TEXT.getMimeType();
				}
				break;
			default:
				result = this.getStyle().getMimeType();
				break;
		}

		return result;
	}

	public boolean isDownload() {
		boolean result = this.isDownload;

		return result;
	}
}
