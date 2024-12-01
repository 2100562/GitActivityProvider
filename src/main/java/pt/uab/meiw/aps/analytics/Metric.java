package pt.uab.meiw.aps.analytics;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a Metric type that the Activity Provide returns.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public class Metric {

  private String name;
  private String type;

  public Metric() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Metric metric = (Metric) o;
    return Objects.equals(name, metric.name)
        && Objects.equals(type, metric.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Metric.class.getSimpleName() + "[",
        "]")
        .add("name='" + name + "'")
        .add("type='" + type + "'")
        .toString();
  }
}
