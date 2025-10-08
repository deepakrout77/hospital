package com.hospital.messaging.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
@Service
public class ZoomTokenService {

    private String clientId = "HrHjq06eQAKOhmaMpiq2Zw";  // Replace with your actual client ID
    private String clientSecret = "xpFeL7sbGRep6jVqpBnBIFzh9Zya5meY";  // Replace with your actual client secret
    private String accountId = "NUXp52b_SzmslVo6Iti2-Q";  // Replace with your actual account ID

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        // Set request body with required fields
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "account_credentials");
        body.add("account_id", accountId);

        // Create the HttpEntity object
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Send POST request to Zoom API
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://zoom.us/oauth/token",  // Zoom OAuth token URL
                request,
                Map.class
        );

        // Extract and return the access token from the response
        return response.getBody().get("access_token").toString();
    }
}
