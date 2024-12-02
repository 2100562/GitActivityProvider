package pt.uab.meiw.aps.analytics.model;

import org.bson.Document;

/**
 * To easily convert objects into Mongo.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public interface AsDocument {

  Document getDocument();
}
