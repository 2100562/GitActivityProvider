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

  private final ServiceFactory serviceFactory;

  public AnalyticsControllerFactory() {
    serviceFactory = new AnalyticsServiceFactory();
  }

  @Override
  public Controller create() {
    return new AnalyticsController(
        (AnalyticsService) serviceFactory.create());
  }
}
