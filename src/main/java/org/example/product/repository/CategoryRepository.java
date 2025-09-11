package org.example.product.repository;

import org.apache.ibatis.annotations.Param;
import org.example.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryIsDelOrderByCategoryIdAsc(Integer isDel);

    @Modifying
    @Query("UPDATE Category c SET c.categoryIsDel = :isDel WHERE c.categoryId = :categoryId")
    int updateCategoryStatus(@Param("categoryId") Integer categoryId, @Param("isDel") Integer isDel);
}

