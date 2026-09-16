package com.devhub164.shopping_cart.exception;

public class ProductRetrievalException extends RuntimeException {

    public ProductRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductRetrievalException(String message) {
        super(message);
    }
}
