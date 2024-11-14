package pt.uab.meiw.aps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

/**
 * Set of utility methods.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class Utils {

  private Utils() {
    throw new UnsupportedOperationException("");
  }

  /**
   * Attempts to read the contents of the file specified by path. It can be a
   * simple file name where the method tries to load the file from the classpath
   * or current directory or a full path.
   *
   * @param path the path to the file
   * @return the contents of the file or empty
   */
  public static Optional<byte[]> readFile(String path) {
    Optional<byte[]> fileContents;
    try (final var is = Thread
        .currentThread()
        .getContextClassLoader()
        .getResourceAsStream(path)) {
      if (is != null) {
        fileContents = Optional.of(is.readAllBytes());
      } else {
        try (final var dis = Files.newInputStream(Path.of(path),
            StandardOpenOption.READ)) {
          fileContents = Optional.of(dis.readAllBytes());
        }
      }
    } catch (IOException e) {
      fileContents = Optional.empty();
    }

    return fileContents;
  }
}
