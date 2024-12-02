package pt.uab.meiw.aps.analytics;

import java.util.List;
import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.activity.model.ActivityInstance;
import pt.uab.meiw.aps.analytics.model.ActivityAnalytics;

/**
 * The Analytics Service must provide two methods, one that returns the
 * Analytics Contract and another that returns the Analytics collected as
 * specified in the Inven!RA Platform. As part of the MVP, it is only providing
 * the Analytics Contract and the Analytics returns an empty list.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface AnalyticsService extends Service {

  /**
   * Returns the Analytics Contract for this AP.
   *
   * @return the Analytics Contract.
   */
  AnalyticsContract getAnalyticsContract();

  /**
   * Fetches activity analytics for all students for the given activity id.
   *
   * @param activityId the activity id to fetch analytics.
   * @return a collection of analytics, if any.
   */
  List<ActivityAnalytics> getAnalytics(String activityId);

  /**
   * Tests if the given URL is valid and the service is able to start
   * collection.
   *
   * @param repositoryUrl the repository URL to test.
   * @return true if able to start collection, false otherwise.
   */
  boolean canStartCollection(String repositoryUrl);

  /**
   * Starts the periodic collection of metrics.
   *
   * @param activityInstance the activity to start the collection.
   */
  void startCollection(ActivityInstance activityInstance);
}
