package test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import de.hbrs.ia.model.SalesMan;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HighPerformanceTest {

    private MongoClient client;
    private MongoDatabase supermongo;
    private MongoCollection<Document> salesmen;

    /**
     * Attention: You might update the version of the Driver
     * for newer version of MongoDB!
     * This tests run with MongoDB 4.2.17 Community
     */
    @BeforeEach
    void setUp() {
        // Setting up the connection to a local MongoDB with standard port 27017
        // must be started within a terminal with command 'mongod'.
        client = new MongoClient("localhost", 27017);

        // Get database 'highperformance' (creates one if not available)
        supermongo = client.getDatabase("highperformance");

        // Get Collection 'salesmen' (creates one if not available)
        salesmen = supermongo.getCollection("salesman");
        salesmen.drop();
    }

    @Test
    void insertSalesMan() {
        // CREATE (Storing) the salesman object
        Document document = new Document();
        document.append("firstname" , "Sascha");
        document.append("lastname" , "Alda");
        document.append("id" , 90133);

        // ... now storing the object
        salesmen.insertOne(document);

        // READ (Finding) the stored Documnent
        Document newDocument = this.salesmen.find().first();
        System.out.println("Printing the object (JSON): " + newDocument );

        // Assertion
        Integer id = (Integer) newDocument.get("id");
        assertEquals( 90133 , id );

        // Deletion
        salesmen.drop();
    }

    @Test
    void insertSalesManMoreObjectOriented() {
        // CREATE (Storing) the salesman business object
        // Using setter instead
        SalesMan salesMan = new SalesMan( "Leslie" , "Malton" , 90444 );

        // ... now storing the object
        salesmen.insertOne(salesMan.toDocument());

        // READ (Finding) the stored Documnent
        // Mapping Document to business object would be fine...
        Document newDocument = this.salesmen.find().first();
        System.out.println("Printing the object (JSON): " + newDocument );

        // Assertion
        Integer id = (Integer) newDocument.get("id");
        assertEquals( 90444 , id );

        // Deletion
        salesmen.drop();
    }


    @Test
    void updateSalesMan() {
        SalesMan salesMan = new SalesMan("Alice", "Johnson", 90111);
        salesmen.insertOne(salesMan.toDocument());

        Document document = this.salesmen.find().first();
        SalesMan retrievedSalesMan = SalesMan.fromDocument(document);

        Document query = new Document().append("id",  retrievedSalesMan.getId());

        Bson updatedSalesman = Updates.combine(
                Updates.set("firstname", "NewFirst"),
                Updates.set("lastname", "NewLast"),
                Updates.set("id", 99999)
        );

        Document d = retrievedSalesMan.toDocument();
        salesmen.updateOne(query, updatedSalesman);

        Document updatedDocument = this.salesmen.find().first();
        SalesMan updatedSalesMan = SalesMan.fromDocument(updatedDocument);

        assertEquals("NewFirst", updatedSalesMan.getFirstname());
        assertEquals("NewLast", updatedSalesMan.getLastname());
        assertEquals(99999, updatedSalesMan.getId());

        salesmen.drop();
    }

    @Test
    void deleteSalesMan() {
        SalesMan salesMan = new SalesMan("Jane", "Smith", 90122);
        salesmen.insertOne(salesMan.toDocument());

        Document document = this.salesmen.find().first();

        salesmen.deleteOne(document);

        Document deletedDocument = this.salesmen.find().first();

        assertNull(deletedDocument); // The record should be deleted

        salesmen.drop();
    }
}