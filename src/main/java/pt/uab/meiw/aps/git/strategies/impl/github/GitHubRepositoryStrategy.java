package pt.uab.meiw.aps.git.strategies.impl.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.http.Status;
import io.helidon.webclient.api.WebClient;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import pt.uab.meiw.aps.Serdes;
import pt.uab.meiw.aps.git.strategies.GitRepositoryStrategy;
import pt.uab.meiw.aps.git.strategies.model.github.GetRepository;

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

  @Override
  public Optional<Instant> getRepositoryCreationDate(String repositoryUrl)
      throws IOException {
    throwIfInvalidUrl(repositoryUrl);

    final var repoDetails = detailsFromUrl(repositoryUrl);
    GetRepository response;

    try {
      final var resp = client
          .get()
          .path("/repos/" + repoDetails.username() + "/" + repoDetails.name())
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
    return Set.of();
  }

  @Override
  public Set<String> getRepositoryFileNames(String repositoryUrl, String branch)
      throws IOException {
    return Set.of();
  }

  @Override
  public long getRepositoryCommitCount(String repositoryUrl)
      throws IOException {
    return 0;
  }

  @Override
  public long getRepositoryCommitCount(String repositoryUrl, String branch)
      throws IOException {
    return 0;
  }

  @Override
  public long getRepositoryAvgDurationBetweenCommits(String repositoryUrl)
      throws IOException {
    return 0;
  }

  @Override
  public long getRepositoryAvgDurationBetweenCommits(String repositoryUrl,
      String branch) throws IOException {
    return 0;
  }

  @Override
  public long getRepositoryFileCount(String repositoryUrl) throws IOException {
    return 0;
  }

  @Override
  public long getRepositoryFileCount(String repositoryUrl, String branch)
      throws IOException {
    return 0;
  }
}
