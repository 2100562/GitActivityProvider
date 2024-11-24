package pt.uab.meiw.aps;

/**
 * The base Service Factory interface.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface ServiceFactory {

  /**
   * Creates a service.
   *
   * @return the created service.
   */
  Service create();
}
