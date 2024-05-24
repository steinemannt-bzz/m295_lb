package com.tiago.m295_lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PutTest {

    private final TestConfig testConfig = new TestConfig();

    @Test
    public void testUpdateExistingAnimal() throws IOException {
        String requestBody = "{\"name\":\"Updated Name\",\"species\":\"Updated Species\",\"dateAcquired\":\"2024-05-10\",\"weight\":\"250\",\"habitat\":\"Updated Habitat\",\"isEndangered\":false,\"keeperId\":2}";

        HttpPut request = new HttpPut(TestConfig.SERVICE_URL + "/update/" + TestConfig.EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderPut(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
    }

    @Test
    public void testUpdateNonExistingAnimal() throws IOException {
        String requestBody = "{\"name\":\"Updated Name\",\"species\":\"Updated Species\",\"dateAcquired\":\"2024-05-10\",\"weight\":\"250\",\"habitat\":\"Updated Habitat\",\"isEndangered\":false,\"keeperId\":2}";

        HttpPut request = new HttpPut(TestConfig.SERVICE_URL + "/update/" + TestConfig.NON_EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderPut(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_NOT_FOUND, statusCode);
        assertEquals("Animal with Id: " + TestConfig.NON_EXISTING_ANIMAL_ID + " not found", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testUpdateAnimalWithoutPermission() throws IOException {
        String requestBody = "{\"name\":\"Updated Name\",\"species\":\"Updated Species\",\"dateAcquired\":\"2024-05-10\",\"weight\":\"250\",\"habitat\":\"Updated Habitat\",\"isEndangered\":false,\"keeperId\":2}";

        HttpPut request = new HttpPut(TestConfig.SERVICE_URL + "/update/" + TestConfig.EXISTING_ANIMAL_ID);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(requestBody));

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertEquals("You cannot access this resource", testConfig.extractResponseBody(httpResponse));
    }
}
