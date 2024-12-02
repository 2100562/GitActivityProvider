package pt.uab.meiw.aps.analytics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import org.bson.Document;

/**
 * The Activity Analytics per-Student Model.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityAnalytics implements AsDocument {

  @JsonIgnore
  private String id;

  private String inveniraStdID;

  @JsonProperty("qualAnalytics")
  private List<MetricWithValue<?>> qualitativeAnalytics;

  @JsonProperty("quantAnalytics")
  private List<MetricWithValue<?>> quantitativeAnalytics;

  @JsonIgnore
  private String externalActivityId;

  @JsonIgnore
  private Instant updatedAt;

  public ActivityAnalytics() {
    id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    updatedAt = Instant.now();
  }

  public String getInveniraStdID() {
    return inveniraStdID;
  }

  public void setInveniraStdID(String inveniraStdID) {
    this.inveniraStdID = inveniraStdID;
    updatedAt = Instant.now();
  }

  public List<MetricWithValue<?>> getQualitativeAnalytics() {
    return Collections.unmodifiableList(qualitativeAnalytics);
  }

  public void setQualitativeAnalytics(
      List<MetricWithValue<?>> qualitativeAnalytics) {
    this.qualitativeAnalytics = new LinkedList<>(qualitativeAnalytics);
    updatedAt = Instant.now();
  }

  public List<MetricWithValue<?>> getQuantitativeAnalytics() {
    return Collections.unmodifiableList(quantitativeAnalytics);
  }

  public void setQuantitativeAnalytics(
      List<MetricWithValue<?>> quantitativeAnalytics) {
    this.quantitativeAnalytics = new LinkedList<>(quantitativeAnalytics);
    updatedAt = Instant.now();
  }

  public String getExternalActivityId() {
    return externalActivityId;
  }

  public void setExternalActivityId(String externalActivityId) {
    this.externalActivityId = externalActivityId;
    updatedAt = Instant.now();
  }

  @JsonIgnore
  @Override
  public Document getDocument() {
    return new Document("inveniraStdID", inveniraStdID)
        .append("qualAnalytics",
            qualitativeAnalytics.stream().map(MetricWithValue::getDocument)
                .toList())
        .append("quantAnalytics",
            quantitativeAnalytics.stream().map(MetricWithValue::getDocument)
                .toList())
        .append("externalActivityId", externalActivityId)
        .append("updatedAt", updatedAt);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivityAnalytics that = (ActivityAnalytics) o;
    return Objects.equals(inveniraStdID, that.inveniraStdID)
        && Objects.equals(qualitativeAnalytics,
        that.qualitativeAnalytics) && Objects.equals(
        quantitativeAnalytics, that.quantitativeAnalytics)
        && Objects.equals(externalActivityId, that.externalActivityId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inveniraStdID, qualitativeAnalytics,
        quantitativeAnalytics, externalActivityId);
  }


  @Override
  public String toString() {
    return new StringJoiner(", ",
        ActivityAnalytics.class.getSimpleName() + "[", "]")
        .add("inveniraStdID='" + inveniraStdID + "'")
        .add("qualitativeAnalytics=" + qualitativeAnalytics)
        .add("quantitativeAnalytics=" + quantitativeAnalytics)
        .add("externalActivityId='" + externalActivityId + "'")
        .toString();
  }
}
