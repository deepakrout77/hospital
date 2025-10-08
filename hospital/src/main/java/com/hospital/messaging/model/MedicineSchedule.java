package com.hospital.messaging.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "medicine_schedules")
public class MedicineSchedule {
    @Id
    private String id;

    private String patientName;
    private String patientPhone;
    private String doctorName;
    private String doctorId;

    private List<MedicineEntry> medicines;

    public static class MedicineEntry {
        private String medicineName;
        private String dosage; // e.g., 1 tablet
        private List<LocalDateTime> scheduleTimes; // When to send reminders

        // Getters & Setters
        public String getMedicineName() { return medicineName; }
        public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }

        public List<LocalDateTime> getScheduleTimes() { return scheduleTimes; }
        public void setScheduleTimes(List<LocalDateTime> scheduleTimes) { this.scheduleTimes = scheduleTimes; }
    }

    // Getters & Setters for outer class
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public List<MedicineEntry> getMedicines() { return medicines; }
    public void setMedicines(List<MedicineEntry> medicines) { this.medicines = medicines; }
}
