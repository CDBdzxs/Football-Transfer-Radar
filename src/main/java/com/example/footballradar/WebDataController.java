package com.example.footballradar;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebDataController {
    private final WebDataService service;

    public WebDataController(WebDataService service) {
        this.service = service;
    }

    @GetMapping("/api/transfers")
    public ResponseEntity<?> getFreshTransfers() {
        try {
            return ResponseEntity.ok(service.fetchFreshReport());
        } catch (IOException exception) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("message", "Live football transfer radar crawler failed. Check network access and run again.");
            error.put("details", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
        }
    }
}
