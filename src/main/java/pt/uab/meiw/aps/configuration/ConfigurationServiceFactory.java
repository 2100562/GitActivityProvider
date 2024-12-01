package pt.uab.meiw.aps.configuration;

import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.ServiceFactory;
import pt.uab.meiw.aps.configuration.impl.ConfigurationServiceImpl;

/**
 * The Configuration Service Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ConfigurationServiceFactory implements ServiceFactory {

  private static final Service INSTANCE = new ConfigurationServiceImpl();

  public ConfigurationServiceFactory() {

  }

  @Override
  public Service create() {
    return INSTANCE;
  }
}
