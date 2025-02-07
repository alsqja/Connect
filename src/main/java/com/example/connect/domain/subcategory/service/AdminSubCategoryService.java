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

    @Transactional
    public AdminSubCategoryResDto updateSubCategory(Long id, String name, String imageUrl, Long categoryId) {

        SubCategory subCategory = subCategoryRepository.findByIdOrElseThrow(id);

        if (name != null && !name.isEmpty()) {
            subCategory.updateName(name);
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            subCategory.updateImageUrl(imageUrl);
        }

        if (categoryId != null) {
            Category category = categoryRepository.findByIdOrElseThrow(categoryId);
            subCategory.updateCategory(category);
        }

        return AdminSubCategoryResDto.toDto(subCategory);
    }

    @Transactional
    public void deleteSubCategory(Long id) {

        SubCategory subCategory = subCategoryRepository.findByIdOrElseThrow(id);

        subCategoryRepository.delete(subCategory);
    }
}
