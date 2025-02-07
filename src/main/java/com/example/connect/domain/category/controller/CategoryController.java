package com.example.connect.domain.category.controller;

import com.example.connect.domain.category.dto.AllCategoriesResDto;
import com.example.connect.domain.category.service.CategoryService;
import com.example.connect.global.common.dto.CommonListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<CommonListResDto<AllCategoriesResDto>> getAllCategories() {

        List<AllCategoriesResDto> result = categoryService.getAllCategories();

        return new ResponseEntity<>(new CommonListResDto<>("카테고리 및 서브 카테고리 조회 완료.", result), HttpStatus.OK);
    }
}
