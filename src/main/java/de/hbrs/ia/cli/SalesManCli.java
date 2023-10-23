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



}

