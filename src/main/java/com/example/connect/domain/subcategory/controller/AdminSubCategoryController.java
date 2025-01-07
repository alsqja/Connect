package com.example.connect.domain.subcategory.controller;

import com.example.connect.domain.subcategory.dto.AdminSubCategoryReqDto;
import com.example.connect.domain.subcategory.dto.AdminSubCategoryResDto;
import com.example.connect.domain.subcategory.service.AdminSubCategoryService;
import com.example.connect.global.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/sub-categories")
@RequiredArgsConstructor
public class AdminSubCategoryController {

    private final AdminSubCategoryService adminSubCategoryService;

    @PostMapping
    public ResponseEntity<CommonResDto<AdminSubCategoryResDto>> createSubCategory(@Valid @RequestBody AdminSubCategoryReqDto adminSubCategoryReqDto) {

        AdminSubCategoryResDto result = adminSubCategoryService.createSubCategory(adminSubCategoryReqDto.getName(), adminSubCategoryReqDto.getImageUrl(), adminSubCategoryReqDto.getCategoryId());

        return new ResponseEntity<>(new CommonResDto<>("서브 카테고리 생성 완료.", result), HttpStatus.CREATED);
    }
}
