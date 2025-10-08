package com.hospital.messaging.repository;

import com.hospital.messaging.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    List<Prescription> findByPatientPhone(String patientPhone);
}
