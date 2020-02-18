package main.java;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    MongoDatabase.init("mongolab");

    MongoCollection<Document> collection = MongoDatabase.getCollection(
            "restaurants");

    Operations operations = new Operations(collection);


    List<Document> list = new ArrayList<Document>();
    list.add(new Document("_id", new ObjectId("5c39f9b5df831369c19b6bca"))
            .append("name", "Sun Bakery Trattoria")
            .append("stars", 4)
            .append("categories", Arrays.asList("Pizza", "Pasta",
                    "Italian", "Coffee", "Sandwiches")));
    list.add(new Document("_id", new ObjectId("5c39f9b5df831369c19b6bcb"))
            .append("name", "Blue Bagels Grill")
            .append("stars", 3)
            .append("categories", Arrays.asList("Bagels", "Cookies",
                    "Sandwiches")));

    list.add(new Document("_id", new ObjectId("5c39f9b5df831369c19b6bcc"))
            .append("name", "Hot Bakery Cafe")
            .append("stars", 4)
            .append("categories", Arrays.asList("Bakery", "Cafe",
                    "Coffee", "Dessert")));

    list.add(new Document("_id", new ObjectId("5c39f9b5df831369c19b6bcd"))
            .append("name", "XYZ Coffee Bar")
            .append("stars", 5)
            .append("categories", Arrays.asList("Coffee", "Cafe",
                    "Bakery", "Chocolates")));

    list.add(new Document("_id", new ObjectId("5c39f9b5df831369c19b6bce"))
            .append("name", "456 Cookies Shop")
            .append("stars", 4)
            .append("categories", Arrays.asList("Bakery", "Cookies",
                    "Cake", "Coffee")));

    operations.insert(list);




    FindIterable<Document> result = operations.findAll();
    operations.printMany(result);



    //    System.out.println("\n****** CATEGORIES:CAFE ***********\n");
    //    operations.printMany(operations.findWithFilter(Filters.eq("categories", "Cafe")));


    System.out.println("\n********CATEGORIES:CAFE ONLY NAME ***********\n");
    operations.printMany(operations.project(operations.findWithFilter(Filters.eq(
            "categories", "Cafe")), Projections.fields(
            Projections.include("name", "categories"),
            Projections.excludeId())));


    System.out.println("\n******INCREMENT XYZ CAFE increment stars by 1 + PRINT ALL****\n");
    operations.updateOne(Filters.eq("name", "XYZ Coffee Bar"),
            Updates.inc("stars", 1));
    operations.printMany(result);



    System.out.println("\n***RENAME 456 Cookies Shop as 123 Cookies Heaven****\n");
    operations.updateOne(Filters.eq("name", "456 Cookies Shop"),
            Updates.set("name", "123 Cookies Heaven"));
    operations.printMany(result);



    System.out.println("\n***RESTAURANTS WITH MORE THAN 4 STARS***\n");

    operations.printMany(operations.project(operations.findWithFilter(Filters.gte(
            "stars", 4)), Projections.fields(
            Projections.include("name", "stars"),
            Projections.excludeId())));

    MongoDatabase.close();

  }


}
