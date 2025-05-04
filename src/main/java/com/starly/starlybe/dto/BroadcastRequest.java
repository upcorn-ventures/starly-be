package com.starly.starlybe.dto;

import lombok.Data;

@Data
public class BroadcastRequest {
    private String platform;// "ios", "android" veya "both"
    private String title;
    private String body;

}
