package com.devhub164.shopping_cart.provider;

import com.devhub164.shopping_cart.client.ProductClient;
import com.devhub164.shopping_cart.domain.Product;
import com.devhub164.shopping_cart.exception.ProductRetrievalException;

public class ProductProvider {

    private final ProductClient productClient;

    public ProductProvider(ProductClient productClient) {
        this.productClient = productClient;
    }

    public Product findProductByTitle(String title) throws ProductRetrievalException {
        return productClient.findProductByTitle(title);
    }

}
