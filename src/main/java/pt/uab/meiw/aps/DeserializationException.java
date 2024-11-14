package pt.uab.meiw.aps;

/**
 * Exception throw when deserialization with Jackson fails.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class DeserializationException extends RuntimeException {

  public DeserializationException(String message) {
    super(message);
  }

  public DeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeserializationException(Throwable cause) {
    super(cause);
  }

  public DeserializationException() {
  }
}
