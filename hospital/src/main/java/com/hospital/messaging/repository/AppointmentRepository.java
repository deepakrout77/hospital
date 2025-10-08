// AppointmentRepository.java
package com.hospital.messaging.repository;

import com.hospital.messaging.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    List<Appointment> findByDoctorPhoneAndAppointmentTimeBetween(
            String doctorPhone,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}
