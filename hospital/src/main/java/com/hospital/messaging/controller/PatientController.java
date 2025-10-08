package com.hospital.messaging.controller;


import com.hospital.messaging.model.*;
import com.hospital.messaging.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodPressureRepository bloodPressureRepository;

    @Autowired
    private DiabetesRepository diabetesRepository;

    @Autowired
    private SugarLevelRepository sugarLevelRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private CholesterolRepository cholesterolRepository;

    // Doctor scans patient QR -> opens this dashboard
    @GetMapping("/dashboard/{phone}")
    public String viewPatientDashboard(@PathVariable String phone) {

        // Check if the doctor is logged in and has the "doctor" role


        // Fetch the patient based on phone number
        Optional<User> patient = userRepository.findByPhoneNumber(phone);
        if (patient.isEmpty()) {
            return "Patient not found.";
        }

        // Fetch the patient's medical data
        List<BloodPressure> bloodPressureData = bloodPressureRepository.findByPatientId(patient.get().getId());
        List<Diabetes> diabetesData = diabetesRepository.findByPatientId(patient.get().getId());
        List<SugarLevel> sugarLevelData = sugarLevelRepository.findByPatientId(patient.get().getId());
        List<Cholesterol> cholesterolData = cholesterolRepository.findByPatientId(patient.get().getId());

        // Construct the dashboard string
        StringBuilder dashboard = new StringBuilder();
        dashboard.append("Dashboard for ").append(patient.get().getName()).append("<br>");

        if (!bloodPressureData.isEmpty()) {
            BloodPressure bp = bloodPressureData.get(0); // assuming the most recent record
            dashboard.append("Blood Pressure: ").append(bp.getSystolic()).append("/").append(bp.getDiastolic()).append("<br>");
        } else {
            dashboard.append("Blood Pressure: Not available<br>");
        }

        if (!diabetesData.isEmpty()) {
            Diabetes diabetes = diabetesData.get(0); // assuming the most recent record
            dashboard.append("Diabetes: ").append(diabetes.getLevel()).append("<br>");
        } else {
            dashboard.append("Diabetes: Not available<br>");
        }

        if (!sugarLevelData.isEmpty()) {
            SugarLevel sugar = sugarLevelData.get(0); // assuming the most recent record
            dashboard.append("Sugar Level: ").append(sugar.getValue()).append("<br>");
        } else {
            dashboard.append("Sugar Level: Not available<br>");
        }

        if (!cholesterolData.isEmpty()) {
            Cholesterol cholesterol = cholesterolData.get(0); // assuming the most recent record
            dashboard.append("Cholesterol: ").append(cholesterol.getValue()).append("<br>");
        } else {
            dashboard.append("Cholesterol: Not available<br>");
        }

        // Add other patient details like prescriptions, appointments, reports (These can be fetched similarly if required)
        // Prescriptions
        List<Prescription> prescriptions = prescriptionRepository.findAll()
                .stream()
                .filter(p -> p.getPatientPhone().equals(phone))
                .collect(Collectors.toList());


        if (!prescriptions.isEmpty()) {
            Prescription firstPrescription = prescriptions.get(0); // Get the first prescription
            String doctorName = firstPrescription.getDoctorName();
            dashboard.append("Dr.Name: ").append(doctorName).append("<br>");
            dashboard.append("<br><b>🧾 Prescriptions:</b><br>");
            for (Prescription p : prescriptions) {
                dashboard.append("• From Dr. ").append(p.getDoctorName())
                        .append(": <a href='").append(p.getFileUrl())
                        .append("' target='_blank'>Download</a><br>");
            }
        } else {
            dashboard.append("<br>🧾 Prescriptions: Not available<br>");
        }

        return dashboard.toString();
    }
}
