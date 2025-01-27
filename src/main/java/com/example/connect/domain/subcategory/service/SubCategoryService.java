package com.example.connect.domain.subcategory.service;

import com.example.connect.domain.subcategory.dto.AdminSubCategoryResDto;
import com.example.connect.domain.subcategory.dto.SubCategoryResDto;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.domain.subcategory.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    public List<SubCategoryResDto> getSubCategoriesRanking() {

        LocalDateTime startDate = LocalDateTime.now().minusDays(7);

        return subCategoryRepository.findSubCategoryRanking(startDate);
    }
}
