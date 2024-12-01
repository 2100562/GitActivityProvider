package pt.uab.meiw.aps.analytics;

import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.ServiceFactory;
import pt.uab.meiw.aps.analytics.impl.AnalyticsServiceImpl;

/**
 * The Analytics Service Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsServiceFactory implements ServiceFactory {

  private static final Service INSTANCE = new AnalyticsServiceImpl();

  public AnalyticsServiceFactory() {

  }

  @Override
  public Service create() {
    return INSTANCE;
  }
}
