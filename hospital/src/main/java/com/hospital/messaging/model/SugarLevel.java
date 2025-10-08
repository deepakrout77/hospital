package com.hospital.messaging.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sugar_levels")
public class SugarLevel {

    @Id
    private String id;
    private String patientId;
    private String value; // example: 95 mg/dL
    private String recordedDate;

    public SugarLevel() {}

    public SugarLevel(String id, String patientId, String value, String recordedDate) {
        this.id = id;
        this.patientId = patientId;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }
// Getters and Setters...
}
