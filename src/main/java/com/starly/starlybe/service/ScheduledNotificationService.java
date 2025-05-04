package com.starly.starlybe.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.starly.starlybe.client.SupabaseClient;
import com.starly.starlybe.dto.AffirmationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ScheduledNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledNotificationService.class);

    private final SupabaseClient supabaseClient;
    private final AffirmationService affirmationService;
    private final NotificationService notificationService;

    public ScheduledNotificationService(
            SupabaseClient supabaseClient,
            AffirmationService affirmationService,
            NotificationService notificationService) {
        this.supabaseClient = supabaseClient;
        this.affirmationService = affirmationService;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 * * * * *") // her dakika başı
    public void checkAndSendNotifications() {
        String currentUtcTime = ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("HH:mm"));

        log.info("🕒 Scheduled kontrol başladı. Şu anki UTC saat: {}", currentUtcTime);

        List<Map<String, Object>> users = supabaseClient.queryPreferencesByTime(currentUtcTime);

        if (users.isEmpty()) {
            log.info("🔕 Bu saate uygun kullanıcı bulunamadı.");
        }

        for (Map<String, Object> pref : users) {
            String userId = (String) pref.get("user_id");
            String deviceId = (String) pref.get("device_id");

            log.info("🎯 Kullanıcı bulundu: userId={}, deviceId={}", userId, deviceId);

            AffirmationDto affirmation = affirmationService.getRandomActiveByUserId(userId);
            if (affirmation == null) {
                log.warn("⚠️ Aktif affirmation bulunamadı: userId={}", userId);
                continue;
            }

            log.info("📨 Gönderilecek affirmation: id={}, text=\"{}\"", affirmation.getId(), affirmation.getText());

            try {
                notificationService.sendNotificationToDevice(deviceId, "Bugünkü Mesajın", affirmation.getText());
                log.info("✅ Bildirim gönderildi: deviceId={}, userId={}", deviceId, userId);

                affirmationService.deactivateAffirmation(affirmation);
                log.info("📉 Affirmation pasife çekildi: id={}", affirmation.getId());

            } catch (FirebaseMessagingException e) {
                log.error("❌ Bildirim gönderimi başarısız: userId={}, cihaz={}, hata={}",
                        userId, deviceId, e.getMessage());
            }
        }
    }
}
