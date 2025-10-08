package com.hospital.messaging.controller;



import com.hospital.messaging.model.DoctorProfile;
import com.hospital.messaging.repository.DoctorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/doctor-profile")
public class DoctorProfileController {

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    // Admin/Staff: Create or Update a profile
    @PostMapping("/save")
    public DoctorProfile saveProfile(@RequestBody DoctorProfile profile) {
        return doctorProfileRepository.save(profile);
    }

    // Public: View all doctor profiles
    @GetMapping("/all")
    public List<DoctorProfile> getAllProfiles() {
        return doctorProfileRepository.findAll();
    }
    // Public: Search doctors by department
    @GetMapping("/department/{dept}")
    public List<DoctorProfile> getDoctorsByDepartment(@PathVariable("dept") String dept) {
        return doctorProfileRepository.findByDepartmentIgnoreCase(dept);
    }


    // Public: View one doctor's profile by phone
    @GetMapping("/{phone}")
    public DoctorProfile getDoctorByPhone(@PathVariable String phone) {
        return doctorProfileRepository.findByPhone(phone).orElse(null);
    }
}
