package com.hospital.messaging.repository;





import com.hospital.messaging.model.Diabetes;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiabetesRepository extends MongoRepository<Diabetes, String> {
    List<Diabetes> findByPatientId(String patientId);
}
