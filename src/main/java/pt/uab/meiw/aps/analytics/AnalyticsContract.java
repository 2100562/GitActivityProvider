package pt.uab.meiw.aps.analytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents the Analytics Contract that this Activity Provider offers.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsContract {

  @JsonProperty("qualAnalytics")
  private List<Metric> qualitativeAnalytics;

  @JsonProperty("quantAnalytics")
  private List<Metric> quantitativeAnalytics;

  public AnalyticsContract() {

  }

  public List<Metric> getQualitativeAnalytics() {
    return qualitativeAnalytics;
  }

  public void setQualitativeAnalytics(
      List<Metric> qualitativeAnalytics) {
    this.qualitativeAnalytics = qualitativeAnalytics;
  }

  public List<Metric> getQuantitativeAnalytics() {
    return quantitativeAnalytics;
  }

  public void setQuantitativeAnalytics(
      List<Metric> quantitativeAnalytics) {
    this.quantitativeAnalytics = quantitativeAnalytics;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsContract that = (AnalyticsContract) o;
    return
        Objects.equals(qualitativeAnalytics, that.qualitativeAnalytics)
            && Objects.equals(quantitativeAnalytics,
            that.quantitativeAnalytics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(qualitativeAnalytics, quantitativeAnalytics);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ",
        AnalyticsContract.class.getSimpleName() + "[", "]")
        .add("qualitativeAnalytics=" + qualitativeAnalytics)
        .add("quantitativeAnalytics=" + quantitativeAnalytics)
        .toString();
  }
}
