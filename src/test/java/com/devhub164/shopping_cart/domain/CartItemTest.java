package com.devhub164.shopping_cart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemTest {

    private CartItem underTest;

    @BeforeEach
    public void setup() {
        Product product = new Product("Cornflakes", BigDecimal.valueOf(2.52));
        underTest = new CartItem(product, 1);
    }

    @Test
    void shouldIncreaseQuantity() {
        underTest.increaseQuantity(1);
        assertEquals(2, underTest.getQuantity());
    }

    @Test
    void shouldCalculateItemTotalPrice() {
        underTest.increaseQuantity(1);
        BigDecimal itemTotal = underTest.calculateItemTotalPrice();
        assertEquals(0, BigDecimal.valueOf(5.04).compareTo(itemTotal));
    }

    @Test
    void shouldApplyBuyTwoGetOneFree(){
        underTest.increaseQuantity(2);
        assertEquals(3, underTest.getQuantity());
        BigDecimal itemTotal = underTest.calculateItemTotalPrice();
        assertEquals(0, BigDecimal.valueOf(5.04).compareTo(itemTotal));
    }

    @Test
    void shouldBeBilledForThreeItemsWhenQuantityIsFour(){
        underTest.increaseQuantity(3);
        assertEquals(4, underTest.getQuantity());
        BigDecimal itemTotal = underTest.calculateItemTotalPrice();
        assertEquals(0, BigDecimal.valueOf(7.56).compareTo(itemTotal));
    }

    @Test
    void shouldBeBilledForFourItemsWhenQuantityIsSix(){
        underTest.increaseQuantity(5);
        assertEquals(6, underTest.getQuantity());
        BigDecimal itemTotal = underTest.calculateItemTotalPrice();
        assertEquals(0, BigDecimal.valueOf(10.08).compareTo(itemTotal));
    }
}