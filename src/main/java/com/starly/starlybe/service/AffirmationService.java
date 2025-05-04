package com.starly.starlybe.service;

import com.starly.starlybe.client.SupabaseClient;
import com.starly.starlybe.dto.AffirmationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class AffirmationService {

    private final SupabaseClient supabaseClient;

    public AffirmationService(SupabaseClient supabaseClient) {
        this.supabaseClient = supabaseClient;
    }

    public AffirmationDto getRandomActiveByUserId(String userId) {
        String query = UriComponentsBuilder.fromPath("affirmations")
                .queryParam("user_id", "eq." + userId)
                .queryParam("is_active", "eq.true")
                .toUriString();

        List<Map<String, Object>> result = supabaseClient.query(query);

        if (result.isEmpty()) {
            return null;
        }

        Map<String, Object> selected = result.get(new Random().nextInt(result.size()));
        return mapToAffirmationDto(selected);
    }

    public void deactivateAffirmation(AffirmationDto affirmation) {
        Map<String, Object> update = new HashMap<>();
        update.put("id", affirmation.getId()); // upsert için gerekli
        update.put("is_active", false);
        update.put("text", affirmation.getText()); // zorunlu alan
        update.put("user_id", affirmation.getUserId()); // zorunlu alan

        supabaseClient.upsert("affirmations", update, "id"); // ← PATCH yerine UPSERT
    }

    private AffirmationDto mapToAffirmationDto(Map<String, Object> data) {
        String id = String.valueOf(data.get("id"));
        String userId = String.valueOf(data.get("user_id"));
        String text = String.valueOf(data.get("text"));
        boolean isActive = Boolean.parseBoolean(String.valueOf(data.get("is_active")));

        return new AffirmationDto(id, userId, text, isActive);
    }
}
