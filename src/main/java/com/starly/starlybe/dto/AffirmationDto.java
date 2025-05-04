package com.starly.starlybe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AffirmationDto {
    private String id;
    private String userId;
    private String text;
    private boolean isActive;
}
