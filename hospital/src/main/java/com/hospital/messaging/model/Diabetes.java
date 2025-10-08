package com.hospital.messaging.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "diabetes")
public class Diabetes {

    @Id
    private String id;
    private String patientId;
    private String level; // example: Normal, Prediabetic, Diabetic
    private String recordedDate;

    public Diabetes() {}

    public Diabetes(String id, String patientId, String level, String recordedDate) {
        this.id = id;
        this.patientId = patientId;
        this.level = level;
        this.recordedDate = recordedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }
// Getters and Setters...
}
