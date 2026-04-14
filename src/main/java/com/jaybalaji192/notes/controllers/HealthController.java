package com.jaybalaji192.notes.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final AtomicLong counter = new AtomicLong(0);

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Server is awake and healthy! Hit count: " + counter.incrementAndGet());
    }
}