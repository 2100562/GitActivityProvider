package pt.uab.meiw.aps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Git Activity Provider main class. It is responsible for loading the
 * configuration, bootstrap all required components and start the Helidon
 * Webserver.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public class ActivityProvider {

  private static final Logger LOG = LoggerFactory.getLogger(
      ActivityProvider.class);

  public static void main(String[] args) {
    LOG.info("Starting Git Activity Provider");
  }
}
