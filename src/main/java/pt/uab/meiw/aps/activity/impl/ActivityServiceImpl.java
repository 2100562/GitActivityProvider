package pt.uab.meiw.aps.activity.impl;

import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.uab.meiw.aps.Utils;
import pt.uab.meiw.aps.activity.ActivityService;
import pt.uab.meiw.aps.activity.DeployRequest;
import pt.uab.meiw.aps.activity.model.ActivityInstance;
import pt.uab.meiw.aps.analytics.AnalyticsService;

/**
 * The Activity Service default implementation providing methods to create new
 * Activity Instances.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityServiceImpl implements ActivityService {

  private static final Logger LOG = LogManager.getLogger(
      ActivityServiceImpl.class);

  private final DbClient dbClient;
  private final String activityInterface;
  private final AnalyticsService analyticsService;

  public ActivityServiceImpl(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
    final var config = Config.global();
    dbClient = DbClient.create(config.get("db"));

    final var activityHtmlPath = config
        .get("ap")
        .get("git")
        .get("interface-path")
        .asString()
        .orElse("");

    final var activityHtmlFile = Utils.readFile(activityHtmlPath);

    activityInterface = activityHtmlFile
        .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
        .orElse("");

    // Re-start any previously started collections, for crash/restart scenários.
    dbClient
        .execute()
        .createNamedQuery("get-activities-with-collection")
        .execute()
        .map(row -> row.as(ActivityInstance.class))
        .peek(ai -> LOG.info("Resuming collection for activity {}", ai.getId()))
        .forEach(analyticsService::startCollection);
  }

  @Override
  public ActivityInstance createFor(DeployRequest req) {
    final var ai = new ActivityInstance(req);

    var rows = dbClient
        .execute()
        .createNamedInsert("insert-activity")
        .namedParam(ai)
        .execute();

    if (rows != 1) {
      throw new RuntimeException("Failed to create activity instance");
    }

    return ai;
  }

  @Override
  public ActivityInstance setRepository(String id, String repositoryUrl) {
    if (!analyticsService.canStartCollection(repositoryUrl)) {
      throw new RuntimeException("Unable to start analytics collection");
    }

    var rows = dbClient
        .execute()
        .createNamedUpdate("update-activity-repository")
        .addParam("id", id)
        .addParam("repositoryUrl", repositoryUrl)
        .execute();

    if (rows != 1) {
      throw new RuntimeException("Failed to update activity instance");
    }

    var activity = getActivity(id);

    analyticsService.startCollection(activity);

    return activity;
  }

  @Override
  public ActivityInstance getActivity(String id) {
    return dbClient
        .execute()
        .createNamedGet("get-activity")
        .addParam("id", id)
        .execute()
        .map(r -> r.as(ActivityInstance.class))
        .orElse(null);
  }

  @Override
  public String getActivityInterface() {
    return activityInterface;
  }
}
