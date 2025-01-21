package com.example.connect.domain.category.service;

import com.example.connect.domain.category.dto.AllCategoriesResDto;
import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.category.repository.CategoryRepository;
import com.example.connect.domain.subcategory.dto.AdminSubCategoryResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<AllCategoriesResDto> getAllCategories() {

        List<Category> categories = categoryRepository.findAllCategoryWithSubCategories();

        return categories.stream().map(category -> new AllCategoriesResDto(
                category.getId(),
                category.getName(),
                category.getImageUrl(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getSubCategories().stream().map(subCategory -> new AdminSubCategoryResDto(
                        subCategory.getId(),
                        subCategory.getName(),
                        subCategory.getImageUrl(),
                        subCategory.getCategory().getId(),
                        subCategory.getCreatedAt(),
                        subCategory.getUpdatedAt()
                )).toList()
        )).toList();
    }
}
