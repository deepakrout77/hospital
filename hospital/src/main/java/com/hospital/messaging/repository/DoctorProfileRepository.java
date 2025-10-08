package com.hospital.messaging.repository;



import com.hospital.messaging.model.DoctorProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;import java.util.List;




public interface DoctorProfileRepository extends MongoRepository<DoctorProfile, String> {
    Optional<DoctorProfile> findByPhone(String phone);
    List<DoctorProfile> findByDepartmentIgnoreCase(String department);
}
