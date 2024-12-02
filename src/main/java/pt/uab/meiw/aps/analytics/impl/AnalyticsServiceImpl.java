package pt.uab.meiw.aps.analytics.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.uab.meiw.aps.DeserializationException;
import pt.uab.meiw.aps.Serdes;
import pt.uab.meiw.aps.Utils;
import pt.uab.meiw.aps.activity.model.ActivityInstance;
import pt.uab.meiw.aps.analytics.AnalyticsContract;
import pt.uab.meiw.aps.analytics.AnalyticsService;
import pt.uab.meiw.aps.analytics.model.ActivityAnalytics;
import pt.uab.meiw.aps.analytics.model.MetricWithValue;
import pt.uab.meiw.aps.git.GitAnalytics;
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

  private static final Logger LOG = LoggerFactory.getLogger(
      AnalyticsServiceImpl.class);

  private static final String DESER_ERR = "Exception while deserializing configuration parameters";
  private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  private final AnalyticsContract analyticsContract;
  private final Duration delayBetweenCol;
  private final GitService gitService;
  private final DbClient dbClient;
  private final MongoCollection<Document> analyticsCollection;

  public AnalyticsServiceImpl(GitService gitService) {
    this.gitService = gitService;
    final var om = Serdes.INSTANCE.getObjectMapper();
    final var config = Config.global();

    dbClient = DbClient.create(config.get("db"));

    analyticsCollection = dbClient
        .unwrap(MongoClient.class)
        .getDatabase(dbClient.unwrap(MongoDatabase.class).getName())
        .getCollection(config.get("db.col").asString().get());

    delayBetweenCol = Duration.parse(
        config.get("ap.analytics.collection.schedule").asString()
            .orElse("PT5M"));

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
  public void startCollection(ActivityInstance activityInstance) {
    scheduler.scheduleAtFixedRate(() -> executor.submit(() -> {
      Optional<GitAnalytics> analyticsO;
      try {
        analyticsO = gitService.getAnalyticsFor(
            activityInstance.getRepositoryUrl());
      } catch (IOException e) {
        return;
      }

      try {
        if (analyticsO.isPresent()) {
          final var filter = Filters.eq("inveniraStdID",
              activityInstance.getExternalStudentId());

          var analytics = analyticsO.get();

          var newAnalytics = new ActivityAnalytics();
          newAnalytics.setInveniraStdID(
              activityInstance.getExternalStudentId());
          newAnalytics.setQualitativeAnalytics(
              getQualAnalytics(analytics));
          newAnalytics.setQuantitativeAnalytics(
              getQuantAnalytics(analytics));
          newAnalytics.setExternalActivityId(
              activityInstance.getExternalActivityId());

          analyticsCollection
              .replaceOne(filter, newAnalytics.getDocument(),
                  new ReplaceOptions().upsert(true));
        }
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
      }
    }), 0, delayBetweenCol.toMillis(), TimeUnit.MILLISECONDS);
  }

  private List<MetricWithValue<?>> getQualAnalytics(GitAnalytics analytics) {
    final var list = new LinkedList<MetricWithValue<?>>();

    final var repoCreatedAt = new MetricWithValue<>();
    repoCreatedAt.setName("repo_created_at");
    repoCreatedAt.setType("text/plain");
    repoCreatedAt.setValue(analytics.getRepoCreatedAt().toString());
    list.add(repoCreatedAt);

    final var repoFileNames = new MetricWithValue<>();
    repoFileNames.setName("repo_names");
    repoFileNames.setType("text/csv");
    repoFileNames.setValue(String.join(", ", analytics.getRepoFileNames()));
    list.add(repoFileNames);

    return list;
  }

  private List<MetricWithValue<?>> getQuantAnalytics(GitAnalytics analytics) {
    final var list = new LinkedList<MetricWithValue<?>>();

    final var commitCount = new MetricWithValue<>();
    commitCount.setName("commit_count");
    commitCount.setType("integer");
    commitCount.setValue(analytics.getCommitCount());
    list.add(commitCount);

    final var avgDurBetweenCommitsMs = new MetricWithValue<>();
    avgDurBetweenCommitsMs.setName("avg_duration_between_commits_ms");
    avgDurBetweenCommitsMs.setType("long");
    avgDurBetweenCommitsMs.setValue(analytics.getAvgDurationBetweenCommits());
    list.add(avgDurBetweenCommitsMs);

    final var repoFileCount = new MetricWithValue<>();
    repoFileCount.setName("repo_file_count");
    repoFileCount.setType("integer");
    repoFileCount.setValue(analytics.getRepoFileCount());
    list.add(repoFileCount);

    return list;
  }
}
