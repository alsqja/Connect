package com.example.connect.domain.schedulesubcategory.entity;

import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedule_content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleSubCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    public ScheduleSubCategory(String description, Schedule schedule, SubCategory subCategory) {
        this.description = description;
        this.schedule = schedule;
        this.subCategory = subCategory;
    }
}
