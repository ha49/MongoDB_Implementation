package main.java;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;

public class MongoDatabase {

  private static String databaseName;
  private static MongoClient client;
  private static com.mongodb.client.MongoDatabase database;
  private static MongoClientSettings settings;

  public static void init(String newName) {
    databaseName = newName;
    client = MongoClients.create();
    database = client.getDatabase(databaseName);
  }

  public static void createCollection(String name) {
    database.createCollection(name);
  }


  public static void createValidatedCollection(String name, String field) {
    ValidationOptions options = new ValidationOptions().validator(
            Filters.exists(field));

    database.createCollection(name,
            new CreateCollectionOptions().validationOptions(options));
  }


  public static MongoCollection<Document> getCollection(String collName) {
    return database.getCollection(collName);
  }

  public static void dropCollection(String collName) {
    MongoCollection<Document> coll = database.getCollection(collName);
    coll.drop();
  }

  public static void close() {
    client.close();

  }
}
