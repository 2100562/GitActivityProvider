package pt.uab.meiw.aps.activity;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents the payload of a request from a student/trainee with their Git
 * repository URL. This should originate from the Activity interface, however,
 * direct requests also work, provided that the values are valid.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ProvideRequest {

  private String studentId;
  private String activityId;
  private String gitRepositoryUrl;

  public ProvideRequest() {

  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public String getGitRepositoryUrl() {
    return gitRepositoryUrl;
  }

  public void setGitRepositoryUrl(String gitRepositoryUrl) {
    this.gitRepositoryUrl = gitRepositoryUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProvideRequest that = (ProvideRequest) o;
    return Objects.equals(studentId, that.studentId)
        && Objects.equals(activityId, that.activityId)
        && Objects.equals(gitRepositoryUrl, that.gitRepositoryUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, activityId, gitRepositoryUrl);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ",
        ProvideRequest.class.getSimpleName() + "[", "]")
        .add("studentId='" + studentId + "'")
        .add("activityId='" + activityId + "'")
        .add("gitRepositoryUrl='" + gitRepositoryUrl + "'")
        .toString();
  }
}
