package com.starly.starlybe.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String deviceId;
    private String title;
    private String body;
}
