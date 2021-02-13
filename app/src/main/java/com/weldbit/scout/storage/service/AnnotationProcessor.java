package com.weldbit.scout.storage.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.hash.Hashing;
import com.weldbit.scout.storage.annotations.Column;
import com.weldbit.scout.storage.annotations.NotEmpty;
import com.weldbit.scout.storage.annotations.PrimaryKey;
import com.weldbit.scout.storage.exceptions.ExceptionAnnotation;

public class AnnotationProcessor<T> {
    T object ;
    Map<String, String> jsonElementsMapColumnKey = new HashMap<>();
    Map<String, String> jsonElementsMap = new HashMap<>();

    public AnnotationProcessor(T _object) {
        object = _object;
 
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Column.class)) {
                    if (field.isAnnotationPresent(NotEmpty.class) && field.get(object) == null) {
                        try {
                            throw new ExceptionAnnotation("1001", "Empty Value.");
                        } catch (ExceptionAnnotation e) {
                            e.printStackTrace();
                        }
                    }
                    jsonElementsMap.put(field.getName(), (String) field.get(object));
                } else if (field.isAnnotationPresent(PrimaryKey.class)) {
                    jsonElementsMapColumnKey.put(field.getName(), (String) field.get(object));
                }
                field.setAccessible(false);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }

    public  String jsonString() throws IllegalArgumentException, IllegalAccessException {

        String jsonPKString = jsonElementsMapColumnKey.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));

        String jsonString = jsonElementsMap.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));
        return "{" + (jsonPKString.isEmpty() ? jsonString : jsonPKString + "," + jsonString) + "}";
    }

    public String primaryKeyValue() {
        return jsonElementsMapColumnKey.entrySet().stream()
        .map(entry -> entry.getValue())
        .collect(Collectors.joining());
    }

    public String primaryKeyHash() {
        return Hashing.sha256().hashString(primaryKeyValue(), StandardCharsets.UTF_8).toString();
    }
}
