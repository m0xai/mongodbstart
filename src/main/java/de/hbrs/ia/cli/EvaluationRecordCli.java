package de.hbrs.ia.cli;


import com.mongodb.client.FindIterable;
import de.hbrs.ia.MongoDbConnection;
import de.hbrs.ia.model.EvaluationRecord;
import org.bson.Document;

import java.util.Scanner;

public class EvaluationRecordCli {
    private static Scanner scanner = new Scanner(System.in);
    private static MongoDbConnection elevationCollection = MongoDbConnection.getEvaluationCollection();
    private static MongoDbConnection salesmanCollection = MongoDbConnection.getSalesmanCollection();

    public static void main(String[] args) {
        // Connect to MongoDB

        while (true) {
            System.out.println("Choose an operation for EvaluationRecord:");
            System.out.println("1. Create an EvaluationRecord");
            System.out.println("2. Read an EvaluationRecord");
            System.out.println("3. Update an EvaluationRecord");
            System.out.println("4. Delete an EvaluationRecord");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    createEvaluationRecord();
                    break;
                case 2:
                    readEvaluationRecord();
                    break;
                case 3:
                    updateEvaluationRecord();
                    break;
                case 4:
                    deleteEvaluationRecord();
                    break;
                case 5:
                    elevationCollection.close();
                    salesmanCollection.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void createEvaluationRecord() {
        System.out.print("Enter Record ID: ");
        int recordId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter SalesMan ID: ");
        int salesmanId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter grade: ");
        int grade = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Check if the SalesMan with the specified ID exists
        Document query = new Document("id", salesmanId);
        FindIterable<Document> result = salesmanCollection.getCollection().find(query);

        if (result.iterator().hasNext()) {
            EvaluationRecord newEvaluationRecord = new EvaluationRecord(salesmanId, grade, recordId);
            elevationCollection.getCollection().insertOne(newEvaluationRecord.toDocument());
            System.out.println("EvaluationRecord created successfully.");
        } else {
            System.out.println("SalesMan with ID " + salesmanId + " does not exist. EvaluationRecord creation aborted.");
        }
    }


}
