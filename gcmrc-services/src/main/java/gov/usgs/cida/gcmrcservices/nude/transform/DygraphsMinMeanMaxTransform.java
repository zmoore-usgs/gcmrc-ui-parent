package gov.usgs.cida.gcmrcservices.nude.transform;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import gov.usgs.cida.nude.column.Column;
import gov.usgs.cida.nude.filter.transform.WindowedMathTransform;
import gov.usgs.cida.nude.resultset.inmemory.TableRow;

/**
 *
 * @author dmsibley
 */
public class DygraphsMinMeanMaxTransform extends WindowedMathTransform {

	private static final Logger log = LoggerFactory.getLogger(DygraphsMinMeanMaxTransform.class);

	public DygraphsMinMeanMaxTransform(Column time, Column val, Period duration) {
		super(time, val, null, duration);
	}

	@Override
	public String transform(TableRow row) {
		String result = null;
		String val = row.getValue(col);
		String time = row.getValue(timeCol);

		BigDecimal bigVal = null;
		if (null != val) {
			try {
				bigVal = new BigDecimal(val);
			} catch (NumberFormatException e) {
				log.trace("NumberFormatException for " + val);
			}
		}

		boolean fullDuration = sync(new DateTime(Long.parseLong(time)), bigVal, times, vals, duration);
		started = started || fullDuration;

		if (!started) {
			started = checkLogicallyFull(times, duration);
		}

		if (started) {
			result = aggRun(vals);
		}

		return result;
	}

	public static String aggRun(Iterable<BigDecimal> items) {
		StringBuilder result = new StringBuilder();

		AggregateResult aggRes = runAggregate(items);

		if (null != aggRes) {
//			result.append(aggRes.mean.toPlainString())
//				.append(";")
//				.append(aggRes.stdDev.toPlainString());
			result.append(aggRes.min.toPlainString())
				.append(";")
				.append(aggRes.mean.toPlainString())
				.append(";")
				.append(aggRes.max.toPlainString());
		}

		return StringUtils.trimToNull(result.toString());
	}

	protected static AggregateResult runAggregate(Iterable<BigDecimal> items) {
		AggregateResult result = null;

		if (null != items) {
			BigDecimal sum = null;
			int cnt = 0;
			BigDecimal mean = null;
			BigDecimal min = null;
			BigDecimal max = null;

			for (BigDecimal i : items) {
				if (null == sum) {
					sum = BigDecimal.ZERO;
				}
				sum = sum.add(i);
				cnt++;
				min = min(min, i);
				max = max(max, i);
			}

			BigDecimal count = new BigDecimal(cnt);

			if (null != sum && count.compareTo(BigDecimal.ZERO) != 0) {
				mean = sum.divide(count, new MathContext(sum.precision(), RoundingMode.HALF_EVEN));
				BigDecimal stdDev = stdDev(sumDeviation(items, mean), count);

				result = new AggregateResult(count, sum, mean, min, max, stdDev);
			}
		}

		return result;
	}

	protected static class AggregateResult {

		public final BigDecimal count;
		public final BigDecimal sum;
		public final BigDecimal mean;
		public final BigDecimal min;
		public final BigDecimal max;
		public final BigDecimal stdDev;

		public AggregateResult(BigDecimal count, BigDecimal sum, BigDecimal mean, BigDecimal min, BigDecimal max, BigDecimal stdDev) {
			this.count = count;
			this.sum = sum;
			this.mean = mean;
			this.min = min;
			this.max = max;
			this.stdDev = stdDev;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder()
					.append(this.count)
					.append(this.mean)
					.append(this.sum)
					.append(this.min)
					.append(this.max)
					.append(this.stdDev)
					.toHashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof AggregateResult) {
				AggregateResult rhs = (AggregateResult) obj;
				return new EqualsBuilder()
						.append(this.count, rhs.count)
						.append(this.sum, rhs.sum)
						.append(this.mean, rhs.mean)
						.append(this.min, rhs.min)
						.append(this.max, rhs.max)
						.append(this.stdDev, rhs.stdDev)
						.isEquals();
			}
			return false;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(AggregateResult.class)
					.add("count", this.count)
					.add("sum", this.sum)
					.add("mean", this.mean)
					.add("min", this.min)
					.add("max", this.max)
					.add("stdDev", this.stdDev)
					.toString();
		}
	}

	public static BigDecimal min(BigDecimal a, BigDecimal b) {
		BigDecimal result = a;

		if (null == result || (null != b && b.compareTo(result) < 0)) {
			result = b;
		}

		return result;
	}

	public static BigDecimal max(BigDecimal a, BigDecimal b) {
		BigDecimal result = a;

		if (null == result || (null != b && b.compareTo(result) > 0)) {
			result = b;
		}

		return result;
	}
	
	public static BigDecimal sumDeviation(Iterable<BigDecimal> items, BigDecimal mean) {
		BigDecimal sumDeviation = null;
		for (BigDecimal i : items) {
			if (null == sumDeviation) {
				sumDeviation = BigDecimal.ZERO;
			}
			sumDeviation = sumDeviation.add((i.subtract(mean)).pow(2, new MathContext(mean.precision(), RoundingMode.HALF_EVEN)));
		}
		return sumDeviation;
	}

	public static BigDecimal stdDev(BigDecimal sumDeviation, BigDecimal count) {
		BigDecimal result = null;

		if (null != sumDeviation && count.compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal preSR = sumDeviation.divide(count, new MathContext(sumDeviation.precision(), RoundingMode.HALF_EVEN));

			double postSR = Math.sqrt(preSR.doubleValue());

			result = new BigDecimal(Double.toString(postSR), new MathContext(sumDeviation.precision(), RoundingMode.HALF_EVEN));
		}
		return result;
	}
}
