package com.example.connect.domain.subcategory.controller;

import com.example.connect.domain.subcategory.dto.AdminSubCategoryResDto;
import com.example.connect.domain.subcategory.dto.SubCategoryResDto;
import com.example.connect.domain.subcategory.service.SubCategoryService;
import com.example.connect.global.common.dto.CommonListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping("/api/sub-categories/ranking")
    public ResponseEntity<CommonListResDto<SubCategoryResDto>> getSubCategoriesRanking() {

        List<SubCategoryResDto> result = subCategoryService.getSubCategoriesRanking();

        return new ResponseEntity<>(new CommonListResDto<>("카테고리 탑10 조회 완료.", result), HttpStatus.OK);
    }
}
