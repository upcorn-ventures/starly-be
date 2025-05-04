package com.starly.starlybe.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationPreferenceRequest {
    private String userId;
    private String deviceId;
    private String timeZone;
    private List<String> userLocalTimes;
    private boolean enabled;  // true = aktif, false = bildirim kapalı
}
