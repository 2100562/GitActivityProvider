package pt.uab.meiw.aps.activity.model;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import pt.uab.meiw.aps.activity.DeployRequest;

/**
 * The Activity Instance Model, representing a deployed activity for the given
 * student id.
 *
 * @author HUgo Gonçalves
 * @since 0.0.1
 */
public final class ActivityInstance {

  private String id;
  private String activityName;
  private String activityInstructions;
  private String externalActivityId;
  private String externalStudentId;
  private String repositoryUrl;

  public ActivityInstance() {

  }

  public ActivityInstance(DeployRequest deployRequest) {
    id = UUID.randomUUID().toString();
    activityName = deployRequest.getParams().get("activity_name");
    activityInstructions = deployRequest.getParams().get("activity_name");
    externalActivityId = deployRequest.getActivityId();
    externalStudentId = deployRequest.getStudentId();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getActivityName() {
    return activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  public String getActivityInstructions() {
    return activityInstructions;
  }

  public void setActivityInstructions(String activityInstructions) {
    this.activityInstructions = activityInstructions;
  }

  public String getExternalActivityId() {
    return externalActivityId;
  }

  public void setExternalActivityId(String externalActivityId) {
    this.externalActivityId = externalActivityId;
  }

  public String getExternalStudentId() {
    return externalStudentId;
  }

  public void setExternalStudentId(String externalStudentId) {
    this.externalStudentId = externalStudentId;
  }

  public String getRepositoryUrl() {
    return repositoryUrl;
  }

  public void setRepositoryUrl(String repositoryUrl) {
    this.repositoryUrl = repositoryUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivityInstance that = (ActivityInstance) o;
    return Objects.equals(id, that.id) && Objects.equals(activityName,
        that.activityName) && Objects.equals(activityInstructions,
        that.activityInstructions) && Objects.equals(externalActivityId,
        that.externalActivityId) && Objects.equals(externalStudentId,
        that.externalStudentId) && Objects.equals(repositoryUrl,
        that.repositoryUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, activityName, activityInstructions,
        externalActivityId, externalStudentId, repositoryUrl);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ",
        ActivityInstance.class.getSimpleName() + "[", "]")
        .add("id='" + id + "'")
        .add("activityName='" + activityName + "'")
        .add("activityInstructions='" + activityInstructions + "'")
        .add("externalActivityId='" + externalActivityId + "'")
        .add("externalStudentId='" + externalStudentId + "'")
        .add("repositoryUrl='" + repositoryUrl + "'")
        .toString();
  }
}
