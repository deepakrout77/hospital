package com.hospital.messaging.repository;



import com.hospital.messaging.model.MedicineSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicineScheduleRepository extends MongoRepository<MedicineSchedule, String> {
}

