package com.alien.attendance.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class Location {
    private final RestTemplate restTemplate;

    public Location() {
        this.restTemplate = new RestTemplate();
    }

    public String getLocation(String latitude, String longitude) {
        String url = String.format("https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json", latitude, longitude);
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(response, Map.class);
            String display = map.get("display_name").toString();
            return display;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
