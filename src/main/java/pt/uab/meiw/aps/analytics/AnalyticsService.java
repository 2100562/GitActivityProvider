package pt.uab.meiw.aps.analytics;

/**
 * The Analytics Service must provide two methods, one that returns the
 * Analytics Contract and another that returns the Analytics collected as
 * specified in the Inven!RA Platform. As part of the MVP, it is only providing
 * the Analytics Contract and the Analytics returns an empty list.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface AnalyticsService {

  /**
   * Returns the Analytics Contract for this AP.
   *
   * @return the Analytics Contract.
   */
  AnalyticsContract getAnalyticsContract();

  /**
   * Returns an empty List.
   *
   * @return an empty List.
   */
  Object getAnalytics();
}
