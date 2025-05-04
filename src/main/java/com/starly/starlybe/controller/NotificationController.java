package com.starly.starlybe.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.starly.starlybe.dto.BroadcastRequest;
import com.starly.starlybe.dto.NotificationRequest;
import com.starly.starlybe.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/push")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request) {
        try {
            String result = notificationService.sendNotificationToDevice(
                    request.getDeviceId(), request.getTitle(), request.getBody());
            return ResponseEntity.ok(Map.of("message", result));
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(500).body(Map.of("message", "Gönderim hatası: " + e.getMessage()));
        }
    }

    @PostMapping("/broadcast")
    public ResponseEntity<?> sendBroadcast(@RequestBody BroadcastRequest request) {
        String result = notificationService.sendToPlatform(
                request.getPlatform(), request.getTitle(), request.getBody());
        return ResponseEntity.ok(Map.of("message", result));
    }

}
