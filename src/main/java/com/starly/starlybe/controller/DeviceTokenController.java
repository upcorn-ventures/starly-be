package com.starly.starlybe.controller;

import com.starly.starlybe.dto.DeviceTokenDto;
import com.starly.starlybe.service.DeviceTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/device-token")
public class DeviceTokenController {

    @Autowired
    private DeviceTokenService service;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody DeviceTokenDto dto) {
        String message = service.registerToken(dto);
        return ResponseEntity.ok(Map.of("message", message));
    }
}