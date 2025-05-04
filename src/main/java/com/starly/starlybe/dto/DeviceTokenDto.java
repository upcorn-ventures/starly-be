package com.starly.starlybe.dto;


import lombok.Data;

@Data
public class DeviceTokenDto {
    private String fcmToken;
    private String apnsToken;
    private String platform;
    private String deviceId;
}