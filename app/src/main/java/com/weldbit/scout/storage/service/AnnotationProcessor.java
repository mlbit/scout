package com.weldbit.scout.storage.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.weldbit.scout.storage.annotations.Column;
import com.weldbit.scout.storage.annotations.PrimaryKey;

public class AnnotationProcessor {
    public static String jsonString(Object object) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Map<String, String> jsonElementsMapColumnKey = new HashMap<>();
        Map<String, String> jsonElementsMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) {
                jsonElementsMap.put(field.getName(), (String) field.get(object));
            } else if (field.isAnnotationPresent(PrimaryKey.class)) {
                jsonElementsMapColumnKey.put(field.getName(), (String) field.get(object));
            }
            field.setAccessible(false);
        }

        String jsonPKString = jsonElementsMapColumnKey.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));

        String jsonString = jsonElementsMap.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));
        return "{" + (jsonPKString.isEmpty() ? jsonString : jsonPKString + "," + jsonString) + "}";
    }

}
