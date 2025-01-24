package pt.uab.meiw.aps;

import pt.uab.meiw.aps.activity.ActivityControllerFactory;
import pt.uab.meiw.aps.activity.ActivityServiceFactory;
import pt.uab.meiw.aps.analytics.AnalyticsControllerFactory;
import pt.uab.meiw.aps.analytics.AnalyticsServiceFactory;
import pt.uab.meiw.aps.configuration.ConfigurationControllerFactory;
import pt.uab.meiw.aps.git.GitServiceFactory;

/**
 * The Controller Abstract Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AbstractControllerFactory {

  private static final GitServiceFactory GIT_SV_FACTORY = new GitServiceFactory();
  private static final ServiceFactory ANALYTICS_SV_FACTORY = new AnalyticsServiceFactory(
      GIT_SV_FACTORY);
  private static final ServiceFactory ACTIVITY_SV_FACTORY = new ActivityServiceFactory(
      ANALYTICS_SV_FACTORY);
  private static final ControllerFactory CONFIG_CTRL_FACTORY = new ConfigurationControllerFactory();
  private static final ControllerFactory ANALYTICS_CTRL_FACTORY = new AnalyticsControllerFactory(
      ANALYTICS_SV_FACTORY);
  private static final ControllerFactory ACTIVITY_CTRL_FACTORY = new ActivityControllerFactory(
      ACTIVITY_SV_FACTORY);

  public AbstractControllerFactory() {

  }

  /**
   * Creates a REST Controller of the given type.
   *
   * @param type the type to create.
   * @return the created controller.
   */
  public Controller createController(ControllerType type) {

    return switch (type) {
      case Configuration -> CONFIG_CTRL_FACTORY.create();
      case Analytics -> ANALYTICS_CTRL_FACTORY.create();
      case Activity -> ACTIVITY_CTRL_FACTORY.create();
    };
  }
}
