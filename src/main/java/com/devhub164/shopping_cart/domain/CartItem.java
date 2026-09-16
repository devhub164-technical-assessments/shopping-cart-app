package com.devhub164.shopping_cart.domain;

import java.math.BigDecimal;

public class CartItem {

    private final Product product;

    private static final String PROMOTIONAL_PRODUCT = "Cornflakes";

    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public BigDecimal calculateItemTotalPrice() {
        int billingQuantity = quantity;

       if(PROMOTIONAL_PRODUCT.equalsIgnoreCase(product.title())){
           int freeItems = quantity / 3;
           billingQuantity = quantity - freeItems;
       }
        return product.price().multiply(BigDecimal.valueOf(billingQuantity));
    }

}
