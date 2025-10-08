package com.hospital.messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ZoomService {

    @Autowired
    private ZoomTokenService zoomTokenService;

    public String createZoomMeeting(String topic, String startTime) {
        String accessToken = zoomTokenService.getAccessToken();

        RestTemplate restTemplate = new RestTemplate();

        // Request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request body
        Map<String, Object> body = new HashMap<>();
        body.put("topic", topic);
        body.put("type", 2); // Scheduled meeting
        body.put("start_time", startTime); // Format: 2025-04-20T10:00:00Z
        body.put("duration", 30); // 30 minutes
        body.put("timezone", "Asia/Kolkata");

        Map<String, Object> settings = new HashMap<>();
        settings.put("join_before_host", true);
        body.put("settings", settings);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // API Call
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.zoom.us/v2/users/me/meetings",
                request,
                Map.class
        );

        // Return Zoom meeting join URL
        return response.getBody().get("join_url").toString();
    }
}
