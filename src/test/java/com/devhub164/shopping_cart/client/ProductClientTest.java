package com.devhub164.shopping_cart.client;

import com.devhub164.shopping_cart.domain.Product;
import com.devhub164.shopping_cart.exception.ProductRetrievalException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductClientTest {

    private final ProductClient underTest = new ProductClient();

    @Test
    void shouldFindProductByTitle() throws ProductRetrievalException {
        Product product = underTest.findProductByTitle("cheerios");
        assertNotNull(product);
        assertEquals("Cheerios", product.title());
        assertEquals(BigDecimal.valueOf(8.43), product.price());
    }

    @Test
    void shouldThrowExceptionOnRetrievingProductWithUnknownTitle() throws ProductRetrievalException {
        assertThrows(ProductRetrievalException.class, () -> underTest.findProductByTitle("unknown_product"));
    }

    @Test
    void shouldThrowExceptionOnRetrievingProductWithEmptyTitle() throws ProductRetrievalException {
        assertThrows(ProductRetrievalException.class, () -> underTest.findProductByTitle(""));
    }

    @Test
    void shouldThrowExceptionOnRetrievingProductWithNullTitle() throws ProductRetrievalException {
        assertThrows(ProductRetrievalException.class, () -> underTest.findProductByTitle(null));
    }
}