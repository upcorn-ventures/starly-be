package com.starly.starlybe.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class SupabaseClient {

    @Value("${supabase.api.url}")
    private String supabaseApiUrl;

    @Value("${supabase.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<Map<String, Object>> queryPreferencesByTime(String hhmm) {
        try {
            String encoded = URLEncoder.encode("{\"" + hhmm + "\"}", StandardCharsets.UTF_8);
            String url = supabaseApiUrl + "notification_preferences" +
                    "?enabled=eq.true&server_local_times=cs." + encoded;

            ResponseEntity<List> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    new HttpEntity<>(getHeaders()),
                    List.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("❌ queryPreferencesByTime hatası: " + e.getMessage());
            return List.of();
        }
    }

    public List<Map<String, Object>> query(String fullPathWithQueryParams) {
        try {
            String url = supabaseApiUrl + fullPathWithQueryParams;
            ResponseEntity<List> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    new HttpEntity<>(getHeaders()),
                    List.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("❌ Supabase query hatası: " + e.getMessage());
            return List.of();
        }
    }

    public boolean upsert(String table, Map<String, Object> data, String conflictColumn) {
        try {
            String url = supabaseApiUrl + table + "?on_conflict=" + conflictColumn;

            HttpHeaders headers = getHeaders();
            headers.set("Prefer", "resolution=merge-duplicates");
            headers.add("Prefer", "return=minimal");

            HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(List.of(data), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.POST,
                    request,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            System.err.println("❌ Supabase upsert hatası: " + e.getMessage());
            return false;
        }
    }

    public boolean insert(String tableName, Map<String, Object> data) {
        try {
            String url = supabaseApiUrl + tableName;

            HttpHeaders headers = getHeaders();
            headers.set("Prefer", "return=representation");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("❌ Supabase insert başarısız: " + response.getStatusCode() + " - " + response.getBody());
                return false;
            } else {
                System.out.println("✅ Insert başarılı: " + response.getBody());
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ Supabase insert hatası: " + e.getMessage());
            return false;
        }
    }
}
