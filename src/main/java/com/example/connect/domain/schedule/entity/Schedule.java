package com.example.connect.domain.schedule.entity;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.schedule.dto.ScheduleUpdateServiceDto;
import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule",
        indexes = {
                @Index(name = "idx_is_deleted", columnList = "is_deleted"),
                @Index(name = "idx_date_is_deleted", columnList = "`date`, is_deleted")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE schedule SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Schedule extends BaseEntity {

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

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "fromSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matching> fromMatchings = new ArrayList<>();

    @OneToMany(mappedBy = "toSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matching> toMatchings = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleSubCategory> scheduleContents = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
        if (count == null) {
            count = 0;
        }
    }

    public Schedule(LocalDate date, String title, String details, String address, Double latitude, Double longitude, User user) {
        this.date = date;
        this.title = title;
        this.details = details;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }

    public Schedule(LocalDate date, String title, String details, String address, Double latitude, Double longitude) {
        this.date = date;
        this.title = title;
        this.details = details;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateField(ScheduleUpdateServiceDto updateServiceDto) {

        if (date != null) {
            this.date = updateServiceDto.getDate();
        }
        if (title != null) {
            this.title = updateServiceDto.getTitle();
        }
        if (details != null) {
            this.details = updateServiceDto.getDetails();
        }
        if (address != null) {
            this.address = updateServiceDto.getAddress();
        }
        if (latitude != null) {
            this.latitude = updateServiceDto.getLatitude();
        }
        if (longitude != null) {
            this.longitude = updateServiceDto.getLongitude();
        }
    }

    public void addCount() {
        this.count++;
    }

    public void decreaseCount(Integer count) {
        this.count -= count;
    }
}
