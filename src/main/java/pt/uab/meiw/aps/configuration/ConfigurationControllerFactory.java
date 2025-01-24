package pt.uab.meiw.aps.configuration;

import pt.uab.meiw.aps.Controller;
import pt.uab.meiw.aps.ControllerFactory;

/**
 * The Configuration Controller Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ConfigurationControllerFactory implements ControllerFactory {

  private final Controller instance;

  public ConfigurationControllerFactory() {
    instance = new ConfigurationController();
  }

  @Override
  public Controller create() {
    return instance;
  }
}
