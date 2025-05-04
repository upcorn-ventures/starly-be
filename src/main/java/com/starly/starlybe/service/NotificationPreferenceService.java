package com.starly.starlybe.service;

import com.starly.starlybe.client.SupabaseClient;
import com.starly.starlybe.util.TimeConversionUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationPreferenceService {

    private final SupabaseClient supabaseClient;

    public NotificationPreferenceService(SupabaseClient supabaseClient) {
        this.supabaseClient = supabaseClient;
    }

    public String savePreferences(String userId, String deviceId, String timeZone,
                                  List<String> userLocalTimes, boolean enabled) {

        List<String> serverLocalTimes = TimeConversionUtil.convertToServerLocalTimes(userLocalTimes, timeZone);

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", userId);
        data.put("device_id", deviceId);
        data.put("time_zone", timeZone);
        data.put("user_local_times", userLocalTimes);
        data.put("server_local_times", serverLocalTimes);
        data.put("enabled", enabled);

        try {
            boolean success = supabaseClient.upsert("notification_preferences", data, "device_id");

            if (success) {
                System.out.println("✅ Kayıt başarıyla eklendi veya güncellendi (device_id=" + deviceId + ")");
                return "Preferences saved or updated successfully";
            } else {
                System.err.println("❌ Supabase upsert başarısız.");
                return "Upsert failed";
            }

        } catch (Exception e) {
            System.err.println("❌ Supabase işlem hatası: " + e.getMessage());
            return "Unexpected error occurred";
        }
    }
}
