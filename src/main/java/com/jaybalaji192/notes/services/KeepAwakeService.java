package com.jaybalaji192.notes.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepAwakeService {
    private final RestTemplate restTemplate = new RestTemplate();

    // Runs every 12 minutes (720,000 milliseconds)
    @Scheduled(fixedRate = 720000)
    public void selfPing() {
        try {
            String url = "https://notes-rest-spring.onrender.com/api/health";
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Self-ping response: " + response);
        } catch (Exception e) {
            System.err.println("Self-ping failed: " + e.getMessage());
        }
    }
}
