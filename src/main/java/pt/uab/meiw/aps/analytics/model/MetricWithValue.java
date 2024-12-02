package pt.uab.meiw.aps.analytics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.StringJoiner;
import org.bson.Document;
import pt.uab.meiw.aps.analytics.Metric;

/**
 * Represents a Metric type holding the value that the Activity Provider
 * returns.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class MetricWithValue<T> extends Metric implements AsDocument {

  private T value;

  public MetricWithValue() {
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  @JsonIgnore
  @Override
  public Document getDocument() {
    return new Document("name", getName())
        .append("type", getType())
        .append("value", getValue());
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    MetricWithValue<?> that = (MetricWithValue<?>) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), value);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ",
        MetricWithValue.class.getSimpleName() + "[", "]")
        .add("value=" + value)
        .add(super.toString())
        .toString();
  }
}
