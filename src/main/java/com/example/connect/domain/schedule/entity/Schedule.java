package com.example.connect.domain.schedule.entity;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`date`", nullable = false)
    private LocalDate date;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matching> matchings = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleSubCategory> scheduleContents = new ArrayList<>();

    public Schedule(LocalDate date, String title, String details, String address, Double latitude, Double longitude, User user) {
        this.date = date;
        this.title = title;
        this.details = details;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }
}
