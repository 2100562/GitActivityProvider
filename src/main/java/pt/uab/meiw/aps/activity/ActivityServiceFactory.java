package pt.uab.meiw.aps.activity;

import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.ServiceFactory;
import pt.uab.meiw.aps.activity.impl.ActivityServiceImpl;

/**
 * The Activity Service Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityServiceFactory implements ServiceFactory {

  public ActivityServiceFactory() {

  }

  @Override
  public Service create() {
    return new ActivityServiceImpl();
  }
}
