package com.alien.attendance.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class BodyValidation {
    public <T> ResponseEntity<?> validateBody(T dto) throws IllegalAccessException {
        Field[] fields = dto.getClass().getDeclaredFields();
        boolean isValid = true;
        List<String> listMissing = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(dto) == null) {
                isValid = false;
                listMissing.add(field.getName());
            }
        }
        if (!isValid) {
            return ResponseEntity.badRequest().body("One or more properties are null: " + listMissing);
        }
        return ResponseEntity.ok("ok");
    }
}