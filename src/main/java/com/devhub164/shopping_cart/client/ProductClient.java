package com.devhub164.shopping_cart.client;

import com.devhub164.shopping_cart.domain.Product;
import com.devhub164.shopping_cart.exception.ProductRetrievalException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProductClient {

    private static final String BASE_URL = "https://equalexperts.github.io/backend-take-home-test-data/";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public ProductClient(HttpClient client) {
       this.client = client;
        objectMapper = new ObjectMapper();
    }

    public Product findProductByTitle(String title) {

        if(title == null || title.isBlank()) {
            throw new ProductRetrievalException("Failed to retrieve product: title is null or empty");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + title + ".json"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw new ProductRetrievalException("Failed to retrieve product with title: " + title + ". " +
                        "HTTP status code: " + response.statusCode());
            }

            return objectMapper.readValue(response.body(), new TypeReference<>() {});

        } catch (IOException e) {
            throw new ProductRetrievalException("Failed to retrieve product with title: " + title, e);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProductRetrievalException("Request interrupted while retrieving product with title: " + title, e);
        }

    }
}
