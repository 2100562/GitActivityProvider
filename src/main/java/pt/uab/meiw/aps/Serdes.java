package pt.uab.meiw.aps;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A Singleton that services can use to access a global ObjectMapper.
 *
 * @author Hugo Gonçalves
 * @see 0.0.1
 */
public enum Serdes {
  INSTANCE;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public ObjectMapper getObjectMapper() {
    return this.objectMapper;
  }
}
