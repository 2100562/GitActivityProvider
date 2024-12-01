package pt.uab.meiw.aps.analytics;

import pt.uab.meiw.aps.Controller;
import pt.uab.meiw.aps.ControllerFactory;
import pt.uab.meiw.aps.ServiceFactory;

/**
 * The Analytics Controller Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsControllerFactory implements ControllerFactory {

  private final Controller instance;

  public AnalyticsControllerFactory(ServiceFactory serviceFactory) {
    instance = new AnalyticsController(
        (AnalyticsService) serviceFactory.create());
  }

  @Override
  public Controller create() {
    return instance;
  }
}
