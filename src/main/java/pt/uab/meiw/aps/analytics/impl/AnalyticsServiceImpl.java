package pt.uab.meiw.aps.analytics.impl;

import io.helidon.config.Config;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import pt.uab.meiw.aps.DeserializationException;
import pt.uab.meiw.aps.Serdes;
import pt.uab.meiw.aps.Utils;
import pt.uab.meiw.aps.analytics.AnalyticsContract;
import pt.uab.meiw.aps.analytics.AnalyticsService;
import pt.uab.meiw.aps.git.GitService;

/**
 * The Analytics Service implementation, which loads the analytics contract from
 * the specified file in the configuration, if no configuration or the file
 * cannot be read, empty values are used. The Analytics Service also maintains a
 * pool of Threads for each centralized git repository that collect metrics for
 * each deployed activity.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsServiceImpl implements AnalyticsService {

  private static final String DESER_ERR = "Exception while deserializing configuration parameters";
  private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  private final AnalyticsContract analyticsContract;

  private final GitService gitService;

  public AnalyticsServiceImpl(GitService gitService) {
    this.gitService = gitService;
    final var om = Serdes.INSTANCE.getObjectMapper();
    final var config = Config.global();

    final var analyticsContractPath = config.get("ap").get("analytics")
        .get("contract-path").asString().orElse("");

    final var analyticsContractFile = Utils.readFile(analyticsContractPath);

    if (analyticsContractFile.isEmpty()) {
      analyticsContract = new AnalyticsContract();
    } else {
      try {
        analyticsContract = om.readValue(analyticsContractFile.get(),
            AnalyticsContract.class);
      } catch (IOException e) {
        throw new DeserializationException(DESER_ERR, e);
      }
    }
  }

  @Override
  public AnalyticsContract getAnalyticsContract() {
    return analyticsContract;
  }

  @Override
  public Object getAnalytics() {
    return Collections.emptyList();
  }

  @Override
  public boolean canStartCollection(String repositoryUrl) {
    try {
      return gitService.accepts(repositoryUrl);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Override
  public void startCollection(String repositoryUrl) {

  }
}
