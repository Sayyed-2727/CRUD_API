package com.Solution.CRUDAPI.interfaces.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO used for create and update operations.
 *
 * Keeps API layer isolated from persistence model.
 */
public class ProductRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 150, message = "Name must not exceed 150 characters")
    private String name;

    private String description;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock is mandatory")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description, BigDecimal price, Integer stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
}
