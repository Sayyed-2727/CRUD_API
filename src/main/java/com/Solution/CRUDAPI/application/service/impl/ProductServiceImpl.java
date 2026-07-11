package com.Solution.CRUDAPI.application.service.impl;

import com.Solution.CRUDAPI.application.service.ProductService;
import com.Solution.CRUDAPI.domain.model.Product;
import com.Solution.CRUDAPI.domain.repository.ProductRepository;
import com.Solution.CRUDAPI.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// Implementation of ProductService that contains business logic and validation rules

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        validateBusinessRules(product);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public Product update(Long id, Product updatedProduct) {
        Product existing = findById(id);

        validateBusinessRules(updatedProduct);

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());

        return productRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Product existing = findById(id);
        productRepository.delete(existing);
    }

    // Centralized business rule validation. These validations complement Bean Validation.
    private void validateBusinessRules(Product product) {
        if (product.getPrice() == null ||
                product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("Stock must be greater than or equal to 0");
        }
    }
}
