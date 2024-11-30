package pt.uab.meiw.aps.git;

import java.io.IOException;
import java.util.Optional;
import pt.uab.meiw.aps.Service;

/**
 * The Git Service provides two methods, one that checks if the repository is
 * accessible and there is a
 * {@link pt.uab.meiw.aps.git.strategies.GitRepositoryStrategy} available in the
 * classpath to fetch the metrics and a method to actually fetch the metrics.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface GitService extends Service {

  /**
   * Tests if the repository is accessible and there is a
   * {@link pt.uab.meiw.aps.git.strategies.GitRepositoryStrategy} available in
   * the classpath to fetch the metrics.
   *
   * @param repositoryUrl the repository URL to test.
   * @return true if accessible, false otherwise.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  boolean accepts(String repositoryUrl) throws IOException;


  /**
   * Fetches the metrics for the given repository.
   *
   * @param repositoryUrl the repository URL to fetch the metrics from.
   * @return the fetched metrics.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  Optional<GitAnalytics> getAnalyticsFor(String repositoryUrl)
      throws IOException;
}
