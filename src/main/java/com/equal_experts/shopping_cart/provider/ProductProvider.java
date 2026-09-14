package com.equal_experts.shopping_cart.provider;

import com.equal_experts.shopping_cart.client.ProductClient;
import com.equal_experts.shopping_cart.domain.Product;
import com.equal_experts.shopping_cart.exception.ProductRetrievalException;

public class ProductProvider {

    private final ProductClient productClient;

    public ProductProvider(ProductClient productClient) {
        this.productClient = productClient;
    }

    public Product findProductByTitle(String title) throws ProductRetrievalException {
        return productClient.findProductByTitle(title);
    }

}
