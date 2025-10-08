package com.hospital.messaging.controller;



import com.hospital.messaging.model.DoctorAvailability;
import com.hospital.messaging.repository.DoctorAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    // Save or update availability
    @PostMapping("/set")
    public String setAvailability(@RequestBody DoctorAvailability availability) {
        availabilityRepository.save(availability);
        return "✅ Doctor availability saved.";
    }

    // Get availability by doctor phone number
    @GetMapping("/get/{phone}")
    public DoctorAvailability getAvailability(@PathVariable String phone) {
        return availabilityRepository.findByDoctorPhone(phone);
    }
}

