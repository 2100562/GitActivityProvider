package pt.uab.meiw.aps.analytics.impl;

import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import pt.uab.meiw.aps.DeserializationException;
import pt.uab.meiw.aps.Serdes;
import pt.uab.meiw.aps.Utils;
import pt.uab.meiw.aps.analytics.AnalyticsContract;
import pt.uab.meiw.aps.analytics.AnalyticsService;
import pt.uab.meiw.aps.analytics.model.ActivityAnalytics;
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

  private final AnalyticsContract analyticsContract;
  private final GitService gitService;
  private final DbClient dbClient;

  public AnalyticsServiceImpl(GitService gitService) {
    this.gitService = gitService;
    final var om = Serdes.INSTANCE.getObjectMapper();
    final var config = Config.global();

    dbClient = DbClient.create(config.get("db"));

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
  public List<ActivityAnalytics> getAnalytics(String activityId) {
    return dbClient
        .execute()
        .createNamedQuery("get-analytics")
        .addParam("externalActivityId", activityId)
        .execute()
        .map(row -> row.as(ActivityAnalytics.class))
        .toList();
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
