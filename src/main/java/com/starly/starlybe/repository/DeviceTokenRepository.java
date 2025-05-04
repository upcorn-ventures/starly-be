package com.starly.starlybe.repository;

import com.starly.starlybe.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByDeviceId(String userId);
    List<DeviceToken> findByPlatformIgnoreCase(String platform);

}