package de.hbrs.ia.cli;


import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import de.hbrs.ia.MongoDbConnection;
import de.hbrs.ia.model.EvaluationRecord;
import org.bson.Document;

import java.util.Scanner;

public class EvaluationRecordCli {
    private static Scanner scanner = new Scanner(System.in);
    private static MongoDbConnection elevationCollection = MongoDbConnection.getEvaluationCollection();
    private static MongoDbConnection salesmanCollection = MongoDbConnection.getSalesmanCollection();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Choose an operation for EvaluationRecord:");
            System.out.println("1. Create an EvaluationRecord");
            System.out.println("2. Read an EvaluationRecord");
            System.out.println("3. Update an EvaluationRecord");
            System.out.println("4. Delete an EvaluationRecord");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

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
        scanner.nextLine();

        System.out.print("Enter SalesMan ID: ");
        int salesmanId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter grade: ");
        int grade = scanner.nextInt();
        scanner.nextLine();

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
        scanner.nextLine();

        Document query = new Document("id", salesmanId);
        FindIterable<Document> result = salesmanCollection.getCollection().find(query);

        if (result.iterator().hasNext()) {
            System.out.println("Evaluation Records for SalesMan with ID " + salesmanId + ":");

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
        scanner.nextLine();

        Document query = new Document("id", salesmanId);
        FindIterable<Document> result = salesmanCollection.getCollection().find(query);

        if (result.iterator().hasNext()) {
            System.out.print("Enter EvaluationRecord ID to update: ");
            int evaluationRecordId = scanner.nextInt();
            scanner.nextLine();

            Document evaluationQuery = new Document("id", evaluationRecordId);
            evaluationQuery.append("salesmanId", salesmanId);
            FindIterable<Document> evaluationResult = elevationCollection.getCollection().find(evaluationQuery);

            if (evaluationResult.iterator().hasNext()) {
                System.out.print("Enter new grade: ");
                int newGrade = scanner.nextInt();
                scanner.nextLine();

                Document updateQuery = new Document("$set", new Document("grade", newGrade));

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
        System.out.print("Enter SalesMan ID to delete EvaluationRecord: ");
        int salesmanId = scanner.nextInt();
        scanner.nextLine();

        Document query = new Document("id", salesmanId);
        FindIterable<Document> result = salesmanCollection.getCollection().find(query);

        if (result.iterator().hasNext()) {
            System.out.print("Enter EvaluationRecord ID to delete: ");
            int evaluationRecordId = scanner.nextInt();
            scanner.nextLine();

            Document evaluationQuery = new Document("id", evaluationRecordId);
            evaluationQuery.append("salesmanId", salesmanId);

            DeleteResult deleteResult = elevationCollection.getCollection().deleteOne(evaluationQuery);

            if (deleteResult.getDeletedCount() > 0) {
                System.out.println("EvaluationRecord with ID " + evaluationRecordId + " deleted successfully.");
            } else {
                System.out.println("EvaluationRecord with ID " + evaluationRecordId + " does not exist or does not belong to SalesMan with ID " + salesmanId);
            }
        } else {
            System.out.println("SalesMan with ID " + salesmanId + " does not exist. Delete operation aborted.");
        }
    }
}
