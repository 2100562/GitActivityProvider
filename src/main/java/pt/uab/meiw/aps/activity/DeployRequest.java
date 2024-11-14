package pt.uab.meiw.aps.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents the Inven!RA platform request to deploy an activity.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class DeployRequest {

  @JsonProperty("activityID")
  private String activityId;

  @JsonProperty("Inven!RAstdID")
  private String studentId;

  @JsonProperty("json_params")
  private Map<String, String> params;

  public DeployRequest() {

  }

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeployRequest that = (DeployRequest) o;
    return Objects.equals(activityId, that.activityId)
        && Objects.equals(studentId, that.studentId)
        && Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activityId, studentId, params);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ",
        DeployRequest.class.getSimpleName() + "[", "]")
        .add("activityId='" + activityId + "'")
        .add("studentId='" + studentId + "'")
        .add("params=" + params)
        .toString();
  }
}
