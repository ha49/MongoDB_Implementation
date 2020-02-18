package main.java;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

public class Operations {

  private MongoCollection<Document> collection;

  public Operations(MongoCollection<Document> collection) {
    this.collection = collection;
  }
  public void insert(Document doc) {
    collection.insertOne(doc);
  }

  public void insert(List<Document> docs) {
    collection.insertMany(docs);
  }


  public void updateOne(Bson query, Bson update) {
    collection.updateOne(query, update);
  }

  public FindIterable<Document> findAll(){
    FindIterable<Document> result = collection.find();
    return result;
  }

  public FindIterable<Document> findWithFilter(Bson filter){
    FindIterable<Document> result = collection.find(filter);
    return result;
  }


  public FindIterable<Document> project(FindIterable<Document> unsorted, Bson projection){
    FindIterable<Document> result = unsorted.projection(projection);
    return result;
  }

  public void printAggregation(String matchField, int matchValue,
                               String groupField) {
    AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                    Aggregates.match(Filters.gte(matchField, matchValue)),
                    Aggregates.group(("$" + groupField), Accumulators.sum(
                            "count", 1))));
    printMany(result);

  }




  public void printAggregation2(String matchField, int matchValue,
                                String groupField) {
    AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(  Aggregates.project(fields(include("name", "stars"))),
                    Aggregates.match(Filters.gte(matchField, matchValue)),
                    Aggregates.group(("$" + groupField), Accumulators.sum(
                            "count", 1))   ));
    printMany(result);

  }

  public void printAggregation3(String matchField, int matchValue,
                                String groupField) {
    AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(

                    Aggregates.match(Filters.gte(matchField, matchValue)),
                    Aggregates.group(("$" + groupField), Accumulators.sum(
                            "count", 1)), Aggregates.project(
                            Projections.fields(
                                    Projections.excludeId(),
                                    Projections.include("name", "stars"))))



    );
    printMany(result);

  }

  public void printMany(MongoIterable<Document> docs) {
    Consumer<Document> printBlock = new Consumer<Document>() {
      @Override
      public void accept(final Document document) {
        System.out.println(document.toJson());
      }
    };

    docs.forEach(printBlock);
  }


/*  public void printAggregation4(String matchField, int matchValue,
                               String groupField) {
    AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                    Aggregates.match(Filters.gte(matchField, matchValue)),
                    Aggregates.group(("$" + groupField), Accumulators.sum(
                            "count", 1))));
    for (Document doc: result
         ) {

      printMany(collection.find(findWithFilter()));

    }
//    printMany(result);

  }*/




/*  public void getAggregation(String matchField, int matchValue,
                               String groupField, String... inclusions  ) {


    AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                    Aggregates.project(
                            Projections.fields(
                                    Projections.excludeId(),
                                    Projections.include(inclusions),
                                    Projections.computed(
                                            matchField,
                                            new Document("$" + groupField, Arrays.asList("$" + matchValue, 4))
                                    )
                            )
                    )
            )
    );
    printMany(result);

  }*/
}
