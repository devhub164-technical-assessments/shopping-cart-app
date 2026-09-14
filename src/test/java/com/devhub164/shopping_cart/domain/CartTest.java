package com.devhub164.shopping_cart.domain;

import com.devhub164.shopping_cart.exception.ProductQuantityInvalidException;
import com.devhub164.shopping_cart.exception.ProductRetrievalException;
import com.devhub164.shopping_cart.provider.ProductProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartTest {

    private Cart underTest;

    @Mock
    private ProductProvider productProvider;

    private static final String FIRST_PRODUCT = "Cornflakes";
    private static final String SECOND_PRODUCT = "Weetabix";

    @BeforeEach
    public void setup() {
        underTest = new Cart(productProvider);
    }

    @Test
    void shouldIncreaseQuantityOnAddingSameProduct() throws ProductQuantityInvalidException, ProductRetrievalException {
        when(productProvider.findProductByTitle(FIRST_PRODUCT)).thenReturn(new Product(FIRST_PRODUCT, BigDecimal.valueOf(2.52)));
        underTest.addProduct(FIRST_PRODUCT, 1);
        underTest.addProduct(FIRST_PRODUCT, 1);

        assertEquals(2, underTest.getProductQuantity(FIRST_PRODUCT));
        assertEquals(BigDecimal.valueOf(2.52), underTest.getProductPrice(FIRST_PRODUCT));
        verify(productProvider, times(1)).findProductByTitle(FIRST_PRODUCT);

    }

    @Test
    void shouldAddDifferentProduct() throws ProductQuantityInvalidException, ProductRetrievalException {
        setupCartProducts();

        assertEquals(2, underTest.getProductQuantity(FIRST_PRODUCT));
        assertEquals(1, underTest.getProductQuantity(SECOND_PRODUCT));
    }

    @Test
    void shouldCalculateSubTotal() throws ProductQuantityInvalidException, ProductRetrievalException {
        setupCartProducts();
        BigDecimal subTotal = underTest.calculateSubTotal();
        assertEquals(0, BigDecimal.valueOf(15.02).compareTo(subTotal));
    }

    @Test
    void shouldCalculateTax() throws ProductQuantityInvalidException, ProductRetrievalException {
        setupCartProducts();
        BigDecimal subTotal = underTest.calculateSubTotal();
        BigDecimal tax = underTest.calculateTax(subTotal);
        assertEquals(0, BigDecimal.valueOf(1.88).compareTo(tax));
    }

    @Test
    void shouldCalculateTotal() throws ProductQuantityInvalidException, ProductRetrievalException {
        setupCartProducts();
        BigDecimal total = underTest.calculateTotal();
        assertEquals(0, BigDecimal.valueOf(16.90).compareTo(total));
    }

    @Test
    public void shouldThrowExceptionWhenAddingProductWithZeroValue() {
        assertThrows(ProductQuantityInvalidException.class, () ->
                underTest.addProduct(FIRST_PRODUCT, 0));
    }

    @Test
    void shouldThrowExceptionWhenAddingProductWithNegativeValue() {
        assertThrows(ProductQuantityInvalidException.class, () ->
                underTest.addProduct(FIRST_PRODUCT, -1));
    }

    private void setupCartProducts() throws ProductQuantityInvalidException, ProductRetrievalException {
        when(productProvider.findProductByTitle(FIRST_PRODUCT)).thenReturn(new Product(FIRST_PRODUCT, BigDecimal.valueOf(2.52)));
        when(productProvider.findProductByTitle(SECOND_PRODUCT)).thenReturn(new Product(SECOND_PRODUCT, BigDecimal.valueOf(9.98)));

        underTest.addProduct(FIRST_PRODUCT, 2);
        underTest.addProduct(SECOND_PRODUCT, 1);

    }
}