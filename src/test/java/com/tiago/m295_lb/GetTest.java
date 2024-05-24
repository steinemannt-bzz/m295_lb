package com.tiago.m295_lb;

import com.tiago.m295_lb.models.Animal;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetTest {

    private final TestConfig testConfig = new TestConfig();

    @Test
    public void testGetExistingAnimalById() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/get/" + TestConfig.EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
    }

    @Test
    public void testGetNonExistingAnimalById() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/get/" + TestConfig.NON_EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_NOT_FOUND, statusCode);
    }

    @Test
    public void testCheckIfAnimalExists() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/exists/" + TestConfig.EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
        assertEquals("true", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testCheckIfNonExistingAnimalExists() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/exists/" + TestConfig.NON_EXISTING_ANIMAL_ID);
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
        assertEquals("false", testConfig.extractResponseBody(httpResponse));
    }

    @Test
    public void testGetAllAnimals() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/getAll");
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            String responseBody = testConfig.extractResponseBody(httpResponse);
            List<Animal> animals = testConfig.parseAnimalListFromJson(responseBody);
            assertFalse(animals.isEmpty());
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, statusCode);
        }
    }

    @Test
    public void testGetAnimalsByDate() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/filter/date/" + TestConfig.EXISTING_DATE_FILTER);
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            String responseBody = testConfig.extractResponseBody(httpResponse);
            List<Animal> animals = testConfig.parseAnimalListFromJson(responseBody);
            assertFalse(animals.isEmpty());
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, statusCode);
        }
    }

    @Test
    public void testGetAnimalsByText() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/filter/text/" + TestConfig.EXISTING_TEXT_FILTER);
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            String responseBody = testConfig.extractResponseBody(httpResponse);
            List<Animal> animals = testConfig.parseAnimalListFromJson(responseBody);
            assertFalse(animals.isEmpty(), "No animals found for the provided text");
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, statusCode, "Unexpected status code");
        }
    }

    @Test
    public void testGetAnimalCount() throws IOException {
        HttpGet request = new HttpGet(TestConfig.SERVICE_URL + "/count");
        testConfig.addBasicAuthHeaderGet(request, TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        assertEquals(HttpStatus.SC_OK, statusCode);
    }
}