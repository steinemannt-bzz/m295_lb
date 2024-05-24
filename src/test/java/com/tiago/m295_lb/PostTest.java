package com.tiago.m295_lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    private final TestConfig testConfig = new TestConfig();

    @Test
    public void testCreateTables() throws IOException {
        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/createTables");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
        assertEquals("Tables created successfully", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testCreateTablesWithoutPermission() throws IOException {
        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/createTables");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertEquals("You cannot access this resource", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testCreateAnimal() throws IOException {
        String requestBody = "{\"name\":\"Leon\",\"species\":\"Lion\",\"dateAcquired\":\"2024-05-05\",\"weight\":\"200\",\"habitat\":\"rain forest\",\"isEndangered\":true,\"keeperId\":1}";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/create");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_CREATED, statusCode);
    }

    @Test
    public void testCreateAnimalWithoutAttributesNameAndSpecies() throws IOException {
        String requestBody = "{\"dateAcquired\":\"2024-05-05\",\"weight\":\"200\",\"habitat\":\"rain forest\",\"isEndangered\":true,\"keeperId\":1}";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/create");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String expectedOutput1 = "Validation errors:\n- Name cannot be null.\n- Species cannot be null.";
        String expectedOutput2 = "Validation errors:\n- Species cannot be null.\n- Name cannot be null.";

        String responseBody = testConfig.extractResponseBody(httpResponse);

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
        assertTrue(responseBody.equals(expectedOutput1) || responseBody.equals(expectedOutput2));
    }

    @Test
    public void testCreateAnimalWithNullAttributesNameAndSpecies() throws IOException {
        String requestBody = "{\"name\":null,\"species\":null,\"dateAcquired\":\"2024-05-05\",\"weight\":\"200\",\"habitat\":\"rain forest\",\"isEndangered\":true,\"keeperId\":1}";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/create");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String expectedOutput1 = "Validation errors:\n- Name cannot be null.\n- Species cannot be null.";
        String expectedOutput2 = "Validation errors:\n- Species cannot be null.\n- Name cannot be null.";

        String responseBody = testConfig.extractResponseBody(httpResponse);

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
        assertTrue(responseBody.equals(expectedOutput1) || responseBody.equals(expectedOutput2));
    }

    @Test
    public void testCreateAnimalWithWrongDataTypeForAttributeName() throws IOException {
        String requestBody = "{\1:\"Leon\",\"species\":\"Lion\",\"dateAcquired\":\"2024-05-05\",\"weight\":\"200\",\"habitat\":\"rain forest\",\"isEndangered\":true,\"keeperId\":1}";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/create");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
    }

    @Test
    public void testCreateAnimalWithFutureAttributeAcquireDate() throws IOException {
        String requestBody = "{\"name\":\"Leon\",\"species\":\"Lion\",\"dateAcquired\":\"2099-05-05\",\"weight\":\"200\",\"habitat\":\"rain forest\",\"isEndangered\":true,\"keeperId\":1}";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/create");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
        assertEquals("Validation errors:\n- Acquire date must be in the past or present.", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testCreateAnimalWithFaultyAttributeAcquireDate() throws IOException {
        String requestBody = "{\"name\":\"Leon\",\"species\":\"Lion\",\"dateAcquired\":\"2024-05.05\",\"weight\":\"200\",\"habitat\":\"rain forest\",\"isEndangered\":true,\"keeperId\":1}";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/create");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
    }

    @Test
    public void testCreateAnimalWithoutPermission() throws IOException {
        String requestBody = "{\"name\":\"Leon\",\"species\":\"Lion\",\"dateAcquired\":\"2024-05-05\",\"weight\":\"200\",\"habitat\":\"rain forest\",\"isEndangered\":true,\"keeperId\":1}";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/create");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertEquals("You cannot access this resource", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testCreateMultipleAnimals() throws IOException {
        String requestBody = "[" + "{\"name\":\"Simba\",\"species\":\"Lion\",\"dateAcquired\":\"2024-05-05\",\"weight\":\"150\",\"habitat\":\"Savanna\",\"isEndangered\":true,\"keeperId\":2}," + "{\"name\":\"Dumbo\",\"species\":\"Elephant\",\"dateAcquired\":\"2024-05-05\",\"weight\":\"300\",\"habitat\":\"Forest\",\"isEndangered\":false,\"keeperId\":3}" + "]";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/createMultiple");
        testConfig.addBasicAuthHeaderPost(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_CREATED, statusCode);
    }

    @Test
    public void testCreateMultipleAnimalsWithoutPermission() throws IOException {
        String requestBody = "[" + "{\"name\":\"Simba\",\"species\":\"Lion\",\"dateAcquired\":\"2024-05-05\",\"weight\":\"150\",\"habitat\":\"Savanna\",\"isEndangered\":true,\"keeperId\":2}," + "{\"name\":\"Dumbo\",\"species\":\"Elephant\",\"dateAcquired\":\"2024-05-05\",\"weight\":\"300\",\"habitat\":\"Forest\",\"isEndangered\":false,\"keeperId\":3}" + "]";

        HttpPost request = new HttpPost(TestConfig.SERVICE_URL + "/createMultiple");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertEquals("You cannot access this resource", testConfig.extractResponseBody(httpResponse));
    }
}
