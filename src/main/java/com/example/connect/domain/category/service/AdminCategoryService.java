package com.example.connect.domain.category.service;

import com.example.connect.domain.category.dto.AdminCategoryReqDto;
import com.example.connect.domain.category.dto.AdminCategoryResDto;
import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.category.repository.CategoryRepository;
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
}
