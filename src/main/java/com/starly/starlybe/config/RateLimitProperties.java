package com.starly.starlybe.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ratelimit")
@Data
public class RateLimitProperties {
    private long pushSend;
    private long pushBroadcast;
    private long notificationPreferencesSave;
    private long deviceTokenRegister;
}
