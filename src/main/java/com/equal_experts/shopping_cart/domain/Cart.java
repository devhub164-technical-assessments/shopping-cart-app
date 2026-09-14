package com.equal_experts.shopping_cart.domain;

import com.equal_experts.shopping_cart.exception.ProductQuantityInvalidException;
import com.equal_experts.shopping_cart.exception.ProductRetrievalException;
import com.equal_experts.shopping_cart.provider.ProductProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final ProductProvider productProvider;

    private final Map<String, CartItem> items = new HashMap<>();

    public Cart(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.125);

    public void addProduct(String title, int quantity) throws ProductRetrievalException, ProductQuantityInvalidException {

        if (quantity < 1) {
            throw new ProductQuantityInvalidException("Quantity must be 1 or above.");
        }

        if (this.items.containsKey(title)) {
            CartItem item = this.items.get(title);
            item.increaseQuantity(quantity);
            return;
        }

        Product product = productProvider.findProductByTitle(title);
        this.items.put(product.title(), new CartItem(product, quantity));

    }

    public BigDecimal getProductPrice(String title) {
        CartItem item = items.get(title);
        return item == null ? BigDecimal.ZERO : item.getProduct().price();
    }

    public int getProductQuantity(String title) {
        CartItem item = items.get(title);
        return item == null ? 0 : item.getQuantity();
    }

    public BigDecimal calculateSubTotal() {
        return this.items.values().stream()
                .map(CartItem::calculateItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTax(BigDecimal subTotal) {
        return subTotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotal() {
        BigDecimal subTotal = calculateSubTotal();
        BigDecimal tax = calculateTax(subTotal);

        return subTotal.add(tax);
    }

}
