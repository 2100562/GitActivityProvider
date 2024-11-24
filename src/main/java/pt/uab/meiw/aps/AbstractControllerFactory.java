package pt.uab.meiw.aps;

import pt.uab.meiw.aps.activity.ActivityControllerFactory;
import pt.uab.meiw.aps.analytics.AnalyticsControllerFactory;
import pt.uab.meiw.aps.configuration.ConfigurationControllerFactory;

/**
 * The Controller Abstract Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class AbstractControllerFactory {

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
      case Configuration -> new ConfigurationControllerFactory().create();
      case Analytics -> new AnalyticsControllerFactory().create();
      case Activity -> new ActivityControllerFactory().create();
    };
  }
}
