package pt.uab.meiw.aps.activity;

import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.ServiceFactory;
import pt.uab.meiw.aps.activity.impl.ActivityServiceImpl;
import pt.uab.meiw.aps.analytics.AnalyticsService;

/**
 * The Activity Service Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityServiceFactory implements ServiceFactory {

  private final Service instance;

  public ActivityServiceFactory(
      ServiceFactory analyticsServiceFactory) {

    instance = new ActivityServiceImpl(
        (AnalyticsService) analyticsServiceFactory.create());
  }

  @Override
  public Service create() {
    return instance;
  }
}
