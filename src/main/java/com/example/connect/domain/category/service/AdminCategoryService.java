package com.example.connect.domain.category.service;

import com.example.connect.domain.category.dto.AdminCategoryReqDto;
import com.example.connect.domain.category.dto.AdminCategoryResDto;
import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    public AdminCategoryResDto createCategory(AdminCategoryReqDto adminCategoryReqDto) {

        Category category = new Category(adminCategoryReqDto.getName(), adminCategoryReqDto.getImageUrl());

        Category savedCategory = categoryRepository.save(category);

        return new AdminCategoryResDto(savedCategory);
    }

    @Transactional
    public AdminCategoryResDto updateCategory(Long id, String name, String imageUrl) {

        Category category = categoryRepository.findByIdOrElseThrow(id);

        if (name != null && !name.isEmpty()) {
            category.updateName(name);
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            category.updateImageUrl(imageUrl);
        }

        return AdminCategoryResDto.toDto(category);
    }
}
