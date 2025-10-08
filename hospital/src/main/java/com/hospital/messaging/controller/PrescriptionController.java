package com.hospital.messaging.controller;

import com.hospital.messaging.model.Prescription;
import com.hospital.messaging.model.User;
import com.hospital.messaging.repository.PrescriptionRepository;
import com.hospital.messaging.repository.UserRepository;
import com.hospital.messaging.service.WhatsAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private WhatsAppService whatsappService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPrescription(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientPhone") String patientPhone,
            @RequestParam("doctorId") String doctorId,
            @RequestParam("doctorName") String doctorName
    ) {
        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
            Path filePath = uploadPath.resolve(uniqueFileName);

            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Find patient
            Optional<User> userOpt = userRepository.findByPhoneNumber(patientPhone);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ Patient not found with phone: " + patientPhone);
            }

            User patient = userOpt.get();

            // Create download link
            String downloadUrl = "http://localhost:" + serverPort + "/api/prescriptions/download/" + uniqueFileName;

            // Send WhatsApp message to patient
            String message = "👨‍⚕️ Hello " + patient.getName() + ", your prescription from Dr. " + doctorName + " is ready. Download it here: " + downloadUrl;
            whatsappService.sendWhatsAppMessage(patientPhone, message);

            // Save prescription to MongoDB
            Prescription prescription = new Prescription();
            prescription.setPatientName(patient.getName());
            prescription.setPatientPhone(patientPhone);
            prescription.setDoctorName(doctorName);
            prescription.setDoctorId(doctorId);
            prescription.setFileName(uniqueFileName);
            prescription.setFileUrl(downloadUrl);

            prescriptionRepository.save(prescription);

            return ResponseEntity.ok("✅ Prescription uploaded, saved & WhatsApp message sent.");

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("❌ Error uploading file: " + e.getMessage());
        }
    }

    // File download endpoint
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> downloadPrescription(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        UrlResource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
