package com.hospital.messaging.controller;

import com.google.zxing.WriterException;
import com.hospital.messaging.model.User;
import com.hospital.messaging.repository.UserRepository;
import com.hospital.messaging.util.QrCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Health check endpoint
    @GetMapping("/test")
    public String test() {
        return "App is working!";
    }

    // ✅ Login or Register User with QR
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userRepository.findByPhoneNumber(user.getPhoneNumber())
                .map(existingUser -> "Welcome back, " + existingUser.getName())
                .orElseGet(() -> {
                    try {
                        if ("patient".equalsIgnoreCase(user.getRole())) {
                            String qrUrl = QrCodeGenerator.generateQrCodeBase64(
                                    "http://localhost:8080/api/patient/dashboard/" + user.getPhoneNumber());
                            user.setQrCodeUrl(qrUrl);

                            userRepository.save(user); // Save with QR

                            // 🟢 Return HTML string with download link + image preview
                            return "User registered: " + user.getName() + " (" + user.getRole() + ")<br>" +
                                    "<a href=\"/api/users/qr/download?name=" + user.getName() +
                                    "&phone=" + user.getPhoneNumber() + "\">Download QR Code</a><br>" +
                                    "<img src=\"" + qrUrl + "\" width=\"200\" />";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    userRepository.save(user);
                    return "✅User registered: " + user.getName() + " (" + user.getRole() + ")";
                });
    }

    // ✅ QR Code PNG Download Endpoint
    @GetMapping("/qr/download")
    public ResponseEntity<byte[]> downloadQrCode(@RequestParam String name, @RequestParam String phone) {
        try {
            String content = "http://localhost:8080/api/patient/dashboard/" + phone;
            byte[] qrImage = QrCodeGenerator.generateQrCodeImage(content);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", name + "_qr.png");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(qrImage);
        } catch (WriterException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
