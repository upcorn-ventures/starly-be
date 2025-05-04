package com.starly.starlybe.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimitProperties rateLimitProperties;
    private final Map<String, Long> lastAccessMap = new ConcurrentHashMap<>();

    public RateLimitingInterceptor(RateLimitProperties rateLimitProperties) {
        this.rateLimitProperties = rateLimitProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        long now = System.currentTimeMillis();

        Long lastAccess = lastAccessMap.getOrDefault(path, 0L);
        long limit = getRateLimitForPath(path);

        if (now - lastAccess < limit) {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Rate limit exceeded for " + path);
            return false;
        }

        lastAccessMap.put(path, now);
        return true;
    }

    private long getRateLimitForPath(String path) {
        return switch (path) {
            case "/push/send" -> rateLimitProperties.getPushSend();
            case "/push/broadcast" -> rateLimitProperties.getPushBroadcast();
            case "/notification-preferences/save" -> rateLimitProperties.getNotificationPreferencesSave();
            case "/device-token/register" -> rateLimitProperties.getDeviceTokenRegister();
            default -> 0L; // Limit uygulanmayan path'ler
        };
    }
}
