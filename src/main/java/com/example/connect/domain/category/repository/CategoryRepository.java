package com.example.connect.domain.category.repository;

import com.example.connect.domain.category.entity.Category;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    default Category findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("SELECT c FROM Category c JOIN FETCH c.subCategories")
    List<Category> findAllCategoryWithSubCategories();
}
