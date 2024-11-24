package pt.uab.meiw.aps;

/**
 * The base Controller Factory interface.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface ControllerFactory {

  /**
   * Creates a controller.
   *
   * @return the created controller.
   */
  Controller create();
}
