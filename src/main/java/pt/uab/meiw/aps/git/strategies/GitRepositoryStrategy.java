package pt.uab.meiw.aps.git.strategies;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * Git Strategies must implement this interface.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface GitRepositoryStrategy {

  /**
   * Returns the repository creation date.
   *
   * @param repositoryUrl the repository URL to get the creation date from.
   * @return the creation date if the repository exits and is public or empty.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  Optional<Instant> getRepositoryCreationDate(String repositoryUrl)
      throws IOException;

  /**
   * Returns a set of file names currently in the main/master branch.
   *
   * @param repositoryUrl the repository URL to get the file names from.
   * @return A Set of file names.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  Set<String> getRepositoryFileNames(String repositoryUrl) throws IOException;

  /**
   * Returns a set of file names currently in the given branch.
   *
   * @param repositoryUrl the repository URL to get the file names from.
   * @param branch        the repository branch to get the file names from.
   * @return A Set of file names.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  Set<String> getRepositoryFileNames(String repositoryUrl, String branch)
      throws IOException;

  /**
   * Returns the commit count for the main/master branch for the given
   * repository.
   *
   * @param repositoryUrl the repository URL to get the commit count from.
   * @return the commit count.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  long getRepositoryCommitCount(String repositoryUrl) throws IOException;

  /**
   * Returns the commit count for the main/master branch for the given
   * repository.
   *
   * @param repositoryUrl the repository URL to get the commit count from.
   * @param branch        the repository branch to get the commit count from.
   * @return the commit count.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  long getRepositoryCommitCount(String repositoryUrl, String branch)
      throws IOException;

  /**
   * Returns the average duration, in seconds, between commits for the
   * main/master branch for the given repository.
   *
   * @param repositoryUrl the repository URL to get average from.
   * @return the average duration, in seconds.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  long getRepositoryAvgDurationBetweenCommits(String repositoryUrl)
      throws IOException;

  /**
   * Returns the average duration, in seconds, between commits for the
   * main/master branch for the given repository.
   *
   * @param repositoryUrl the repository URL to get average from.
   * @param branch        the repository branch to get average from.
   * @return the average duration, in seconds.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  long getRepositoryAvgDurationBetweenCommits(String repositoryUrl,
      String branch) throws IOException;


  /**
   * Returns the file count for the main/master branch for the given
   * repository.
   *
   * @param repositoryUrl the repository URL to get the file count from.
   * @return the file count.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  long getRepositoryFileCount(String repositoryUrl) throws IOException;

  /**
   * Returns the file count for the main/master branch for the given
   * repository.
   *
   * @param repositoryUrl the repository URL to get the file count from.
   * @param branch        the repository branch to get the file count from.
   * @return the file count.
   * @throws IOException when unable to establish connection to the target
   *                     repository.
   */
  long getRepositoryFileCount(String repositoryUrl, String branch)
      throws IOException;
}
