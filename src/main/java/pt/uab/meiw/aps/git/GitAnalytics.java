package pt.uab.meiw.aps.git;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The Git Analytics Model.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class GitAnalytics {

  private Instant repoCreatedAt;
  private List<String> repoFileNames;
  private long commitCount;
  private long avgDurationBetweenCommits;
  private long repoFileCount;

  private GitAnalytics(Builder builder) {
    this.repoCreatedAt = builder.repoCreatedAt;
    this.repoFileNames = builder.repoFileNames;
    this.commitCount = builder.commitCount;
    this.avgDurationBetweenCommits = builder.avgDurationBetweenCommits;
    this.repoFileCount = builder.repoFileCount;
  }

  public GitAnalytics() {

  }

  public static Builder builder() {
    return new Builder();
  }

  public Instant getRepoCreatedAt() {
    return repoCreatedAt;
  }

  public void setRepoCreatedAt(Instant repoCreatedAt) {
    this.repoCreatedAt = repoCreatedAt;
  }

  public List<String> getRepoFileNames() {
    return repoFileNames;
  }

  public void setRepoFileNames(List<String> repoFileNames) {
    this.repoFileNames = repoFileNames;
  }

  public long getCommitCount() {
    return commitCount;
  }

  public void setCommitCount(long commitCount) {
    this.commitCount = commitCount;
  }

  public long getAvgDurationBetweenCommits() {
    return avgDurationBetweenCommits;
  }

  public void setAvgDurationBetweenCommits(long avgDurationBetweenCommits) {
    this.avgDurationBetweenCommits = avgDurationBetweenCommits;
  }

  public long getRepoFileCount() {
    return repoFileCount;
  }

  public void setRepoFileCount(long repoFileCount) {
    this.repoFileCount = repoFileCount;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GitAnalytics that = (GitAnalytics) o;
    return commitCount == that.commitCount
        && avgDurationBetweenCommits == that.avgDurationBetweenCommits
        && repoFileCount == that.repoFileCount && Objects.equals(
        repoCreatedAt, that.repoCreatedAt) && Objects.equals(
        repoFileNames, that.repoFileNames);
  }

  @Override
  public int hashCode() {
    return Objects.hash(repoCreatedAt, repoFileNames, commitCount,
        avgDurationBetweenCommits, repoFileCount);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ",
        GitAnalytics.class.getSimpleName() + "[", "]")
        .add("repoCreatedAt=" + repoCreatedAt)
        .add("repoFileNames=" + repoFileNames)
        .add("commitCount=" + commitCount)
        .add("avgDurationBetweenCommits=" + avgDurationBetweenCommits)
        .add("repoFileCount=" + repoFileCount)
        .toString();
  }

  public static final class Builder {

    private Instant repoCreatedAt;
    private List<String> repoFileNames;
    private long commitCount;
    private long avgDurationBetweenCommits;
    private long repoFileCount;

    public Builder repoCreatedAt(Instant repoCreatedAt) {
      this.repoCreatedAt = repoCreatedAt;
      return this;
    }

    public Builder repoFileNames(List<String> repoFileNames) {
      this.repoFileNames = repoFileNames;
      return this;
    }

    public Builder commitCount(long commitCount) {
      this.commitCount = commitCount;
      return this;
    }

    public Builder avgDurationBetweenCommits(long avgDurationBetweenCommits) {
      this.avgDurationBetweenCommits = avgDurationBetweenCommits;
      return this;
    }

    public Builder repoFileCount(long repoFileCount) {
      this.repoFileCount = repoFileCount;
      return this;
    }

    public GitAnalytics build() {
      return new GitAnalytics(this);
    }
  }
}
