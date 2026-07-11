package com.Solution.CRUDAPI.application.service;

import com.Solution.CRUDAPI.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


//Service contract for Product use cases.
//Defines business operations independently from infrastructure concerns (Controller, Repository).
/**
    The SOLID principles apply here are:
        - SRP: only product-related business logic.
        - DIP: higher layers depend on this abstraction.
 */
public interface ProductService {

    // Creates a new product after validating business rules.
    Product create(Product product);

    // Retrieves a paginated list of products.
    Page<Product> findAll(Pageable pageable);

    // Retrieves a product by id, throws ResourceNotFoundException if not found.
    Product findById(Long id);

    // Updates an existing product.
    Product update(Long id, Product product);

    // Deletes a product by id.
    void delete(Long id);
}
