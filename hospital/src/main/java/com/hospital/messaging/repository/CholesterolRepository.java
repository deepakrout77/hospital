package com.hospital.messaging.repository;

import com.hospital.messaging.model.Cholesterol;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CholesterolRepository extends MongoRepository<Cholesterol, String> {
    List<Cholesterol> findByPatientId(String patientId);
}
