package com.tiago.m295_lb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tiago.m295_lb.models.Animal;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class TestConfig {

    public static final String SERVICE_URL = "http://localhost:8080/webapp/resources/animals";

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "0000";
    public static final String EMPLOYEE_USERNAME = "employee";
    public static final String EMPLOYEE_PASSWORD = "1111";

    public static final int EXISTING_ANIMAL_ID = 1;
    public static final int NON_EXISTING_ANIMAL_ID = 999;

    public static final String EXISTING_DATE_FILTER = "2024-05-01";
    public static final String EXISTING_TEXT_FILTER = "Savanna";

    public void addBasicAuthHeaderGet(HttpGet request, String username, String password) {
        String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        request.setHeader("Authorization", authHeader);
    }

    public void addBasicAuthHeaderPost(HttpPost request, String username, String password) {
        String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        request.setHeader("Authorization", authHeader);
    }

    public void addBasicAuthHeaderPut(HttpPut request, String username, String password) {
        String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        request.setHeader("Authorization", authHeader);
    }

    public void addBasicAuthHeaderDelete(HttpDelete request, String username, String password) {
        String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        request.setHeader("Authorization", authHeader);
    }

    public String extractResponseBody(HttpResponse httpResponse) throws IOException {
        try (Scanner scanner = new Scanner(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }
    }

    public List<Animal> parseAnimalListFromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }
}
