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

    private static void readEvaluationRecord() {
        System.out.print("Enter SalesMan ID to view EvaluationRecords: ");
        int salesmanId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Check if the SalesMan with the specified ID exists
        Document query = new Document("id", salesmanId);
        FindIterable<Document> result = salesmanCollection.getCollection().find(query);

        if (result.iterator().hasNext()) {
            System.out.println("Evaluation Records for SalesMan with ID " + salesmanId + ":");

            // Perform a query to retrieve EvaluationRecords for the specified SalesMan
            Document evaluationQuery = new Document("salesmanId", salesmanId);
            FindIterable<Document> evaluationResult = elevationCollection.getCollection().find(evaluationQuery);

            for (Document evaluationDocument : evaluationResult) {
                int evaluationId = evaluationDocument.getInteger("id");
                int grade = evaluationDocument.getInteger("grade");

                System.out.println("EvaluationRecord ID: " + evaluationId);
                System.out.println("Grade: " + grade);
                System.out.println("------------");
            }
        } else {
            System.out.println("SalesMan with ID " + salesmanId + " does not exist or has no evaluation records.");
        }
    }


    private static void updateEvaluationRecord() {
        System.out.print("Enter SalesMan ID to update EvaluationRecord: ");
        int salesmanId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Check if the SalesMan with the specified ID exists
        Document query = new Document("id", salesmanId);
        FindIterable<Document> result = salesmanCollection.getCollection().find(query);

        if (result.iterator().hasNext()) {
            System.out.print("Enter EvaluationRecord ID to update: ");
            int evaluationRecordId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Check if the EvaluationRecord with the specified ID exists
            Document evaluationQuery = new Document("id", evaluationRecordId);
            evaluationQuery.append("salesmanId", salesmanId); // Ensure the EvaluationRecord belongs to the specified SalesMan
            FindIterable<Document> evaluationResult = elevationCollection.getCollection().find(evaluationQuery);

            if (evaluationResult.iterator().hasNext()) {
                System.out.print("Enter new grade: ");
                int newGrade = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Create an update query
                Document updateQuery = new Document("$set", new Document("grade", newGrade));

                // Update the EvaluationRecord's grade
                elevationCollection.getCollection().updateOne(evaluationQuery, updateQuery);

                System.out.println("EvaluationRecord with ID " + evaluationRecordId + " updated successfully.");
            } else {
                System.out.println("EvaluationRecord with ID " + evaluationRecordId + " does not exist for SalesMan with ID " + salesmanId);
            }
        } else {
            System.out.println("SalesMan with ID " + salesmanId + " does not exist. Update operation aborted.");
        }
    }


    private static void deleteEvaluationRecord() {
        // Implement the delete operation for EvaluationRecord using the salesmanId
    }
}
