package pt.uab.meiw.aps.configuration;

import pt.uab.meiw.aps.Controller;
import pt.uab.meiw.aps.ControllerFactory;
import pt.uab.meiw.aps.ServiceFactory;

/**
 * The Configuration Controller Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ConfigurationControllerFactory implements ControllerFactory {

  private final Controller instance;

  public ConfigurationControllerFactory(ServiceFactory serviceFactory) {
    instance = new ConfigurationController(
        (ConfigurationService) serviceFactory.create());
  }

  @Override
  public Controller create() {
    return instance;
  }
}
