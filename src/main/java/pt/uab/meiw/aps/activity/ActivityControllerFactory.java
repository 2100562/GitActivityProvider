package pt.uab.meiw.aps.activity;

import pt.uab.meiw.aps.Controller;
import pt.uab.meiw.aps.ControllerFactory;

/**
 * The Activity Controller Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class ActivityControllerFactory implements ControllerFactory {

  public ActivityControllerFactory() {
  }

  @Override
  public Controller create() {
    return new ActivityController();
  }
}
