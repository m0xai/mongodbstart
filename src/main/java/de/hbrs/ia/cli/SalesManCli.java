package de.hbrs.ia.cli;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import de.hbrs.ia.MongoDbConnection;
import de.hbrs.ia.model.SalesMan;
import org.bson.Document;

import java.util.Scanner;



public class SalesManCli {
    private static Scanner scanner = new Scanner(System.in);
    private static MongoDbConnection salesmanDB = MongoDbConnection.getSalesmanCollection();

    public static void main(String[] args) {
        // Connect to MongoDB

        while (true) {
            System.out.println("Choose an operation:");
            System.out.println("1. Create a SalesMan");
            System.out.println("2. Read a SalesMan");
            System.out.println("3. Update a SalesMan");
            System.out.println("4. Delete a SalesMan");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    createSalesMan();
                    break;
                case 2:
                    readSalesMan();
                    break;
                case 3:
                    updateSalesMan();
                    break;
                case 4:
                    deleteSalesMan();
                    break;
                case 5:
                    salesmanDB.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void createSalesMan() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        SalesMan newSalesMan = new SalesMan(firstName, lastName, id);
        salesmanDB.getCollection().insertOne(newSalesMan.toDocument());
        System.out.println("SalesMan created successfully.");
    }

    private static void readSalesMan() {
        System.out.print("Enter ID of the SalesMan you want to retrieve: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Perform a query to retrieve the SalesMan by ID
        Document query = new Document("id", id);
        FindIterable<Document> result = salesmanDB.getCollection().find(query);

        if (result.iterator().hasNext()) {
            Document salesManDocument = result.iterator().next();
            String firstName = salesManDocument.getString("firstname");
            String lastName = salesManDocument.getString("lastname");

            System.out.println("-- SalesMan Details --");
            System.out.println("ID: " + id);
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
        } else {
            System.out.println("-- SalesMan with ID " + id + " not found. --");
        }
    }


    private static void updateSalesMan() {
        System.out.print("Enter ID of the SalesMan you want to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Check if the SalesMan with the given ID exists
        Document query = new Document("id", id);
        FindIterable<Document> result = salesmanDB.getCollection().find(query);

        if (result.iterator().hasNext()) {
            System.out.print("Enter new first name: ");
            String newFirstName = scanner.nextLine();

            System.out.print("Enter new last name: ");
            String newLastName = scanner.nextLine();

            // Create an update query
            Document updateQuery = new Document("$set", new Document("firstname", newFirstName).append("lastname", newLastName));

            // Update the SalesMan's details
            UpdateResult updateResult = salesmanDB.getCollection().updateOne(query, updateQuery);

            if (updateResult.getModifiedCount() > 0) {
                System.out.println("SalesMan with ID " + id + " updated successfully.");
            } else {
                System.out.println("SalesMan with ID " + id + " was not updated.");
            }
        } else {
            System.out.println("SalesMan with ID " + id + " not found. Update operation aborted.");
        }
    }

    private static void deleteSalesMan() {
        System.out.print("Enter ID of the SalesMan you want to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Create a query to find the SalesMan by ID
        Document query = new Document("id", id);

        // Perform the delete operation
        DeleteResult deleteResult = salesmanDB.getCollection().deleteOne(query);

        if (deleteResult.getDeletedCount() > 0) {
            System.out.println("SalesMan with ID " + id + " deleted successfully.");
        } else {
            System.out.println("SalesMan with ID " + id + " not found or was not deleted.");
        }
    }
}

