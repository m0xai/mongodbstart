package de.hbrs.ia;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDbConnection {
        private MongoClient mongoClient;
        private MongoDatabase database;
        private MongoCollection<Document> collection;

        public MongoDbConnection(String host, int port, String dbName, String collectionName) {
            mongoClient = new MongoClient(host, port);
            database = mongoClient.getDatabase(dbName);
            collection = database.getCollection(collectionName);
        }

        public MongoCollection<Document> getCollection() {
            return collection;
        }

        public void close() {
            mongoClient.close();
        }

    public static MongoDbConnection getEvaluationCollection() {
        return new MongoDbConnection("localhost", 27017, "personalTrack", "evaluation");
    }

    public static MongoDbConnection getSalesmanCollection() {
        return new MongoDbConnection("localhost", 27017, "personalTrack", "evaluation");
    }
}

