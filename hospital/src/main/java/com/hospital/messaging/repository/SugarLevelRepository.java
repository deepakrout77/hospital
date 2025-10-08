package com.hospital.messaging.repository;





import com.hospital.messaging.model.SugarLevel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SugarLevelRepository extends MongoRepository<SugarLevel, String> {
    List<SugarLevel> findByPatientId(String patientId);
}
