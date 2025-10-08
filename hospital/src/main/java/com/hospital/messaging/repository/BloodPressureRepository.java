package com.hospital.messaging.repository;



import com.hospital.messaging.model.BloodPressure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BloodPressureRepository extends MongoRepository<BloodPressure, String> {
    List<BloodPressure> findByPatientId(String patientId);
}
