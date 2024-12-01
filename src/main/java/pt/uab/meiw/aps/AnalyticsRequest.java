package pt.uab.meiw.aps;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The Inven!RA Get Analytics request.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsRequest {

  @JsonProperty("activityID")
  private String activityId;

  public AnalyticsRequest() {
  }

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsRequest that = (AnalyticsRequest) o;
    return Objects.equals(activityId, that.activityId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(activityId);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ",
        AnalyticsRequest.class.getSimpleName() + "[", "]")
        .add("activityId='" + activityId + "'")
        .toString();
  }
}
