package com.hospital.messaging.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    private String reportName;
    private String patientId;
    private String uploadedById; // Staff or Doctor ID
    private String downloadLink; // File path or downloadable URL

    private String uploadedAt; // ISO Date or LocalDateTime String

    public Report() {}

    public Report(String reportName, String patientId, String uploadedById, String downloadLink, String uploadedAt) {
        this.reportName = reportName;
        this.patientId = patientId;
        this.uploadedById = uploadedById;
        this.downloadLink = downloadLink;
        this.uploadedAt = uploadedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(String uploadedById) {
        this.uploadedById = uploadedById;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
// Getters & Setters ...
}
