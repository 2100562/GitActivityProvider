package pt.uab.meiw.aps.git.strategies.impl.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.http.Status;
import io.helidon.webclient.api.WebClient;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import pt.uab.meiw.aps.Serdes;
import pt.uab.meiw.aps.git.strategies.GitRepositoryStrategy;
import pt.uab.meiw.aps.git.strategies.model.github.Commit;
import pt.uab.meiw.aps.git.strategies.model.github.GetRepository;
import pt.uab.meiw.aps.git.strategies.model.github.GetTree;
import pt.uab.meiw.aps.git.strategies.model.github.Tree;

/**
 * The GitHub Strategy.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public class GitHubRepositoryStrategy implements GitRepositoryStrategy {

  private final ObjectMapper mapper = Serdes.INSTANCE.getObjectMapper();
  private final Pattern urlPattern = Pattern.compile(
      "^https://(www\\.)?github\\.com/\\w+/\\w+$");

  private final WebClient client;

  /**
   * Create a new instance that uses the given WebClient. The WebClient must
   * have the baseUri correctly set.
   *
   * @param client the WebClient to use.
   */
  public GitHubRepositoryStrategy(WebClient client) {
    this.client = client;
  }

  private void throwIfInvalidUrl(String url) {
    Objects.requireNonNull(url, "repositoryUrl must not be null");

    if (!urlPattern.matcher(url).matches()) {
      throw new IllegalArgumentException("Invalid repository url: " + url);
    }
  }

  private record RepositoryDetails(String username, String name) {

  }

  private RepositoryDetails detailsFromUrl(String url) {
    throwIfInvalidUrl(url);
    final var parts = url.split("/");

    if (parts.length != 5) {
      throw new IllegalArgumentException("Invalid repository url: " + url);
    }

    return new RepositoryDetails(parts[3], parts[4]);
  }

  private String repoApiUrl(RepositoryDetails details) {
    final var sb = new StringBuilder();

    return sb
        .append("/repos/")
        .append(details.username)
        .append("/")
        .append(details.name)
        .toString();
  }

  private String treeApiUrl(RepositoryDetails details, String branch) {
    final var sb = new StringBuilder();

    return sb
        .append("/repos/")
        .append(details.username)
        .append("/")
        .append(details.name)
        .append("/git/trees/")
        .append(branch)
        .toString();
  }

  private String commitsApiUrl(RepositoryDetails details) {
    final var sb = new StringBuilder();

    return sb
        .append("/repos/")
        .append(details.username)
        .append("/")
        .append(details.name)
        .append("/commits")
        .toString();
  }

  private List<Commit> getCommits(String repositoryUrl) throws IOException {
    final var repoDetails = detailsFromUrl(repositoryUrl);
    try {
      final var resp = client
          .get()
          .path(commitsApiUrl(repoDetails))
          .request(String.class);

      if (!resp.status().equals(Status.OK_200)) {
        return List.of();
      }

      final var type = new TypeReference<List<Commit>>() {
      };
      return mapper.readValue(resp.entity(), type);
    } catch (Exception e) {
      throw new IOException(e.getMessage(), e);
    }
  }

  @SuppressWarnings("unchecked")
  private Instant getCommitDate(Commit commit) {
    return Instant.parse(
        ((Map<String, Map<String, String>>) commit.getAdditionalProperties()
            .get("commit")).get("author").get(
            "date"));
  }

  @Override
  public Optional<Instant> getRepositoryCreationDate(String repositoryUrl)
      throws IOException {
    final var repoDetails = detailsFromUrl(repositoryUrl);
    GetRepository response;

    try {
      final var resp = client.get()
          .path(repoApiUrl(repoDetails))
          .request(String.class);

      if (!resp.status().equals(Status.OK_200)) {
        return Optional.empty();
      }

      response = mapper.readValue(resp.entity(), GetRepository.class);
    } catch (Exception e) {
      throw new IOException(e.getMessage(), e);
    }

    return Optional.of(response.getCreatedAt().toInstant());
  }

  @Override
  public Set<String> getRepositoryFileNames(String repositoryUrl)
      throws IOException {

    final var mainFiles = getRepositoryFileNames(repositoryUrl, "main");

    if (!mainFiles.isEmpty()) {
      return mainFiles;
    }

    return getRepositoryFileNames(repositoryUrl, "master");
  }

  @Override
  public Set<String> getRepositoryFileNames(String repositoryUrl, String branch)
      throws IOException {
    final var repoDetails = detailsFromUrl(repositoryUrl);
    GetTree response;

    try {
      final var resp = client
          .get()
          .path(treeApiUrl(repoDetails, branch))
          .queryParam("recursive", "1")
          .request(String.class);

      if (!resp.status().equals(Status.OK_200)) {
        return Set.of();
      }

      response = mapper.readValue(resp.entity(), GetTree.class);
    } catch (Exception e) {
      throw new IOException(e.getMessage(), e);
    }

    return response
        .getTree()
        .stream()
        .map(Tree::getPath)
        .collect(Collectors.toSet());
  }

  @Override
  public long getRepositoryCommitCount(String repositoryUrl)
      throws IOException {
    return getCommits(repositoryUrl).size();
  }

  @Override
  public long getRepositoryAvgDurationBetweenCommits(String repositoryUrl)
      throws IOException {
    final var commitDates = getCommits(repositoryUrl)
        .stream()
        .map(this::getCommitDate)
        .sorted()
        .toList();

    var seconds = 0L;
    for (int i = 1; i < commitDates.size(); i++) {
      final var duration = Duration.between(commitDates.get(i - 1),
          commitDates.get(i));
      seconds += duration.getSeconds();
    }

    int count = commitDates.size() - 1;
    return count > 0 ? seconds / count : 0;
  }

  @Override
  public long getRepositoryFileCount(String repositoryUrl) throws IOException {
    final var mainFiles = getRepositoryFileNames(repositoryUrl, "main");

    if (!mainFiles.isEmpty()) {
      return mainFiles.size();
    }

    return getRepositoryFileNames(repositoryUrl, "master").size();
  }

  @Override
  public long getRepositoryFileCount(String repositoryUrl, String branch)
      throws IOException {
    return getRepositoryFileNames(repositoryUrl, branch).size();
  }
}
