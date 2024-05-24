package com.tiago.m295_lb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tiago.m295_lb.models.Animal;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GetTest {

    public static final String SERVICE_URL = "http://localhost:8080/webapp/resources/animals";
    public static final int EXISTING_ANIMAL_ID = 1;
    public static final int NON_EXISTING_ANIMAL_ID = 999;
    public static final String EXISTING_DATE_FILTER = "2024-05-01";
    public static final String EXISTING_TEXT_FILTER = "Savanna";

    private void addBasicAuthHeader(HttpGet request, String username, String password) {
        String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        request.setHeader("Authorization", authHeader);
    }

    private String extractResponseBody(HttpResponse httpResponse) throws IOException {
        Scanner scanner = new Scanner(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        scanner.useDelimiter("\\A");
        String responseBody = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return responseBody;
    }

    private List<Animal> parseAnimalListFromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    @Test
    public void testGetExistingAnimalById() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/get/" + EXISTING_ANIMAL_ID);
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
    }

    @Test
    public void testGetNonExistingAnimalById() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/get/" + NON_EXISTING_ANIMAL_ID);
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_NOT_FOUND, statusCode);
    }

    @Test
    public void testCheckIfAnimalExists() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/exists/" + EXISTING_ANIMAL_ID);
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
        assertEquals("true", extractResponseBody(httpResponse));
    }

    @Test
    public void testCheckIfNonExistingAnimalExists() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/exists/" + NON_EXISTING_ANIMAL_ID);
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
        assertEquals("false", extractResponseBody(httpResponse));
    }

    @Test
    public void testGetAllAnimals() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/getAll");
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            String responseBody = extractResponseBody(httpResponse);
            List<Animal> animals = parseAnimalListFromJson(responseBody);
            assertFalse(animals.isEmpty());
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, statusCode);
        }
    }

    @Test
    public void testGetAnimalsByDate() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/filter/date/" + EXISTING_DATE_FILTER);
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            String responseBody = extractResponseBody(httpResponse);
            List<Animal> animals = parseAnimalListFromJson(responseBody);
            assertFalse(animals.isEmpty());
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, statusCode);
        }
    }

    @Test
    public void testGetAnimalsByText() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/filter/text/" + EXISTING_TEXT_FILTER);
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            String responseBody = extractResponseBody(httpResponse);
            List<Animal> animals = parseAnimalListFromJson(responseBody);
            assertFalse(animals.isEmpty(), "No animals found for the provided text");
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, statusCode, "Unexpected status code");
        }
    }

    @Test
    public void testGetAnimalCount() throws IOException {
        HttpGet request = new HttpGet(SERVICE_URL + "/count");
        addBasicAuthHeader(request, "admin", "0000");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
    }
}
