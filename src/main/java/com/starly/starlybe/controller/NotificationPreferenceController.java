package com.starly.starlybe.controller;

import com.starly.starlybe.dto.NotificationPreferenceRequest;
import com.starly.starlybe.service.NotificationPreferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notification-preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    public NotificationPreferenceController(NotificationPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> savePreferences(@RequestBody NotificationPreferenceRequest request) {
        String resultMessage = preferenceService.savePreferences(
                request.getUserId(),
                request.getDeviceId(),
                request.getTimeZone(),
                request.getUserLocalTimes(),
                request.isEnabled()
        );
        return ResponseEntity.ok(Map.of("message", resultMessage));
    }


}
