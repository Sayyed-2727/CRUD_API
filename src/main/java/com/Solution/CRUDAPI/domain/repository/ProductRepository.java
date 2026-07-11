package com.Solution.CRUDAPI.domain.repository;

import com.Solution.CRUDAPI.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// Repository abstraction for Product entity, that acts as a layer that is responsible ONLY for data access.
// Extends JpaRepository to provide CRUD operations and pagination support out of the box.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
