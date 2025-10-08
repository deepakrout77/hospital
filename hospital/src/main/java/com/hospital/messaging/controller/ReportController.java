package com.hospital.messaging.controller;

import com.hospital.messaging.model.Report;
import com.hospital.messaging.model.User;
import com.hospital.messaging.repository.ReportRepository;
import com.hospital.messaging.repository.UserRepository;
import com.hospital.messaging.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final String FILE_UPLOAD_PATH = "C:/reports/"; // 📝 Change this to your local folder

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    // ✅ STEP 3: Upload Report + WhatsApp Notification
    @PostMapping("/upload")
    public String uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("reportName") String reportName,
            @RequestParam("patientId") String patientId,
            @RequestParam("uploadedById") String uploadedById
    ) {
        try {
            // Save file
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(FILE_UPLOAD_PATH + fileName);
            FileOutputStream fos = new FileOutputStream(dest);
            fos.write(file.getBytes());
            fos.close();

            String downloadLink = "http://localhost:8080/reports/download/" + fileName;

            // Save in MongoDB
            Report report = new Report(reportName, patientId, uploadedById, downloadLink, LocalDateTime.now().toString());
            reportRepository.save(report);

            // Notify patient
            Optional<User> patientOpt = userRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                User patient = patientOpt.get();
                String msg = "Hi " + patient.getName() +
                        ", your test report \"" + reportName + "\" is ready. 📄\n" +
                        "Download here: " + downloadLink;

                whatsAppService.sendWhatsAppMessage(patient.getPhoneNumber(), msg);
            }

            return "✅ Report uploaded & patient notified!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to upload report.";
        }
    }

    // ✅ STEP 4: Download Link for Report
    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String filename) {
        try {
            File file = new File("C:/reports/" + filename);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
