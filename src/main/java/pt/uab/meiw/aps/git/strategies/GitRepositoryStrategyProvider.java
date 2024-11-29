package pt.uab.meiw.aps.git.strategies;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * SPI for Git Strategies. All Strategies must provide an implementation of this
 * interface. A file named
 * pt.uab.meiw.aps.git.strategies.GitRepositoryStrategyProvider must exist in
 * the classpath containing all available implementation class names, one per
 * line.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface GitRepositoryStrategyProvider {

  /**
   * Returns and instance of a GitRepositoryStrategyProvider for the given URL,
   * if any.
   *
   * @param repositoryUrl the repository URL.
   * @return the provider instance of null.
   */
  static GitRepositoryStrategyProvider getProviderFor(String repositoryUrl) {
    final var serviceLoader = ServiceLoader.load(
        GitRepositoryStrategyProvider.class);

    for (final var provider : serviceLoader) {
      if (provider.accepts(repositoryUrl)) {
        return provider;
      }
    }

    return null;
  }

  /**
   * Creates a new Strategy instance.
   *
   * @return the created Strategy instance.
   */
  GitRepositoryStrategy create();

  /**
   * Tests if this Strategy supports the given centralized repository.
   *
   * @param repositoryUrl the centralized repository URL.
   * @return true if the given centralized repository is supported, false
   * otherwise.
   */
  boolean accepts(String repositoryUrl);
}
