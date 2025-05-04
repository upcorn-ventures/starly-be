package com.starly.starlybe.service;

import com.google.firebase.messaging.*;
import com.starly.starlybe.entity.DeviceToken;
import com.starly.starlybe.repository.DeviceTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private DeviceTokenRepository repo;

    public String sendNotificationToDevice(String deviceId, String title, String body) throws FirebaseMessagingException {
        Optional<DeviceToken> tokenOpt = repo.findByDeviceId(deviceId);

        if (tokenOpt.isEmpty()) {
            return "Cihaza ait token bulunamadı";
        }

        String fcmToken = tokenOpt.get().getFcmToken();

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        return "Gönderildi: " + response;
    }

    public String sendToPlatform(String platform, String title, String body) {
        List<DeviceToken> targets;

        if ("both".equalsIgnoreCase(platform)) {
            targets = repo.findAll();
        } else {
            targets = repo.findByPlatformIgnoreCase(platform);
        }

        if (targets.isEmpty()) {
            return "Uygun cihaz bulunamadı.";
        }

        int success = 0, fail = 0;
        for (DeviceToken token : targets) {
            try {
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();

                Message message = Message.builder()
                        .setToken(token.getFcmToken())
                        .setNotification(notification)
                        .build();

                FirebaseMessaging.getInstance().send(message);
                success++;
            } catch (FirebaseMessagingException e) {
                fail++;
                System.err.println("❌ Bildirim hatası (" + token.getDeviceId() + "): " + e.getMessage());
            }
        }

        return "Gönderim tamamlandı. Başarılı: " + success + ", Hatalı: " + fail;
    }
}
