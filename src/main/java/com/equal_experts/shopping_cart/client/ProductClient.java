package com.equal_experts.shopping_cart.client;

import com.equal_experts.shopping_cart.domain.Product;
import com.equal_experts.shopping_cart.exception.ProductRetrievalException;
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
        this.objectMapper = new ObjectMapper();
    }

    public Product findProductByTitle(String title) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + title + ".json"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            String message = (title == null || title.isEmpty()) ?
                    "Failed to retrieve product: title is null or empty" : "Failed to retrieve product with title: " + title;
            throw new ProductRetrievalException(message, e);
        }

    }
}
