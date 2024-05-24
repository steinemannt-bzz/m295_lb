package com.tiago.m295_lb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteTest {

    private final TestConfig testConfig = new TestConfig();

    @Test
    public void testDeleteExistingAnimal() throws IOException {
        HttpDelete request = new HttpDelete(TestConfig.SERVICE_URL + "/delete/" + TestConfig.EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderDelete(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_NO_CONTENT, statusCode);
    }

    @Test
    public void testDeleteNonExistingAnimal() throws IOException {
        HttpDelete request = new HttpDelete(TestConfig.SERVICE_URL + "/delete/" + TestConfig.NON_EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderDelete(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_NOT_FOUND, statusCode);
        assertEquals("Animal with Id: " + TestConfig.NON_EXISTING_ANIMAL_ID + " not found", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testDeleteAnimalWithoutPermission() throws IOException {
        HttpDelete request = new HttpDelete(TestConfig.SERVICE_URL + "/delete/" + TestConfig.EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderDelete(request, TestConfig.EMPLOYEE_USERNAME, TestConfig.EMPLOYEE_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertEquals("You cannot access this resource", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testDeleteAllAnimals() throws IOException {
        HttpDelete request = new HttpDelete(TestConfig.SERVICE_URL + "/deleteAll");
        testConfig.addBasicAuthHeaderDelete(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_NO_CONTENT, statusCode);
    }

    @Test
    public void testDeleteAllAnimalWithoutPermission() throws IOException {
        HttpDelete request = new HttpDelete(TestConfig.SERVICE_URL + "/deleteAll");
        testConfig.addBasicAuthHeaderDelete(request, TestConfig.EMPLOYEE_USERNAME, TestConfig.EMPLOYEE_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        assertEquals("You cannot access this resource", testConfig.extractResponseBody(httpResponse));
    }
}
