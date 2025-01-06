package com.example.connect.domain.category.controller;

import com.example.connect.domain.category.dto.AdminCategoryReqDto;
import com.example.connect.domain.category.dto.AdminCategoryResDto;
import com.example.connect.domain.category.service.AdminCategoryService;
import com.example.connect.global.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<CommonResDto<AdminCategoryResDto>> createCategory(@Valid @RequestBody AdminCategoryReqDto adminCategoryReqDto) {

        AdminCategoryResDto result = adminCategoryService.createCategory(adminCategoryReqDto);

        return new ResponseEntity<>(new CommonResDto<>("카테고리 생성 완료.", result), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<AdminCategoryResDto>> updateCategory(@PathVariable Long id,
                                                                            @RequestBody AdminCategoryReqDto adminCategoryReqDto) {

        AdminCategoryResDto result = adminCategoryService.updateCategory(id, adminCategoryReqDto.getName(), adminCategoryReqDto.getImageUrl());

        return new ResponseEntity<>(new CommonResDto<>("카테고리 수정 완료.", result), HttpStatus.OK);
    }
}

