package pt.uab.meiw.aps.activity;

import pt.uab.meiw.aps.Controller;
import pt.uab.meiw.aps.ControllerFactory;
import pt.uab.meiw.aps.ServiceFactory;

/**
 * The Activity Controller Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityControllerFactory implements ControllerFactory {

  private final ServiceFactory serviceFactory;

  public ActivityControllerFactory() {
    serviceFactory = new ActivityServiceFactory();
  }

  @Override
  public Controller create() {
    return new ActivityController((ActivityService) serviceFactory.create());
  }
}
