package de.hbrs.ia.model;

import org.bson.Document;

/**
 *
 */
public class EvaluationRecord {
    private int id;
    private int salesmanId;
    private int grade;

    public EvaluationRecord(int salesmanId, int grade, int id) {
        this.salesmanId = salesmanId;
        this.grade = grade;
        this.id = id;
    }


    public Document toDocument() {
        org.bson.Document document = new Document();
        document.append("id" , this.id );
        document.append("salesmanId" , this.salesmanId );
        document.append("grade" , this.grade);
        return document;
    }
}
