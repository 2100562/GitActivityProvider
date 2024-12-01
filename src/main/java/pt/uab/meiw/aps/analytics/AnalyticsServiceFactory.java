package pt.uab.meiw.aps.analytics;

import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.ServiceFactory;
import pt.uab.meiw.aps.analytics.impl.AnalyticsServiceImpl;
import pt.uab.meiw.aps.git.GitService;

/**
 * The Analytics Service Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AnalyticsServiceFactory implements ServiceFactory {

  private final Service instance;

  public AnalyticsServiceFactory(ServiceFactory gitServiceFactory) {
    instance = new AnalyticsServiceImpl(
        (GitService) gitServiceFactory.create());
  }

  @Override
  public Service create() {
    return instance;
  }
}
