package com.hospital.messaging.repository;


import com.hospital.messaging.model.DoctorAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorAvailabilityRepository extends MongoRepository<DoctorAvailability, String> {
    DoctorAvailability findByDoctorPhone(String doctorPhone);
}