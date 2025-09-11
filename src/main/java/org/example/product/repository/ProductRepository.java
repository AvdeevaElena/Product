package org.example.product.repository;

import org.apache.ibatis.annotations.Param;
import org.example.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductIsDelAndProductIsActiveOrderByProductIdAsc(Integer isDel, Integer isActive);

    @Modifying
    @Query("UPDATE Product p SET p.productIsActive = 0 WHERE p.category.categoryId = :categoryId")
    int updateProductIsActiveByCategory(@Param("categoryId") int categoryId);
}
