package pt.uab.meiw.aps.git.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import pt.uab.meiw.aps.git.GitAnalytics;
import pt.uab.meiw.aps.git.GitService;
import pt.uab.meiw.aps.git.strategies.GitRepositoryStrategy;
import pt.uab.meiw.aps.git.strategies.GitRepositoryStrategyProvider;

/**
 * The Git Service implementation.
 *
 * @author Hugo Gonçalves
 * @see GitService
 * @since 0.0.1
 */
public class GitServiceImpl implements GitService {

  private final Map<String, GitRepositoryStrategy> strategyMap = new HashMap<>();

  @Override
  public boolean accepts(String repositoryUrl) throws IOException {
    final var strategyProvider = GitRepositoryStrategyProvider
        .getProviderFor(repositoryUrl);

    if (strategyProvider.isEmpty()) {
      return false;
    }

    final var strategy = strategyProvider.get().create();

    if (strategy.canFetchMetrics(repositoryUrl)) {
      strategyMap.put(repositoryUrl, strategy);
      return true;
    }

    return false;
  }

  @Override
  public Optional<GitAnalytics> getAnalyticsFor(String repositoryUrl)
      throws IOException {
    Objects.requireNonNull(repositoryUrl, "repositoryUrl must not be null");
    var strategy = strategyMap.get(repositoryUrl);

    if (strategy == null && !accepts(repositoryUrl)) {
      return Optional.empty();
    }

    strategy = strategyMap.get(repositoryUrl);

    return Optional.of(GitAnalytics
        .builder()
        .repoCreatedAt(strategy.getRepositoryCreationDate(repositoryUrl).get())
        .repoFileNames(
            new LinkedList<>(strategy.getRepositoryFileNames(repositoryUrl)))
        .commitCount(strategy.getRepositoryCommitCount(repositoryUrl))
        .avgDurationBetweenCommits(
            strategy.getRepositoryAvgDurationBetweenCommits(repositoryUrl))
        .repoFileCount(strategy.getRepositoryFileCount(repositoryUrl))
        .build());
  }
}
