package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.dto.ProductResponseDTO;
import com.inventory_backend.inventory_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


//    @Query(value = "select com.inventory_backend.inventory_backend.dto.ProductResponseDTO(p.productId,p.name,p.unit,p.description,p.prefixPrice) from product as p",nativeQuery = true)
//    List<ProductResponseDTO> getProductDetails();

    @Query("SELECT new com.inventory_backend.inventory_backend.dto.ProductResponseDTO(" +
            "p.productId, p.description, p.name, p.prefixPrice, p.unit) " +
            "FROM Product p")
    List<ProductResponseDTO> getProductDetails();
}
