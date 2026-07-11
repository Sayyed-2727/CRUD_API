package com.Solution.CRUDAPI.application.service.impl;

import com.Solution.CRUDAPI.domain.model.Product;
import com.Solution.CRUDAPI.domain.repository.ProductRepository;
import com.Solution.CRUDAPI.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Creates product")
    void create_shouldSaveProduct_whenProductIsValid() {
        Product product = buildProduct(null, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10);
        Product savedProduct = buildProduct(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10);

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.create(product);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Rejects null price")
    void create_shouldThrowException_whenPriceIsNull() {
        Product product = buildProduct(null, "Laptop", "Gaming laptop", null, 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(product)
        );

        assertEquals("Price must be greater than 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Rejects zero price")
    void create_shouldThrowException_whenPriceIsZero() {
        Product product = buildProduct(null, "Laptop", "Gaming laptop", BigDecimal.ZERO, 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(product)
        );

        assertEquals("Price must be greater than 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Rejects negative price")
    void create_shouldThrowException_whenPriceIsNegative() {
        Product product = buildProduct(null, "Laptop", "Gaming laptop", BigDecimal.valueOf(-50), 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(product)
        );

        assertEquals("Price must be greater than 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Rejects null stock")
    void create_shouldThrowException_whenStockIsNull() {
        Product product = buildProduct(null, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(product)
        );

        assertEquals("Stock must be greater than or equal to 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Rejects negative stock")
    void create_shouldThrowException_whenStockIsNegative() {
        Product product = buildProduct(null, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), -1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(product)
        );

        assertEquals("Stock must be greater than or equal to 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Returns paged products")
    void findAll_shouldReturnPaginatedProducts() {
        PageRequest pageable = PageRequest.of(0, 2);
        List<Product> products = List.of(
                buildProduct(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10),
                buildProduct(2L, "Mouse", "Wireless mouse", BigDecimal.valueOf(50), 20)
        );
        Page<Product> page = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.findAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        verify(productRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Finds product by id")
    void findById_shouldReturnProduct_whenProductExists() {
        Product product = buildProduct(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Throws when id not found")
    void findById_shouldThrowException_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.findById(99L)
        );

        assertEquals("Product with id 99 not found", exception.getMessage());
        verify(productRepository).findById(99L);
    }

    @Test
    @DisplayName("Updates existing product")
    void update_shouldUpdateProduct_whenProductExistsAndDataIsValid() {
        Product existingProduct = buildProduct(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10);
        Product updatedData = buildProduct(null, "Laptop Pro", "Updated version", BigDecimal.valueOf(1500), 8);
        Product savedProduct = buildProduct(1L, "Laptop Pro", "Updated version", BigDecimal.valueOf(1500), 8);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(savedProduct);

        Product result = productService.update(1L, updatedData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop Pro", result.getName());
        assertEquals("Updated version", result.getDescription());
        assertEquals(BigDecimal.valueOf(1500), result.getPrice());
        assertEquals(8, result.getStock());
        verify(productRepository).findById(1L);
        verify(productRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Rejects update for missing id")
    void update_shouldThrowException_whenProductDoesNotExist() {
        Product updatedData = buildProduct(null, "Laptop Pro", "Updated version", BigDecimal.valueOf(1500), 8);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(1L, updatedData)
        );

        assertEquals("Product with id 1 not found", exception.getMessage());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Rejects invalid updated price")
    void update_shouldThrowException_whenUpdatedPriceIsInvalid() {
        Product existingProduct = buildProduct(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10);
        Product updatedData = buildProduct(null, "Laptop Pro", "Updated version", BigDecimal.ZERO, 8);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.update(1L, updatedData)
        );

        assertEquals("Price must be greater than 0", exception.getMessage());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Rejects invalid updated stock")
    void update_shouldThrowException_whenUpdatedStockIsInvalid() {
        Product existingProduct = buildProduct(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10);
        Product updatedData = buildProduct(null, "Laptop Pro", "Updated version", BigDecimal.valueOf(1500), -2);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.update(1L, updatedData)
        );

        assertEquals("Stock must be greater than or equal to 0", exception.getMessage());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Deletes existing product")
    void delete_shouldRemoveProduct_whenProductExists() {
        Product existingProduct = buildProduct(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(1200), 10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        productService.delete(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(existingProduct);
    }

    @Test
    @DisplayName("Rejects delete for missing id")
    void delete_shouldThrowException_whenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.delete(1L)
        );

        assertEquals("Product with id 1 not found", exception.getMessage());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).delete(any(Product.class));
    }

    private Product buildProduct(Long id, String name, String description, BigDecimal price, Integer stock) {
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();
    }
}
