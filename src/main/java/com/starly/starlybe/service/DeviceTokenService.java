package com.starly.starlybe.service;

import com.starly.starlybe.dto.DeviceTokenDto;
import com.starly.starlybe.entity.DeviceToken;
import com.starly.starlybe.repository.DeviceTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DeviceTokenService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceTokenService.class);

    @Autowired
    private DeviceTokenRepository repo;

    public String registerToken(DeviceTokenDto dto) {
        try {
            Optional<DeviceToken> existing = repo.findByDeviceId(dto.getDeviceId());

            DeviceToken token = existing.orElseGet(DeviceToken::new);
            token.setDeviceId(dto.getDeviceId());
            token.setFcmToken(dto.getFcmToken());
            token.setApnsToken(dto.getApnsToken());
            token.setPlatform(dto.getPlatform());
            token.setUpdatedAt(LocalDateTime.now());

            String message = existing.isPresent() ? "Token güncellendi" : "Token kaydedildi";
            repo.save(token);

            MDC.put("LOG_TAG", "info");
            logger.info("{}: {}", message, dto.getDeviceId());
            return message;
        } catch (Exception e) {
            MDC.put("LOG_TAG", "error");
            logger.error("Token kayıt hatası: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}
