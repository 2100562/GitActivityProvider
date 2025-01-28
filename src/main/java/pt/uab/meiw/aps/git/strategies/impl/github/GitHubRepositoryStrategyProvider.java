package pt.uab.meiw.aps.git.strategies.impl.github;

import io.helidon.config.Config;
import io.helidon.webclient.api.WebClient;
import java.util.Objects;
import pt.uab.meiw.aps.git.strategies.GitRepositoryStrategy;
import pt.uab.meiw.aps.git.strategies.GitRepositoryStrategyProvider;

/**
 * The GitHub Strategy Provider.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class GitHubRepositoryStrategyProvider implements
    GitRepositoryStrategyProvider {

  private final Config config = Config.global();

  @Override
  public GitRepositoryStrategy create() {
    final var baseUri = config
        .get("ap")
        .get("git")
        .get("strategies")
        .get("github")
        .get("base-uri")
        .asString()
        .get();

    final var token = config
        .get("ap")
        .get("git")
        .get("strategies")
        .get("github")
        .get("token")
        .asString();

    final var clientBuilder = WebClient
        .builder()
        .baseUri(baseUri)
        .config(config.get("ap.git.strategies.github.client"));

    if (token.isPresent()) {
      clientBuilder.defaultHeadersMap().put("Authorization", token.get());
    }

    return new GitHubRepositoryStrategy(clientBuilder.build());
  }

  @Override
  public boolean accepts(String repositoryUrl) {
    Objects.requireNonNull(repositoryUrl, "repositoryUrl must not be null");

    return repositoryUrl.startsWith("https://github.com/")
        || repositoryUrl.startsWith("https://www.github.com/");
  }
}
