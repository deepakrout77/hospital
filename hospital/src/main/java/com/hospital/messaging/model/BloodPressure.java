package com.hospital.messaging.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blood_pressure")
public class BloodPressure {

    @Id
    private String id;
    private String patientId; // user id
    private String systolic;  // example: 120
    private String diastolic; // example: 80
    private String recordedDate;

    public BloodPressure() {}

    public BloodPressure(String id, String patientId, String systolic, String diastolic, String recordedDate) {
        this.id = id;
        this.patientId = patientId;
        this.systolic = systolic;
        this.diastolic = diastolic;
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

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }
// Getters and Setters...
}
