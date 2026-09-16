package com.devhub164.shopping_cart.client;

import com.devhub164.shopping_cart.domain.Product;
import com.devhub164.shopping_cart.exception.ProductRetrievalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private ProductClient underTest;

    @BeforeEach
    public void setup() {
        underTest = new ProductClient(httpClient);
    }

    @Test
    void shouldFindProductByTitle() throws ProductRetrievalException, IOException, InterruptedException {
        String expectedJsonResponse = "{\"title\":\"Cheerios\",\"price\":8.43}";

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(expectedJsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        Product product = underTest.findProductByTitle("cheerios");

        assertEquals("Cheerios", product.title());
        assertEquals(BigDecimal.valueOf(8.43), product.price());
    }

    @Test
    void shouldThrowExceptionWhenProductTitleIsNull(){
        assertThrows(ProductRetrievalException.class, () -> underTest.findProductByTitle(null));
        verifyNoInteractions(httpClient);
    }

    @Test
    void shouldThrowExceptionWhenProductTitleIsEmpty(){
        assertThrows(ProductRetrievalException.class, () -> underTest.findProductByTitle(" "));
        verifyNoInteractions(httpClient);
    }

    @Test
    void shouldThrowExceptionWhenProductIsNotFound() throws IOException, InterruptedException {
        when(httpResponse.statusCode()).thenReturn(404);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        ProductRetrievalException exception = assertThrows(ProductRetrievalException.class, () ->
                underTest.findProductByTitle("unknown_product"));

        assertEquals("Failed to retrieve product with title: unknown_product. HTTP status code: 404", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenHttpClientFails() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        ProductRetrievalException exception = assertThrows(ProductRetrievalException.class, () ->
                underTest.findProductByTitle("cheerios"));

        assertEquals("Failed to retrieve product with title: cheerios", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidJsonResponse() throws IOException, InterruptedException {
        String invalidResponse = "invalid json";

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(invalidResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        assertThrows(ProductRetrievalException.class, () ->
                underTest.findProductByTitle("cheerios"));

    }

    @Test
    void shouldSendGetRequestForProductTitle() throws IOException, InterruptedException, ProductRetrievalException {
        String expectedJsonResponse = "{\"title\":\"Cheerios\",\"price\":8.43}";

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(expectedJsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        underTest.findProductByTitle("cheerios");

        ArgumentCaptor<HttpRequest> captor =
                ArgumentCaptor.forClass(HttpRequest.class);

        verify(httpClient).send(captor.capture(), any(HttpResponse.BodyHandler.class));

        HttpRequest request = captor.getValue();

        assertEquals("https://equalexperts.github.io/backend-take-home-test-data/cheerios.json",
                request.uri().toString());
        assertEquals("GET", request.method());
    }

}