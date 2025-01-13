package com.example.connect.global.util;

import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.domain.subcategory.entity.SubCategory;
import lombok.Getter;
import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Jaccard {

    private final List<Double> similarities = new ArrayList<>();
    private int biggestIndex = -1;
    private double biggestSimilarity = -1;

    @TestOnly
    public void reset() {
        this.similarities.clear();
        this.biggestIndex = -1;
        this.biggestSimilarity = -1;
    }

    public void addSimilaritySchedule(Schedule schedule1, Schedule schedule2) {

        double similarity = calculateScheduleSimilarity(
                schedule1.getScheduleContents().stream().map(ScheduleSubCategory::getSubCategory).toList(),
                schedule2.getScheduleContents().stream().map(ScheduleSubCategory::getSubCategory).toList(),
                schedule1.getScheduleContents().stream().map(i -> i.getSubCategory().getCategory()).toList(),
                schedule2.getScheduleContents().stream().map(i -> i.getSubCategory().getCategory()).toList()
        );

        if (similarity > biggestSimilarity) {
            this.biggestSimilarity = similarity;
            this.biggestIndex = this.similarities.size();
        }
        similarities.add(similarity);
    }

    private double calculateJaccardSimilarity(Set<Long> set1, Set<Long> set2) {

        Set<Long> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Long> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / (double) union.size();
    }

    private double calculateScheduleSimilarity(
            List<SubCategory> subCategories1,
            List<SubCategory> subCategories2,
            List<Category> categories1,
            List<Category> categories2
    ) {

        Set<Long> subCategoryIds1 = new HashSet<>();
        Set<Long> subCategoryIds2 = new HashSet<>();

        for (SubCategory subCategory : subCategories1) {
            subCategoryIds1.add(subCategory.getId());
        }
        for (SubCategory subCategory : subCategories2) {
            subCategoryIds2.add(subCategory.getId());
        }


        Set<Long> categoriesIds1 = new HashSet<>();
        Set<Long> categoriesIds2 = new HashSet<>();

        for (Category category : categories1) {
            categoriesIds1.add(category.getId());
        }
        for (Category category : categories2) {
            categoriesIds2.add(category.getId());
        }

        return 0.7 * calculateJaccardSimilarity(subCategoryIds1, subCategoryIds2) + 0.3 * calculateJaccardSimilarity(categoriesIds1, categoriesIds2);
    }
}
