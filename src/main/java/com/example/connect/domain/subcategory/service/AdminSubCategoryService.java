package com.example.connect.domain.subcategory.service;

import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.category.repository.CategoryRepository;
import com.example.connect.domain.subcategory.dto.AdminSubCategoryResDto;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.domain.subcategory.repository.SubCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public AdminSubCategoryResDto createSubCategory(String name, String imageUrl, Long categoryId) {

        Category category = categoryRepository.findByIdOrElseThrow(categoryId);

        SubCategory subCategory = new SubCategory(name, imageUrl, category);

        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);

        return AdminSubCategoryResDto.toDto(savedSubCategory);
    }
}
